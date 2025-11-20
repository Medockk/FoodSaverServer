package com.foodback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Entry point of current application
 */
@SpringBootApplication
@EnableScheduling
class FoodSaverApplication

fun main(args: Array<String>) {
    runApplication<FoodSaverApplication>(*args)
}
