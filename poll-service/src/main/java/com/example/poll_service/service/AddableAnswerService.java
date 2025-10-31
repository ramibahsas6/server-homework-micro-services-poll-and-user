package com.example.poll_service.service;

import java.util.Map;

public interface AddableAnswerService {
    Map<String, Object> getUsersNumbersPerQuestionOptions(Integer questionId);

    Map<String, Object> getTotalUsersAnsweredQuestion(Integer questionId);

    Map<String, Object> getUserAnswers(Long userId);

    int getQuestionCountForUser(Long userId);
}
