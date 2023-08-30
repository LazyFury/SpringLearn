package io.lazyfury.mall.code.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.lazyfury.mall.code.convert.ArticleJsonConverter;
import io.lazyfury.mall.code.convert.HashMapConverter;
import io.lazyfury.mall.code.convert.UuidConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Article extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="article_id")
    long id;

    @Comment(value = "标题")
    @Nonnull
    @Schema(name = "title",description = "Title of the article")
    private  String title;

    @Column(length = 1500)
    private  String description;

    @Lob
    @Column(columnDefinition = "text")
    String Content;

    @Column(length = 3200,columnDefinition = "text")
    @Convert(converter = ArticleJsonConverter.class)
    ArticleJsonConfig config;

    @Column(columnDefinition = "text")
    @Convert(converter = HashMapConverter.class)
    HashMap<String,Object> ext;


    @OneToMany(mappedBy = "article",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    List<ArticleTagRef> tags = new ArrayList<ArticleTagRef>();

    @Transient
    List<Long> tag_ids;
    @PrePersist
    void beforeCreate(){
        var map  = new HashMap<String,Object>();
        map.put("hello",1);
        map.put("img","/sxxx/ss.png");
        ext = map;
    }
}
