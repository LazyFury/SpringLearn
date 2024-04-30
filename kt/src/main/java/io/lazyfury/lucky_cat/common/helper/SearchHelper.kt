package io.lazyfury.lucky_cat.common.helper

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime
import java.util.Date
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

object SearchHelper {
    private fun isTypedString(v: Any): String {
        if (v is String) {
            //        if int
            if (v.matches(Regex("^-?\\d+\$"))) {
                return "int"
            }

            if (v.matches(Regex("^-?\\d+\\.\\d+\$"))) {
                return "float"
            }

            if (v.matches(Regex("^-?\\d{4}-\\d{2}-\\d{2}[\\s|T]\\d{2}:\\d{2}:\\d{2}\$"))) {
                return "datetime"
            }

//        , split
            if (v.contains(",")) {
                return "list"
            }

//        bool
            if (v == "true" || v == "false") {
                return "bool"
            }

            return "string"
        }
        if (v is Boolean) return "bool"
        if (v is Int) return "int"
        if (v is Float) return "float"
        if (v is LocalDateTime) return "datetime"
        if (v is List<*>) return "list"
        if (v is Array<*>) return "list"
        return "unknown"
    }

    private fun <T> search(
        cb: CriteriaBuilder,
        root: Root<T>,
        mode: String,
        k: String,
        v: Any
    ): jakarta.persistence.criteria.Predicate? {
        return when (mode) {
            "eq" -> when (isTypedString(v.toString())) {
                "int" -> cb.equal(root.get<Int>(k), v.toString().toInt())
                "float" -> cb.equal(root.get<Float>(k), v.toString().toFloat())
                "datetime" -> cb.equal(root.get<Date>(k), LocalDateTime.parse(v.toString()))
                "string" -> cb.equal(root.get<String>(k), v.toString())
                "bool" -> cb.equal(root.get<Boolean>(k), v.toString().toBoolean())
                else -> throw IllegalArgumentException("Unsupported type: $v")
            }
            "neq" -> cb.notEqual(root.get<Any>(k), v)
            "like" -> cb.like(root.get(k), "%$v%")
            "like_l" -> cb.like(root.get(k), "$v%")
            "like_r" -> cb.like(root.get(k), "%$v")

            "in" -> when (isTypedString(v.toString())) {
                "string" -> root.get<Any>(k).`in`(v.toString().split(",").map { it.toInt() })
                "list" -> root.get<Any>(k).`in`(v.toString().split(","))
                else -> throw IllegalArgumentException("Unsupported type: $v")
            }

            "nin" -> when (isTypedString(v.toString())) {
                "string" -> cb.not(root.get<Any>(k).`in`(v.toString().split(",").map { it.toInt() }))
                "list" -> cb.not(root.get<Any>(k).`in`(v.toString().split(",")))
                else -> throw IllegalArgumentException("Unsupported type: $v")
            }

            "null" -> cb.isNull(root.get<Any>(k))

            "notnull" -> cb.isNotNull(root.get<Any>(k))

            "gt" -> when (isTypedString(v.toString())) {
                "int" -> cb.gt(root.get(k), v.toString().toInt())
                "float" -> cb.gt(root.get(k), v.toString().toFloat())
                "datetime" -> cb.greaterThan(root.get(k), LocalDateTime.parse(v.toString()))
                "string" -> cb.greaterThan(root.get(k), v.toString())
                else -> throw IllegalArgumentException("Unsupported type: $v")
            }

            "lt" -> when (isTypedString(v.toString())) {
                "int" -> cb.lt(root.get(k), v.toString().toInt())
                "float" -> cb.lt(root.get(k), v.toString().toFloat())
                "datetime" -> cb.lessThan(root.get(k), LocalDateTime.parse(v.toString()))
                "string" -> cb.lessThan(root.get(k), v.toString())
                else -> throw IllegalArgumentException("Unsupported type: $v")
            }

            "gte" -> when (isTypedString(v.toString())) {
                "int" -> cb.ge(root.get(k), v.toString().toInt())
                "float" -> cb.ge(root.get(k), v.toString().toFloat())
                "datetime" -> cb.greaterThanOrEqualTo(root.get(k), LocalDateTime.parse(v.toString()))
                "string" -> cb.greaterThanOrEqualTo(root.get(k), v.toString())
                else -> throw IllegalArgumentException("Unsupported type: $v")
            }

            "lte" -> when (isTypedString(v.toString())) {
                "int" -> cb.le(root.get(k), v.toString().toInt())
                "float" -> cb.le(root.get(k), v.toString().toFloat())
                "datetime" -> cb.lessThanOrEqualTo(root.get(k), LocalDateTime.parse(v.toString()))
                "string" -> cb.lessThanOrEqualTo(root.get(k), v.toString())
                else -> throw IllegalArgumentException("Unsupported type: $v")
            }

            else -> null
        }
    }


    fun <T> specification(where: Map<String, Any>): Specification<T> {
        return Specification { root, _, cb ->
            cb.and(
                *where.map { (k, v) ->
                    print("v is $v")
                    if (v.toString().isEmpty() || v.toString().isBlank()) return@map null
                    val key = k.split("__")
                    when (key.size) {
                        1 -> search(cb, root, "eq", k, v)
                        2 -> search(cb, root, key[1], key[0], v)
                        3 -> {
                            print("fk search")
                            val mode = key[1]
                            when (mode) {
                                "fk" -> {
                                    val fk = key[0]
                                    val fkKey = key[2]
                                    val join = root.join<T, Any>(fk, JoinType.LEFT)
                                    cb.equal(join.get<Any>(fkKey), v)
                                }

                                "or" -> {
                                    cb.or(
                                        search(cb, root, key[2], key[0], v)
                                    )
                                }

                                else -> null
                            }
                        }

                        else -> throw IllegalArgumentException("Unsupported operator: $k")
                    }
                }.filterNotNull().toTypedArray()
            )
        }

    }

}