package io.element.ui


/**
 * 适合枚举值
 */
data class ElRadioButtonGroup(
    override var field:String,
    val options:List<Map<String,Any>>? = null,
    val remoteDataApi:String? = null,
): ElEditable(
    componentName = "ElRadioButtonGroup"
){

    companion object {
        var SKIP = "skip__all"
    }

}