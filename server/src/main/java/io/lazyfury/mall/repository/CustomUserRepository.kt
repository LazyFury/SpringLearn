package io.lazyfury.mall.repository

import io.lazyfury.mall.entity.User
import org.springframework.data.repository.CrudRepository

interface CustomUserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
}