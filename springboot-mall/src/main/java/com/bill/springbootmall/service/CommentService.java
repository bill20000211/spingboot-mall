package com.bill.springbootmall.service;

import com.bill.springbootmall.dto.CommentQueryParams;
import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;

import java.util.List;

public interface CommentService {

    Integer createComment(Integer orderId, CreateCommentRequest createCommentRequest);

    Comment getCommentById(Integer commentId);

    List<Comment> getComments(CommentQueryParams commentQueryParams);

    Integer countComments(CommentQueryParams commentQueryParams);
}
