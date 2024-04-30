package io.lazyfury.lucky_cat.app.store.entity

import io.lazyfury.lucky_cat.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "store_order")
class Order(
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @OneToMany
    var orderInfoList: List<OrderInfo>,

    var amount: Double,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    var invoice: Invoice,

    var orderStatus: OrderStatus = OrderStatus.PREPARE

):BaseEntity() {
    constructor(orderInfoList: List<OrderInfo>, amount: Double, invoice: Invoice):this(0, orderInfoList, amount, invoice,
        OrderStatus.PREPARE
    )

    enum class OrderStatus(val value: Int) {
        PREPARE(-1),//预计算订单
        WAITING(0),
        PAID(1),
        WAITING_DELIVERY(2),
        DELIVERED(3),
        CANCELLED(4),
        COMPLETED(5)
    }
}