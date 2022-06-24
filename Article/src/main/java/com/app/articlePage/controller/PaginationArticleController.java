package com.app.articlePage.controller;

import com.app.articlePage.Model.Article;
import com.app.articlePage.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/searchArticle")
public class PaginationArticleController {
    @Autowired
    ArticlesRepository articlesRepository;
    @GetMapping("/search")
    public ResponseEntity<Map<String,Object>> searchByMc(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String desc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    )
    {
        try {
            List<Article> articles = new ArrayList<>();
            Pageable pageable = PageRequest.of(page, size);
            Page<Article> _page;
            if (title == null || desc == null) {
                _page = articlesRepository.findAll(pageable);
            } else
                _page = articlesRepository.findByTitleContainingOrDescriptionContaining(title, desc, pageable);

            articles = _page.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("articles", articles);
            response.put("totalPages", _page.getTotalPages());
            response.put("totalItem", _page.getTotalElements());
            response.put("currentPage", _page.getNumber());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
