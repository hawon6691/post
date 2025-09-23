package com.example.board.dao;

import com.example.board.dto.Post;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
    public Post addPost(long userId, String title, String content, boolean is_public) {
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(title);
        post.setContent(content);
        post.set_public(is_public);
        post.setCreated_at(LocalDateTime.now());
        post.setUpdated_at(LocalDateTime.now());

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        insertPost.execute(params);

        return post;
    }

    @Transactional
    public void mappingPostTag(long postId, long tagId) {
        String sql = "insert into post_tag(post_id, tag_id) values(:postId,:tagId);";
        SqlParameterSource params = new MapSqlParameterSource("postId", postId);
        jdbcTemplate.update(sql, params);
    }

    @Transactional (readOnly = true)
    public int getTotalCount() {
        String sql = "select count(*) as total_count from post";
        Integer totalCount = jdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        return totalCount.intValue();
    }

    @Transactional (readOnly = true)
    public List<Post> getPosts(int page) {
        int start = (page - 1) * 10;
        String sql = "select p.user_id, p.post_id, p.title, p.created_at, p.updated_at, p.view_count, u.name from post p, user u where p.user_id = u.user_id order by post_id desc limit :start, 10";
        RowMapper<Post> rowMapper = BeanPropertyRowMapper.newInstance(Post.class);
        List<Post> list = jdbcTemplate.query(sql, Map.of("start", start), rowMapper);
        return list;
    }

    @Transactional (readOnly = true)
    public Post getPost(long postId) {
        String sql = "select p.user_id, p.post_id, p.title, p.created_at, p.updated_at, p.view_count, u.name, p.content from post p, user u where p.user_id = u.user_id and p.post_id = :postId";
        RowMapper<Post> rowMapper = BeanPropertyRowMapper.newInstance(Post.class);
        Post post = jdbcTemplate.queryForObject(sql, Map.of("postId", postId), rowMapper);
        return post;
    }

    @Transactional
    public void updateViewCount(long postId) {
        String sql = "update post set view_count = view_count + 1 where post_id = :postId";
        jdbcTemplate.update(sql, Map.of("postId", postId));
    }

    @Transactional
    public void deletePost(long postId) {
        String sql = "delete from post where post_id - :postId";
        jdbcTemplate.update(sql, Map.of("postId", postId));
    }

    @Transactional
    public void updatePost(long postId, String title, String content, boolean is_public) {
        String sql = "update post set title = :title, content = :content, is_public = :is_public where post_id = :postId";

        Post post = new Post();
        post.setPostId(postId);
        post.setTitle(title);
        post.setContent(content);
        post.set_public(is_public);
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        jdbcTemplate.update(sql, params);
    }
}
