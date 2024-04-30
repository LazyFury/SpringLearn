package io.lazyfury.lucky_cat.app.store.entity

import io.lazyfury.lucky_cat.common.entity.BaseEntity
import jakarta.persistence.*


@Entity
@Table(name = "store_payment")
class Payment(
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var amount: Double,
    var type: Type,
    var remark: String? = null,
    var callBackData: String? = null,
    var requestParams: String? = null,
    var status: Status = Status.UNPAID

) : BaseEntity() {
    constructor(
        amount: Double,
        type: Type,
        remark: String? = null,
        callBackData: String? = null,
        status: Status = Status.UNPAID,
        requestParams: String? = null
    ) : this(0, amount, type, remark, callBackData, requestParams, status)

    enum class Type(
        val value: Int,
        val payName: String,
        val remark: String? = "",
        val platform: List<String> = listOf()
    ) {
        CASH(0, "现金", platform = listOf("h5", "app")),
        ALIPAY(1, "支付宝", platform = listOf("h5", "app", "pc")),
        WECHAT(2, "微信", platform = listOf("app")),
        CARD(3, "银行卡", platform = listOf("h5", "app")),
        ;

        companion object {
            fun fromValue(value: Int): Type {
                return entries.first { it.value == value }
            }

            fun fromPayName(payName: String): Type {
                return entries.first { it.payName == payName }
            }
        }
    }

    enum class Status(val value: Int) {

        UNPAID(0),
        PAID(1),
        CANCELLED(2)
    }
}