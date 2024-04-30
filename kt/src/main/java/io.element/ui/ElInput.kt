package io.element.ui

data class ElInput(
    val type: String,
    val placeholder: String,
    override var field: String,
): ElEditable(
    field = field,
    componentName = "ElInput"
);
