package io.lazyfury.lucky_cat.app.store.entity

import io.lazyfury.lucky_cat.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "store_order_info")
class OrderInfo(

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @ManyToOne
    var product: Product,

    var quantity: Int,

    var amount: Double,

    ):BaseEntity()