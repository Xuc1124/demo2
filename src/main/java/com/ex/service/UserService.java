package com.ex.service;

import com.ex.dao.LoginTicketDAO;
import com.ex.dao.UserDAO;
import com.ex.model.LoginTicket;
import com.ex.model.User;
import com.ex.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xc on 2017/2/23.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUserById(int id){
        return userDAO.selectById(id);
    }
    public void addUser(User user){
        userDAO.addUser(user);
    }
    public Map<String, Object> register(String username, String password){
        Map<String, Object> map=new HashMap<String,Object>();
        if (StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用于已存在");
            return map;
        }
        user=new User();
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        user.setName(username);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000)));
        userDAO.addUser(user);
        //登陆
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String, Object> login(String username, String password){
        Map<String, Object> map=new HashMap<String,Object>();
        if (StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user==null){
            map.put("msgname","用户名不存在");
            return map;
        }
        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码错误");
            return map;
        }
        //ticket
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }

}
