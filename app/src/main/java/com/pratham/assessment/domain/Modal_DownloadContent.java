package com.pratham.assessment.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Modal_DownloadContent {
    @SerializedName("nodelist")
    List<ContentTable> nodelist;
    @SerializedName("downloadurl")
    String downloadurl;
    @SerializedName("foldername")
    String foldername;

    @Override
    public String toString() {
        return "Modal_DownloadContent{" +
                "nodelist=" + nodelist +
                ", downloadurl='" + downloadurl + '\'' +
                ", foldername='" + foldername + '\'' +
                '}';
    }

    public List<ContentTable> getNodelist() {
        return nodelist;
    }

    public void setNodelist(List<ContentTable> nodelist) {
        this.nodelist = nodelist;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }
}
