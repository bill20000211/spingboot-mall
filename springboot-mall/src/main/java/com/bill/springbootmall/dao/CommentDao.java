package com.bill.springbootmall.dao;

import com.bill.springbootmall.dto.CommentQueryParams;
import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;

import java.util.List;

public interface CommentDao {

    Integer createComment(Integer orderId, CreateCommentRequest createCommentRequest);

    Comment getCommentById(Integer commentId);

    Comment getCommentByOrderId(Integer orderId);

    List<Comment> getComments(CommentQueryParams commentQueryParams);

    Integer countComments(CommentQueryParams commentQueryParams);
}
