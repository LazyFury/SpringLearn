package io.lazyfury.mall.code.controller


import io.lazyfury.mall.code.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import kotlin.jvm.optionals.getOrNull


@RequestMapping("/blog")
@Controller
class ArticleController {

    @Autowired
    lateinit var repo:ArticleRepository

    @GetMapping("")
    fun getArticles(@RequestParam(defaultValue = "1") page: Int): ModelAndView{
        val view = ModelAndView("blog")
        view.addObject("articles",repo.findAll(PageRequest.of(page-1,10)))
        return view
    }

    @GetMapping("/{id}") fun articleDetail(@PathVariable id:Int):ModelAndView{
        val view = ModelAndView("blog/detail")
        view.addObject("detail",repo.findById(id).getOrNull())
        return view
    }
}