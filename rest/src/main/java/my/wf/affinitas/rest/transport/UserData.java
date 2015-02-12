package my.wf.affinitas.rest.transport;

import my.wf.affinitas.core.model.User;

public class UserData {

    private User user;
    private String name;
    private String email;
    private String lastName;

    public UserData() {
    }

    public UserData(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.lastName = user.getLastName();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
