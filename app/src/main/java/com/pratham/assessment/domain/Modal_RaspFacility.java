package com.pratham.assessment.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modal_RaspFacility {

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("kind")
    private List<String> kind;

    @SerializedName("facility_id")
    private String facilityId;

    @SerializedName("id")
    private String id;

    @SerializedName("can_manage_content")
    private boolean canManageContent;

    @SerializedName("error")
    private String error;

    @SerializedName("username")
    private String username;

    @Override
    public String toString() {
        return "Modal_RaspFacility{" +
                "fullName='" + fullName + '\'' +
                ", userId='" + userId + '\'' +
                ", kind=" + kind +
                ", facilityId='" + facilityId + '\'' +
                ", id='" + id + '\'' +
                ", canManageContent=" + canManageContent +
                ", error='" + error + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setKind(List<String> kind) {
        this.kind = kind;
    }

    public List<String> getKind() {
        return kind;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCanManageContent(boolean canManageContent) {
        this.canManageContent = canManageContent;
    }

    public boolean isCanManageContent() {
        return canManageContent;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}