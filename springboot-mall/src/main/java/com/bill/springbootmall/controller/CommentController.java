package com.bill.springbootmall.controller;

import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;
import com.bill.springbootmall.service.CommentService;
import com.bill.springbootmall.service.OrderService;
import com.bill.springbootmall.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    private final static Logger log = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/comments/{orderId}")
    public ResponseEntity<Comment> createComment(@PathVariable Integer orderId,
                                                 @RequestBody CreateCommentRequest createCommentRequest) {

        // 從 Token 解析出 userId
        Integer userId = jwtUtil.getUserIdFromToken();

        Integer buyUserId = orderService.getUserIdByOrderId(orderId);

        if (userId == null) {
            log.warn("尚未登入");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (!userId.equals(buyUserId)) {
            log.warn("並非此用戶之訂單，不可評論");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer commentId = commentService.createComment(orderId, createCommentRequest);

        Comment comment = commentService.getCommentById(commentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
}
