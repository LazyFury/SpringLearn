package io.lazyfury.lucky_cat.app.store.service

import io.lazyfury.lucky_cat.app.auth.entity.User
import io.lazyfury.lucky_cat.app.store.entity.Invoice
import io.lazyfury.lucky_cat.app.store.entity.Order
import io.lazyfury.lucky_cat.app.store.entity.OrderInfo
import io.lazyfury.lucky_cat.app.store.entity.Payment
import io.lazyfury.lucky_cat.app.store.repo.*
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


@Transactional
class OrderService(
    private val orderInfoRepository: OrderInfoRepository,
    private val invoiceRepository: InvoiceRepository,
    private val paymentRepository: PaymentRepository,
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository,
    private val entityManager: EntityManager
) {

    private fun saveOrderAndReduceProductStock(order: Order) {
        entityManager.transaction.begin()
        orderRepository.save(order)
        order.orderInfoList.forEach {
            val product = it.product
            product.stock -= it.quantity
            productRepository.save(product)
        }
        entityManager.transaction.commit()
    }

    fun createOrder(
        orderInfoList: List<OrderInfo>,
        payType: String,
        user: User
    ) {
        // create order
        val payment = Payment(0.0, Payment.Type.fromPayName(payType), status = Payment.Status.UNPAID)
        val invoice =
            Invoice(
                0, user = user, title = "", description = "", amount = 0.0,
                payment = payment,
                paymentTime = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC))
            )
        val order = Order(orderInfoList,0.0,invoice)
        saveOrderAndReduceProductStock(order)
    }

}