package com.pratham.assessment.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity
public class AssessmentSubjects implements Comparable, Parcelable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String subjectid;
    private String subjectname;
    private String languageid;

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public AssessmentSubjects() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public static final Creator<AssessmentSubjects> CREATOR = new Creator<AssessmentSubjects>() {
        @Override
        public AssessmentSubjects createFromParcel(Parcel in) {
            return new AssessmentSubjects(in);
        }

        @Override
        public AssessmentSubjects[] newArray(int size) {
            return new AssessmentSubjects[size];
        }
    };

    public AssessmentSubjects(Parcel in) {
        subjectid = in.readString();
        languageid = in.readString();
        subjectname = in.readString();
    }

    public String getLanguageid() {
        return languageid;
    }

    public void setLanguageid(String languageid) {
        this.languageid = languageid;
    }
}
