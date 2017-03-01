package com.ex.controller;

import com.ex.async.EventModel;
import com.ex.async.EventProducer;
import com.ex.async.EventType;
import com.ex.model.EntityType;
import com.ex.model.HostHolder;
import com.ex.model.User;
import com.ex.service.LikeService;
import com.ex.service.NewsService;
import com.ex.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xc on 17-2-28.
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String like(Model model, @RequestParam("newsId") int newsId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.like(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        //触发like事件
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                                .setEntityOwnerId(newsService.getById(newsId).getUserId())
                                .setActorId(hostHolder.getUser().getId())
                                .setEntityId(newsId));

        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));

    }

    @RequestMapping(path = {"/dislike"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String dislike(Model model, @RequestParam("newsId") int newsId){
        int userId=hostHolder.getUser().getId();
        long likeCount=likeService.dislike(userId, EntityType.ENTITY_NEWS,newsId);
        newsService.updateLikeCount(newsId,(int)likeCount);
        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));

    }

}
