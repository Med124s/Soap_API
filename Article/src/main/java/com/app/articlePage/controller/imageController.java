package com.app.articlePage.controller;

import com.app.articlePage.Model.Article;
import com.app.articlePage.payload.response.MessageResponse;
import com.app.articlePage.repository.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@CrossOrigin(value = "*",maxAge = 3600)
@RestController
@RequestMapping("/api/articles")
public class imageController {
    @Autowired
    ArticlesRepository articlesRepository;
    @GetMapping(path = "/getImage/{id}",produces = MediaType.IMAGE_PNG_VALUE)
    public byte[]image(@PathVariable(name = "id")long code) throws IOException {
        Article article = articlesRepository.findByCode(code);
        if(article.getImageUrl() ==null){
            article.setImageUrl("unknown.jpg");
            articlesRepository.save(article);
        }
        System.out.println("***************"+article.getImageUrl());
        return Files.readAllBytes(Paths.get(System.getProperty("user.home")+"/article/images/"+article.getImageUrl()));
    }
    @PostMapping(path = "/uploadPhoto/{id}")
    public void upload(MultipartFile file, @PathVariable(name = "id") long code) throws Exception{
        Article article = articlesRepository.findByCode(code);
        article.setImageUrl(code+"png");
        Files.write(Paths.get(System.getProperty("user.home")+"/article/images"),file.getBytes());
        articlesRepository.save(article);
    }
    @PutMapping("/update/{idProduct}")
    public ResponseEntity<Article> updateProduct(@PathVariable("idProduct") long idProduct, @RequestBody Article art){
        Optional<Article> article = articlesRepository.findById(idProduct);
        if(article.isPresent()){
            Article _art = article.get();
            _art.setTitle(art.getTitle());
            _art.setDescription(art.getDescription());
            _art.setPrice(art.getPrice());
            _art.setQuantity(art.getQuantity());
            return new ResponseEntity<>(articlesRepository.save(_art), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{code}")
    public ResponseEntity<?>deleteProduct(@PathVariable("code") long idProduct){
        if(!articlesRepository.existsById(idProduct)){
            return ResponseEntity.badRequest().body(new MessageResponse("Error Article is Not exist !"));
        }
        try {
            articlesRepository.deleteById(idProduct);
            return ResponseEntity.ok(new MessageResponse("Article deleted successfully!"));
        }
        catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
