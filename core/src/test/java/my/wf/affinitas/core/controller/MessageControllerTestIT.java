package my.wf.affinitas.core.controller;

import my.wf.affinitas.CoreApplication;
import my.wf.affinitas.core.error.NotFavorite;
import my.wf.affinitas.core.model.Message;
import my.wf.affinitas.core.model.User;
import my.wf.affinitas.core.repository.MessageRepository;
import my.wf.affinitas.core.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreApplication.class)
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageControllerTestIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageController messageController;
    @Autowired
    MessageRepository messageRepository;

    private User sender;
    private User favorite1;
    private User favorite2;
    private User favorite3;
    private User notFavorite;

    @Before
    public void setUp() throws Exception {
        sender = createUser("sender", "n1", "l1");
        favorite1 = createUser("favorite1", "n2", "l2");
        favorite2 = createUser("favorite2", "n3", "l3");
        favorite3 = createUser("favorite3", "n4", "l4");
        notFavorite = createUser("notFavorite", "n5", "l5");
        addFavorites(sender, favorite1, favorite2, favorite3);
        addFavorites(favorite1, sender);
        addFavorites(notFavorite, favorite1);
    }

    @Test
    public void shouldSendMessageToFavorite() throws Exception {
        Message message = messageController.sendMessage(sender, favorite1, "subj1", "text1");
        assertThat(message, hasProperty("id", greaterThan(0L)));
        assertThat(message, hasProperty("sender", equalTo(sender)));
        assertThat(message, hasProperty("recipient", equalTo(favorite1)));
        assertThat(message, hasProperty("subject", equalTo("subj1")));
        assertThat(message, hasProperty("text", equalTo("text1")));
    }

    @Test(expected = NotFavorite.class)
    public void shouldNotSendMessageToNotFavorite() throws Exception {
        messageController.sendMessage(sender, notFavorite, "subj1", "text1");
    }

    @Test
    public void testGetSentMessages() throws Exception {
        Message message1 = createMessage(sender, favorite1, "s1", "t1");
        Message message2 = createMessage(sender, favorite1, "s2", "t2");
        Message message3 = createMessage(sender, favorite2, "s11", "t11");
        Message message4 = createMessage(notFavorite, favorite1, "s12", "t12");
        List<Message> sentMessages = messageController.getSentMessages(sender);
        assertThat(sentMessages, hasItems(message1, message2, message3));
        assertThat(sentMessages, not(hasItems(message4)));
    }

    @Test
    public void testGetReceivedMessages() throws Exception {
        Message message1 = createMessage(sender, favorite1, "s1", "t1");
        Message message2 = createMessage(sender, favorite1, "s2", "t2");
        Message message3 = createMessage(sender, favorite2, "s11", "t11");
        Message message4 = createMessage(notFavorite, favorite1, "s12", "t12");
        List<Message> receivedMessages = messageController.getReceivedMessages(favorite1);
        assertThat(receivedMessages, hasItems(message1, message2, message4));
        assertThat(receivedMessages, not(hasItems(message3)));
    }

    @Transactional
    protected User createUser(String email, String name, String lastName) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    @Transactional
    protected Message createMessage(User sender, User recipient, String subject, String text){
        Message message = new Message();
        message.setRecipient(recipient);
        message.setSender(sender);
        message.setSubject(subject);
        message.setText(text);
        return messageRepository.save(message);
    }

    @Transactional
    protected User addFavorites(User owner, User... favorites){
        owner = userRepository.getUserWithFavorites(owner);
        for (User favorite : favorites) {
            owner.getFavorites().add(favorite);
        }
        return userRepository.save(owner);

    }
}