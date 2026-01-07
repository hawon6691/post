package com.example.board.service;

import com.example.board.dao.PostDao;
import com.example.board.dto.Post;
import com.example.board.exception.InvalidInputException;
import com.example.board.exception.ResourceNotFoundException;
import com.example.board.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Post operations.
 * Handles business logic and transaction management for posts.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostDao postDao;

    /**
     * Creates a new post.
     *
     * @param userId  the user creating the post
     * @param title   the post title
     * @param content the post content
     * @param active  whether the post is active
     */
    @Transactional
    public void addPost(int userId, String title, String content, boolean active) {
        log.info("Creating new post for user: {}", userId);
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidInputException("Post title cannot be empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidInputException("Post content cannot be empty");
        }
        postDao.addPost(userId, title, content, active);
        log.info("Post created successfully for user: {}", userId);
    }

    /**
     * Gets the total count of posts.
     *
     * @return total number of posts
     */
    @Transactional(readOnly = true)
    public int getTotalCount() {
        return postDao.getTotalCount();
    }

    /**
     * Gets a page of posts.
     *
     * @param page the page number (1-based)
     * @return list of posts for the given page
     */
    @Transactional(readOnly = true)
    public List<Post> getPosts(int page) {
        if (page < 1) {
            throw new InvalidInputException("Page number must be greater than 0");
        }
        return postDao.getPosts(page);
    }

    /**
     * Gets a post by ID and increments view count.
     *
     * @param postId the post ID
     * @return the post
     */
    @Transactional
    public Post getPost(int postId) {
        return getPost(postId, true);
    }

    /**
     * Gets a post by ID with optional view count increment.
     *
     * @param postId           the post ID
     * @param updateViewCount  whether to increment view count
     * @return the post
     * @throws ResourceNotFoundException if post not found
     */
    @Transactional
    public Post getPost(int postId, boolean updateViewCount) {
        try {
            Post post = postDao.getPost(postId);
            if (post == null) {
                throw new ResourceNotFoundException("Post", postId);
            }
            if (updateViewCount) {
                postDao.updateViewCount(postId);
            }
            return post;
        } catch (Exception ex) {
            log.error("Error retrieving post with ID: {}", postId, ex);
            throw new ResourceNotFoundException("Post", postId);
        }
    }

    /**
     * Deletes a post if the user owns it.
     *
     * @param userId the user attempting to delete
     * @param postId the post ID
     * @throws UnauthorizedException if user doesn't own the post
     */
    @Transactional
    public void deletePost(int userId, int postId) {
        log.info("User {} attempting to delete post {}", userId, postId);
        Post post = getPost(postId, false);
        if (post.getUserId() != userId) {
            log.warn("Unauthorized delete attempt: user {} tried to delete post {} owned by user {}",
                     userId, postId, post.getUserId());
            throw new UnauthorizedException("You don't have permission to delete this post");
        }
        postDao.deletePost(postId);
        log.info("Post {} deleted successfully by user {}", postId, userId);
    }

    /**
     * Deletes a post (admin function).
     *
     * @param postId the post ID
     */
    @Transactional
    public void deletePost(int postId) {
        log.info("Admin deleting post {}", postId);
        postDao.deletePost(postId);
        log.info("Post {} deleted successfully", postId);
    }

    /**
     * Updates a post.
     *
     * @param postId  the post ID
     * @param title   the new title
     * @param content the new content
     * @param active  the active status
     */
    @Transactional
    public void updatePost(int postId, String title, String content, boolean active) {
        log.info("Updating post {}", postId);
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidInputException("Post title cannot be empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new InvalidInputException("Post content cannot be empty");
        }
        postDao.updatePost(postId, title, content, active);
        log.info("Post {} updated successfully", postId);
    }
}
