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
    public void addPost(int userId, String title, String content, boolean is_public, List<Integer> tagIds) {
        postDao.addPost(userId, title, content, is_public);

        int postId = postDao.getLastInsertId();

        for(int tagId : tagIds) {
            postDao.mappingPostTag(postId, tagId);
        }
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
    public Post getPost(int postId) {
        return getPost(postId, true);
    }

    @Transactional
    public Post getPost(int postId, boolean updateViewCount) {
        Post post = postDao.getPost(postId);
        if(updateViewCount) {
            postDao.updateViewCount(postId);
        }
        return post;
    }

    @Transactional
    public void deletePost(int userId, int postId) {
        Post post = postDao.getPost(postId);
        if(post.getUserId() == userId) {
            postDao.deletePost(postId);
        }
    }

    @Transactional
    public void deletePost(int postId) {
        postDao.deletePost(postId);
    }

    @Transactional
    public void updatePost(int postId, String title, String content, boolean is_public) {
        postDao.updatePost(postId, title, content, is_public);
    }
}
