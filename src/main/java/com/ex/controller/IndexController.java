package com.ex.controller;

import com.ex.model.*;
import com.ex.service.LikeService;
import com.ex.service.NewsService;
import com.ex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xc on 2017/2/23.
 */
@Controller
public class IndexController {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);
        int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUserById(news.getUserId()));
            if(localUserId!=0){
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/","/index"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop",defaultValue = "0") int pop){
        model.addAttribute("vos", getNews(0, 0, 10));
        if (hostHolder.getUser() != null) {
            pop = 0;
        }
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping("/thymeleaf")
    public String test(Model model){
        String str="hello i'm xu cheng";
        model.addAttribute("hello",str);
        return "testThymeleaf";
    }

    @RequestMapping(value = "/user/{id}")
    @ResponseBody
    public String testId(@PathVariable("id")int id){
        User user=userService.getUserById(id);

        return "user";
    }

}
