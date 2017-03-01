package com.ex.async.handler;

import com.ex.async.EventHandler;
import com.ex.async.EventModel;
import com.ex.async.EventType;
import com.ex.model.Message;
import com.ex.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xc on 17-3-1.
 */
@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MessageService messageService;


    @Override
    public void doHandle(EventModel model) {
        //处理异常登录，发站内信或这邮件给用户
        Message message=new Message();

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
