package com.spqr.graphql.dataloader.repository;

import com.spqr.graphql.dataloader.bean.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author ankushnakaskar
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findById(Long id);
    void deleteById(Long id);
}

