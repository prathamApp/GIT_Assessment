package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class ScienceAssessmentAnswer implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ansid;

    private String qid;

    private String languageid;

    private String subjectid;

    private String topicid;

    private String lessonid;

    private String qtid;

    private String qlevel;

    private boolean isAttempted;

    private boolean isCorrect;

    private String scoredMarks;

    private String outofmarks;

    private String examid;

    private String paperid;

    private String questionStartTime;

    private String timeTakenToAnswer;

    private String StudentID;
    private String sentFlag;

    public String getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(String sentFlag) {
        this.sentFlag = sentFlag;
    }

    public int getAnsid() {
        return ansid;
    }

    public void setAnsid(int ansid) {
        this.ansid = ansid;
    }

    public String getScoredMarks() {
        return scoredMarks;
    }

    public void setScoredMarks(String scoredMarks) {
        this.scoredMarks = scoredMarks;
    }

    public String getQuestionStartTime() {
        return questionStartTime;
    }

    public void setQuestionStartTime(String questionStartTime) {
        this.questionStartTime = questionStartTime;
    }

    public String getTimeTakenToAnswer() {
        return timeTakenToAnswer;
    }

    public void setTimeTakenToAnswer(String timeTakenToAnswer) {
        this.timeTakenToAnswer = timeTakenToAnswer;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
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


    public String getQlevel() {
        return qlevel;
    }

    public void setQlevel(String qlevel) {
        this.qlevel = qlevel;
    }


    public String getLanguageid() {
        return languageid;
    }

    public void setLanguageid(String languageid) {
        this.languageid = languageid;
    }


    public String getLessonid() {
        return lessonid;
    }

    public void setLessonid(String lessonid) {
        this.lessonid = lessonid;
    }


    public String getQtid() {
        return qtid;
    }

    public void setQtid(String qtid) {
        this.qtid = qtid;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }


    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }


    public String getOutofmarks() {
        return outofmarks;
    }

    public void setOutofmarks(String outofmarks) {
        this.outofmarks = outofmarks;
    }


    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }


    public String getPaperid() {
        return paperid;
    }

    public void setPaperid(String paperid) {
        this.paperid = paperid;
    }


}
