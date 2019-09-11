package com.spqr.graphql.dataloader.repository;

import com.spqr.graphql.dataloader.bean.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author ankushnakaskar
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);

    List<Comment> findByArticleIdIn(List<Long> articleId);
    void deleteById(Long id);
}
