package io.lazyfury.mall.code.repository;

import io.lazyfury.mall.code.entity.Article;
import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Integer>, PagingAndSortingRepository<Article, Integer> {

    @Cacheable(value = "article", key = "#pageable")
    @Nonnull
    @Override
    Page<Article> findAll(@Nonnull Pageable pageable);

    @Cacheable(value = "article", key = "#title")
    Page<Article> findByTitleContainingAndDescriptionContaining(String title, String description, Pageable pageable);


    @Cacheable(value = "article", key = "#tagId")
    @Query(value = "select a.* from article_tag_ref t left join article a on t.article_id = a.article_id where t.tag_id = :tag_id order by a.created DESC", nativeQuery = true,
            countQuery = "select count(a.article_id) from article_tag_ref t left join article a on t.article_id = a.article_id where t.tag_id = :tag_id")
    Page<Article> findByTag(@Param("tag_id") Long tagId, Pageable pageable);

    @Nonnull
    @Cacheable(value = "article", key = "#id")
    @Override
    Optional<Article> findById(@Nonnull Integer id);

    Article findByTitle(String title);

    Page<Article> searchByTitleLike(String title, Pageable pageable);


}
