package io.lazyfury.mall.code.entity;

import com.alibaba.fastjson.annotation.JSONField;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArticleTag extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    @JSONField(name = "tag_id")
    long id;

    public ArticleTag(String name) {
        this.name = name;
    }

    public ArticleTag(long id) {
        this.id = id;
    }

    public String name;
}
