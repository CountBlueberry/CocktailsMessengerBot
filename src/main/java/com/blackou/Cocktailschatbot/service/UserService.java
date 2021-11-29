package com.blackou.Cocktailschatbot.service;

import com.blackou.Cocktailschatbot.constant.ChatState;
import com.blackou.Cocktailschatbot.entity.User;
import com.blackou.Cocktailschatbot.repository.UserRepo;
import com.botscrew.messengercdk.model.MessengerUser;
import com.botscrew.messengercdk.service.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserProvider {
    private final UserRepo userRepo;

    public User createUser(Long chatId) {
        User user = userRepo.findByChatId(chatId);
        if (user == null) {
            user = new User();
            user.setChatId(chatId);
            user.setState(ChatState.DEFAULT);
            userRepo.save(user);
        }
        return user;
    }

    public void changeState(User user, String state) {
        user.setState(state);
        userRepo.save(user);
    }

    @Override
    public MessengerUser getByChatIdAndPageId(Long chatId, Long pageId) {
        return createUser(chatId);
    }
}
