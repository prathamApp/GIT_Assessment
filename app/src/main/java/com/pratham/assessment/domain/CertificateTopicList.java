package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//@Entity
public class CertificateTopicList {
    @NonNull
    @PrimaryKey
    private String topicid;
    private String certificatequestion;

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }

    public String getCertificatequestion() {
        return certificatequestion;
    }

    public void setCertificatequestion(String certificatequestion) {
        this.certificatequestion = certificatequestion;
    }
}