package io.lazyfury.lucky_cat.app.auth.service

import io.lazyfury.lucky_cat.app.auth.entity.User
import io.lazyfury.lucky_cat.app.auth.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    // create validate
    private fun validateUser(user: User) {
        if (user.phone.isEmpty() || user.email.isEmpty()) {
            throw Exception("User not found")
        }
    }
    // create user
    fun createUser(user: User): User {
        validateUser(user);
        return userRepository.save(user)
    }
}