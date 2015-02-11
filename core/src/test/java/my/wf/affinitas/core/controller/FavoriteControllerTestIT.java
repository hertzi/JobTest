package my.wf.affinitas.core.controller;

import my.wf.affinitas.CoreApplication;
import my.wf.affinitas.core.model.User;
import my.wf.affinitas.core.repository.UserRepository;
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
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreApplication.class)
@IntegrationTest("")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FavoriteControllerTestIT {
    private static final String email = "un@server";
    private static final String password = "p";
    private static final String name = "n";
    private static final String lastName = "ln";

    @Autowired
    FavoriteController favoriteController;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testGetAllUsersWithoutData() throws Exception {
        assertTrue(favoriteController.getAllUsers().isEmpty());
    }

    @Test
    public void testGetAllUsersWithData() throws Exception {
        User user0 = createUser(email, name, lastName);
        User user1 = createUser(email + "1", name + "1", lastName + "1");
        User user2 = createUser(email + "2", name + "2", lastName + "2");
        User user3 = createUser(email + "3", name + "3", lastName + "3");
        User user4 = createUser(email + "4", name + "4", lastName + "4");
        assertThat(favoriteController.getAllUsers(), hasItems(user1, user2, user3, user4, user0));
    }

    @Test
    public void testAddToFavorites() throws Exception {
        User user0 = createUser(email, name, lastName);
        User user1 = createUser(email + "1", name + "1", lastName + "1");
        User user2 = createUser(email + "2", name + "2", lastName + "2");
        User user3 = createUser(email + "3", name + "3", lastName + "3");
        User user4 = createUser(email + "4", name + "4", lastName + "4");
        favoriteController.addToFavorites(user1, user0);
        favoriteController.addToFavorites(user3, user0);
        favoriteController.addToFavorites(user4, user0);
        assertThat(favoriteController.getFavorites(user0), hasItems(user1, user3, user4));
        assertThat(favoriteController.getFavorites(user0), not(hasItems(user2)));
    }

    @Transactional
    protected User createUser(String email, String name, String lastName) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setLastName(lastName);
        return userRepository.save(user);
    }
}
