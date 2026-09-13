package org.example.chatgpt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final ChatClient chatClient;
    private final QuestionAnswerAdvisor questionAnswerAdvisor;

    public RagController(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore).build();
    }

    public record RagRequest(String question) {
    }

    public record RagResponse(String answer) {
    }

    @PostMapping("/api/rag/query")
    public RagResponse query(@RequestBody RagRequest request) {
        String answer = chatClient.prompt()
                .advisors(questionAnswerAdvisor)
                .user(request.question())
                .call()
                .content();
        return new RagResponse(answer);
    }
}
