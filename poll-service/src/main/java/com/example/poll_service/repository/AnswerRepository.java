package com.example.poll_service.repository;

import com.example.poll_service.model.AnswerOfUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AnswerRepository implements AddableAnswerRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /// /////////////////// crud /////////////////////////////////
    public AnswerOfUser findAnswerById(Integer id){
        String sql = "SELECT * FROM answers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new AnswerMapper(), id);
    }

    public List<AnswerOfUser> getAnswers() {
        String sql = "SELECT * FROM answers";
        return jdbcTemplate.query(sql, new AnswerMapper());
    }

    public int saveAnswer(AnswerOfUser answer){
        String sql = "INSERT INTO answers(id, user_id, question_id, selected_option) VALUES(?,?,?,?)";
        return jdbcTemplate.update(sql, answer.getId(), answer.getUserId(), answer.getQuestionId(), answer.getSelectedOption());
    }

    public int updateAnswer(AnswerOfUser answer){
        String sql = "UPDATE answers SET id = ?, user_id = ?, question_id = ?, selected_option = ? WHERE id = ?";
        return jdbcTemplate.update(sql, answer.getId(), answer.getUserId(), answer.getQuestionId(), answer.getSelectedOption(),
                answer.getId());
    }

    public Integer deleteAnswer(Integer id) {
        String sql = "DELETE FROM answers WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /// //////////////////  addable answer repository ////////////////////////
    @Override
    public Integer deleteAnswersPerUser(Long user_id){
        String sql = "DELETE FROM answers WHERE user_id = ?";
        return jdbcTemplate.update(sql, user_id);
    }

    @Override
    public Map<Character, Integer> countAnswersByQuestionId(Integer questionId) {
        String sql =
            "SELECT selected_option, COUNT(*) AS total " +
            "FROM answers " +
            "WHERE question_id = ? " +
            "GROUP BY selected_option";

        Map<Character, Integer> result = new HashMap<>();

        try {
            jdbcTemplate.query(sql, new Object[]{questionId}, rs -> {
                result.put(rs.getString("selected_option").charAt(0), rs.getInt("total"));
            });
        }catch (Exception e){
            return null;
        }

        for (char option : new char[]{'A', 'B', 'C', 'D'}) {
            result.putIfAbsent(option, 0);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getAnswersByUserId(Long userId) {
        String sql = "SELECT question_id, selected_option FROM answers WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, userId);
    }

    @Override
    public int countQuestionsByUserId(Long userId) {
        String sql = "SELECT COUNT(DISTINCT question_id) FROM answers WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    /// ///////////////////// helpers /////////////////////////////////
    public boolean questionExists(int questionId) {
        String sql = "SELECT COUNT(*) FROM questions WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, questionId);
        return count != null && count > 0;
    }

    public List<Integer> answerExistsByIdHelper(Integer id){
        String sql = "SELECT id FROM answers where id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, id);
    }

    public List<Integer> existsTheSameUserIdWithTheSameQuestionHelper(Integer id,Long userId, Integer questionId){
        String sql = "SELECT id FROM answers WHERE user_id = ? AND question_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, userId, questionId);
    }

    public List<Integer> answerExistsByQuestionIdHelper(Integer id){
        String sql = "SELECT id FROM answers where question_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, id);
    }

    public List<Integer> answerExistsByTheSameQuestionIdAndUserIdAtAnotherRowHelper(Integer id,Integer questionId, Long userId){
        String sql = "SELECT id FROM answers where id <> ? AND question_id = ? AND user_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, id, questionId, userId);
    }
}
