package io.lazyfury.lucky_cat.admin.ui.base

import com.fasterxml.jackson.annotation.JsonGetter

data class Api(
    val create:String,
    val update:String,
    val delete:String,
    val list:String,
    val detail:String,
    val export:String = ""
){

    @JsonGetter
    fun createAble():Boolean{
        return create.isNotBlank()
    }

    @JsonGetter
    fun updateAble():Boolean{
        return update.isNotBlank()
    }

    @JsonGetter
    fun deleteAble():Boolean{
        return delete.isNotBlank()
    }

    @JsonGetter
    fun exportAble():Boolean{
        return export.isNotBlank()
    }
}
