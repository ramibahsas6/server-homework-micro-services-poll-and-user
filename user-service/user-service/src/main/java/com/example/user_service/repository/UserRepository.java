package com.example.user_service.repository;

import com.example.user_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository implements AddableUserRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /// ////////////// crud /////////////////////////////////////
    public User findUserById(Long user_id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), user_id);
    }

    public List<User> getUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    public int saveUser(User user){
        String sql = "INSERT INTO users(user_id, first_name, last_name, email, password, age, address, joining_date) VALUES(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getPassword(), user.getAge(), user.getAddress().toString(), user.getJoiningDate());
    }

    public int updateUser(User user){
        String sql = "UPDATE users SET user_id = ?, first_name = ?, last_name = ?, email = ?, password = ?, age = ?, address = ?, joining_date = ? WHERE user_id = ?";
        return jdbcTemplate.update( sql,
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getAge(),
                user.getAddress().toString(),
                user.getJoiningDate(),
                user.getUserId()
        );
    }

    public int deleteUserById(Long user_id){
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, user_id);
    }

    /// //////////// addable user repository ///////////////
    @Override
    public User isRegisterByUserId(Long user_id){
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), user_id);
    }

    /// ////////////// helpers ///////////////////////////////////////
    public List<String> getEmailHelper(String email){ // for unique
        String sql = "SELECT email FROM users WHERE email = ?";
        return jdbcTemplate.queryForList(sql, String.class, email);
    }

    public List<Long> userExistsByIdHelper(Long user_id){
        String sql = "SELECT user_id FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, user_id);
    }

    public List<Integer> existsEmailAtAnotherRowHelper(Long user_id, String email){
        String sql = "SELECT user_id FROM users where user_id <> ? AND email = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, user_id, email);
    }
}
