package com.pratham.assessment.domain;

import java.io.Serializable;
import java.util.List;

public class ResultModalClass implements Serializable {

    String qId;
    String Question;
    String QuestionImg;
    String correctAnswer;
    String userAnswer;
    String userAnswerId;
    List<ScienceQuestionChoice> userAnsList;
    boolean isCorrect;
    boolean isAttempted;

    public String getUserAnswerId() {
        return userAnswerId;
    }

    public void setUserAnswerId(String userAnswerId) {
        this.userAnswerId = userAnswerId;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public boolean isAttempted() {
        return isAttempted;
    }

    public void setAttempted(boolean attempted) {
        isAttempted = attempted;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }


    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getQuestionImg() {
        return QuestionImg;
    }

    public void setQuestionImg(String questionImg) {
        QuestionImg = questionImg;
    }

    public List<ScienceQuestionChoice> getUserAnsList() {
        return userAnsList;
    }

    public void setUserAnsList(List<ScienceQuestionChoice> userAnsList) {
        this.userAnsList = userAnsList;
    }
}
