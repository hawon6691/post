package com.example.board.dao;

import com.example.board.dto.User;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Repository
public class UserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations insertUser;

    public UserDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertUser = new SimpleJdbcInsert(dataSource).withTableName("user").usingGeneratedKeyColumns("user_id");
    }

    @Transactional
    public User addUser(String email, String name, String password) {
        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);
        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
        Number number = insertUser.executeAndReturnKey(params);
        int userId = number.intValue();
        user.setUserId(userId);
        return user;
    }

    @Transactional
    public void mappingUserRole(int userId) {
        String sql = "";
    }
}
