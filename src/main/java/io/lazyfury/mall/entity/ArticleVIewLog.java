package io.lazyfury.mall.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVIewLog extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne
    private Article article;

    public ArticleVIewLog(Article article) {
        this.article = article;
    }
}
