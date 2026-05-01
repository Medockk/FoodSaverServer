package com.foodback

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Entry point of current application
 */

@SpringBootApplication(
//    scanBasePackages = [
//        "com.foodback"
//    ]
)
//@EnableJpaRepositories(basePackages = ["com.foodback"])
//@EntityScan(basePackages = ["com.foodback"])
@EnableScheduling
@EnableAsync
class FoodSaverApplication

fun main(args: Array<String>) {
    runApplication<FoodSaverApplication>(*args)
}

// TODO in ProductEntity make photoUri - List<String>!!
// TODO rewrite ingredients model. Add something like ingredient image?