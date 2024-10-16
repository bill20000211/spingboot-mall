package com.bill.springbootmall.service.impl;

import com.bill.springbootmall.dao.CommentDao;
import com.bill.springbootmall.dto.CommentQueryParams;
import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;
import com.bill.springbootmall.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    private final static Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Transactional
    @Override
    public Integer createComment(Integer orderId, CreateCommentRequest createCommentRequest) {
        Comment comment = commentDao.getCommentByOrderId(orderId);
        if (comment != null) {
            log.warn("訂單 {} 已有評論", orderId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Integer commentId = commentDao.createComment(orderId, createCommentRequest);
        return commentId;
    }

    @Override
    public Comment getCommentById(Integer commentId) {
        Comment comment = commentDao.getCommentById(commentId);
        return comment;
    }

    @Override
    public List<Comment> getComments(CommentQueryParams commentQueryParams) {
        List<Comment> commentList = commentDao.getComments(commentQueryParams);
        return commentList;
    }

    @Override
    public Integer countComments(CommentQueryParams commentQueryParams) {
        Integer total = commentDao.countComments(commentQueryParams);
        return total;
    }
}
