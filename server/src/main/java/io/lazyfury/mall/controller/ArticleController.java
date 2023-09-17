package io.lazyfury.mall.controller;

import io.lazyfury.mall.repository.ArticleRepository;
import io.lazyfury.mall.repository.ArticleTagRepository;
import io.lazyfury.mall.aspect.AddSidebarData;
import io.lazyfury.mall.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    ArticleService articleService;

    @Autowired
    ArticleTagRepository articleTagRepository;

    //获取文章列表
    @AddSidebarData
    @GetMapping("")
    public ModelAndView getArticleList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(defaultValue = "") String description,
            ModelAndView model
    ) {
        model.setViewName("blog");
        model.addObject("articles", articleRepository.findByTitleContainingAndDescriptionContaining(
                title, description, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"))
        ));
        return model;
    }

    //获取文章归档
    @AddSidebarData
    @GetMapping("/archive")
    public ModelAndView getArticleArchive(ModelAndView model,
                                          @RequestParam(defaultValue = "") String created
    ) {
        model.setViewName("blog");
        if (created.length() == 4) {
            model.addObject("articles", articleRepository.findByCreatedByYear(created, PageRequest.of(0, 10)));
        } else if (created.length() == 7) {
            model.addObject("articles", articleRepository.findByCreatedByMonth(created, PageRequest.of(0, 10)));
        } else if (created.length() == 10) {
            model.addObject("articles", articleRepository.findByCreated(created, PageRequest.of(0, 10)));
        } else {
            model.setViewName("blog_archive");
            model.addObject("articles", articleService.archiveYearMonthAndDay());
        }
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
//    @AddSidebarData
//    @GetMapping("/{id}")
//    public ModelAndView getArticle(@PathVariable("id") Integer id,
//                                   ModelAndView model) {
//        model.setViewName("blog/detail");
//        model.addObject("article", articleRepository.findById(id).orElseThrow());
//        return model;
//    }

    @AddSidebarData
    @GetMapping("/{title}")
    public ModelAndView getArticle(@PathVariable("title") String title,
                                   ModelAndView model) {
        model.setViewName("blog/detail");
        model.addObject("article", articleService.findByTitle(title).orElseThrow());
        return model;
    }
}
