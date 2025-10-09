package com.example.board.service;

import com.example.board.dao.CommentDao;
import com.example.board.dto.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDao commentDao;

    @Transactional
    public void addComment(int postId, int userId, String content, Integer parentId) { commentDao.addComment(postId, userId, content, parentId); }

    @Transactional(readOnly = true)
    public List<Comment> getComments(int postId) {
        return commentDao.getComments(postId);
    }

    @Transactional
    public Comment getComment(int commentId) { return commentDao.getComment(commentId); }

    @Transactional
    public void deleteComment(int commentId, int userId) {
        Comment comment = commentDao.getComment(commentId);
        if(comment.getUserId() == userId) {
            commentDao.deleteComment(commentId);
        }
    }

    @Transactional
    public void updateComment(int commentId, String content) {
        commentDao.updateComment(commentId, content);
    }
}
