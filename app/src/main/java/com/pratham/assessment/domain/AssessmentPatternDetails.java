package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class AssessmentPatternDetails {
    private String totalmarks;

    private String noofquestion;

    private String qtname;

    private String marksperquestion;

    private String topicid;

    private String qlevel;

    private String paralevel;

    private String qlevelmarks;

    private String examId;

    private String topicname;

    private String keyworddetail;

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }


    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int patternId;

    private String qtid;

    public int getPatternId() {
        return patternId;
    }

    public void setPatternId(int patternId) {
        this.patternId = patternId;
    }


    public String getQlevel() {
        return qlevel;
    }

    public void setQlevel(String qlevel) {
        this.qlevel = qlevel;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    public String getTotalmarks() {
        return totalmarks;
    }

    public void setTotalmarks(String totalmarks) {
        this.totalmarks = totalmarks;
    }

    public String getNoofquestion() {
        return noofquestion;
    }

    public void setNoofquestion(String noofquestion) {
        this.noofquestion = noofquestion;
    }

    public String getQtname() {
        return qtname;
    }

    public void setQtname(String qtname) {
        this.qtname = qtname;
    }

    public String getMarksperquestion() {
        return marksperquestion;
    }

    public void setMarksperquestion(String marksperquestion) {
        this.marksperquestion = marksperquestion;
    }

    public String getQtid() {
        return qtid;
    }

    public void setQtid(String qtid) {
        this.qtid = qtid;
    }

    public String getParalevel() {
        return paralevel;
    }

    public void setParalevel(String paralevel) {
        this.paralevel = paralevel;
    }

    public String getQlevelmarks() {
        return qlevelmarks;
    }

    public void setQlevelmarks(String qlevelmarks) {
        this.qlevelmarks = qlevelmarks;
    }

    public String getKeyworddetail() {
        return keyworddetail;
    }

    public void setKeyworddetail(String keyworddetail) {
        this.keyworddetail = keyworddetail;
    }
}

