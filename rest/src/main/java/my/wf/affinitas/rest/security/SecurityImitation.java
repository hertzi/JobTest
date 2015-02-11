package my.wf.affinitas.rest.security;

import my.wf.affinitas.core.model.User;
import my.wf.affinitas.rest.error.BadSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecurityImitation {
    private static final Logger logger = LoggerFactory.getLogger(SecurityImitation.class);
    private ConcurrentHashMap<String, User> sessions = new ConcurrentHashMap<>();

    public void checkSession(String session){
        if(!sessions.containsKey(session)){
            throw new BadSession();
        }
    }
    public User getUserBySession(String session) {
        return  sessions.get(session);
    }

    public String addUserSession(User user){
        String session = UUID.randomUUID().toString().replace("-", "");
        sessions.putIfAbsent(session, user);
        logger.debug("New login: " + user + ": " + session + ". logged users: " + sessions.size());
        for(String key: sessions.keySet()){
            User u = sessions.get(key);
            logger.debug(u.toString());
        }
        return session;
    }

    public void killSession(String session) {
        User user = sessions.get(session);

        sessions.remove(session);
        logger.debug("Kill session of "+user+": " +session+". logged users: " + sessions.size());
    }
}
