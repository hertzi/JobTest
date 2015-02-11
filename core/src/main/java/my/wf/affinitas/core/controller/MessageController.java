package my.wf.affinitas.core.controller;

import my.wf.affinitas.core.error.NotFavorite;
import my.wf.affinitas.core.model.Message;
import my.wf.affinitas.core.model.User;
import my.wf.affinitas.core.repository.MessageRepository;
import my.wf.affinitas.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;

    public Message sendMessage(User sender, User recipient, String subject, String text){
        if(!checkFavorite(sender, recipient)){
            throw new NotFavorite();
        }
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setSubject(subject);
        message.setText(text);
        return messageRepository.save(message);
    }

    public List<Message> getSentMessages(User sender){
        return messageRepository.getSentMessages(sender);
    }

    public List<Message> getReceivedMessages(User recipient){
        return messageRepository.getReceivedMessages(recipient);
    }

    protected boolean checkFavorite(User owner, User favorite){
        return 1 == userRepository.isFavorite(owner, favorite);
    }
}
