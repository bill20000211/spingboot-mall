package com.bill.springbootmall.dao;

import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;

public interface CommentDao {

    Integer createComment(Integer orderId, CreateCommentRequest createCommentRequest);

    Comment getCommentById(Integer commentId);

    Comment getCommentByOrderId(Integer orderId);
}
