package com.foodback.swagger

import com.foodback.core.coreCommon.api.errorCode.ServerErrorCodeProvider
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContext
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition
internal class OpenApiConfig {

    @Bean
    fun registerErrorSchema(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("all")
            .pathsToMatch("/**")
            // Добавляем GlobalErrorResponse в список типов, которые нужно сканировать
            .addOpenApiCustomizer { openApi ->
                // Это заставит Swagger создать схему, если её ещё нет
            }
            .build()
    }

    @Bean
    fun openApiCustomizer(serverErrorCodeProviders: List<ServerErrorCodeProvider>): OpenApiCustomizer {

        println("SWAGGER: Найдено провайдеров кодов: ${serverErrorCodeProviders.size}")
        serverErrorCodeProviders.forEach {
            println("SWAGGER: Провайдер ${it.javaClass.simpleName} предоставил коды: ${it.getAllCodes()}")
        }

        return OpenApiCustomizer { openAPI ->
            val allCodes = serverErrorCodeProviders.flatMap { it.getAllCodes() }

            if (allCodes.isEmpty()) return@OpenApiCustomizer

            val descriptionBuilder = StringBuilder("### Список кодов ошибок\n\n")
            descriptionBuilder.append("| Код | Константа |\n| :--- | :--- |\n")

            allCodes.sortedBy { it.code }.forEach {
                descriptionBuilder.append("| ${it.code} | `${it.toString()}` |\n")
            }

            // Пытаемся найти схему. В SpringDoc 3 компоненты могут быть ленивыми.
            val schemas = openAPI.components?.schemas
            val errorSchema = schemas?.get("GlobalErrorResponse")

            if (errorSchema != null) {
                val property = errorSchema.properties?.get("serverErrorCode")
                property?.description = descriptionBuilder.toString()
                println("SWAGGER: Описание для GlobalErrorResponse успешно обновлено.")
            } else {
                println("SWAGGER: Схема GlobalErrorResponse не найдена в OpenAPI компонентах!")
                // Выведем все доступные схемы, чтобы понять, как она называется
                println("SWAGGER: Доступные схемы: ${schemas?.keys}")
            }
        }
    }

    @Bean
    fun errorCodeModelConverter(
        serverErrorCodeProviders: List<ServerErrorCodeProvider>
    ): ModelConverter {
        return ModelConverter { type, context, chain ->
            val schema = if (chain.hasNext()) chain.next().resolve(type, context, chain) else null

            // Если это наше поле serverErrorCode (находим его по описанию или типу)
            if (schema != null && type.name == "serverErrorCode") {
                val allCodes = serverErrorCodeProviders.flatMap { it.getAllCodes() }

                // Генерируем описание
                val descriptionText = "Available codes:\n" + allCodes
                    .sortedBy { it.code }
                    .joinToString(separator = "\n") { "* ${it.code} : ${it.toString()}" }

                schema.description = descriptionText
                // Принудительно устанавливаем список доступных значений
                schema.enum = allCodes.map { it.code }
            }
            schema
        }
    }
}