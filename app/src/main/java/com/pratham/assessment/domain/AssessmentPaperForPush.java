package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class AssessmentPaperForPush {
    String languageId;
    String subjectId;
    @NonNull
    @PrimaryKey
    String examId;
    String paperId;
    String paperStartTime;
    String paperEndTime;
    String outOfMarks;
    String totalMarks;

    @Embedded
    ArrayList<Score> scoreList;

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

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
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
}
