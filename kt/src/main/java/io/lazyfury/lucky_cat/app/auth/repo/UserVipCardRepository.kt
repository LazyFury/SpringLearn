package io.lazyfury.lucky_cat.app.auth.repo

import io.lazyfury.lucky_cat.app.auth.entity.UserVipCard
import io.lazyfury.lucky_cat.common.repo.CustomCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserVipCardRepository:CustomCrudRepository<UserVipCard,Long> {

}