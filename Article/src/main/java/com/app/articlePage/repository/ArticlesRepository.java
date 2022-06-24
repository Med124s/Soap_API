package com.app.articlePage.repository;

import com.app.articlePage.Model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticlesRepository extends JpaRepository<Article,Long> {
    Article findByCode(long code);
    List<Article> findByTitleAndDescription(String title, String description);
    Page<Article> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);

}
