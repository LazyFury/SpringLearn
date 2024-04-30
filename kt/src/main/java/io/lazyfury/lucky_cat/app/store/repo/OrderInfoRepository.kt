package io.lazyfury.lucky_cat.app.store.repo

import io.lazyfury.lucky_cat.common.repo.CustomCrudRepository
import io.lazyfury.lucky_cat.app.store.entity.OrderInfo
import org.springframework.stereotype.Repository

@Repository
interface OrderInfoRepository: CustomCrudRepository<OrderInfo, Long> {
}