package com.bill.springbootmall.controller;

import com.bill.springbootmall.dto.CommentQueryParams;
import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;
import com.bill.springbootmall.service.CommentService;
import com.bill.springbootmall.service.OrderService;
import com.bill.springbootmall.util.JwtUtil;
import com.bill.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    private final static Logger log = LoggerFactory.getLogger(CommentController.class);

    @PostMapping("/orders/{orderId}/comments")
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

    @GetMapping("/products/{productId}/comments")
    public ResponseEntity<Page<Comment>> getComments(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "10") @Max(100) @Min(0)Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {

        CommentQueryParams commentQueryParams = new CommentQueryParams();
        commentQueryParams.setOrderBy(orderBy);
        commentQueryParams.setSort(sort);
        commentQueryParams.setProductId(productId);
        commentQueryParams.setLimit(limit);
        commentQueryParams.setOffset(offset);

        List<Comment> commentList = commentService.getComments(commentQueryParams);

        Integer total = commentService.countComments(commentQueryParams);

        Page<Comment> pageInfo = new Page<>();
        pageInfo.setLimit(limit);
        pageInfo.setOffset(offset);
        pageInfo.setTotal(total);
        pageInfo.setResults(commentList);

        return ResponseEntity.status(HttpStatus.OK).body(pageInfo);
    }
}
