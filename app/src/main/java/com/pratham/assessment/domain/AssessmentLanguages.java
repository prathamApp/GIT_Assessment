package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class AssessmentLanguages {
    @NonNull
    @PrimaryKey
    private String languageid;

    private String languagename;

    public String getLanguageid ()
    {
        return languageid;
    }

    public void setLanguageid (String languageid)
    {
        this.languageid = languageid;
    }

    public String getLanguagename ()
    {
        return languagename;
    }

    public void setLanguagename (String languagename)
    {
        this.languagename = languagename;
    }


}
