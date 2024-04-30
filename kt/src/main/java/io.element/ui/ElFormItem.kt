package io.element.ui

import com.fasterxml.jackson.annotation.JsonGetter

data class ElFormItem(
    var label: String,
    val prop: String,
    val placeholder: String = "",
    val component: ElWidget,
    val default: Any? = null,
): ElWidget(
    componentName = "ElFormItem"
) {
    constructor() : this("", "", "",  ElWidget())

    @JsonGetter
    fun name():String{
        return this.prop
    }
}

