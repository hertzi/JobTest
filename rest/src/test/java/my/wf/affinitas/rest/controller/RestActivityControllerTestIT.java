package my.wf.affinitas.rest.controller;

import my.wf.affinitas.rest.RestApplication;
import my.wf.affinitas.rest.transport.UserData;
import my.wf.affinitas.rest.transport.UserDataList;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9999")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestActivityControllerTestIT {

    @Value("${server.port}")
    private int port;

    private String activeSession;
    private UserData[] users;

    @Before
    public void setUp() throws Exception {
        users = RestHelper.createListOfUsers(port);
        activeSession = new RestHelper(port).doLogin("john@doe2", "qwerty").getBody();
    }

    @Test
    public void shouldAccessUserListAfterLogin() throws Exception {
        ResponseEntity<UserDataList> response = new RestHelper(port).withSession(activeSession).doGetUserList();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getData().size());
    }

    @Test
    public void shouldNotAccessUserListBeforeLogin() throws Exception {
        ResponseEntity<UserDataList> response = new RestHelper(port).withoutSession().doGetUserList();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void shouldNotAccessUserListWithWrongSession() throws Exception {
        ResponseEntity<UserDataList> response = new RestHelper(port).withSession("wrong session").doGetUserList();
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void shouldNotAccessUserListAfterLogout() throws Exception {
        new RestHelper(port).withSession(activeSession).doLogout();
        ResponseEntity<UserDataList> response = new RestHelper(port).withSession(activeSession).doGetUserList();
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void shouldAccessFavoritesAfterLogin() throws Exception {
        ResponseEntity<UserDataList> response = new RestHelper(port).withSession(activeSession).doGetFavorites();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldNotAccessFavoritesAfterLogout() throws Exception {
        new RestHelper(port).withSession(activeSession).doLogout();
        ResponseEntity<UserDataList> response = new RestHelper(port).withSession(activeSession).doGetFavorites();
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void shouldAddNewFavoriteAndSeeInList() throws Exception {
        ResponseEntity<UserDataList> checkResponse = new RestHelper(port).withSession(activeSession).doGetFavorites();
        assertEquals(HttpStatus.OK, checkResponse.getStatusCode());
        new RestHelper(port).withSession(activeSession).doAddNewFavorite(users[3]);

        checkResponse = new RestHelper(port).withSession(activeSession).doGetFavorites();
        assertEquals(HttpStatus.OK, checkResponse.getStatusCode());
        assertEquals(1, checkResponse.getBody().getData().size());


        new RestHelper(port).withSession(activeSession).doAddNewFavorite(users[4]);
        checkResponse = new RestHelper(port).withSession(activeSession).doGetFavorites();
        assertEquals(HttpStatus.OK, checkResponse.getStatusCode());
        assertEquals(2, checkResponse.getBody().getData().size());
    }

}