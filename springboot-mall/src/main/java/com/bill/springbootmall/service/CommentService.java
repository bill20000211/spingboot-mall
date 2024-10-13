package com.bill.springbootmall.service;

import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;

public interface CommentService {

    Integer createComment(Integer orderId, CreateCommentRequest createCommentRequest);

    Comment getCommentById(Integer commentId);
}
