package io.lazyfury.lucky_cat.admin.ui.base

class Menu(
    var name: String,
    var icon: String,
    var children: List<Menu> = listOf(),
    var hidden: Boolean = false,
    var disabled: Boolean = false,
    var meta: Map<String, Any> = mapOf(),
    val table: Table? = null
) {
}