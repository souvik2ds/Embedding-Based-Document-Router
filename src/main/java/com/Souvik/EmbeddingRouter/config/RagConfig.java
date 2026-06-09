package com.Souvik.EmbeddingRouter.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class RagConfig {
    @Bean
    public SimpleVectorStore vectorStore(EmbeddingModel embeddingModel)
    {
        SimpleVectorStore vectorStore= SimpleVectorStore.builder(embeddingModel).build();
        File file=new File("vector-store.json");
        if(file.exists())
        {
            vectorStore.load(file);
            System.out.println("File loaded from disk");
        }
        return vectorStore;
    }
    @Bean
    public SimpleVectorStore routerVectorStore(EmbeddingModel embeddingModel)
    {
        SimpleVectorStore routerVectorStore=SimpleVectorStore
                .builder(embeddingModel)
                .build();
        File file=new File("router-vector-store.json");
        if(file.exists())
        {
            routerVectorStore.load(file);
            System.out.println("Description embeddings loaded from disk");
        }
        return routerVectorStore;
    }
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder)
    {
        return builder.build();
    }
}
