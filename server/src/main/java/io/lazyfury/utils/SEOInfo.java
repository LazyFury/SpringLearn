package io.lazyfury.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SEOInfo {
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
