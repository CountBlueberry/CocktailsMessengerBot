package com.blackou.Cocktailschatbot.builder;


import com.botscrew.messengercdk.model.MessengerUser;
import com.botscrew.messengercdk.model.outgoing.builder.ButtonTemplate;
import com.botscrew.messengercdk.model.outgoing.builder.GenericTemplate;
import com.botscrew.messengercdk.model.outgoing.builder.QuickReplies;
import com.botscrew.messengercdk.model.outgoing.builder.TextMessage;
import com.botscrew.messengercdk.model.outgoing.element.TemplateElement;
import com.botscrew.messengercdk.model.outgoing.element.button.Button;
import com.botscrew.messengercdk.model.outgoing.element.quickreply.QuickReply;
import com.botscrew.messengercdk.model.outgoing.request.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestBuilder {

    public Request buildRequestForTemplate(MessengerUser user, TemplateElement element, List<QuickReply> quickReplies) {
        return GenericTemplate.builder()
                .addElement(element)
                .quickReplies(quickReplies)
                .user(user)
                .build();
    }

    public Request buildRequestForTemplate(MessengerUser user, List<TemplateElement> elements, List<QuickReply> quickReplies) {
        return GenericTemplate.builder()
                .elements(elements)
                .quickReplies(quickReplies)
                .user(user)
                .build();
    }

    public Request buildRequestForButtonReply(MessengerUser user, List<Button> postbackButtons, String text) {
        return ButtonTemplate.builder()
                .user(user)
                .text(text)
                .buttons(postbackButtons)
                .build();
    }

    public Request buildRequestForQuickReply(MessengerUser user, List<QuickReply> quickReplies, String text) {
        return QuickReplies.builder()
                .user(user)
                .text(text)
                .quickReplies(quickReplies)
                .build();
    }

    public Request buildTextRequest(MessengerUser user, String text) {
        return TextMessage.builder()
                .user(user)
                .text(text)
                .build();
    }

}
