package my.wf.affinitas.core.controller;

import my.wf.affinitas.CoreApplication;
import my.wf.affinitas.core.error.EmailIsAlreadyPresent;
import my.wf.affinitas.core.model.SecurityData;
import my.wf.affinitas.core.model.User;
import my.wf.affinitas.core.repository.SecurityDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreApplication.class)
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SecurityControllerTestIT {
    private static final String email = "un@server";
    private static final String password = "p1";
    private static final String name = "n1";
    private static final String lastName = "ln1";
    private static final long MINIMAL_ID = 0;


    @Autowired
    SecurityController securityController;
    @Autowired
    SecurityDataRepository securityDataRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldReturnUserWithId() throws Exception {
        User actual = securityController.registerNewUser(email, name, lastName, password);
        assertThat(actual, hasProperty("id", greaterThan(MINIMAL_ID)));
        assertThat(actual, hasProperty("email", equalTo(email)));
        assertThat(actual, hasProperty("name", equalTo(name)));
        assertThat(actual, hasProperty("lastName", equalTo(lastName)));
    }

    @Test
    public void shouldContainSecurityDataForUser() throws Exception {
        User user = securityController.registerNewUser(email, name, lastName, password);
        SecurityData actual = securityDataRepository.getByLoginName(email);
        assertThat(actual, hasProperty("id", equalTo(user.getId())));
        assertThat(actual, hasProperty("loginName", equalTo(user.getEmail())));
        assertThat(actual, hasProperty("password", equalTo(password)));
        assertThat(actual, hasProperty("user", equalTo(user)));
    }

    @Test(expected = EmailIsAlreadyPresent.class)
    public void shouldNotCreateTwoRecordsForSameEmail() throws Exception {
        securityController.registerNewUser(email, name, lastName, password);
        securityController.registerNewUser(email, name + "1", lastName + "1", password + "1");
    }

    @Test
    public void testCheckEmailIsFree_True(){
        securityController.registerNewUser(email, name, lastName, password);
        securityController.checkEmailIsPresent(email);
    }

    @Test
    public void testCheckEmailIsFree_False(){
        securityController.registerNewUser(email + "1", name, lastName, password);
        securityController.checkEmailIsPresent(email);
    }

    @Test
    public void testGetUserByCredentials() throws Exception {
        User expected = securityController.registerNewUser(email, name, lastName, password);
        securityController.registerNewUser(email + "1", name + "1", lastName + "1", password + "1");
        User actual = securityController.getUserByCredentials(email, password);
        assertEquals(expected, actual);
    }

}