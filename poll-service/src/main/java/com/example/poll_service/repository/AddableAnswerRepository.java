package com.example.poll_service.repository;

import java.util.List;
import java.util.Map;

public interface AddableAnswerRepository {
    Integer deleteAnswersPerUser(Long user_id);

    Map<Character, Integer> countAnswersByQuestionId(Integer questionId);

    List<Map<String, Object>> getAnswersByUserId(Long userId);

    int countQuestionsByUserId(Long userId);
}
