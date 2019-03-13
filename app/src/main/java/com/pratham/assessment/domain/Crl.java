package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Crl")
public class Crl {

    @PrimaryKey(autoGenerate = true)
    private int CrlTableId;
    @SerializedName("CRLId")
    private String CRLId;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("LastName")
    private String LastName;
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("Password")
    private String Password;
    @SerializedName("ProgramId")
    private int ProgramId;
    @SerializedName("ProgramName")
    private String ProgramName;
    @SerializedName("Mobile")
    private String Mobile;
    @SerializedName("State")
    private String State;
    @SerializedName("Email")
    private String Email;
    private String CreatedBy;
    private boolean newCrl;


    @Override
    public String toString() {
        return "Crl{" +
                "  CrlTableId=" + CrlTableId +
                ", CRLId='" + CRLId + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", ProgramId=" + ProgramId +
                ", Mobile='" + Mobile + '\'' +
                ", State='" + State + '\'' +
                ", Email='" + Email + '\'' +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", newCrl=" + newCrl +
                '}';
    }

    public String getProgramName() {
        return ProgramName;
    }

    public void setProgramName(String programName) {
        ProgramName = programName;
    }

    public int getCrlTableId() {
        return CrlTableId;
    }

    public void setCrlTableId(int crlTableId) {
        CrlTableId = crlTableId;
    }

    public String getCRLId() {
        return CRLId;
    }

    public void setCRLId(String CRLId) {
        this.CRLId = CRLId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getProgramId() {
        return ProgramId;
    }

    public void setProgramId(int programId) {
        ProgramId = programId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public boolean isNewCrl() {
        return newCrl;
    }

    public void setNewCrl(boolean newCrl) {
        this.newCrl = newCrl;
    }
}
