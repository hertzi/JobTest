package my.wf.affinitas.rest.controller;

import my.wf.affinitas.rest.transport.*;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;


public class RestHelper {
    private static final String URL_TEMPLATE = "http://localhost:";
    private static String loginUrl;
    private static String logoutUrl;
    private static String newUserUrl;
    private static String userListUrl;
    private static String favoritesUrl;
    private static String sendMessageUrl;
    private static String receivedMessagesUrl;
    private static String sentMessagesUrl;

    private String session;
    private int port;

    public RestHelper(int port) {
        this.port = port;
        loginUrl = URL_TEMPLATE + port + RestConst.LOGIN;
        logoutUrl = URL_TEMPLATE + port + RestConst.LOGOUT;
        newUserUrl = URL_TEMPLATE + port + RestConst.REGISTER;
        userListUrl = URL_TEMPLATE + port + RestConst.USERS + RestConst.LIST;
        favoritesUrl = URL_TEMPLATE + port + RestConst.USERS + RestConst.FAVORITES;
        sendMessageUrl = URL_TEMPLATE + port + RestConst.MESSAGES + RestConst.SEND;
        receivedMessagesUrl = URL_TEMPLATE + port + RestConst.MESSAGES + RestConst.RECEIVED;
        sentMessagesUrl = URL_TEMPLATE + port + RestConst.MESSAGES + RestConst.SENT;
    }

    public RestHelper withSession(String session){
        this.session = session;
        return this;
    }

    public RestHelper withoutSession(){
        this.session = null;
        return this;
    }

    public  ResponseEntity<String> doLogin(String userName, String password){
        HttpEntity<LoginData> entity = createHttpEntity(createLoginData(userName, password));
        return new TestRestTemplate().exchange(loginUrl, HttpMethod.POST, entity, String.class);
    }

    public  ResponseEntity<String> doLogout(){
        HttpEntity<String> entity = createHttpEntity("");
        return new TestRestTemplate().exchange(logoutUrl, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<UserData> doRegistration(String email, String name, String lastName, String password){
        HttpEntity<NewUserData> entity = createHttpEntity(createNewUserData(email, name, lastName, password));
        return new TestRestTemplate().exchange(newUserUrl, HttpMethod.POST, entity, UserData.class);
    }

    public ResponseEntity<UserDataList> doGetUserList() {
        return new TestRestTemplate().exchange(userListUrl, HttpMethod.GET, createHttpEntity(""), UserDataList.class);
    }

    public ResponseEntity<UserDataList> doGetFavorites() {
        return new TestRestTemplate().exchange(favoritesUrl, HttpMethod.GET, createHttpEntity(""), UserDataList.class);
    }

    public ResponseEntity<UserData> doAddNewFavorite(UserData user) {
        return new TestRestTemplate().exchange(favoritesUrl, HttpMethod.PUT, createHttpEntity(user), UserData.class);
    }

    public ResponseEntity<MessageData> doSendMessage(String email, String subject, String text) {
        return new TestRestTemplate().exchange(sendMessageUrl, HttpMethod.POST, createHttpEntity(createNewMessageData(email, subject, text)), MessageData.class);
    }

    public ResponseEntity<MessageDataList> doGetSentMessages() {
        return new TestRestTemplate().exchange(sentMessagesUrl, HttpMethod.GET, createHttpEntity(""), MessageDataList.class);
    }

    public ResponseEntity<MessageDataList> doGetReceivedMessages() {
        return new TestRestTemplate().exchange(receivedMessagesUrl, HttpMethod.GET, createHttpEntity(""), MessageDataList.class);
    }

    public static UserData[] createListOfUsers(int port){
        return new UserData[]{
        new RestHelper(port).withoutSession().doRegistration("john@doe0", "John0", "Doe0", "qwerty").getBody(),
        new RestHelper(port).withoutSession().doRegistration("john@doe1", "John1", "Doe1", "qwerty").getBody(),
        new RestHelper(port).withoutSession().doRegistration("john@doe2", "John2", "Doe2", "qwerty").getBody(),
        new RestHelper(port).withoutSession().doRegistration("john@doe3", "John3", "Doe3", "qwerty").getBody(),
        new RestHelper(port).withoutSession().doRegistration("john@doe4", "John4", "Doe4", "qwerty").getBody()
        };

    }

    private <T> HttpEntity<T> createHttpEntity(T entity){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(null != session) {
            headers.set(RestConst.SESSIONID, session);
        }
        return new HttpEntity<T>(entity,headers);
    }

    private LoginData createLoginData(String email, String password){
        LoginData data = new LoginData();
        data.setEmail(email);
        data.setPassword(password);
        return data;
    }

    private NewUserData createNewUserData(String email, String name, String lastName, String password) {
        NewUserData userData = new NewUserData();
        userData.setEmail(email);
        userData.setLastName(lastName);
        userData.setName(name);
        userData.setPassword(password);
        return userData;
    }

    private NewMessageData createNewMessageData(String recipientEmail, String subject, String text){
        NewMessageData data = new NewMessageData();
        data.setRecipientEmail(recipientEmail);
        data.setSubject(subject);
        data.setText(text);
        return data;
    }

}
