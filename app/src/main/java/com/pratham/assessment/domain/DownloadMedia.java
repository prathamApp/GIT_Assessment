package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class DownloadMedia {
    @NonNull
    @PrimaryKey(autoGenerate = true)
   public int id;
   public String qId;
   public String qtId;
   public String paperId;
   public String photoUrl;
   public boolean downloadSuccessful;

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isDownloadSuccessful() {
        return downloadSuccessful;
    }

    public void setDownloadSuccessful(boolean downloadSuccessful) {
        this.downloadSuccessful = downloadSuccessful;
    }

    public String getQtId() {
        return qtId;
    }

    public void setQtId(String qtId) {
        this.qtId = qtId;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
