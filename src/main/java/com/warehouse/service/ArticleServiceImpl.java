package com.warehouse.service;

import com.warehouse.mapper.ArticleMapper;
import com.warehouse.model.Article;
import com.warehouse.model.dto.ArticleDTO;
import com.warehouse.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService{

    ArticleRepository articleRepository;
    ArticleMapper articleMapper;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public Iterable<ArticleDTO> findAllDTOS() {

        List<ArticleDTO> articleDTOS = new ArrayList<>();
        Iterable<Article> articles = articleRepository.findAll();

        for( Article article: articles){
            articleDTOS.add( articleMapper.toArticleDTO( article));
        }

        return articleDTOS;
    }

    @Override
    public ArticleDTO saveDTO(ArticleDTO articleDTO) {

        Article article = articleMapper.toArticle( articleDTO);
        article = articleRepository.save( article);

        return articleMapper.toArticleDTO( article);
    }

    @Override
    public Optional<ArticleDTO> findDTOByEan(String ean) {
        Optional<Article> article = articleRepository.findByEan( ean);

        if( article.isPresent()){
            ArticleDTO articleDTO = articleMapper.toArticleDTO(article.get());
            return Optional.of( articleDTO);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save( article);
    }

    @Override
    public Optional<Article> adjustStock(String ean, int amount) throws Exception {
        Optional<Article> article = articleRepository.findByEan( ean);
        if(article.isEmpty()){
            return Optional.empty();
        }

        if( amount > article.get().getStock()){
            throw new Exception("Amount greater than stock");
        }

        article.get().setStock();
        return null;
    }
}
