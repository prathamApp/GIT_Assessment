package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
@Entity
public class AssessmentPaper {
   String languageid;
   String subjectid;
   String examid;
   @NonNull
   @PrimaryKey
   String paperid;
   String examtime;
   String outofmarks;
   String active;
   @Embedded
   ArrayList<ScienceQuestion> lstpaperquestion;

    public String getLanguageid() {
        return languageid;
    }

    public void setLanguageid(String languageid) {
        this.languageid = languageid;
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

    public String getPaperid() {
        return paperid;
    }

    public void setPaperid(String paperid) {
        this.paperid = paperid;
    }

    public String getExamtime() {
        return examtime;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }

    public String getOutofmarks() {
        return outofmarks;
    }

    public void setOutofmarks(String outofmarks) {
        this.outofmarks = outofmarks;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public ArrayList<ScienceQuestion> getLstpaperquestion() {
        return lstpaperquestion;
    }

    public void setLstpaperquestion(ArrayList<ScienceQuestion> lstpaperquestion) {
        this.lstpaperquestion = lstpaperquestion;
    }
}
