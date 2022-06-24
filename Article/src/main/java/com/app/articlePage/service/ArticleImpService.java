package com.app.articlePage.service;

import com.app.articlePage.Model.Article;

import com.app.articlePage.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleImpService implements IArticleService {
    @Autowired
    private ArticlesRepository articleRepository;

    @Override
    public Article getArticleByCode(long code) {
        Article obj = articleRepository.findByCode(code);
        return obj;
    }
    @Override
    public List<Article> getAllArticles(){
        List<Article> list = new ArrayList<>();
        articleRepository.findAll().forEach(e -> list.add(e));
        return list;
    }
    @Override
    public synchronized boolean addArticle(Article article){
        List<Article> list = articleRepository.findByTitleAndDescription(article.getTitle(), article.getDescription());
        if (list.size() > 0) {
            return false;
        } else {
            article = articleRepository.save(article);
            return true;
        }
    }
    @Override
    public void updateArticle(Article article) {
        articleRepository.save(article);
    }
    @Override
    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }

}
