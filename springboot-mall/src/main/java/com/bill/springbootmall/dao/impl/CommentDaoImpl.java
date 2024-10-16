package com.bill.springbootmall.dao.impl;

import com.bill.springbootmall.dao.CommentDao;
import com.bill.springbootmall.dto.CommentQueryParams;
import com.bill.springbootmall.dto.CreateCommentRequest;
import com.bill.springbootmall.model.Comment;
import com.bill.springbootmall.rowmapper.CommentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createComment(Integer orderId, CreateCommentRequest createCommentRequest) {
        String sql ="INSERT INTO comment(order_id, score, content, created_date, last_modified_date)" +
                " VALUES(:orderId, :score, :content, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("score", createCommentRequest.getScore());
        map.put("content", createCommentRequest.getContent());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int commentId = keyHolder.getKey().intValue();

        return commentId;
    }

    @Override
    public Comment getCommentById(Integer commentId) {
        String sql = "SELECT comment_id, order_id, score, content, created_date,last_modified_date " +
                "FROM comment WHERE comment_id = :commentId";
        Map<String, Object> map = new HashMap<>();
        map.put("commentId", commentId);

        List<Comment> commentList = namedParameterJdbcTemplate.query(sql, map, new CommentRowMapper());
        if (commentList != null && commentList.size() > 0) {
            return commentList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Comment getCommentByOrderId(Integer orderId) {
        String sql = "SELECT comment_id, order_id, score, content, created_date,last_modified_date " +
                "FROM comment WHERE order_id = :orderId";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Comment> commentList = namedParameterJdbcTemplate.query(sql, map, new CommentRowMapper());
        if (commentList != null && commentList.size() > 0) {
            return commentList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<Comment> getComments(CommentQueryParams commentQueryParams) {
        String sql = "SELECT DISTINCT c.comment_id, c.order_id, c.score, c.content, c.created_date, c.last_modified_date" +
                " FROM comment as c " +
                "JOIN `order` as o ON c.order_id = o.order_id " +
                "JOIN order_item as oi ON o.order_id = oi.order_id " +
                "WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(sql, map, commentQueryParams);

        sql += " ORDER BY " + commentQueryParams.getOrderBy() +
                " " + commentQueryParams.getSort();

        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit", commentQueryParams.getLimit());
        map.put("offset", commentQueryParams.getOffset());

        List<Comment> commentList = namedParameterJdbcTemplate.query(sql, map, new CommentRowMapper());

        return commentList;
    }

    @Override
    public Integer countComments(CommentQueryParams commentQueryParams) {
        String sql = "SELECT count(*) FROM comment as c " +
                "JOIN `order` as o ON c.order_id = o.order_id " +
                "JOIN order_item as oi ON o.order_id = oi.order_id " +
                "WHERE 1=1";
        Map<String, Object> map = new HashMap<>();

        sql = addFilteringSql(sql, map, commentQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    private String addFilteringSql(String sql,
                                   Map<String, Object> map,
                                   CommentQueryParams commentQueryParams) {
        if (commentQueryParams.getProductId() != null) {
            sql += " AND oi.product_id = :productId";
            map.put("productId", commentQueryParams.getProductId());
        }
        return sql;
    }
}
