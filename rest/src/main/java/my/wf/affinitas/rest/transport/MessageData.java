package my.wf.affinitas.rest.transport;

import my.wf.affinitas.core.model.Message;

public class MessageData {
    private UserData sender;
    private UserData recipient;
    private String subject;
    private String text;

    public MessageData() {
    }

    public MessageData(Message message) {
        this.sender= new UserData(message.getSender());
        this.recipient = new UserData(message.getRecipient());
        this.subject = message.getSubject();
        this.text = message.getText();
    }

    public UserData getSender() {
        return sender;
    }

    public void setSender(UserData sender) {
        this.sender = sender;
    }

    public UserData getRecipient() {
        return recipient;
    }

    public void setRecipient(UserData recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
