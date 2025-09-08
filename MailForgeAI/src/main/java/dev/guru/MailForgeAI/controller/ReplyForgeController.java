package dev.guru.MailForgeAI.controller;

import dev.guru.MailForgeAI.dto.EmailReplyRequest;
import dev.guru.MailForgeAI.dto.EmailReplyResponse;
import dev.guru.MailForgeAI.service.ReplyForgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyForgeController {

    private final ReplyForgeService replyForgeService;

    @Autowired
    public  ReplyForgeController(ReplyForgeService replyForgeService) {
        this.replyForgeService = replyForgeService;
    }

    @PostMapping("/")
    public EmailReplyResponse emailReply(@RequestBody EmailReplyRequest emailReplyRequest) {
        return replyForgeService.generateReply(emailReplyRequest);
    }
}
