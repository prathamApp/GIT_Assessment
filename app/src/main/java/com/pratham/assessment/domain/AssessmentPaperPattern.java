package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class AssessmentPaperPattern implements Serializable {
    private String subjectname;
    @Embedded
    private ArrayList<AssessmentPatternDetails> lstpatterndetail;

    private String examname;

    private String examduration;

    @SerializedName("question1")
    private String certificateQuestion1;
    @SerializedName("question2")
    private String certificateQuestion2;
    @SerializedName("question3")
    private String certificateQuestion3;


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

    public String getCertificateQuestion1() {
        return certificateQuestion1;
    }

    public void setCertificateQuestion1(String certificateQuestion1) {
        this.certificateQuestion1 = certificateQuestion1;
    }

    public String getCertificateQuestion2() {
        return certificateQuestion2;
    }

    public void setCertificateQuestion2(String certificateQuestion2) {
        this.certificateQuestion2 = certificateQuestion2;
    }

    public String getCertificateQuestion3() {
        return certificateQuestion3;
    }

    public void setCertificateQuestion3(String certificateQuestion3) {
        this.certificateQuestion3 = certificateQuestion3;
    }
}


