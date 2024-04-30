package io.lazyfury.lucky_cat.admin.ui.base

import io.element.ui.ElFormItem
import io.element.ui.ElFormLayout
import io.element.ui.ElTableColumn

abstract class Table(
    /**
     * 名称
     */
    var name:String,

    /**
     * 描述
     */
    var description:String,

    /**
     * 接口
     */
    var api: Api?,

    /**
     * 编辑表单
     */
    var edit: ElFormLayout?,

    /**
     * 搜索表单，点击搜索按钮时触发
     */
    var search: ElFormLayout?,

    /**
     * 表格列
     */
    var columns: List<ElTableColumn>?,

    /**
     * 数据变更时刷新列表，尽量不要与 search 有重复字段
     */
    var filters: List<ElFormItem>?,

    /**
     * 详情页展示方式
     */
    val detail: DetailMode = DetailMode.DIALOG,
){
    enum class DetailMode(
        var value: String
    ){
        DIALOG("dialog"),
        PAGE("page"),
    }
}
