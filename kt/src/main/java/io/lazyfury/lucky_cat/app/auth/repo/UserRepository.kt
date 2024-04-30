package io.lazyfury.lucky_cat.app.auth.repo

import io.lazyfury.lucky_cat.app.auth.entity.User
import io.lazyfury.lucky_cat.common.repo.CustomCrudRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : CustomCrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}