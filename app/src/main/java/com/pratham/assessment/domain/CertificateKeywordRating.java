package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class CertificateKeywordRating {
    @PrimaryKey(autoGenerate = true)
    private int keywordRatingId;
    private String paperId;
    private String certificatequestion;
    private String certificatekeyword;
    private String rating;
    private String subjectId;
    private String examId;
    private String studentId;
    private String languageId;
    @NonNull
    private int sentFlag;

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public int getKeywordRatingId() {
        return keywordRatingId;
    }

    public void setKeywordRatingId(int keywordRatingId) {
        this.keywordRatingId = keywordRatingId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }
}
