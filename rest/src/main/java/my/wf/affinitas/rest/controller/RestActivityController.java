package my.wf.affinitas.rest.controller;

import my.wf.affinitas.core.controller.FavoriteController;
import my.wf.affinitas.core.controller.UserController;
import my.wf.affinitas.rest.error.BadSession;
import my.wf.affinitas.rest.security.SecurityImitation;
import my.wf.affinitas.rest.transport.UserData;
import my.wf.affinitas.rest.transport.UserDataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = RestConst.USERS)
public class RestActivityController  {
    @Autowired
    private FavoriteController activityController;
    @Autowired
    private UserController userHelper;
    @Autowired
    private SecurityImitation securityImitation;

    @RequestMapping(value = RestConst.LIST, method = RequestMethod.GET)
    public UserDataList getAllUsers(@RequestHeader(RestConst.SESSIONID) String sessionId) {
        securityImitation.checkSession(sessionId);
        return new UserDataList(activityController.getAllUsers());
    }

    @RequestMapping(value = RestConst.FAVORITES, method = RequestMethod.GET)
    public UserDataList getFavorites(@RequestHeader(RestConst.SESSIONID) String sessionId) {
        securityImitation.checkSession(sessionId);
        return new UserDataList(activityController.getFavorites(securityImitation.getUserBySession(sessionId)));
    }

    @RequestMapping(value = RestConst.FAVORITES, method = RequestMethod.PUT)
    public @ResponseBody UserData addToFavorites(@RequestHeader(RestConst.SESSIONID) String sessionId, @RequestBody UserData newFavorite) {
        securityImitation.checkSession(sessionId);
        return new UserData(activityController.addToFavorites(userHelper.getUserByEmail(newFavorite.getEmail()), securityImitation.getUserBySession(sessionId)));
    }

    @ExceptionHandler(BadSession.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody BadSession handleException(BadSession e) {
        return e;
    }
}
