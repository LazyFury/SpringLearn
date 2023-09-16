package io.lazyfury.mall.repository;

import io.lazyfury.mall.entity.ArticleTag;
import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {

    @Cacheable(value = "articleTag")
    @Override
    @Nonnull
    List<ArticleTag> findAll();
}
