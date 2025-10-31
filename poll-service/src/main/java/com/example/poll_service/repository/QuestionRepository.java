package com.example.poll_service.repository;

import com.example.poll_service.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Question findQuestionById(Integer id){
        String sql = "SELECT * FROM questions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new QuestionMapper(), id);
    }

    public List<Question> getQuestions() {
        String sql = "SELECT * FROM questions";
        return jdbcTemplate.query(sql, new QuestionMapper());
    }

    public int saveQuestion(Question question){
        String sql = "INSERT INTO questions(title, option_a, option_b, option_c, option_d) VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql, question.getTitle(),question.getOptionA(),question.getOptionB(),question.getOptionC(),
                question.getOptionD());
    }

    public int updateQuestion(Question question){
        String sql = "UPDATE questions SET id = ?, title = ?, option_a = ?, option_b = ?," +
                " option_c = ?, option_d = ? WHERE id = ?";
        return jdbcTemplate.update(sql, question.getId(), question.getTitle(), question.getOptionA(), question.getOptionB(), question.getOptionC(),
                question.getOptionD(), question.getId());
    }

    public int deleteQuestion(Integer id){
        String sql = "DELETE FROM questions WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /// //////////////////// helpers ///////////////////////////////////////////
    public List<Integer> questionExistsByIdHelper(Integer id){
        String sql = "SELECT id FROM questions where id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, id);
    }

    public List<Integer> questionExistsByTheSameValuesHelper(Question question){
        String sql = "SELECT id FROM questions WHERE title = ? AND option_a = ? AND option_b = ?" +
                " AND option_c = ? AND option_d = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, question.getTitle(), question.getOptionA(),
                question.getOptionB(), question.getOptionC(), question.getOptionD());
    }

    public Integer getQuestionsSize(){
        String sql = "SELECT COUNT(*) questions";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
