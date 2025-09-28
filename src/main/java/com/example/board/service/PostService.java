package com.example.board.service;

import com.example.board.dao.PostDao;
import com.example.board.dto.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostDao postDao;

    @Transactional
    public void addPost(int userId, String title, String content, boolean active) {
        postDao.addPost(userId, title, content, active);
    }

    @Transactional(readOnly = true)
    public int getTotalCount() {
        return postDao.getTotalCount();
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts(int page) {
        return postDao.getPosts(page);
    }

    @Transactional
    public Post getPost(int boardId) {
        return getPost(boardId, true);
    }

    @Transactional
    public Post getPost(int boardId, boolean updateViewCount) {
        Post post = postDao.getPost(boardId);
        if(updateViewCount) {
            postDao.updateViewCount(boardId);
        }
        return post;
    }

    @Transactional
    public void deletePost(int userId, int boardId) {
        Post post = postDao.getPost(boardId);
        if(post.getUserId() == userId) {
            postDao.deletePost(boardId);
        }
    }

    @Transactional
    public void deletePost(int boardId) {
        postDao.deletePost(boardId);
    }

    @Transactional
    public void updatePost(int boardId, String title, String content, boolean active) {
        postDao.updatePost(boardId, title, content, active);
    }
}
