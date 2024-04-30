package io.lazyfury.lucky_cat.core.exception

import io.lazyfury.lucky_cat.common.reponse.ApiJsonResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.sql.SQLException


@ControllerAdvice
class SQLExceptionHandler {

    @ExceptionHandler(SQLException::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    fun handleSQLErrorException(error:SQLException): ApiJsonResponse<Any> {
        val code = error.sqlState
        var message = error.message

        if (code.startsWith("23")){
//            FOREIGN KEY (`vip_id`) REFERENCES `user_vip_card`
            val regex = Regex("FOREIGN KEY \\(`(\\w+)`\\) REFERENCES `(\\w+)`")
            val match = regex.find(message ?: "")
            if (match != null){
                val (column, table) = match.destructured
                message = "外键约束违反:$table.$column"
            }else{
                message = "数据库约束违反:$message"
            }
        }

        if (code.startsWith("22")){
            message = "数据完整性约束违反:$message"
        }

        if (code.startsWith("42")){
            message = "语法错误:$message"
        }

        if (code.startsWith("21")){
            message = "数据操作错误:$message"
        }

        if (code.startsWith("25")){
            message = "无效事务:$message"
        }

        if (code.startsWith("40")){
            message = "事务回滚:$message"
        }

        if (code.startsWith("53")){
            message = "数据库无效操作:$message"
        }

        if (code.startsWith("08")){
            message = "连接错误:$message"
        }

        if (code.startsWith("09")){
            message = "触发器错误:$message"
        }

        if (code.startsWith("0A")){
            message = "特权错误:$message"
        }

        println("sql err;code:$code,message:$message")
        return ApiJsonResponse.error<Any>(message = message.toString())
    }

}