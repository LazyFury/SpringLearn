package io.lazyfury.lucky_cat.admin.ui.base

import com.alibaba.fastjson2.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonGetter

class Route(
    val path: String,
    val component: String,
    val name: String,
    val menu: Menu? = null,
    val meta: Meta? = null,
    val children: List<Route> = listOf(),
) {
}