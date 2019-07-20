package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class AssessmentPaperForPush implements Serializable {

    String languageId;
    String subjectId;
    String examId;
    String examName;
    @NonNull
    @PrimaryKey
    String paperId;
    String paperStartTime;
    String paperEndTime;
    String examTime;
    String outOfMarks;
    String totalMarks;
    String studentId;
    int CorrectCnt;
    int wrongCnt;
    int SkipCnt;


    private int sentFlag;

    private String SessionID;
    @Embedded
    ArrayList<Score> scoreList;

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    @NonNull
    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(@NonNull String paperId) {
        this.paperId = paperId;
    }

    public String getPaperStartTime() {
        return paperStartTime;
    }

    public void setPaperStartTime(String paperStartTime) {
        this.paperStartTime = paperStartTime;
    }

    public String getPaperEndTime() {
        return paperEndTime;
    }

    public void setPaperEndTime(String paperEndTime) {
        this.paperEndTime = paperEndTime;
    }

    public String getOutOfMarks() {
        return outOfMarks;
    }

    public void setOutOfMarks(String outOfMarks) {
        this.outOfMarks = outOfMarks;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    public ArrayList<Score> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<Score> scoreList) {
        this.scoreList = scoreList;
    }

    public int getCorrectCnt() {
        return CorrectCnt;
    }

    public void setCorrectCnt(int correctCnt) {
        CorrectCnt = correctCnt;
    }

    public int getWrongCnt() {
        return wrongCnt;
    }

    public void setWrongCnt(int wrongCnt) {
        this.wrongCnt = wrongCnt;
    }

    public int getSkipCnt() {
        return SkipCnt;
    }

    public void setSkipCnt(int skipCnt) {
        SkipCnt = skipCnt;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }
}
