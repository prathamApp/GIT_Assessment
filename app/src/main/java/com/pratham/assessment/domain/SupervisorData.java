package com.pratham.assessment.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "SupervisorData")
public class SupervisorData {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @SerializedName("sId")
    public int sId;
    @SerializedName("supervisorId")
    public String supervisorId;
    @SerializedName("assessmentSessionId")
    public String assessmentSessionId;
    @SerializedName("supervisorName")
    public String supervisorName;
    @SerializedName("supervisorPhoto")
    public String supervisorPhoto;
    @ColumnInfo(name = "sentFlag")
    private int sentFlag;

    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    @NonNull
    public int getsId() {
        return sId;
    }

    public void setsId(@NonNull int sId) {
        this.sId = sId;
    }

    public String getAssessmentSessionId() {
        return assessmentSessionId;
    }

    public void setAssessmentSessionId(String assessmentSessionId) {
        this.assessmentSessionId = assessmentSessionId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getSupervisorPhoto() {
        return supervisorPhoto;
    }

    public void setSupervisorPhoto(String supervisorPhoto) {
        this.supervisorPhoto = supervisorPhoto;
    }
}