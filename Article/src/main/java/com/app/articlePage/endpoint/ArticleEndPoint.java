package com.app.articlePage.endpoint;

import com.app.articlePage.Model.Article;
import com.app.articlePage.gs_ws.*;
import com.app.articlePage.service.IArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.util.ArrayList;
import java.util.List;

@Endpoint
@CrossOrigin(value = "http://localhost:4200")
public class ArticleEndPoint {
     private static final String NAMESPACE_URI = "http://www.concretepage.com/article-ws";
        @Autowired
        private IArticleService articleService;
        //une method payloadRoot qui accept incoming request
        @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getArticleByCodeRequest")
        @ResponsePayload
        public GetArticleByCodeResponse getArticle(@RequestPayload GetArticleByCodeRequest request) {
            GetArticleByCodeResponse  response = new GetArticleByCodeResponse();
            ArticleInfo articleInfo = new ArticleInfo();
            BeanUtils.copyProperties(articleService.getArticleByCode(request.getCode()), articleInfo);
            response.setArticleInfo(articleInfo);
            return response;
        }

        @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllArticlesRequest")
        @ResponsePayload
        public GetAllArticlesResponse getAllArticles() {
            GetAllArticlesResponse response = new GetAllArticlesResponse();
            List<ArticleInfo> articleInfoList = new ArrayList<>();
            List<Article> articleList = articleService.getAllArticles();
            for (int i = 0; i < articleList.size(); i++) {
                ArticleInfo ob = new ArticleInfo();
                BeanUtils.copyProperties(articleList.get(i), ob);
                articleInfoList.add(ob);
            }
            response.getArticleInfo().addAll(articleInfoList);
            return response;
        }

        @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addArticleRequest")
        @ResponsePayload
        public AddArticleResponse addArticle(@RequestPayload AddArticleRequest request) {
            AddArticleResponse response = new AddArticleResponse();
            System.out.println("*********adding image name"+request.getImageUrl());
            ServiceStatus serviceStatus = new ServiceStatus();
            Article article = new Article();
            if(request.getImageUrl().equals(null)){
                article.setImageUrl("unknown.jpg");
            }
            article.setImageUrl(request.getImageUrl());
            article.setTitle(request.getTitle());
            article.setDescription(request.getDescription());
            article.setPrice(request.getPrice());
            article.setQuantity(request.getQuantity());
            boolean flag = articleService.addArticle(article);
            if (flag == false) {
                serviceStatus.setStatusCode("CONFLICT");
                serviceStatus.setMessage("Article is Already Available");
                response.setServiceStatus(serviceStatus);
            } else {
                ArticleInfo articleInfo = new ArticleInfo();
                BeanUtils.copyProperties(article, articleInfo);
                response.setArticleInfo(articleInfo);
                serviceStatus.setStatusCode("SUCCESS");
                serviceStatus.setMessage("Article Added Successfully");
                response.setServiceStatus(serviceStatus);
            }
            return response;
        }

        @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateArticleRequest")
        @ResponsePayload
        public UpdateArticleResponse updateArticle(@RequestPayload UpdateArticleRequest request) {
            System.out.println("*****************************Request update********* is :");
            System.out.println(request.getArticleInfo());
            Article article = new Article();
            BeanUtils.copyProperties(request.getArticleInfo(), article);
            articleService.updateArticle(article);
            ServiceStatus serviceStatus = new ServiceStatus();
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Updated Successfully");
            UpdateArticleResponse response = new UpdateArticleResponse();
            response.setServiceStatus(serviceStatus);
            return response;
        }

        @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteArticleRequest")
        @ResponsePayload
        public DeleteArticleResponse deleteArticle(@RequestPayload DeleteArticleRequest request) {
            System.out.println("********** delete id "+request.getCode());
            Article article = articleService.getArticleByCode(request.getCode());
            ServiceStatus serviceStatus = new ServiceStatus();
            if (article == null) {
                serviceStatus.setStatusCode("FAIL");
                serviceStatus.setMessage("Content Not Available");
            } else {
                articleService.deleteArticle(article);
                serviceStatus.setStatusCode("SUCCESS");
                serviceStatus.setMessage("Content Deleted Successfully");
            }
            DeleteArticleResponse response = new DeleteArticleResponse();
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }

