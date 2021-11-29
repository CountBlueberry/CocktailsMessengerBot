package com.blackou.Cocktailschatbot.service;

import com.botscrew.messengercdk.model.MessengerUser;
import com.botscrew.messengercdk.model.outgoing.request.Request;
import com.botscrew.messengercdk.model.outgoing.builder.SenderAction;
import com.botscrew.messengercdk.service.TokenizedSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SenderService {

    @Value("${facebook.messenger.access-token}")
    private String accessToken;
    @Autowired
    private TokenizedSender sender;

    public void sendMessage(Request request, MessengerUser user) {
        sender.send(accessToken, SenderAction.typingOn(user));
        sender.send(accessToken, request);
    }
}
