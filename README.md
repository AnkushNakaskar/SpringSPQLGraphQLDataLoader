* This project of spring graphql for SPQR library with data loader.
* Highlighted thing is the Dataloader config class.
    * In this class We have registered the dataloader in last function.
    * In this class ,You can see the comments for Artist dataloader,Which return the list of list of comments.
    * Basically,The data loader is used for to solve the N+1 problem in graphQL.
    * It caches the result for query ,Need to investigate how to evict that cache.
    * if you cant to see the caching is happening,Have a debug point in data loader invoked the GraphQL API.
        * Only first time the dataloader is invoked ,Second time the cache valued are return.
    * Try to have Dataloader in all the services to fetch data where you think,Batching should be use.    
* Also check for Service class like ArticleService.java
    * This class has   following code snippet
         ```
           @GraphQLQuery
             public CompletableFuture<List<Comment>> getComments(@GraphQLContext Article article, @GraphQLEnvironment ResolutionEnvironment env) {
                 log.info("Fetching the comments for article..." + article);
                 DataLoader<Long, List<Comment>>  dataLoader = env.dataFetchingEnvironment.getDataLoader(DataLoaderConfig.COMMENT_FOR_ARTICLE_DATA_LOADER);
                 final CompletableFuture<List<Comment>> result = dataLoader.load(article.getId());
                 return result;
             }
         ```
      * Check for GraphQLEnvironment annontations and how that particular dataloader is use in service.
* You can refer the Link for More detail points read :
    * https://github.com/graphql-java/java-dataloader 
    * https://stackoverflow.com/questions/47674981/how-to-resolve-graphql-n1-issue-with-graphql-jpa-javat
    * https://github.com/leangen/graphql-spqr/blob/master/src/test/java/io/leangen/graphql/BatchingTest.java
    * https://github.com/graphql-java-kickstart/graphql-java-tools/issues/58
    * https://github.com/leangen/graphql-spqr-spring-boot-starter
    * Must read for authorization : https://github.com/Blacktoviche/springboot-graphql-sqqr-jwt-demo
    
* You can refer the input json (postman from SpringGraphQL project).
* This project uses SPQR library just to automate the schema defination so that there should not be any miss match between resolver and query definations.
* Very soon this library will be integrated in spring boot starter.
* Classes to look into
    * Controller
      * check for registration in controller for every GraphQL API call.
    * Configuration
* Input Json for API : http://localhost:8080/graphiql
    * Query :
       ```
       query AllArticles {
         articles {
           id
           title
           text
           author {
             id
             username
           }
           comments {
             id
           }
         }
       }
      
      
      Query Variables :
       {
         "articleId": 1
       }
       
This is very awesome project i came across.Next project would be around the graphql subscription example.
