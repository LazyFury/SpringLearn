package io.lazyfury.mall.code.controller;

import io.lazyfury.mall.code.aspect.AddSidebarData;
import io.lazyfury.mall.code.repository.ArticleRepository;
import io.lazyfury.mall.code.repository.ArticleTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/blog")
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleTagRepository articleTagRepository;

    //获取文章列表
    @AddSidebarData
    @GetMapping("")
    public ModelAndView getArticleList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description,
            ModelAndView model
    ) {
        model.setViewName("blog");
        model.addObject("articles", articleRepository.findByTitleContainingOrDescriptionContaining(
                title, description, PageRequest.of(page, size)
        ));
        return model;
    }


    //按标签获取文章列表
    @AddSidebarData
    @GetMapping("/tags/{id}")
    public ModelAndView getArticleListByTag(@PathVariable("id") Long id,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            ModelAndView model
    ) {
        model.setViewName("blog");
        model.addObject("tag", articleTagRepository.findById(id).orElse(null));
        model.addObject("articles", articleRepository.findByTag(id, PageRequest.of(page - 1, size)));
        return model;
    }

    //获取文章详情
    @AddSidebarData
    @GetMapping("/{id}")
    public ModelAndView getArticle(@PathVariable("id") Integer id,
                                   ModelAndView model) {
        model.setViewName("blog/detail");
        model.addObject("article", articleRepository.findById(id).orElseThrow());
        return model;
    }
}
