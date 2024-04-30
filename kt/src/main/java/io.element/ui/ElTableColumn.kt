package io.element.ui

import com.fasterxml.jackson.annotation.JsonGetter

class ElTableColumn(
    var label: String,
    var prop: String,
    var width: Long = 100,
    var align: String = "center",
    var sortable: Boolean = false,
    var resizable: Boolean = false,
    var className: String = "",
    val type:String = ""
) {


    @JsonGetter
    fun key(): String {
        return prop
    }
}