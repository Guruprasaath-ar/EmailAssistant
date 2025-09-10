package dev.guru.MailForgeAI.service;

import dev.guru.MailForgeAI.dto.EmailReplyRequest;
import dev.guru.MailForgeAI.dto.EmailReplyResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class ReplyForgeService {

    private final ChatClient chatClient;

    @Autowired
    public ReplyForgeService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    private static Prompt getPrompt(EmailReplyRequest emailReplyRequest) {
        String prompt = """
            You are a professional email communication specialist
            with extensive experience in crafting formal and effective email replies.

            Maintain a {tone} tone.

            Write ONLY the reply message body based on the following email input.
            Do not include subject lines or placeholder fields like [Your Name].

            Email Input:
            {input}
            """;

        Map<String, Object> variables = Map.of(
                "tone", emailReplyRequest.getTone(),
                "input", emailReplyRequest.getEmail()
        );

        return new PromptTemplate(prompt).create(variables);
    }

    public EmailReplyResponse generateReply(EmailReplyRequest emailReplyRequest) {

        Prompt prompt = getPrompt(emailReplyRequest);

        // Send to chatClient
        String reply =  Objects.requireNonNull(chatClient.prompt(prompt)
                        .call()
                        .chatResponse())
                        .getResult()
                        .getOutput()
                        .getText();

        // Wrap in your response object
        EmailReplyResponse emailReplyResponse = new EmailReplyResponse();
        emailReplyResponse.setResponse(reply);
        return emailReplyResponse;
    }
}
