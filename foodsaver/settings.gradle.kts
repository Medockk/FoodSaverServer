pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}
rootProject.name = "FoodSaverServer"

include("feature")
include("feature:featureAddress")
include("feature:featureOrder")
include("feature:featurePaymentMethod")
include("feature:featureIngredients")
include("feature:featureMediaConfig")
include("feature:featureRestaurant")
include("feature:featureCompany")
include("feature:featureUsers")
include("feature:featureProduct")
include("feature:featureCategory")
include("feature:featureCart")

include(":core")
include(":core:coreEvent")
include(":core:coreCommon")
include(":core:coreMedia")
include(":core:coreSecurity")