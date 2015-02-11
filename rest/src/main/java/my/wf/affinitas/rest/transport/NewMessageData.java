package my.wf.affinitas.rest.transport;

/**
 * Created by Serg on 11.02.2015.
 */
public class NewMessageData {
    private String recipientEmail;
    private String subject;
    private String text;

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
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
