package com.app.articlePage.service;

import com.app.articlePage.Model.Article;

import java.util.List;

public interface IArticleService {
    List<Article> getAllArticles();
    Article getArticleByCode(long code);
    boolean addArticle(Article article);
    void updateArticle(Article article);
    void deleteArticle(Article article);
}
