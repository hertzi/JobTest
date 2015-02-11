package my.wf.affinitas.rest.controller;

import my.wf.affinitas.rest.RestApplication;
import my.wf.affinitas.rest.transport.MessageData;
import my.wf.affinitas.rest.transport.MessageDataList;
import my.wf.affinitas.rest.transport.UserData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9999")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestMessageControllerTestIT {

    @Value("${server.port}")
    private int port;
    private UserData[] users;
    private UserData sender;
    private UserData recipientFavorite;
    private UserData recipientNotFavorite;
    private UserData favorite;

    private String senderSession;
    private String recipientSession;
    private String subject = "message subject";
    private String text = "message text";


    @Before
    public void setUp() throws Exception {
        users = RestHelper.createListOfUsers(port);
        sender = users[0];
        recipientFavorite = users[1];
        recipientNotFavorite = users[2];
        favorite = users[3];
        senderSession = new RestHelper(port).doLogin("john@doe0", "qwerty").getBody();
        recipientSession = new RestHelper(port).doLogin("john@doe1", "qwerty").getBody();
        new RestHelper(port).withSession(senderSession).doAddNewFavorite(recipientFavorite);
        new RestHelper(port).withSession(senderSession).doAddNewFavorite(favorite);
    }

    @Test
    public void shouldSendMessageToFavorite() throws Exception {
        ResponseEntity<MessageData> response = new RestHelper(port).withSession(senderSession).doSendMessage(recipientFavorite.getEmail(), subject, text);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), allOf(hasProperty("subject", equalTo(subject)), hasProperty("text", equalTo(text))));
        assertThat(response.getBody().getRecipient(), hasProperty("email", equalTo(recipientFavorite.getEmail())));
        assertThat(response.getBody().getSender(), hasProperty("email", equalTo(sender.getEmail())));
    }

    @Test
    public void shouldNotSendMessageToNotFavorite() throws Exception {
        ResponseEntity<MessageData> response = new RestHelper(port).withSession(senderSession).doSendMessage(recipientNotFavorite.getEmail(), subject, text);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    @Test
    public void shouldSeeAllSentMessages() throws Exception {
        new RestHelper(port).withSession(senderSession).doSendMessage(recipientFavorite.getEmail(), subject, text);
        new RestHelper(port).withSession(senderSession).doSendMessage(recipientFavorite.getEmail(), subject + "1", text + "1");
        new RestHelper(port).withSession(senderSession).doSendMessage(recipientFavorite.getEmail(), subject + "2", text + "2");
        new RestHelper(port).withSession(senderSession).doSendMessage(favorite.getEmail(), subject + "3", text + "3");

        ResponseEntity<MessageDataList> response = new RestHelper(port).withSession(senderSession).doGetSentMessages();
        assertEquals(4, response.getBody().getData().size());
    }

    @Test
    public void shouldSeeOnlyPersonalMessages() throws Exception {
        new RestHelper(port).withSession(senderSession).doSendMessage(recipientFavorite.getEmail(), subject, text);
        new RestHelper(port).withSession(senderSession).doSendMessage(recipientFavorite.getEmail(), subject + "1", text + "1");
        new RestHelper(port).withSession(senderSession).doSendMessage(recipientFavorite.getEmail(), subject + "2", text + "2");
        new RestHelper(port).withSession(senderSession).doSendMessage(favorite.getEmail(), subject + "3", text + "3");

        ResponseEntity<MessageDataList> response = new RestHelper(port).withSession(recipientSession).doGetReceivedMessages();
        assertEquals(3, response.getBody().getData().size());
    }
}