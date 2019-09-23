package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.pratham.assessment.domain.AssessmentPaperForPush;

import java.util.List;

public class AssessmentSubjectsExpandable implements ParentListItem {
    @NonNull
    @PrimaryKey
    private String subjectid;
    private String subjectname;
    private List<AssessmentPaperForPush> examList;


    public AssessmentSubjectsExpandable(@NonNull String subjectid, String subjectname, List<AssessmentPaperForPush> examList) {
        this.subjectid = subjectid;
        this.subjectname = subjectname;
        this.examList = examList;
    }

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


    @Override
    public List<?> getChildItemList() {
        return examList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
