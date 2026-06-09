package com.Souvik.EmbeddingRouter.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Iterator;


@Service
public class PdfReadService {
private final SimpleVectorStore vectorStore;
private final SimpleVectorStore routerVectorStore;
private final ChatClient chatClient;

    public PdfReadService( @Qualifier("vectorStore")
                           SimpleVectorStore vectorStore,
                           @Qualifier("routerVectorStore")
                           SimpleVectorStore routerVectorStore,
                           ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.routerVectorStore = routerVectorStore;
        this.chatClient = chatClient;
    }
    public void readPdf(MultipartFile file) throws IOException {
        Resource resource = new InputStreamResource(file.getInputStream());
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
        List<Document> documents = reader.get();
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(documents);
        for(Document chunk : chunks)
        {
            chunk.getMetadata()
                    .put(
                            "fileName",
                            file.getOriginalFilename()
                    );
        }
        vectorStore.add(chunks);
        vectorStore.save(new File("vector-store.json"));
        addToRouterVectorStore(file.getOriginalFilename(),generateDesc(chunks));
        System.out.println("Documents : " + documents.size());
        System.out.println("Chunks : " + chunks.size());
        System.out.println("PDF loaded into VectorStore");
    }
    //this method creates desc for a pdf file
    public String generateDesc(List<Document> chunks)
    {
        StringBuilder content=new StringBuilder();
        for(int i=0;i<Math.min(10,chunks.size());i++)
        {
            content.append(chunks.get(i).getText());
        }
        String prompt=
                """
                Generate a two-line description
                of the following document.
        
                %s
        
                Keep the description under
                30 words.
                """
                        .formatted(content);
        String description=chatClient
                .prompt(prompt)
                .call()
                .content();
        return description;
    }
    public void addToRouterVectorStore(String fileName,String description)
    {
        Document doc=new Document(description);
        doc.getMetadata().put("fileName",fileName);
        routerVectorStore.add(List.of(doc));
        routerVectorStore.save(new File("router-vector-store.json"));
    }
    public boolean isAlreadyUploaded(String fileName) throws IOException {

        File file = new File("router-vector-store.json");

        if (!file.exists()) {
            return false;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        Iterator<JsonNode> iterator = root.elements();
        while (iterator.hasNext()) {
            JsonNode document = iterator.next();
            JsonNode metadata = document.get("metadata");
            if (metadata != null
                    && metadata.has("fileName")
                    && metadata.get("fileName")
                    .asText()
                    .equals(fileName)) {
                return true;
            }
        }
        return false;
    }
    public List<String> getUploadedFiles() throws IOException {

        List<String> files = new ArrayList<>();
        File file = new File("router-vector-store.json");
        if (!file.exists()) {
            return files;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(file);
        Iterator<JsonNode> iterator = root.elements();
        while (iterator.hasNext()) {
            JsonNode document = iterator.next();
            JsonNode metadata = document.get("metadata");
            if (metadata != null
                    && metadata.has("fileName")) {
                files.add(
                        metadata.get("fileName").asText()
                );
            }
        }

        return files;
    }
}
