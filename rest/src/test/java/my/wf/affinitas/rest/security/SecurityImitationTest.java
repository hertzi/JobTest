package my.wf.affinitas.rest.security;

import my.wf.affinitas.core.model.User;
import my.wf.affinitas.rest.error.BadSession;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SecurityImitationTest {

    private static final Long ID1 = 1L;
    private static final Long ID2 = 2L;
    private static final Long ID3 = 3L;

    private String session1;
    private String session2;
    private String session3;


    @Autowired
    SecurityImitation securityImitation = new SecurityImitation();

    @Before
    public void setUp() throws Exception {
        session1 = securityImitation.addUserSession(createUser(ID1));
        session2 = securityImitation.addUserSession(createUser(ID2));
        session3 = securityImitation.addUserSession(createUser(ID3));
    }

    @Test
    public void shouldRunWithoutException() throws Exception {
        securityImitation.checkSession(session1);
    }

    @Test(expected = BadSession.class)
    public void shouldRunWithBadSessionException() throws Exception {
        securityImitation.checkSession("no session");
    }

    @Test
    public void testGetUserBySession() throws Exception {
        assertThat(securityImitation.getUserBySession(session2), hasProperty("id", equalTo(ID2)));
    }

    @Test
    public void shouldNotReturnUserBySession() throws Exception {
        assertNull(securityImitation.getUserBySession("no session"));
    }

    @Test
    public void shouldCreateTwoSessions() throws Exception {
        String session = securityImitation.addUserSession(createUser(ID1));
        assertNotNull(session);
        assertNotEquals(session1, session);
    }

    @Test
    public void testKillSession() throws Exception {
        assertNotNull(securityImitation.getUserBySession(session2));
        securityImitation.killSession(session2);
        assertNull(securityImitation.getUserBySession(session2));
    }

    private User createUser(Long id){
        User user = new User();
        user.setId(id);
        user.setEmail(id + "@email");
        return user;
    }
}