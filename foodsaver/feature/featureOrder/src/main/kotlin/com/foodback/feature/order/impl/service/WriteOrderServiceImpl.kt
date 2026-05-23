package com.foodback.feature.order.impl.service

import com.foodback.feature.cart.api.service.ReadCartService
import com.foodback.feature.cart.api.service.WriteCartService
import com.foodback.feature.featureProduct.api.service.ReadProductService
import com.foodback.feature.featureProduct.api.service.WriteProductService
import com.foodback.feature.featureRestaurant.api.service.ReadRestaurantService
import com.foodback.feature.order.api.dto.OrderResponse
import com.foodback.feature.order.api.service.WriteOrderService
import com.foodback.feature.order.impl.entity.OrderEntity
import com.foodback.feature.order.impl.entity.OrderItemEntity
import com.foodback.feature.order.impl.exception.QuantityOutOfBoundsException
import com.foodback.feature.order.impl.mapper.OrderItemMapper
import com.foodback.feature.order.impl.mapper.OrderMapper
import com.foodback.feature.order.impl.repository.OrderItemRepository
import com.foodback.feature.order.impl.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@Service
internal class WriteOrderServiceImpl(
    private val cartService: ReadCartService,
    private val writeCartService: WriteCartService,

    private val readProductService: ReadProductService,
    private val writeProductService: WriteProductService,

    private val readRestaurantService: ReadRestaurantService,

    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository,

    private val orderMapper: OrderMapper,
    private val orderItemMapper: OrderItemMapper
) : WriteOrderService {

    @Transactional
    override fun createOrder(userId: UUID): List<OrderResponse> {
        val cart = cartService.getCartByUserId(userId)
            ?: throw IllegalStateException()

        val cartItems = cartService
            .getCartItems(cart.id, userId)
        if (cartItems.isEmpty()) throw IllegalStateException()

        val productIds = cartItems.map { it.productId }
        val productsMap = readProductService
            .getProductsByIds(productIds)
            .associateBy { it.id }

        // валидация продуктов на складе
        cartItems.forEach {
            val product = productsMap[it.productId]
                ?: throw IllegalArgumentException()

            if (product.count < it.quantity) {
                throw QuantityOutOfBoundsException("Quantity out of bounds!")
            }

            // списывание продукта со склада
            writeProductService.decreaseProductCount(it.productId, it.quantity)
        }

        // разбиваем элементы корзины по ресторанам
        val itemsByRestaurant = cartItems.groupBy { item ->
            productsMap[item.productId]?.restaurantId
                ?: throw IllegalStateException("У продукта ${item.productId} нет ресторана")
        }

        // в цикле создаем заказы для каждого ресторана отдельно
        val createdOrders = itemsByRestaurant.map { (restaurantId, restaurantCartItems) ->
            val restaurant = readRestaurantService.getRestaurantById(restaurantId)
                ?: throw IllegalStateException("Ресторан с ID $restaurantId не найден")

            // вычисляем стоимость только для товаров этого ресторана
            var restaurantOrderPrice = BigDecimal.ZERO
            restaurantCartItems.forEach { item ->
                val p = productsMap[item.productId]!!
                val finalUnitPrice = p.price - p.discount
                restaurantOrderPrice += finalUnitPrice * BigDecimal.valueOf(item.quantity)
            }

            // создаем сущность основного заказа
            val orderEntity = OrderEntity(
                userId = userId,
                restaurantName = restaurant.name,
                restaurantImageUri = restaurant.photoUris.firstOrNull(),
                orderPrice = restaurantOrderPrice,
                orderSize = restaurantCartItems.sumOf { it.quantity }.toInt(),
                trackNumber = generateTrackNumber()
            )
            // создаем элементы заказа для конкретного ресторана
            val orderItems = restaurantCartItems.map { item ->
                val product = productsMap[item.productId]!!
                OrderItemEntity(
                    productId = item.productId,
                    name = product.name,
                    price = product.price - product.discount,
                    quantity = item.quantity,
                    order = orderEntity
                )
            }.toMutableList()

            orderEntity.items = orderItems

            // сохраняем текущий заказ в БД
            orderRepository.save(orderEntity)
        }

        // очищаем корзину
        writeCartService.clearCart(userId, cart.id)
        return createdOrders.map { orderMapper.toResponse(it) }
    }

    private fun generateTrackNumber(): String {
        val number = Random.nextInt(100000, 999999)
        return "FB-$number"
    }
}