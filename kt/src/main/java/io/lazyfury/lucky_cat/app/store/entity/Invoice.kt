package io.lazyfury.lucky_cat.app.store.entity

import io.lazyfury.lucky_cat.app.auth.entity.User
import io.lazyfury.lucky_cat.common.entity.BaseEntity
import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "store_invoice")
class Invoice(
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var title: String,

    var description: String,

    var amount: Double,

    @JoinColumn(referencedColumnName = "id", nullable = false)
    @ManyToOne
    var user: User,

    var paymentTime: Date,

    @JoinColumn(referencedColumnName = "id", nullable = false)
    @OneToOne
    var payment: Payment
) : BaseEntity() {


}