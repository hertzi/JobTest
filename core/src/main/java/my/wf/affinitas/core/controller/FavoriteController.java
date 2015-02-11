package my.wf.affinitas.core.controller;

import my.wf.affinitas.core.model.User;
import my.wf.affinitas.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Component
public class FavoriteController {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User addToFavorites(User newFavorite, User owner) {
        owner = userRepository.getUserWithFavorites(owner);
        owner.getFavorites().add(newFavorite);
        return userRepository.save(owner);
    }

    public Set<User> getFavorites(User owner){
        return userRepository.getUserWithFavorites(owner).getFavorites();
    }
}
