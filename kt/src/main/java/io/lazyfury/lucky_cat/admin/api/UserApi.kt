package io.lazyfury.lucky_cat.admin.api

import io.lazyfury.lucky_cat.app.auth.entity.User
import io.lazyfury.lucky_cat.app.auth.repo.UserRepository
import io.lazyfury.lucky_cat.common.controller.CrudApiController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/api/user")
class UserApi(val userRepo: UserRepository) : CrudApiController<User>(
    userRepo,
    disabledDelete = true,
    getId = { it.id },
    className = User::class.java,
    exportName = "用户"
) {


}