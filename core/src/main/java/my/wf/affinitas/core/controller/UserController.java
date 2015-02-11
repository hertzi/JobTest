package my.wf.affinitas.core.controller;

import my.wf.affinitas.core.model.User;
import my.wf.affinitas.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserController {
    @Autowired
    private UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }
}
