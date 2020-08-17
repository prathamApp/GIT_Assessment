package com.pratham.assessment.domain;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity(primaryKeys = {"studentid","examid"})
public class NIOSExam {
    @NonNull
    private String studentid;

    private String subjectname;
    @Embedded
    private ArrayList<NIOSExamTopics> lststudentexamtopic;

    private String examname;
    @NonNull
    private String examid;

    private String languageid;

    private String subjectid;

    private String languagename;

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public ArrayList<NIOSExamTopics> getLststudentexamtopic() {
        return lststudentexamtopic;
    }

    public void setLststudentexamtopic(ArrayList<NIOSExamTopics> lststudentexamtopic) {
        this.lststudentexamtopic = lststudentexamtopic;
    }

    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

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

    public String getLanguagename() {
        return languagename;
    }

    public void setLanguagename(String languagename) {
        this.languagename = languagename;
    }

    @Override
    public String toString() {
        return "ClassPojo [studentid = " + studentid + ", subjectname = " + subjectname + ", lststudentexamtopic = " + lststudentexamtopic + ", examname = " + examname + ", examid = " + examid + ", languageid = " + languageid + ", subjectid = " + subjectid + ", languagename = " + languagename + "]";
    }
}

