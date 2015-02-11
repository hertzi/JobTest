package my.wf.affinitas.core.repository;

import my.wf.affinitas.CoreApplication;
import my.wf.affinitas.core.model.SecurityData;
import my.wf.affinitas.core.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreApplication.class)
@IntegrationTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTestIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private SecurityDataRepository securityDataRepository;

    @Test
    public void shouldCalculateUserAndSecurityDataLinesAfterUserAdding() throws Exception {
        createUserWithSecurityData("u1", "n1", "l1", "p1");
        assertEquals(2, userRepository.getEmailCount("u1"));
    }

    @Test
    public void shouldReturn0becauseNotMatched() throws Exception {
        createUserWithSecurityData("u1", "n1", "l1", "p1");
        createUserWithSecurityData("u2", "n1", "l1", "p1");
        createUserWithSecurityData("u3", "n1", "l1", "p1");
        assertEquals(0, userRepository.getEmailCount("u0"));
    }

    @Test
    public void shouldCalculateOnlyOneLineAfterUserChanging() throws Exception {
        User user = createUserWithSecurityData("u1", "n1", "l1", "p1").getUser();
        changeEmail(user, "u2");
        assertEquals(1, userRepository.getEmailCount("u1"));
        assertEquals(1, userRepository.getEmailCount("u2"));
    }

    @Test
    public void shouldReturnUserWithFavorites(){
        User u1 = createUser("u1", "n1", "l1");
        User u2 = createUser("u2", "n2", "l2");
        User u3 = createUser("u3", "n3", "l3");
        User u4 = createUser("u4", "n4", "l4");
        User u5 = createUser("u5", "n5", "l5");
        addFavoritesToUser(u1, u2, u3);
        assertThat(userRepository.getUserWithFavorites(u1).getFavorites(), hasItems(u2, u3));
    }

    @Test
    public void testIsFavorite(){
        User u1 = createUser("u1", "n1", "l1");
        User u2 = createUser("u2", "n2", "l2");
        addFavoritesToUser(u1, u2);
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
    protected SecurityData createUserWithSecurityData(String email, String name, String lastName, String password) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setLastName(lastName);
        user = userRepository.save(user);
        SecurityData details = new SecurityData();
        details.setUser(user);
        details.setLoginName(user.getEmail());
        details.setPassword(password);
        return securityDataRepository.save(details);
    }

    @Transactional
    protected User changeEmail(User user, String email){
        user.setEmail(email);
        return userRepository.save(user);
    }

    @Transactional
    protected void addFavoritesToUser(User owner, User... favorites){
        for(User u: favorites){
            owner.getFavorites().add(u);
        }
        userRepository.save(owner);
    }
}