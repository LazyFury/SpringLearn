package io.lazyfury.mall.code.service;

import io.lazyfury.mall.code.entity.Article;
import io.lazyfury.mall.code.entity.ArticleVIewLog;
import io.lazyfury.mall.code.repository.ArticleRepository;
import io.lazyfury.mall.code.repository.ArticleTagRefRepository;
import io.lazyfury.mall.code.repository.ArticleTagRepository;
import io.lazyfury.mall.code.repository.ArticleViewLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleTagRepository articleTagRepository;

    @Autowired
    ArticleTagRefRepository articleTagRefRepository;

    @Autowired
    ArticleViewLogRepository articleViewLogRepository;

    /**
     * Archive year month day
     *
     * @return List<HashMap < String, Object>>
     */
    public List<HashMap<String, Object>> archiveYearMonthAndDay() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        var yearGroupArticles = articles.stream().collect(Collectors.groupingBy(v -> v.getCreated().getYear(), Collectors.toList()));
        List<HashMap<String, Object>> years = new ArrayList<>();
        for (var entry : yearGroupArticles.entrySet()) {
            List<HashMap<String, Object>> months = new ArrayList<>();
            var monthGroupArticles = entry.getValue().stream().collect(Collectors.groupingBy(v -> v.getCreated().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.toList()));
            for (var monthEntry : monthGroupArticles.entrySet()) {
                List<HashMap<String, Object>> days = new ArrayList<>();
                var dayGroupArticles = monthEntry.getValue().stream().collect(Collectors.groupingBy(v -> v.getCreated().toLocalDate(), Collectors.toList()));
                for (var dayEntry : dayGroupArticles.entrySet()) {
                    var map = new HashMap<String, Object>();
                    map.put("date", dayEntry.getKey());
                    map.put("count", dayEntry.getValue().size());
                    map.put("articles", dayEntry.getValue());
                    days.add(map);
                }
                var map = new HashMap<String, Object>();
                map.put("date", monthEntry.getKey());
                map.put("days", days);
                map.put("count", days.size());
                months.add(map);
            }
            var map = new HashMap<String, Object>();
            map.put("date", entry.getKey());
            map.put("months", months);
            map.put("count", months.size());
            years.add(map);
        }
        return years;
    }


    public Optional<Article> findByTitle(String title) {
        var article = articleRepository.findByTitle(title);
        article.ifPresent(a -> {
            a.setViewsCount((a.getViewsCount()) + 1);
            articleRepository.save(a);
            articleViewLogRepository.save(new ArticleVIewLog(a));
        });
        return article;
    }

    public List<HashMap<String, Object>> archive() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        var groupArticles = articles.stream().collect(Collectors.groupingBy(v -> v.getCreated().toLocalDate(), Collectors.toList()));
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (var entry : groupArticles.entrySet()) {
            var map = new HashMap<String, Object>();
            map.put("date", entry.getKey());
            map.put("articles", entry.getValue());
            map.put("count", entry.getValue().size());
            result.add(map);
        }
        return result;
    }

}

