package com.example.board.service;

import com.example.board.dao.CommentDao;
import com.example.board.dao.PostDao;
import com.example.board.dto.Comment;
import com.example.board.dto.Post;
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
    private final PostDao postDao;

    @Transactional
    public void addComment(int postId, int userId, String content, Integer parentId) { commentDao.addComment(postId, userId, content, parentId); }

    @Transactional(readOnly = true)
    public List<Comment> getComments(int postId) {
        return commentDao.getComments(postId);
    }

    @Transactional
    public Comment getComment(int commentId) { return commentDao.getComment(commentId); }

    @Transactional
    public void deleteComment(int commentId, int postId, int userId) {
        Comment comment = commentDao.getComment(commentId);
        Post post = postDao.getPost(postId);
        if((comment.getUserId() == userId) || (post.getUserId() == userId)) {
            commentDao.deleteComment(commentId);
        }
    }

    @Transactional
    public void updateComment(int commentId, String content) {
        commentDao.updateComment(commentId, content);
    }
}
