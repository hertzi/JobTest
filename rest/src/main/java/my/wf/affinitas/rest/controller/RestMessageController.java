package my.wf.affinitas.rest.controller;

import my.wf.affinitas.core.controller.MessageController;
import my.wf.affinitas.core.controller.SecurityController;
import my.wf.affinitas.core.controller.UserController;
import my.wf.affinitas.core.error.NotFavorite;
import my.wf.affinitas.core.model.User;
import my.wf.affinitas.rest.error.BadSession;
import my.wf.affinitas.rest.security.SecurityImitation;
import my.wf.affinitas.rest.transport.MessageData;
import my.wf.affinitas.rest.transport.MessageDataList;
import my.wf.affinitas.rest.transport.NewMessageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = RestConst.MESSAGES)
public class RestMessageController {
    private static final Logger logger = LoggerFactory.getLogger(RestMessageController.class);
    @Autowired
    MessageController messageController;
    @Autowired
    SecurityController securityController;
    @Autowired
    UserController userHelper;
    @Autowired
    private SecurityImitation securityImitation;

    @RequestMapping(value = RestConst.SEND, method = RequestMethod.POST)
    public @ResponseBody
    MessageData sendMessage(@RequestHeader(RestConst.SESSIONID) String sessionId, @RequestBody NewMessageData messageData) {
        securityImitation.checkSession(sessionId);
        User recipient = userHelper.getUserByEmail(messageData.getRecipientEmail());
        User sender = securityImitation.getUserBySession(sessionId);
        return new MessageData(messageController.sendMessage(sender, recipient, messageData.getSubject(), messageData.getText()));
    }

    @RequestMapping(value = RestConst.SENT, method = RequestMethod.GET)
    public @ResponseBody
    MessageDataList getSentMessages(@RequestHeader(RestConst.SESSIONID) String sessionId) {
        securityImitation.checkSession(sessionId);
        return new MessageDataList(messageController.getSentMessages(securityImitation.getUserBySession(sessionId)));
    }

    @RequestMapping(value = RestConst.RECEIVED, method = RequestMethod.GET)
    public @ResponseBody MessageDataList getReceivedMessages(@RequestHeader(RestConst.SESSIONID) String sessionId) {
        securityImitation.checkSession(sessionId);
        User user = securityImitation.getUserBySession(sessionId);
        if(null == user){

        }
        return new MessageDataList(messageController.getReceivedMessages(user));
    }

    @ExceptionHandler(BadSession.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public @ResponseBody BadSession handleException(BadSession e) {
        return e;
    }

    @ExceptionHandler(NotFavorite.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody NotFavorite handleException(NotFavorite e) {
        return e;
    }
}
