package io.lazyfury.mall.service;

import io.lazyfury.mall.entity.Article;
import io.lazyfury.mall.entity.ArticleVIewLog;
import io.lazyfury.mall.repository.ArticleRepository;
import io.lazyfury.mall.repository.ArticleTagRefRepository;
import io.lazyfury.mall.repository.ArticleTagRepository;
import io.lazyfury.mall.repository.ArticleViewLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
     * {
     * "date": "2020",
     * "months": [
     * date: "2020-01",
     * days: [
     * date: "2020-01-01",
     * count: 1,
     * articles: []
     * ]
     * ]
     * }
     */
    public List<HashMap<String, Object>> archiveYearMonthAndDay() {
        List<Article> articles = (List<Article>) articleRepository.findAll();

        var sortKeyByLocalDate = Comparator.comparing(LocalDate::toString).reversed();

        var yearGroupArticles = articles.stream().collect(Collectors.groupingBy(v -> v.getCreated().getYear(), Collectors.toList()));
        var yearGroupKeys = yearGroupArticles.keySet().stream().sorted(Comparator.reverseOrder()).toList();
        List<HashMap<String, Object>> years = new ArrayList<>();
        for (var key : yearGroupKeys) {
            var entry = yearGroupArticles.get(key);
            List<HashMap<String, Object>> months = new ArrayList<>();
            var monthGroupArticles = entry.stream().collect(Collectors.groupingBy(v -> v.getCreated().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.toList()));
            var monthGroupKeys = monthGroupArticles.keySet().stream().sorted(Comparator.comparing(String::toString).reversed()).toList();
            for (var monthKey : monthGroupKeys) {
                var monthEntry = monthGroupArticles.get(monthKey);
                List<HashMap<String, Object>> days = new ArrayList<>();
                var dayGroupArticles = monthEntry.stream().collect(Collectors.groupingBy(v -> v.getCreated().toLocalDate(), Collectors.toList()));
                List<LocalDate> dayGroupKeys = dayGroupArticles.keySet().stream().sorted(sortKeyByLocalDate).toList();
                for (var dayKey : dayGroupKeys) {
                    var dayEntry = dayGroupArticles.get(dayKey);
                    var map = new HashMap<String, Object>();
                    map.put("date", dayKey);
                    map.put("count", dayEntry.size());
                    map.put("articles", dayEntry);
                    days.add(map);
                }
                var map = new HashMap<String, Object>();
                map.put("date", monthKey);
                map.put("days", days);
                map.put("count", days.size());
                months.add(map);
            }
            var map = new HashMap<String, Object>();
            map.put("date", key);
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