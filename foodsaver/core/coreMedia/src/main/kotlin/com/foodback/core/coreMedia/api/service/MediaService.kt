package com.foodback.core.coreMedia.api.service

interface MediaService {

    /**
     * Загружает файл и возвращает относительный путь
     * @param bytes содержимое файла
     * @param folder категория/папка
     * @param extension расширение файла
     */
    fun upload(bytes: ByteArray, folder: String, extension: String): String

    fun delete(relativeUrl: String)
}