package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "AssessmentTopic")
public class AssessmentToipcsModal {
    @NonNull
    @PrimaryKey
    private String topicid;

    private String topicname;

    private String subjectid;

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

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

   /* @Override
    public String toString() {
        return "[topicid = " + topicid + ", topicname = " + topicname + ", subjectId = " + subjectId + "]";
    }*/
}