package com.pratham.assessment.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "Score")
public class Score implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ScoreId")
    private int ScoreId;
    @ColumnInfo(name = "SessionID")
    private String SessionID;
    @ColumnInfo(name = "StudentID")
    private String StudentID;
    @ColumnInfo(name = "DeviceID")
    private String DeviceID;
    @ColumnInfo(name = "ResourceID")
    private String ResourceID;
    @ColumnInfo(name = "QuestionId")
    private int QuestionId;
    @ColumnInfo(name = "ScoredMarks")
    private int ScoredMarks;
    @ColumnInfo(name = "TotalMarks")
    private int TotalMarks;
    @ColumnInfo(name = "StartDateTime")
    private String StartDateTime;
    @ColumnInfo(name = "EndDateTime")
    private String EndDateTime;
    @ColumnInfo(name = "revisitedStartDateTime")
    private String revisitedStartDateTime;
    @ColumnInfo(name = "revisitedEndDateTime")
    private String revisitedEndDateTime;


    @ColumnInfo(name = "Level")
    private int Level;
    @ColumnInfo(name = "Label")
    private String Label;
    @ColumnInfo(name = "sentFlag")
    private int sentFlag;
    @ColumnInfo(name = "isAttempted")
    private boolean isAttempted;
    @ColumnInfo(name = "isCorrect")
    private boolean isCorrect;
    @ColumnInfo(name = "userAnswer")
    private String userAnswer;
    @ColumnInfo(name = "examId")
    private String examId;
    @ColumnInfo(name = "paperId")
    private String paperId;


    @Override
    public String toString() {
        return "Score{" +
                "ScoreId='" + ScoreId + '\'' +
                ", SessionID='" + SessionID + '\'' +
                ", StudentID='" + StudentID + '\'' +
                ", DeviceId='" + DeviceID + '\'' +
                ", ResourceID='" + ResourceID + '\'' +
                ", QuestionId=" + QuestionId +
                ", ScoredMarks=" + ScoredMarks +
                ", TotalMarks=" + TotalMarks +
                ", StartDateTime='" + StartDateTime + '\'' +
                ", EndDateTime='" + EndDateTime + '\'' +
                ", Level=" + Level +
                '}';
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean getIsAttempted() {
        return isAttempted;
    }

    public void setIsAttempted(boolean attempted) {
        isAttempted = attempted;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    @NonNull
    public int getScoreId() {
        return ScoreId;
    }

    public void setScoreId(@NonNull int scoreId) {
        ScoreId = scoreId;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getResourceID() {
        return ResourceID;
    }

    public void setResourceID(String resourceID) {
        ResourceID = resourceID;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public int getScoredMarks() {
        return ScoredMarks;
    }

    public void setScoredMarks(int scoredMarks) {
        ScoredMarks = scoredMarks;
    }

    public int getTotalMarks() {
        return TotalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        TotalMarks = totalMarks;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getRevisitedStartDateTime() {
        return revisitedStartDateTime;
    }

    public void setRevisitedStartDateTime(String revisitedStartDateTime) {
        this.revisitedStartDateTime = revisitedStartDateTime;
    }

    public String getRevisitedEndDateTime() {
        return revisitedEndDateTime;
    }

    public void setRevisitedEndDateTime(String revisitedEndDateTime) {
        this.revisitedEndDateTime = revisitedEndDateTime;
    }
}
