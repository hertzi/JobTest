package my.wf.affinitas.rest.transport;

import my.wf.affinitas.core.model.User;

import java.util.*;


public class UserDataList {
    private List<UserData> data;

    public UserDataList() {
        data = new ArrayList<>();
    }

    public UserDataList(Collection<User> users) {
        data = new ArrayList<>(users.size());
        for(User user: users){
            data.add(new UserData(user));
        }
    }

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }
}
