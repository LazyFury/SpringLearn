package io.lazyfury.mall.api;

import io.lazyfury.mall.entity.ArticleTag;
import io.lazyfury.mall.repository.ArticleTagRepository;
import io.lazyfury.utils.response.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article-tag")
@Tag(name = "文章标签管理Api")
public class ArticleTagApi {

    @Autowired
    ArticleTagRepository articleTagRepository;

    @Operation(
            description = "add new article tag",
            summary = "添加文章标签")
    @PostMapping("/add")
    public JsonResponse<ArticleTag> add(@RequestBody ArticleTag articleTag) {
        articleTagRepository.save(articleTag);
        return JsonResponse.success(articleTag);
    }

}
