package my.wf.affinitas.core.model;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Message extends BaseEntity {
    private User sender;
    private User recipient;
    private String subject;
    private String text;

    @ManyToOne
    @JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name="fk_message_from"))
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @ManyToOne
    @JoinColumn(name = "recipient_id", foreignKey = @ForeignKey(name="fk_message_recipient"))
    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @Column(name="subject", length = 100)
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name="text", length = 1024)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
