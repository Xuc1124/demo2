package com.ex.controller;

import com.ex.model.*;
import com.ex.service.CommentService;
import com.ex.service.LikeService;
import com.ex.service.NewsService;
import com.ex.service.UserService;
import com.ex.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xc on 17-2-26.
 */
@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/uploadImage/"},method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try {
            String fileUrl=newsService.saveImage(file);
            //图片静态资源要存储在云上，推荐七牛云，另外做借口调用
            if(fileUrl==null){
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            }
            return ToutiaoUtil.getJSONString(0,fileUrl);
        }catch (Exception e){
            logger.error("上传图片失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传图片失败");
        }
    }

    @RequestMapping(path = {"/image"},method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response){
        try{
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+imageName)),response.getOutputStream());
        }catch (Exception e){
            logger.error("读取图片错误"+imageName+e.getMessage());
        }

    }

    @RequestMapping(path = {"/user/addNews/"},method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        try{
            News news=new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if(hostHolder.getUser()!=null){
                news.setUserId(hostHolder.getUser().getId());
            }else{
                //设置匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }catch (Exception e){
            logger.error("添加资讯失败"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }
    }

    @RequestMapping(path={"/news/{newsId}"},method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId,Model model){
        News news=newsService.getById(newsId);
        if(news!=null){
            int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
            if(localUserId!=0){
                model.addAttribute("like",likeService.getLikeStatus(localUserId,EntityType.ENTITY_NEWS,newsId));
            }else{
                model.addAttribute("like",0);
            }

            //评论
            List<Comment> comments=commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs=new ArrayList<ViewObject>();
            for (Comment comment:comments){
                ViewObject vo=new ViewObject();
                vo.set("comment",comment);
                vo.set("user",userService.getUserById(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments",commentVOs);
        }
        model.addAttribute("news",news);
        model.addAttribute("owner",newsService.getUser(news.getUserId()));
        return "detail";
    }


    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId, @RequestParam("content") String content){
        try{
            Comment comment=new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            //********此处注意，感觉不能硬编码，直接set newsId，应该获取要评论实体的id和type后进行set
            //前端传一个entityId过来，外加一个能显示当前entity type的参数
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            commentService.addComment(comment);

            //更新评论数量，要用异步
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            newsService.updataCommentCount(comment.getEntityId(),count);

        }catch (Exception e){
            logger.error("提交评论错误"+e.getMessage());
        }
        return "redirect:/news/"+String.valueOf(newsId);
    }

}
