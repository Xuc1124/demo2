package com.ex;


import com.ex.dao.CommentDAO;
import com.ex.dao.LoginTicketDAO;
import com.ex.dao.UserDAO;
import com.ex.model.*;
import com.ex.service.NewsService;
import com.ex.service.UserService;
import com.ex.util.ToutiaoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)

public class DemoApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    CommentDAO commentDAO;

    @Test
    public void init() {

        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            //userService.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setUserId(i + 1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            //newsService.addNews(news);

            for(int j = 0; j < 3; ++j) {
                Comment comment = new Comment();
                comment.setUserId(i+1);
                comment.setCreatedDate(new Date());
                comment.setStatus(0);
                comment.setContent("这里是一个评论啊！" + String.valueOf(j));
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                commentDAO.addComment(comment);
            }

            LoginTicket ticket=new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d",i+1));
            //loginTicketDAO.addTicket(ticket);

        }
    }

    @Autowired
    UserDAO userDAO;

    /*@Test
    public void testResgister(){
        String username="xc";
        String password="xx";
        User user=new User();
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        user.setName(username);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000)));
        userDAO.addUser(user);
    }*/

}
