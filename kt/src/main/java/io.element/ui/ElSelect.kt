package io.element.ui

data class ElSelect(
    val options: List<String>,
    val value: String
): ElWidget(
    componentName = "ElSelect"
)
