package io.element.ui

import com.fasterxml.jackson.annotation.JsonGetter


open class ElFormLayout(
) {
    @JsonGetter("rows")
    open fun rows(): List<List<ElWidget>> {
        return listOf()
    }
}
