pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}
rootProject.name = "FoodSaverServer"

include("feature")
include("feature:featureRestaurant")
include("feature:featureCompany")
include("feature:featureUsers")
include("feature:featureProduct")

include(":core")
include(":core:coreCommon")
include(":core:coreMedia")
include(":core:coreSecurity")