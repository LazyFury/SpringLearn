package io.lazyfury.mall.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Entity
@NoArgsConstructor
@Table(name = "article_tag_ref")
public class ArticleTagRef implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @JsonIgnore
    Article article;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private ArticleTag refTag;

    public ArticleTagRef(Article article, ArticleTag tag) {
        this.article = article;
        this.refTag = tag;
    }
}
