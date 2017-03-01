package com.ex.service;

import com.ex.dao.NewsDAO;
import com.ex.dao.UserDAO;
import com.ex.model.News;
import com.ex.model.User;
import com.ex.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by xc on 2017/2/24.
 */
@Service
public class NewsService {

    @Autowired
    NewsDAO newsDAO;
    @Autowired
    UserDAO userDAO;

    public List<News> getNewsFromUserId(int id, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(id, offset, limit);
    }

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public void addNews(News news) {
        newsDAO.addNews(news);
    }

    public News getOneNewsByUserId(int userId) {
        return newsDAO.getOneNewsFromUserId(userId);
    }

    public List<News> getAllNews() {
        return (List<News>) newsDAO.getAllNews();
    }

    public String saveImage(MultipartFile file) throws IOException {
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        //获取图片格式，看是否符合
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    public News getById(int id){
        return newsDAO.getById(id);
    }

    public User getUser(int userId){
        return userDAO.selectById(userId);
    }

    public int updataCommentCount(int entityId, int count){
        return newsDAO.updateCommentCount(entityId,count);
    }

    public int updateLikeCount(int entityId,int count){
        return newsDAO.updateLikeCount(entityId,count);
    }

}
