package com.ex.async.handler;

import com.ex.async.EventHandler;
import com.ex.async.EventModel;
import com.ex.async.EventType;
import com.ex.model.Message;
import com.ex.model.User;
import com.ex.service.MessageService;
import com.ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by xc on 17-3-1.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message=new Message();
        User user=userService.getUserById(model.getActorId());
        int fromId=user.getId();
        int toId=model.getEntityOwnerId();
        message.setToId(toId);
        message.setContent("用户"+user.getName()+"赞了你的资讯,http://127.0.0.1:8080/news/"+ String.valueOf(model.getEntityId()));
        message.setFromId(fromId);
        message.setCreatedDate(new Date());
        message.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):
                String.format("%d_%d",toId,fromId));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
