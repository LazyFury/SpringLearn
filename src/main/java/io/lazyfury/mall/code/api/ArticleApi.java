package io.lazyfury.mall.code.api;

import io.lazyfury.mall.code.entity.Article;
import io.lazyfury.mall.code.entity.ArticleTag;
import io.lazyfury.mall.code.entity.ArticleTagRef;
import io.lazyfury.mall.code.repository.ArticleRepository;
import io.lazyfury.mall.code.repository.ArticleTagRepository;
import io.lazyfury.mall.code.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 文章
 */
@RestController
@RequestMapping(value = "/api/articles")
@Tag(name = "文章管理Api")
public class ArticleApi {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleTagRepository articleTagRepository;

    @Operation(
            description = "add new article",
            summary = "添加文章")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "404", description = "fail", content = {@Content(examples = @ExampleObject(name = "result"))})
    @PostMapping("/add")
    public Article AddArticle(@RequestBody Article article) {
        article.setTags(null);

        var tags = new ArrayList<ArticleTagRef>();
        for (int i = 0; i < article.getTag_ids().size(); i++) {
            var id = article.getTag_ids().get(i);
            tags.add(new ArticleTagRef(article, new ArticleTag(id)));
        }
        article.setTags(tags);

        articleRepository.save(article);
        return article;
    }

    @GetMapping("/search")
    public Iterable<Article> searchArticle(@RequestParam(defaultValue = "") String title) {
        return articleRepository.searchByTitleLike(String.format("%%%s%%", title), PageRequest.of(0, 10));
    }


    @GetMapping("/{id}")
    public Article GetArticleByName(@PathVariable(value = "id") Integer id) {
        System.out.printf("%s\n", id);
        return articleRepository.findById(id).orElseThrow();
    }

    @GetMapping("/count")
    public @ResponseBody HashMap<String, Long> count() {
        var count = articleRepository.count();
        var map = new HashMap<String, Long>();
        map.put("count", count);
        return map;
    }

    @GetMapping(value = "/all")
    public @ResponseBody Page<Article> allArticle(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created")));
    }

    @GetMapping("/archive")
    public List<HashMap<String, Object>> archive() {
        return articleService.archive();
    }
}
