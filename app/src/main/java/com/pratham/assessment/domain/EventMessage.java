package com.pratham.assessment.domain;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class EventMessage {
    String message;
    int downlaodContentSize;
    ContentTable contentDetail;
    Modal_FileDownloading modal_fileDownloading;
    ArrayList<ContentTable> content;
    Drawable connection_resource;
    String connection_name;
    String pushData;
    String downloadId;
    String file_name;
    long progress;

    public Modal_FileDownloading getModal_fileDownloading() {
        return modal_fileDownloading;
    }

    public void setModal_fileDownloading(Modal_FileDownloading modal_fileDownloading) {
        this.modal_fileDownloading = modal_fileDownloading;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public ArrayList<ContentTable> getContent() {
        return content;
    }

    public void setContent(ArrayList<ContentTable> content) {
        this.content = content;
    }

    public Drawable getConnection_resource() {
        return connection_resource;
    }

    public void setConnection_resource(Drawable connection_resource) {
        this.connection_resource = connection_resource;
    }

    public String getConnection_name() {
        return connection_name;
    }

    public void setConnection_name(String connection_name) {
        this.connection_name = connection_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDownlaodContentSize() {
        return downlaodContentSize;
    }

    public void setDownlaodContentSize(int downlaodContentSize) {
        this.downlaodContentSize = downlaodContentSize;
    }

    public ContentTable getContentDetail() {
        return contentDetail;
    }

    public void setContentDetail(ContentTable contentDetail) {
        this.contentDetail = contentDetail;
    }

    public ArrayList<ContentTable> getContentList() {
        return content;
    }

    public void setContentList(ArrayList<ContentTable> content) {
        this.content = content;
    }

    public String getPushData() {
        return pushData;
    }

    public void setPushData(String pushData) {
        this.pushData = pushData;
    }
}
