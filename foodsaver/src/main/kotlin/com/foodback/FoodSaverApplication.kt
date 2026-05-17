package com.foodback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Entry point of current application
 */

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@EnableAsync
class FoodSaverApplication

fun main(args: Array<String>) {
    runApplication<FoodSaverApplication>(*args)
}

// TODO на клиенте
// TODO на RestaurantScreen сделать CollapsingToolbar
// TODO на HomeScreen добавить refresh функцию
// TODO на RestaurantScreen добавить refresh функцию
// TODO на FoodDetailsScreen добавить refresh функцию

// TODO на потом для клиента
// TODO перейти на kamel (?) вместо coil (?) // стоит ли
// TODO плавнее (дольше по времени) анимация загрузки логина (чтобы не было рваности)
// TODO анимация открытия экрана RestaurantScreen
// TODO анимация открытия экрана ProductScreen (?)
// TODO на экране RestaurantScreen добавить каку-то плавность/оптимизацию шимера

// TODO на сервере
// TODO добавить в CartItemEntity и CartItemResponse аттрибуты (размер/добавки)

// TODO Заполнить таблицу ингредиентов, добавить продуктам ссылки
// TODO В ProductServiceImpl делать проверку ингредиентов
// TODO добавить эндпоинт который возвращает категории продуктов ресторана
// TODO Добавить сущность заказа