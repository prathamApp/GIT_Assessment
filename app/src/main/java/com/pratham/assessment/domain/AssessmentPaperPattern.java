package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class AssessmentPaperPattern {
    private String subjectname;
    @Embedded
    private ArrayList<AssessmentPatternDetails> lstpatterndetail;

    private String examname;

    private String examduration;

    private String outofmarks;
    @NonNull
    @PrimaryKey
    private String examid;

    private String subjectid;

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public ArrayList<AssessmentPatternDetails> getLstpatterndetail() {
        return lstpatterndetail;
    }

    public void setLstpatterndetail(ArrayList<AssessmentPatternDetails> lstpatterndetail) {
        this.lstpatterndetail = lstpatterndetail;
    }

    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public String getExamduration() {
        return examduration;
    }

    public void setExamduration(String examduration) {
        this.examduration = examduration;
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

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

}


