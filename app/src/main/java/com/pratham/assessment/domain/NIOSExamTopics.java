package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class NIOSExamTopics {
    private String subjectname;

    private String topicid;

    private String examname;

    @NonNull
    @PrimaryKey
    private String examid;

    private String languageid;

    private String topicname;

    private String subjectid;

    private String languagename;

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
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

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
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
        return "ClassPojo [subjectname = " + subjectname + ", topicid = " + topicid + ", examname = " + examname + ", examid = " + examid + ", languageid = " + languageid + ", topicname = " + topicname + ", subjectid = " + subjectid + ", languagename = " + languagename + "]";
    }
}
