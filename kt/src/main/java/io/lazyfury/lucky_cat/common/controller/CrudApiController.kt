package io.lazyfury.lucky_cat.common.controller

import com.alibaba.excel.EasyExcel
import io.lazyfury.lucky_cat.common.helper.PageHelper
import io.lazyfury.lucky_cat.common.helper.SearchHelper
import io.lazyfury.lucky_cat.common.repo.CustomCrudRepository
import io.lazyfury.lucky_cat.common.reponse.ApiJsonResponse
import io.lazyfury.lucky_cat.common.utils.validate.Rule
import io.lazyfury.lucky_cat.common.utils.validate.Validator
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.io.File

open class CrudApiController<T : Any>(
    var repo: CustomCrudRepository<T, Long>,
    var disabledDelete: Boolean = false,
    val addRules: Map<String, List<Rule>> = mapOf(),
    val updateRules: Map<String, List<Rule>> = mapOf(),
    val getId: (T) -> Long = { 0 },
    val className: Class<T>?,
    val exportName: String
) {

    @GetMapping("/list")
    fun list(
        request: HttpServletRequest,
    ): ApiJsonResponse<PageHelper.PageData<T>> {
        return ApiJsonResponse.success(
            PageHelper<T>(repo).get(request)
        )
    }

    @PostMapping("/create")
    fun create(@RequestBody data: T): ApiJsonResponse<T> {
        return createImpl(data)
    }

    open fun createImpl(data: T): ApiJsonResponse<T> {
        Validator(addRules).validate(data)
        println(data)
        val saved = repo.save(data)
        return ApiJsonResponse.success(saved)
    }

    @PutMapping("/update")
    fun update(@RequestBody data: T): ApiJsonResponse<T> {
        return updateImpl(data)
    }

    open fun updateImpl(data: T): ApiJsonResponse<T> {
        Validator(updateRules).validate(data)
        val id = getId(data)
        val find = repo.findById(id)
        if (find.isEmpty) {
            return ApiJsonResponse.error(message = "not found")
        }
        find.get().let {
            val saved = repo.save(data!!)
            return ApiJsonResponse.success(saved)
        }

    }

    data class DeleteDto(
        var ids: List<Long>
    )

    @DeleteMapping("/delete")
    fun delete(@RequestBody delete: DeleteDto): ApiJsonResponse<Boolean> {
        return deleteImpl(delete)
    }

    open fun deleteImpl(delete: DeleteDto): ApiJsonResponse<Boolean> {
        if (disabledDelete) {
            return ApiJsonResponse.error(message = "disabled delete")
        }
        repo.deleteAllById(delete.ids)
        return ApiJsonResponse.success(message = "success")
    }


    @GetMapping("/export")
    fun export(response: HttpServletResponse, request: HttpServletRequest): ByteArray {
        val dateStr = System.currentTimeMillis().toString()
        val path = "temp/export"
        val filename = "$path/$exportName-$dateStr.xlsx"
        if (!File(path).exists()) {
            File(path).mkdirs()
        }
        println(request.parameterMap.mapValues { it.value[0] })
        SearchHelper.specification<T>(request.parameterMap.mapValues { it.value[0] }).run {
            val data: MutableIterable<T> = repo.findAll(this)
            val list = data.toList()
            EasyExcel.write(filename, className)
                .sheet("sheet1").doWrite(list)
            response.contentType = "application/octet-stream"
            return File(filename).readBytes()
        }
    }
}