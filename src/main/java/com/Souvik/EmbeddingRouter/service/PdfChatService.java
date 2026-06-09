package com.Souvik.EmbeddingRouter.service;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service
public class PdfChatService {
    private final ChatClient chatClient;
    private final SimpleVectorStore vectorStore;
    private final SimpleVectorStore routerVectorStore;

    public PdfChatService(ChatClient chatClient,
                          @Qualifier("vectorStore")
                          SimpleVectorStore vectorStore,
                          @Qualifier("routerVectorStore")
                          SimpleVectorStore routerVectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.routerVectorStore = routerVectorStore;
    }
    public String ask(String question,String fileName) {
        SearchRequest request =
                SearchRequest.builder()
                        .query(question)
                        .topK(2)
                        .filterExpression(
                                "fileName == '" + fileName + "'"
                        )
                        .build();

        List<Document> res = vectorStore.similaritySearch(request);
        StringBuilder context = new StringBuilder();
        for (Document r : res) {
            context.append(r.getText()).append("\n");
        }
        String prompt=
                """
           Answer the question using only the context below.
           Before start answering introduce youself within 1 line.
   
           Context:
           %s
   
           Question:
           %s
           """
                        .formatted(context.toString(), question);
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }
    public String askAuto(String question)
    {
        return ask(question,getRelevantFileName(question));
    }
    public String getRelevantFileName(String question)
    {
        SearchRequest request =
                SearchRequest.builder()
                        .query(question)
                        .topK(1)
                        .build();
        List<Document> list=routerVectorStore.similaritySearch(request);
        if(list.isEmpty())
        {
            throw new RuntimeException(
                    "No relevant document found."
            );
        }
        String fileName= list.get(0).getMetadata().get("fileName").toString();
        return fileName;
    }

}
