package org.example.chatgpt;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

@Configuration
public class VectorStoreConfig {

    private static final File STORE_FILE = new File("berserk-vector-store.json");

    @Value("classpath:/templates/manga.txt")
    private Resource knowledgeBaseResource;

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        if (STORE_FILE.exists()) {
            vectorStore.load(STORE_FILE);
        } else {
            TextReader textReader = new TextReader(knowledgeBaseResource);
            textReader.getCustomMetadata().put("source", "manga.txt");
            List<Document> chunks = TokenTextSplitter.builder().build().apply(textReader.get());
            vectorStore.add(chunks);
            vectorStore.save(STORE_FILE);
        }
        return vectorStore;
    }
}
