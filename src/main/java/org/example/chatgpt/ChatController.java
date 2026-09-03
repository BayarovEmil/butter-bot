package org.example.chatgpt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {
    private final ChatClient client;

    public ChatController(ChatClient.Builder builder) {
        this.client = builder.build();
    }

    @GetMapping("/")
    public String home() {
        return "chat";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String message, Model model) {
        model.addAttribute("question", message);
        try {
            String answer = client.prompt().user(message).call().content();
            model.addAttribute("answer", answer);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage() == null ? "Something went wrong." : e.getMessage());
        }
        return "chat";
    }
}
