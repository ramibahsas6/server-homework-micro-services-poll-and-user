package com.example.poll_service.repository;

import com.example.poll_service.model.AnswerOfUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnswerMapper implements RowMapper<AnswerOfUser> {
    @Override
    public AnswerOfUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new AnswerOfUser(
                rs.getInt("id"),
                rs.getLong("user_id"),
                rs.getInt("question_id"),
                rs.getString("selected_option").charAt(0)
        );
    }
}
