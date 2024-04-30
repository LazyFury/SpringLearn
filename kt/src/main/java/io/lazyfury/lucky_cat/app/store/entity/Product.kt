package io.lazyfury.lucky_cat.app.store.entity

import io.lazyfury.lucky_cat.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "store_product")
class Product(
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    var price: Double,
    var stock: Int,
    var description: String,
    var image: String,
    var status: Status = Status.ON_SALE,
    var supportPay: List<Payment.Type> = Payment.Type.entries
):BaseEntity() {
    enum class Status(val value: Int) {
        ON_SALE(0),
        OFF_SALE(1)
    }
}