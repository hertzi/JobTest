package my.wf.affinitas.rest.transport;

import my.wf.affinitas.core.model.Message;

import java.util.*;

public class MessageDataList {
    private List<MessageData> data;

    public MessageDataList() {
        data = new ArrayList<>();
    }

    public MessageDataList(Collection<Message> messages) {
        data = new ArrayList<>(messages.size());
        for(Message message: messages){
            data.add(new MessageData(message));
        }
    }

    public List<MessageData> getData(){
        return data;
    }

    public void setData(List<MessageData> data) {
        this.data = data;
    }
}
