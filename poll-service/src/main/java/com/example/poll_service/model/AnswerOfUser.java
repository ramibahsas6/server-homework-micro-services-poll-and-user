package com.example.poll_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerOfUser {
    private Integer id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("question_id")
    private Integer questionId;
    @JsonProperty("selected_option")
    private char selectedOption;

    public AnswerOfUser(Integer id, Long userId, Integer questionId, char selectedOption) {
        this.id = id;
        this.userId = userId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public AnswerOfUser() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public char getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(char selectedOption) {
        this.selectedOption = selectedOption;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", userId=" + userId +
                ", questionId=" + questionId +
                ", selectedOption=" + selectedOption +
                '}';
    }
}
