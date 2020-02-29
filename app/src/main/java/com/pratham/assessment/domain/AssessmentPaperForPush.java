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
    String question1Rating;
    String question2Rating;
    String question3Rating;
    String question4Rating;
    String question5Rating;

    private String FullName;
    private String Gender;
    private int Age;

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
        return outOfMarks == null ? "" : outOfMarks;
    }

    public void setOutOfMarks(String outOfMarks) {
        this.outOfMarks = outOfMarks;
    }

    public String getTotalMarks() {
        return totalMarks == null ? "" : totalMarks;

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

    public String getQuestion1Rating() {
        return question1Rating;
    }

    public void setQuestion1Rating(String question1Rating) {
        this.question1Rating = question1Rating;
    }

    public String getQuestion2Rating() {
        return question2Rating;
    }

    public void setQuestion2Rating(String question2Rating) {
        this.question2Rating = question2Rating;
    }

    public String getQuestion3Rating() {
        return question3Rating;
    }

    public void setQuestion3Rating(String question3Rating) {
        this.question3Rating = question3Rating;
    }


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getQuestion4Rating() {
        return question4Rating;
    }

    public void setQuestion4Rating(String question4Rating) {
        this.question4Rating = question4Rating;
    }

    public String getQuestion5Rating() {
        return question5Rating;
    }

    public void setQuestion5Rating(String question5Rating) {
        this.question5Rating = question5Rating;
    }
}
