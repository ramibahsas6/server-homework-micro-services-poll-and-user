package com.example.poll_service.service;

import com.example.poll_service.model.Question;

import java.util.Map;

public interface AddableQuestionService {
    Map<Question, Map<String, Object>> allQuestionsWithCountUsersPerOptions();
}
