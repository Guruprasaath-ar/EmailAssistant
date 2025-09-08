package dev.guru.MailForgeAI.dto;

import org.springframework.stereotype.Component;

@Component
public class EmailReplyRequest {

    private String email;
    private String tone;

    public  EmailReplyRequest(){

    }

    public EmailReplyRequest(Builder builder){
        this.email=builder.email;
        this.tone=builder.tone;
    }

    public String getEmail() {
        return email;
    }

    public String getTone() {
        return tone;
    }

    public static class Builder{
        private String email;
        private String tone;

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setTone(String tone) {
            this.tone = tone;
            return this;
        }

        public EmailReplyRequest build(){
            return new EmailReplyRequest(this);
        }
    }
}
