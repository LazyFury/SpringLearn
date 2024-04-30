package io.lazyfury.lucky_cat.admin.ui.table

import io.element.ui.*
import io.lazyfury.lucky_cat.admin.ui.base.Api
import io.lazyfury.lucky_cat.admin.ui.base.Table

class UserTable(
    name: String = "用户管理",
    description: String = "用户管理 - 用户信息/充值/冻结",
    edit: ElFormLayout = UserAddForm(),
    search: ElFormLayout = UserSearchForm(),
    api: Api = Api(
        create = "",
        update = "/user/update",
        delete = "/user/delete",
        list = "/user/list",
        detail = "/user/detail",
        export = "/user/export"
    ),
    columns: List<ElTableColumn> = columns(),
    filters: List<ElFormItem>? = listOf(
        ElFormItem(
            prop = "accountNonLocked",
            label = "是否锁定",
            default = ElRadioButtonGroup.SKIP,
            component = ElRadioButtonGroup(
                field = "accountNonLocked",
                options = listOf(
                    mapOf("label" to "全部", "value" to ElRadioButtonGroup.SKIP),
                    mapOf("label" to "是", "value" to true),
                    mapOf("label" to "否", "value" to false),
                )
            ),
        )
    ),
) : Table(
    name = name,
    description = description,
    edit = edit,
    search = search,
    api = api,
    columns = columns,
    filters = filters,
) {
    companion object {
        fun columns() = listOf(
            ElTableColumn(
                label = "avatar", prop = "avatar", type = "image"
            ), ElTableColumn(
                label = "username", prop = "username", sortable = true, width = 160
            ), ElTableColumn(
                label = "email", prop = "email", width = 160
            ), ElTableColumn(
                label = "未过期", prop = "accountNonExpired", width = 80, type = "switch"
            ), ElTableColumn(
                label = "未锁定", prop = "accountNonLocked", width = 80, type = "checkbox"
            ),
//            lastLogin
            ElTableColumn(
                label = "最后登录", prop = "lastLogin", width = 200, sortable = true
            ), ElTableColumn(
                label = "create time", prop = "createTime", width = 200, sortable = true
            ), ElTableColumn(
                label = "update time", prop = "updateTime", width = 200, sortable = true
            )
        )
    }

    class UserAddForm : ElFormLayout() {
        override fun rows(): List<List<ElWidget>> {
            return listOf(
                listOf(
                    ElFormItem(
                        prop = "username",
                        label = "用户昵称",
                        component = ElInput("text", "Username", "username"),
                    ), ElFormItem(
                        prop = "accountNonLocked",
                        label = "是否锁定",
                        component = ElSwitch("accountNonLocked"),
                    ),
//                    accountNonExpired
                    ElFormItem(
                        prop = "accountNonExpired",
                        label = "是否过期",
                        component = ElSwitch("accountNonExpired"),
                    )
                ), listOf(
                    ElFormItem(
                        prop = "email",
                        label = "邮箱",
                        component = ElInput("text", "Email", "email"),
                    ),
                    ElFormItem(
                        prop = "password",
                        label = "密码",
                        component = ElInput("password", "Password", "password"),
                    ),
                ), listOf(
                    ElFormItem(
                        prop = "avatar",
                        label = "头像",
                        component = ElInput("text", "Avatar", "avatar"),
                    ),
                )
            )
        }
    }


    class UserSearchForm : ElFormLayout() {
        override fun rows(): List<List<ElWidget>> {
            return listOf(
                listOf(
                    ElFormItem(
                        prop = "username__like",
                        label = "用户昵称",
                        component = ElInput("text", "Username", "username"),
                    ),
                    ElFormItem(
                        prop = "email",
                        label = "邮箱",
                        component = ElInput("text", "Email", "email"),
                    ),
                )
            )
        }
    }
}