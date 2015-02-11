package my.wf.affinitas.core.model;


import javax.persistence.*;

/**
 * Implemented as a separate entity because security data should be stored outside of business data
 */
@Entity
public class SecurityData extends BaseEntity {
    private String loginName;
    private String password;
    private User user;

    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }


}
