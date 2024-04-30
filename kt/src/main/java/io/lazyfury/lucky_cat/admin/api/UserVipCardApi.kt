package io.lazyfury.lucky_cat.admin.api

import io.lazyfury.lucky_cat.app.auth.entity.UserVipCard
import io.lazyfury.lucky_cat.app.auth.repo.UserVipCardRepository
import io.lazyfury.lucky_cat.common.controller.CrudApiController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/api/user-vip")
class UserVipCardApi(
    var userVipRepo: UserVipCardRepository
) : CrudApiController<UserVipCard>(
    repo = userVipRepo,
    getId = { it.id },
    className = UserVipCard::class.java,
    exportName = "用户 VIP"
) {
}