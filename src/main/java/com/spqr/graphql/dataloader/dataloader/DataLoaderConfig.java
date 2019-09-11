package com.spqr.graphql.dataloader.dataloader;

import com.spqr.graphql.dataloader.bean.Article;
import com.spqr.graphql.dataloader.bean.Comment;
import com.spqr.graphql.dataloader.bean.Profile;
import com.spqr.graphql.dataloader.repository.ArticleRepository;
import com.spqr.graphql.dataloader.repository.CommentRepository;
import com.spqr.graphql.dataloader.repository.ProfileRepository;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * @author ankushnakaskar
 */
@Configuration
public class DataLoaderConfig {

    public static final String PROFILE_DATA_LOADER = "profileDataLoader";
    public static final String ARTICLE_DATA_LOADER = "articleDataLoader";
    public static final String COMMENT_DATA_LOADER = "commentDataLoader";
    public static final String COMMENT_FOR_ARTICLE_DATA_LOADER = "commentsForArticleDataLoader";

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Bean(name = "commentDataLoader")
    public DataLoader<Long, Comment> commentDataLoader() {
        BatchLoader<Long, Comment> commentBatchLoader = new BatchLoader<Long, Comment>() {
            @Override
            public CompletionStage<List<Comment>> load(List<Long> commentIds) {
                return CompletableFuture.supplyAsync(() -> {
                    final List<Comment> result = commentRepository.findAllById(commentIds);
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(commentBatchLoader);
    }

    @Bean(name = "commentsForArticleDataLoader")
    public DataLoader<Long, List<Comment>> commentsForArticleDataLoader() {
        BatchLoader<Long, List<Comment>> commentBatchLoader = new BatchLoader<Long, List<Comment>>() {
            @Override
            public CompletionStage<List<List<Comment>>> load(List<Long> articleIds) {
                return CompletableFuture.supplyAsync(() -> {
                    final List<List<Comment>> response =new LinkedList<>();
                    final List<Comment> result = commentRepository.findByArticleIdIn(articleIds);
                    if(!CollectionUtils.isEmpty(result)){
                        Map<Long, List<Comment>> mapOfComments = result.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(Comment::getArticleId));
                        mapOfComments.keySet().stream().forEach(key->{
                            List<Comment> comments = mapOfComments.get(key);
                            response.add(comments);
                        });
                    }
                    return response;
                });
            }
        };
        return DataLoader.newDataLoader(commentBatchLoader);

    }


    @Bean(name = "profileDataLoader")
    public DataLoader<Long, Profile> profileDataLoader() {
        BatchLoader<Long, Profile> userBatchLoader = new BatchLoader<Long, Profile>() {
            @Override
            public CompletionStage<List<Profile>> load(List<Long> profileId) {
                return CompletableFuture.supplyAsync(() -> {
                    final List<Profile> result = profileRepository.findAllById(profileId);
                    return result;
                });
            }
        };

        return DataLoader.newDataLoader(userBatchLoader);

    }

    @Bean(name = "articleDataLoader")
    public DataLoader<Long, Article> articleDataLoader() {
        BatchLoader<Long, Article> articleBatchLoader = new BatchLoader<Long, Article>() {
            @Override
            public CompletionStage<List<Article>> load(List<Long> articleIds) {
                return CompletableFuture.supplyAsync(() -> {
                    final List<Article> result = articleRepository.findAllById(articleIds);
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(articleBatchLoader);
    }

    @Bean
    public DataLoaderRegistry registerDataLoaders() {
        final DataLoaderRegistry loaders = new DataLoaderRegistry();
        loaders.register(PROFILE_DATA_LOADER, profileDataLoader());
        loaders.register(ARTICLE_DATA_LOADER, articleDataLoader());
        loaders.register(COMMENT_DATA_LOADER, commentDataLoader());
        loaders.register(COMMENT_FOR_ARTICLE_DATA_LOADER,commentsForArticleDataLoader());
        return loaders;

    }


}
