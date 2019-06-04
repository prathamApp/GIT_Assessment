package com.pratham.assessment.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Assessment")
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ScoreIda")
    private int ScoreIda;
    @ColumnInfo(name = "SessionIDm")
    private String SessionIDm;
    @ColumnInfo(name = "SessionIDa")
    private String SessionIDa;
    @ColumnInfo(name = "StudentIDa")
    private String StudentIDa;
    @ColumnInfo(name = "DeviceIDa")
    private String DeviceIDa;
    @ColumnInfo(name = "ResourceIDa")
    private String ResourceIDa;
    @ColumnInfo(name = "QuestionIda")
    private int QuestionIda;
    @ColumnInfo(name = "ScoredMarksa")
    private int ScoredMarksa;
    @ColumnInfo(name = "TotalMarksa")
    private int TotalMarksa;
    @ColumnInfo(name = "StartDateTimea")
    private String StartDateTimea;
    @ColumnInfo(name = "EndDateTimea")
    private String EndDateTime;
    @ColumnInfo(name = "Levela")
    private int Levela;
    @ColumnInfo(name = "Labela")
    private String Label;
    @ColumnInfo(name = "sentFlag")
    private int sentFlag;







    public int getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(int sentFlag) {
        this.sentFlag = sentFlag;
    }

    public int getScoreIda() {
        return ScoreIda;
    }

    public void setScoreIda(int scoreIda) {
        ScoreIda = scoreIda;
    }

    public String getSessionIDm() {
        return SessionIDm;
    }

    public void setSessionIDm(String sessionIDm) {
        SessionIDm = sessionIDm;
    }

    public String getSessionIDa() {
        return SessionIDa;
    }

    public void setSessionIDa(String sessionIDa) {
        SessionIDa = sessionIDa;
    }

    public String getStudentIDa() {
        return StudentIDa;
    }

    public void setStudentIDa(String studentIDa) {
        StudentIDa = studentIDa;
    }

    public String getDeviceIDa() {
        return DeviceIDa;
    }

    public void setDeviceIDa(String deviceIDa) {
        DeviceIDa = deviceIDa;
    }

    public String getResourceIDa() {
        return ResourceIDa;
    }

    public void setResourceIDa(String resourceIDa) {
        ResourceIDa = resourceIDa;
    }

    public int getQuestionIda() {
        return QuestionIda;
    }

    public void setQuestionIda(int questionIda) {
        QuestionIda = questionIda;
    }

    public int getScoredMarksa() {
        return ScoredMarksa;
    }

    public void setScoredMarksa(int scoredMarksa) {
        ScoredMarksa = scoredMarksa;
    }

    public int getTotalMarksa() {
        return TotalMarksa;
    }

    public void setTotalMarksa(int totalMarksa) {
        TotalMarksa = totalMarksa;
    }

    public String getStartDateTimea() {
        return StartDateTimea;
    }

    public void setStartDateTimea(String startDateTimea) {
        StartDateTimea = startDateTimea;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    public int getLevela() {
        return Levela;
    }

    public void setLevela(int levela) {
        Levela = levela;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }
}
