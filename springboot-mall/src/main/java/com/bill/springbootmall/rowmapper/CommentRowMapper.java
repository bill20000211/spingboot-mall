package com.bill.springbootmall.rowmapper;

import com.bill.springbootmall.model.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("comment_id"));
        comment.setOrderId(rs.getInt("order_id"));
        comment.setScore(rs.getInt("score"));
        comment.setContent(rs.getString("content"));
        comment.setCreatedDate(rs.getTimestamp("created_date"));
        comment.setLastModifiedDate(rs.getTimestamp("last_modified_date"));

        return comment;
    }
}
