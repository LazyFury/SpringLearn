package io.lazyfury.lucky_cat.admin.ui.table

import io.element.ui.ElFormItem
import io.element.ui.ElFormLayout
import io.element.ui.ElInput
import io.element.ui.ElTableColumn
import io.lazyfury.lucky_cat.admin.ui.base.Api
import io.lazyfury.lucky_cat.admin.ui.base.Table

class UserVipCardTable(
    name: String = "用户会员卡管理",
    description: String = "用户会员卡管理 - 用户会员卡管理",
    detail: Table.DetailMode = Table.DetailMode.DIALOG,
    api: Api = Api(
        create = "/user-vip/create",
        update = "/user-vip/update",
        list = "/user-vip/list",
        detail = "/user-vip/detail",
        delete = "/user-vip/delete",
        export = "/user-vip/export"
    ),
    columns: List<ElTableColumn> = listOf(
        ElTableColumn(
            label = "vipName",
            prop = "vipName",
            width = 200
        )
    ),
    edit: ElFormLayout = UserVipEditForm(),
    search: ElFormLayout = UserVipSearchForm()
) : Table(
    name = name,
    description = description,
    detail = detail,
    api = api,
    edit = edit,
    search = search,
    columns = columns,
    filters = listOf()
) {

    class UserVipEditForm : ElFormLayout() {
        override fun rows(): List<List<ElFormItem>> {
            return listOf(
                listOf(
                    ElFormItem(
                        label = "vipName",
                        prop = "vipName",
                        component = ElInput(
                            field = "vipName",
                            placeholder = "vipName",
                            type = "text"
                        )
                    )
                )
            )
        }
    }

    class UserVipSearchForm : ElFormLayout() {
        override fun rows(): List<List<ElFormItem>> {
            return listOf(
                listOf(
                    ElFormItem(
                        label = "vipName",
                        prop = "vipName",
                        component = ElInput(
                            field = "vipName",
                            placeholder = "vipName",
                            type = "text"
                        )
                    )
                )
            )
        }
    }
}