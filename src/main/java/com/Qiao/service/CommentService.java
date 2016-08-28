package com.Qiao.service;

import com.Qiao.dao.CommentDao;
import com.Qiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by white and black on 2016/8/17.
 */
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;
    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectCommentByEntity(entityId,entityType);
    }
    public Comment getCommentById(int id){
        return commentDao.getCommentById(id);
    }
    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }
    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }
    public void deleteComment(int entityId,int entityType){
        commentDao.updateStatus(entityId,entityType,1);
    }

}
