package com.ex.service;

import com.ex.dao.CommentDAO;
import com.ex.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xc on 17-2-27.
 */
@Service
public class CommentService {
    @Autowired
    private CommentDAO commentDAO;
    public List<Comment> getCommentByEntity(int entityId, int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }


}
