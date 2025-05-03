package com.masterTicket.repository;


import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.masterTicket.model.User;

@Repository
public class UserRepository {
    
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    
    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    };


    public Optional<User> findUsername(String username){
        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";

        return jdbcTemplate.query(sql, userMapper, username).stream().findFirst();
    }

    public void save(User user){
        String sql = "INSERT INTO users (username, password, role) VALUES (?,?,?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getRole());
    }

    public boolean existsByUsername(String username){
        String sql = "SELECT count(id) from users where username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count!= null && count > 0;
    }

    
    
}
