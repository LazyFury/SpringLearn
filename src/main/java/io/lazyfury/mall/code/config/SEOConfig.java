package io.lazyfury.mall.code.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SEOConfig {
    String title;
    String description;
    List<String> keywords;
    String author;

    /**
     * @return String
     */
    public String getKeywordsString() {
        return String.join(", ", keywords);
    }
}
