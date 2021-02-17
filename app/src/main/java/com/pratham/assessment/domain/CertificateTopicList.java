package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class CertificateTopicList {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int certificatequestionid;
    private String certificatequestion;
    private String certificatekeyword;
    private String subjectid;
    private String examid;

    public String getCertificatequestion() {
        return certificatequestion;
    }

    public void setCertificatequestion(String certificatequestion) {
        this.certificatequestion = certificatequestion;
    }

    public String getCertificatekeyword() {
        return certificatekeyword;
    }

    public void setCertificatekeyword(String certificatekeyword) {
        this.certificatekeyword = certificatekeyword;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public int getCertificatequestionid() {
        return certificatequestionid;
    }

    public void setCertificatequestionid(int certificatequestionid) {
        this.certificatequestionid = certificatequestionid;
    }
    /* @NonNull
    @PrimaryKey
    private String topicid;
    private String certificatequestion;
    private String examId;
    private String rating;

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getCertificatequestion() {
        return certificatequestion;
    }

    public void setCertificatequestion(String certificatequestion) {
        this.certificatequestion = certificatequestion;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }*/
}