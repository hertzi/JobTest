package my.wf.affinitas.rest.controller;

import my.wf.affinitas.rest.RestApplication;
import my.wf.affinitas.rest.transport.UserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:9999")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestSecurityControllerTestIT {

    @Value("${server.port}")
    private int port;

    /**
     * Needs only for understanding that system is working
     * @throws Exception
     */
    @Test
    public void testGetTest() throws Exception {
        ResponseEntity<String> response = new TestRestTemplate().exchange("http://localhost:"+port+"/test", HttpMethod.GET, null, String.class);
        assertEquals("TEST OK", response.getBody());
    }

    @Test
    public void shouldCreateNewUserAndReturnDataWithoutIdAndPassword() throws Exception {
        ResponseEntity<UserData> response = new RestHelper(port).withoutSession().doRegistration("john@doe", "John", "Doe", "qwerty");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), hasProperty("email", equalTo("john@doe")));
        assertThat(response.getBody(), hasProperty("name", equalTo("John")));
        assertThat(response.getBody(), hasProperty("lastName", equalTo("Doe")));
        assertThat(response.getBody(), not(hasProperty("password")));
        assertThat(response.getBody(), not(hasProperty("id")));
    }

    @Test
    public void shouldNotCreateNewUserWithExistingEmail() throws Exception {
        ResponseEntity<UserData> response1 = new RestHelper(port).withoutSession().doRegistration("john@doe", "John", "Doe", "qwerty");
        ResponseEntity<UserData> response2 = new RestHelper(port).withoutSession().doRegistration("john@doe", "John", "Doe", "qwerty");
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    public void shouldLogin() throws Exception {
        RestHelper.createListOfUsers(port);
        ResponseEntity<String> response = new RestHelper(port).withoutSession().doLogin("john@doe2", "qwerty");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(32, response.getBody().trim().length());
    }

    @Test
    public void shouldLoginTwice() throws Exception {
        RestHelper.createListOfUsers(port);
        ResponseEntity<String> response1 = new RestHelper(port).withoutSession().doLogin("john@doe2", "qwerty");
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        String session1 = response1.getBody().trim();
        ResponseEntity<String> response2 = new RestHelper(port).withoutSession().doLogin("john@doe2", "qwerty");
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        String session2 = response2.getBody().trim();
        assertNotEquals(session1, session2);
    }

    @Test
    public void shouldNotLogin() throws Exception {
        RestHelper.createListOfUsers(port);
        ResponseEntity<String> response = new RestHelper(port).withoutSession().doLogin("john@doe2", "wrong password");
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void shouldLogoutWithSessionInContext() throws Exception {
        RestHelper.createListOfUsers(port);
        ResponseEntity<String> loginResponse = new RestHelper(port).withoutSession().doLogin("john@doe2", "qwerty");
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        String session = loginResponse.getBody().trim();

        ResponseEntity<String> logoutResponse = new RestHelper(port).withSession(session).doLogout();
        assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());
        assertEquals("Bye.", logoutResponse.getBody());
    }

    @Test
    public void shouldNotLogoutWithWrongSessionInContext() throws Exception {
        RestHelper.createListOfUsers(port);
        ResponseEntity<String> loginResponse = new RestHelper(port).withoutSession().doLogin("john@doe2", "qwerty");
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        ResponseEntity<String> logoutResponse = new RestHelper(port).withSession("wrong session").doLogout();
        assertEquals(HttpStatus.FORBIDDEN, logoutResponse.getStatusCode());
    }

    @Test
    public void shouldNotLogoutWithoutSessionInContext() throws Exception {
        RestHelper.createListOfUsers(port);
        ResponseEntity<String> loginResponse = new RestHelper(port).withoutSession().doLogin("john@doe2", "qwerty");
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        ResponseEntity<String> logoutResponse = new RestHelper(port).withoutSession().doLogout();
        assertEquals(HttpStatus.BAD_REQUEST, logoutResponse.getStatusCode());
    }

    @Test
    public void shouldNotLogoutTwiceWithSameSessionId(){
        RestHelper.createListOfUsers(port);
        ResponseEntity<String> loginResponse = new RestHelper(port).withoutSession().doLogin("john@doe2", "qwerty");
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        String session = loginResponse.getBody().trim();

        ResponseEntity<String> logoutResponse1 = new RestHelper(port).withSession(session).doLogout();
        assertEquals(HttpStatus.OK, logoutResponse1.getStatusCode());
        assertEquals("Bye.", logoutResponse1.getBody());

        ResponseEntity<String> logoutResponse2 = new RestHelper(port).withSession(session).doLogout();
        assertEquals(HttpStatus.FORBIDDEN, logoutResponse2.getStatusCode());
    }
}
