package io.lazyfury.mall.repository;

import io.lazyfury.mall.entity.Article;
import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Integer>, PagingAndSortingRepository<Article, Integer> {

    @Cacheable(value = "article", key = "#pageable")
    @Nonnull
    @Override
    Page<Article> findAll(@Nonnull Pageable pageable);

    @Cacheable(value = "article", key = "#title")
    Page<Article> findByTitleContainingAndDescriptionContaining(String title, String description, Pageable pageable);

    @Query(value = "select a.* from article a where date_format(a.created,'%Y-%m-%d') = :days order by a.created DESC", nativeQuery = true,
            countQuery = "select count(a.article_id) from article a where date_format(a.created,'%Y-%m-%d') = :days")
    Page<Article> findByCreated(@Param("days") String days, Pageable pageable);

    @Query(value = "select a.* from article a where date_format(a.created,'%Y-%m') = :month order by a.created DESC", nativeQuery = true,
            countQuery = "select count(a.article_id) from article a where date_format(a.created,'%Y-%m') = :month")
    Page<Article> findByCreatedByMonth(@Param("month") String month, Pageable pageable);

    @Query(value = "select a.* from article a where date_format(a.created,'%Y') = :year order by a.created DESC", nativeQuery = true,
            countQuery = "select count(a.article_id) from article a where date_format(a.created,'%Y') = :year")
    Page<Article> findByCreatedByYear(@Param("year") String year, Pageable pageable);

    @Cacheable(value = "article", key = "#tagId")
    @Query(value = "select a.* from article_tag_ref t left join article a on t.article_id = a.article_id where t.tag_id = :tag_id order by a.created DESC", nativeQuery = true,
            countQuery = "select count(a.article_id) from article_tag_ref t left join article a on t.article_id = a.article_id where t.tag_id = :tag_id")
    Page<Article> findByTag(@Param("tag_id") Long tagId, Pageable pageable);

    @Nonnull
    @Cacheable(value = "article_detail", key = "#id")
    @Override
    Optional<Article> findById(@Nonnull Integer id);

    @Cacheable(value = "article_detail", key = "#title")
    Optional<Article> findByTitle(String title);


    Page<Article> searchByTitleLike(String title, Pageable pageable);


    @Query(value = """
            select a.*,r.count  from articleview_log al\s
            inner join (select MAX(al2.id) max_id,COUNT(1) count,   al2.article_article_id from articleview_log al2 group by al2.article_article_id) r ON  r.max_id = al.id
            LEFT  JOIN article a on a.article_id = al.article_article_id\s
            ORDER  by al.created DESC LIMIT 10""",
            nativeQuery = true)
    List<Article> LastViewedArticles();

}

