package io.lazyfury.mall.code.repository;

import io.lazyfury.mall.code.entity.Article;
import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article,Integer>, PagingAndSortingRepository<Article,Integer> {

    @Cacheable(value = "article",key = "#pageable")
    @Nonnull
    @Override
    Page<Article> findAll(@Nonnull Pageable pageable);


    @Nonnull
    @Cacheable(value = "article",key = "#id")
    @Override
    Optional<Article> findById(@Nonnull Integer id);
    Article findByTitle(String title);

    Page<Article> searchByTitleLike(String title, Pageable pageable);
}
