package io.element.ui

open class ElEditable(
    open var field: String,
    override var componentName: String = "ElEditable",
): ElWidget(
    componentName = componentName
) {
    constructor(componentName: String) : this("", componentName)
}