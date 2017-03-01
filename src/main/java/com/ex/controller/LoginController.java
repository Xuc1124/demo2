package com.ex.controller;

import com.ex.async.EventModel;
import com.ex.async.EventProducer;
import com.ex.async.EventType;
import com.ex.service.UserService;
import com.ex.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by xc on 2017/2/24.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg/"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remeberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.register(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remeberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0,"注册成功");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }
            }catch (Exception e){
                logger.error("注册异常 "+e.getMessage());
                return ToutiaoUtil.getJSONString(1,"注册异常");
            }
    }

    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue = "0") int remeberme,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(remeberme>0){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);

                //此处把登录操作事件发出去

                return ToutiaoUtil.getJSONString(0,"登录成功");
            }else{
                return ToutiaoUtil.getJSONString(1,map);
            }
        }catch (Exception e){
            logger.error("登录异常 "+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登录异常");
        }
    }

    @RequestMapping("/logout/")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
