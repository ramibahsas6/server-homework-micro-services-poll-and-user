package com.example.poll_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question {
    private Integer id;
    private String title;
    @JsonProperty("A")
    private String optionA;
    @JsonProperty("B")
    private String optionB;
    @JsonProperty("C")
    private String optionC;
    @JsonProperty("D")
    private String optionD;

    public Question(Integer id, String title, String optionA, String optionB, String optionC, String optionD) {
        this.id = id;
        this.title = title;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    public Question() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    @Override
    public String toString() {
        return "the Question with id=" + id +
                ", title=" + title +
                ", option_a=" + optionA +
                ", option_b=" + optionB +
                ", option_c=" + optionC +
                ", option_d=" + optionD;
    }
}
