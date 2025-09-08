package dev.guru.MailForgeAI.service;

import dev.guru.MailForgeAI.dto.EmailReplyRequest;
import dev.guru.MailForgeAI.dto.EmailReplyResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyForgeService {

    private final ChatClient chatClient;

    @Autowired
    public ReplyForgeService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public EmailReplyResponse generateReply(EmailReplyRequest emailReplyRequest) {
        EmailReplyResponse emailReplyResponse = new EmailReplyResponse();
        String reply = chatClient.prompt(emailReplyRequest.getEmail()).call().chatResponse().getResult().getOutput().getText();
        System.out.println("reply: " + reply);
        System.out.println(emailReplyRequest.getEmail());
        emailReplyResponse.setResponse(reply);
        return emailReplyResponse;
    }
}
