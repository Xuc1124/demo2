package com.ex.controller;

import com.ex.dao.MessageDAO;
import com.ex.model.HostHolder;
import com.ex.model.Message;
import com.ex.model.User;
import com.ex.model.ViewObject;
import com.ex.service.MessageService;
import com.ex.service.UserService;
import com.ex.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xc on 17-2-27.
 */
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/addMessage"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        Message msg=new Message();
        msg.setContent(content);
        msg.setFromId(fromId);
        msg.setToId(toId);
        msg.setConversationId(fromId<toId?String.format("%d_%d",fromId,toId):
                String.format("%d_%d",toId,fromId));
        msg.setCreatedDate(new Date());
        messageService.addMessage(msg);
        return ToutiaoUtil.getJSONString(msg.getId());
    }
    //站内信功能不可用，暂无解
    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try{
            int localUserId=hostHolder.getUser().getId();
            List<ViewObject> conversations=new ArrayList<ViewObject>();
            List<Message> conversationList=messageService.getConversationList(localUserId,0,10);
            for (Message msg:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("conversation", msg);
                int targetId=localUserId==msg.getFromId()?msg.getToId():msg.getFromId();
                User user=userService.getUserById(targetId);
                vo.set("headUrl",user.getHeadUrl());
                vo.set("targetId", targetId);
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());
        }


        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String getMessageDetail(@RequestParam("conversationId") String conversationId, Model model){
        try {
            List<Message> messages=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject> messageVOs=new ArrayList<ViewObject>();
            for (Message msg:messages){
                ViewObject vo=new ViewObject();
                User user=userService.getUserById(msg.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("message",msg);
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                messageVOs.add(vo);
            }
            model.addAttribute("messages",messageVOs);
            return "letterDetail";
        }catch(Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());
        }
        return "letterDetail";

    }

}
