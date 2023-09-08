package io.lazyfury.mall.code.api;

import io.lazyfury.mall.code.entity.ArticleTag;
import io.lazyfury.mall.code.repository.ArticleTagRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article-tag")
@Tag(name = "文章标签管理Api")
public class ArticleTagApi {

    @Autowired
    ArticleTagRepository articleTagRepository;

    @Operation(
            description = "add new article tag",
            summary = "添加文章标签")
    @PostMapping("/add")
    public ArticleTag add(@RequestBody ArticleTag articleTag) {
        articleTagRepository.save(articleTag);
        return articleTag;
    }

}
