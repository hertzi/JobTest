package my.wf.affinitas.core.controller;

import my.wf.affinitas.core.error.EmailIsAlreadyPresent;
import my.wf.affinitas.core.model.User;
import my.wf.affinitas.core.model.SecurityData;
import my.wf.affinitas.core.repository.SecurityDataRepository;
import my.wf.affinitas.core.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SecurityController {
    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityDataRepository securityDataRepository;


    @Transactional
    public User registerNewUser(String email, String name, String lastName, String password){
        if(!checkEmailIsPresent(email)){
            throw new EmailIsAlreadyPresent(email);
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setLastName(lastName);
        User saved = userRepository.save(user);
        SecurityData details = new SecurityData();
        details.setUser(saved);
        details.setLoginName(email);
        details.setPassword(password);
        User result = securityDataRepository.save(details).getUser();
        logger.info("USER CREATED: " + result.toString());
        return result;
    }

    protected boolean checkEmailIsPresent(String email){
        return 0 == userRepository.getEmailCount(email) ;
    }

    public User getUserByCredentials(String email, String password) {
        SecurityData securityData = securityDataRepository.getByLoginName(email);
        if(securityData.getPassword().equals(password)){
            return securityData.getUser();
        }
        return null;
    }
}
