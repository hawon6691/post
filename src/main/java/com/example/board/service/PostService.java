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
    public Post addPost(long userId, String title, String content, boolean is_public) {
        Post post = postDao.addPost(userId, title, content, is_public);
        postDao.mappingPostTag(post.getPostId(), 1);
        return post;
    }

    @Transactional
    public int getTotalCount() {
        return postDao.getTotalCount();
    }

    @Transactional
    public List<Post> getPosts(int page) {
        return postDao.getPosts(page);
    }

    @Transactional
    public Post getPost(long postId) {
        return getPost(postId, true);
    }

    @Transactional
    public Post getPost(long postId, boolean updateViewCount) {
        Post post = postDao.getPost(postId);
        if(updateViewCount) {
            postDao.updateViewCount(postId);
        }
        return post;
    }

    @Transactional
    public void deletePost(long postId) {
        postDao.deletePost(postId);
    }

    @Transactional
    public void updatePost(long postId, String title, String content, boolean is_public) {
        postDao.updatePost(postId, title, content, is_public);
    }
}
