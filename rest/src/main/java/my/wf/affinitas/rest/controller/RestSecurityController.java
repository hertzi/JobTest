package my.wf.affinitas.rest.controller;

import my.wf.affinitas.core.controller.SecurityController;
import my.wf.affinitas.core.error.EmailIsAlreadyPresent;
import my.wf.affinitas.core.model.User;
import my.wf.affinitas.rest.error.BadSession;
import my.wf.affinitas.rest.security.SecurityImitation;
import my.wf.affinitas.rest.transport.LoginData;
import my.wf.affinitas.rest.transport.NewUserData;
import my.wf.affinitas.rest.transport.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class RestSecurityController {
    private static final Logger logger = LoggerFactory.getLogger(RestSecurityController.class);
    @Autowired
    private SecurityController securityController;
    @Autowired
    private SecurityImitation securityImitation;



    @RequestMapping(value = RestConst.LOGIN, method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginData login) throws AuthenticationException {
        User user = securityController.getUserByCredentials(login.getEmail(), login.getPassword());
        if(null == user){
            throw new AuthenticationException("Wrong authentication");
        }
        return new ResponseEntity<String>(securityImitation.addUserSession(user), HttpStatus.OK);
    }

    @RequestMapping(value = RestConst.REGISTER, method = RequestMethod.POST)
    public @ResponseBody UserData registerNewUser(@RequestBody NewUserData userData){
        logger.info("Register new User: [email:"+userData.getEmail()+" , name:"+userData.getName()+" , lastName:"+userData.getLastName()+" ]");
        return new UserData(securityController.registerNewUser(userData.getEmail(), userData.getName(), userData.getLastName(), userData.getPassword()));
    }

    @RequestMapping(value = RestConst.LOGOUT, method = RequestMethod.POST)
    public ResponseEntity<String> logout(@RequestHeader(RestConst.SESSIONID) String sessionId){
        securityImitation.checkSession(sessionId);
        securityImitation.killSession(sessionId);
        return new ResponseEntity<String>("Bye.", HttpStatus.OK);
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(){
        return "TEST OK";
    }

    @ExceptionHandler(BadSession.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody BadSession handleException(BadSession e) {
        return e;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody AuthenticationException handleException(AuthenticationException e) {
        return e;
    }

    @ExceptionHandler(EmailIsAlreadyPresent.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody EmailIsAlreadyPresent handleException(EmailIsAlreadyPresent e) {
        return e;
    }

}
