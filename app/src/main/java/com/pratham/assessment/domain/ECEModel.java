package com.pratham.assessment.domain;

import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

public class ECEModel {


    @PrimaryKey(autoGenerate = true)
    private String questionId;
    @SerializedName("question")
    private String question;
    @SerializedName("type")
    private String type;
    @SerializedName("instructions")
    private String instructions;
    @SerializedName("video")
    private String video;
    @SerializedName("rating")
    private String rating;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
