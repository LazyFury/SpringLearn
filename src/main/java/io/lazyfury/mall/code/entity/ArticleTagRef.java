package io.lazyfury.mall.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
public class ArticleTagRef {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne
    @JoinColumn(name = "article_id",nullable = false)
    @JsonIgnore
    Article article;

    @ManyToOne
    @JoinColumn(name = "tag_id",nullable = false)
    ArticleTag refTag;

    public ArticleTagRef(Article article, ArticleTag tag){
        this.article = article;
        this.refTag = tag;
    }
}
