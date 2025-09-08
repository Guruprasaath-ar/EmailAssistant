package dev.guru.MailForgeAI.dto;

import org.springframework.stereotype.Component;

@Component
public class EmailReplyResponse {

    private String response;

    public EmailReplyResponse(){

    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public EmailReplyResponse(String response) {
        this.response = response;
    }
}
