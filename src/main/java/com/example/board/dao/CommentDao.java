package com.example.board.dao;

import com.example.board.dto.Comment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public class CommentDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations insertComment;

    public CommentDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertComment = new SimpleJdbcInsert(dataSource).withTableName("comment").usingGeneratedKeyColumns("commentId");
    }

    @Transactional
    public void addComment(int commentId, String content) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        SqlParameterSource params = new BeanPropertySqlParameterSource(comment);
        insertComment.execute(params);
    }

    @Transactional(readOnly = true)
    public List<Comment> getComments(int postId) {
        String sql = "select c.commentId, c.postId, c.userId, c.parentId, c.name, c.content, c.createdAt, c.updatedAt from comment c join user u on c.userId = u.userId where c.postId = :postId order by c.createdAt asc";
        RowMapper<Comment> rowMapper = BeanPropertyRowMapper.newInstance(Comment.class);
        List<Comment> comment = jdbcTemplate.query(sql, Map.of("postId", postId), rowMapper);
        return comment;
    }

    @Transactional
    public void deleteComment(int commentId) {
        String sql = "delete from post where commentId = :commentId";
        jdbcTemplate.update(sql, Map.of("commentId", commentId));
    }

    @Transactional
    public void updateComment(int commentId, String content) {

    }
}
