package io.element.ui

data class ElSwitch(
    override var field:String
): ElEditable(
    field = field,
    componentName = "ElSwitch"
) {
}