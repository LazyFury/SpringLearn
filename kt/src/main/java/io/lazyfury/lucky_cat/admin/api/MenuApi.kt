package io.lazyfury.lucky_cat.admin.api

import io.lazyfury.lucky_cat.admin.ui.base.Menu
import io.lazyfury.lucky_cat.admin.ui.base.Route
import io.lazyfury.lucky_cat.admin.ui.table.UserTable
import io.lazyfury.lucky_cat.admin.ui.table.UserVipCardTable
import io.lazyfury.lucky_cat.common.reponse.ApiJsonResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/api")
class MenuApi {

    @GetMapping("/menu/list")
    fun list(): ApiJsonResponse<List<Route>> {
        return ApiJsonResponse.success(
            listOf(
                Route(
                    name = "overview",
                    path = "/overview",
                    component = "HomeView",
                    menu = Menu(
                        name = "控制台",
                        icon = "ant-design:compass-outlined",
                    ),
                ),
                Route(
                    path = "/user",
                    component = "TableView",
                    name = "user",
                    menu = Menu(
                        name = "用户管理",
                        icon = "ant-design:user-outlined",
                    ),
                    children = listOf(
                        Route(
                            path = "/user/user-list",
                            component = "TableView",
                            name = "user-list",
                            menu = Menu(
                                name = "用户列表",
                                icon = "ant-design:1-outlined",
                                table = UserTable()
                            )
                        ),
                        Route(
                            path = "/user/user-vip-list",
                            component = "TableView",
                            name = "user-vip-list",
                            menu = Menu(
                                name = "用户 VIP",
                                icon = "",
                                table = UserVipCardTable()
                            )
                        )
                    )
                ),

                Route(
                    path = "/set",
                    name = "set",
                    component = "SettingView",
                    menu = Menu(
                        name = "系统设置",
                        icon = "ant-design:setting-outlined",
                    ),
                    children = listOf(
                        Route(
                            path = "/set/set-basic",
                            component = "setting/Test",
                            name = "set-basic",
                            menu = Menu(
                                name = "基础设置",
                                icon = "",
                            ),
                        )
                    )
                )
            ).filter {
//                todo:permission check
                true
            }
        )
    }
}