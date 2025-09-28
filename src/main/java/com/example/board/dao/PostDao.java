package com.example.board.dao;

import com.example.board.dto.Post;
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
public class PostDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations insertPost;

    public PostDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertPost = new SimpleJdbcInsert(dataSource).withTableName("post").usingGeneratedKeyColumns("post_id");
    }

    @Transactional
    public void addPost(int userId, String title, String content, Boolean isPublic) {
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        post.setIsPublic(isPublic);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        insertPost.execute(params);
    }

    @Transactional(readOnly = true)
    public int getTotalCount() {
        String sql = "select count(*) as total_count from post";
        Integer totalCount = jdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        return totalCount.intValue();
    }

    @Transactional(readOnly = true)
    public List<Post> getPosts(int page) {
        int start = (page - 1) * 10;
        String sql = "select p.userId, p.postId, p.title, p.content, p.viewCount, p.isPublic, p.createdAt, p.updatedAt, p.viewCount, u.name from post p, user u where p.userId = u.userId order by postId desc limit :start, 10";
        RowMapper<Post> rowMapper = BeanPropertyRowMapper.newInstance(Post.class);
        List<Post> list = jdbcTemplate.query(sql, Map.of("start", start), rowMapper);
        return list;
    }

    @Transactional(readOnly = true)
    public Post getPost(int postId) {
        String sql = "select p.userId, p.postId, p.title, p.content, p.viewCount, p.isPublic, p.createdAt, p.updatedAt, p.viewCount, u.name from post p, user u where p.userId = u.userId and p.postId = :postId";
        RowMapper<Post> rowMapper = BeanPropertyRowMapper.newInstance(Post.class);
        Post post = jdbcTemplate.queryForObject(sql, Map.of("postId", postId), rowMapper);
        return post;
    }

    @Transactional
    public void updateViewCount(int postId) {
        String sql = "update post set viewCount = viewCount + 1 where postId = :postId";
        jdbcTemplate.update(sql, Map.of("postId", postId));
    }

    @Transactional
    public void deletePost(int postId) {
        String sql = "delete from post where postId = :postId";
        jdbcTemplate.update(sql, Map.of("postId", postId));
    }

    @Transactional
    public void updatePost(int postId, String title, String content, Boolean isPublic) {
        String sql = "update post set title = :title, content = :content, isPublic = :isPublic where postId = 1";

        Post post = new Post();
        post.setPostId(postId);
        post.setTitle(title);
        post.setContent(content);
        post.setIsPublic(isPublic);
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        jdbcTemplate.update(sql, params);

//        jdbcTemplate.update(sql, Map.of("postId", postId, "title", title, "content", content));
    }
}
