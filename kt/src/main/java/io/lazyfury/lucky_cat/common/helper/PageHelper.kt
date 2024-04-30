package io.lazyfury.lucky_cat.common.helper

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.TypeReference
import io.lazyfury.lucky_cat.common.repo.CustomCrudRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

class PageHelper<T : Any>(
    private var crud: CustomCrudRepository<T, Long>,
    val pageKey: String = "page",
    val sizeKey: String = "size",
    val getter: Getter<T> = object : Getter<T>() {}
) {

    data class PageData<T>(
        val total: Long,
        val page: Int,
        val size: Int,
        val pageCount: Long = total / size + 1,
        val data: List<T>,
    )

    companion object {
        //        get search params from body
        fun getParameterMapFromBody(request: HttpServletRequest): Map<String, Any> {
            val text = request.reader.readText()
            if (text.isBlank() || text.isEmpty()) {
                return mapOf()
            }
            val map: Map<String, Any> = JSON.parseObject(
                text,
                object : TypeReference<Map<String, Any>>() {},
            )
            return map
                .mapValues {
                    it.value
                }
        }

//        get search params from query if not found in body

        fun getParameterMap(request: HttpServletRequest): Map<String, Any> {
            val map = getParameterMapFromQuery(request)
            if (map.isEmpty()) {
                return getParameterMapFromBody(request)
            }
            return map
        }


        //        get search params from query
        fun getParameterMapFromQuery(request: HttpServletRequest): Map<String, Any> {
            return request.parameterMap
                .mapValues { it.value[0] }
        }

        fun getPageSize(map: Map<String,Any>,key:String="limit"): Int {
            map.get(key).let {
                return it.toString().toIntOrNull() ?: 1
            }
        }

        fun getPageNumber(map:Map<String,Any>,key:String="page"): Int {
            map.get(key).let {
                return it.toString().toIntOrNull()?.let {
                    if (it > 0) it - 1 else 1
                } ?: 0
            }
        }
    }


    abstract class Getter<T> {
        open fun getBasePageRequest(request: HttpServletRequest): PageRequest {
            return PageRequest.of(
                PageHelper.getPageNumber(getParameterMap(request)),
                PageHelper.getPageSize(getParameterMap(request))
            )
        }

        var params:Map<String,Any>? = null

        open fun getParameterMap(request: HttpServletRequest):Map<String,Any>{
            if(params==null){
                params = PageHelper.getParameterMap(request)
            }
            return params as Map<String, Any>
        }

        open fun getSortPageRequest(pageRequest: PageRequest, request: HttpServletRequest): PageRequest {
            return SortHelper.sort(pageRequest, getParameterMap(request))
        }

        open fun getSearchParams(request: HttpServletRequest): Map<String, Any> {
            return getParameterMap(request).filter {
                !listOf("page","size","limit").contains(it.key)
            }
        }
    }

    /**
     * @param pageable Pageable pageNumber start from 1
     */
    fun get(request: HttpServletRequest): PageData<T> {
        val pageRequest: PageRequest = getter.getBasePageRequest(request)
        val sortPageRequest = getter.getSortPageRequest(pageRequest, request)

        var data: Page<T>;
        var total: Long = 0
        SearchHelper.specification<T>(getter.getSearchParams(request)).run {
            data = crud.findAll(this, sortPageRequest)
            total = crud.count(this)
        }
        return PageData<T>(
            total = total,
            page = sortPageRequest.pageNumber + 1,
            size = sortPageRequest.pageSize,
            data = data.toList(),
        )

    }

    fun main() {

    }
}