package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class AssessmentPaperForPush implements Serializable {

    private String languageId;
    private String subjectId;
    private String examId;
    private String examName;
    @NonNull
    @PrimaryKey
    private String paperId;
    private String paperStartTime;
    private String paperEndTime;
    private String examTime;
    private String outOfMarks;
    private String totalMarks;
    private String studentId;
    private int CorrectCnt;
    private int wrongCnt;
    private int SkipCnt;
    private String question1Rating;
    private String question2Rating;
    private String question3Rating;
    private String question4Rating;
    private String question5Rating;
    private String question6Rating;
    private String question7Rating;
    private String question8Rating;
    private String question9Rating;
    private String question10Rating;
//    String certificateQuestionRatings;

    private String FullName;
    private String Gender;
    private int Age;

    private int sentFlag;
    private String isniosstudent;

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

    public String getQuestion6Rating() {
        return question6Rating;
    }

    public void setQuestion6Rating(String question6Rating) {
        this.question6Rating = question6Rating;
    }

    public String getQuestion7Rating() {
        return question7Rating;
    }

    public void setQuestion7Rating(String question7Rating) {
        this.question7Rating = question7Rating;
    }

    public String getQuestion8Rating() {
        return question8Rating;
    }

    public void setQuestion8Rating(String question8Rating) {
        this.question8Rating = question8Rating;
    }

    public String getQuestion9Rating() {
        return question9Rating;
    }

    public void setQuestion9Rating(String question9Rating) {
        this.question9Rating = question9Rating;
    }

    public String getQuestion10Rating() {
        return question10Rating;
    }

    public void setQuestion10Rating(String question10Rating) {
        this.question10Rating = question10Rating;
    }

    public String getIsniosstudent() {
        return isniosstudent;
    }

    public void setIsniosstudent(String isniosstudent) {
        this.isniosstudent = isniosstudent;
    }

   /* public String getCertificateQuestionRatings() {
        return certificateQuestionRatings;
    }

    public void setCertificateQuestionRatings(String certificateQuestionRatings) {
        this.certificateQuestionRatings = certificateQuestionRatings;
    }*/
}
