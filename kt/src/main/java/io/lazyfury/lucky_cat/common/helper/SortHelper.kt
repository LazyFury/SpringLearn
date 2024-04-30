package io.lazyfury.lucky_cat.common.helper

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction

object SortHelper {

    fun sort(pageRequest: PageRequest, map: Map<String, Any>): PageRequest {
        val sortMap = mutableMapOf<String, Sort.Order>()

        map.map {
//            println("sort key: ${it.key}, value: ${it.value}")
            val key = it.key
            val keySplit = key.split("__")
            if (keySplit.size != 2) {
                return@map null
            }
            val originKey = keySplit[0]
            val sortKey = keySplit[1]
            if (sortKey != "sort") {
                return@map null
            }
            val direction = if (it.value == "asc") Sort.Direction.ASC else Sort.Direction.DESC
            sortMap.put(originKey, Sort.Order(direction, originKey))
        }
//        println(sortMap)

        val defaultSortMap = mapOf<String,Direction>(
            "id" to Direction.DESC,
        )

        defaultSortMap.map {
            if (!sortMap.containsKey(it.key)) {
                sortMap.put(it.key, Sort.Order(it.value, it.key))
            }
        }

        println("sortMap: $sortMap")
        val sort: Sort = Sort.by(
            *sortMap.values.toTypedArray()
        )


        return pageRequest.withSort(sort)
    }

}