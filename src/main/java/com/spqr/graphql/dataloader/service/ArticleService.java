package com.spqr.graphql.dataloader.service;

import com.spqr.graphql.dataloader.bean.Article;
import com.spqr.graphql.dataloader.bean.Comment;
import com.spqr.graphql.dataloader.bean.Profile;
import com.spqr.graphql.dataloader.dataloader.DataLoaderConfig;
import com.spqr.graphql.dataloader.repository.ArticleRepository;
import com.spqr.graphql.dataloader.repository.CommentRepository;
import com.spqr.graphql.dataloader.repository.ProfileRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLEnvironment;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.execution.ResolutionEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author ankushnakaskar
 */
@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CommentRepository commentRepository;

    public List<Article> articles() {
        log.info("Fetching all the articles ....!!!!");
        return articleRepository.findAll().stream().collect(Collectors.toList());
    }

    @GraphQLQuery
    public CompletableFuture<Profile> author(@GraphQLContext Article article, @GraphQLEnvironment ResolutionEnvironment env) {
        log.info("Fetching the profile for article..." + article);
        DataLoader<Long, Profile>  dataLoader = env.dataFetchingEnvironment.getDataLoader(DataLoaderConfig.PROFILE_DATA_LOADER);
        final CompletableFuture<Profile> result = dataLoader.load(article.getAuthorId());
        return result;
    }

    @GraphQLQuery
    public CompletableFuture<List<Comment>> getComments(@GraphQLContext Article article, @GraphQLEnvironment ResolutionEnvironment env) {
        log.info("Fetching the comments for article..." + article);
        DataLoader<Long, List<Comment>>  dataLoader = env.dataFetchingEnvironment.getDataLoader(DataLoaderConfig.COMMENT_FOR_ARTICLE_DATA_LOADER);
        final CompletableFuture<List<Comment>> result = dataLoader.load(article.getId());
        return result;
    }

    @GraphQLQuery(name = "article")
    public CompletableFuture<Article> getArticle(@GraphQLArgument(name = "articleId") Long articleId, @GraphQLEnvironment ResolutionEnvironment env) {
        DataLoader<Long, Article>  dataLoader = env.dataFetchingEnvironment.getDataLoader(DataLoaderConfig.ARTICLE_DATA_LOADER);
        final CompletableFuture<Article> result = dataLoader.load(articleId);
        return result;
    }

}
