package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.content.Context;

import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.AssessmentSubjectsExpandable;

import java.util.List;

public interface SubjectContract {

    public interface SubjectPresenter {

        public void getSubjectsFromDB(String selectedLang);

        void pullCertificates();

        void setView(SubjectContract.SubjectView subjectView);
    }

    public interface SubjectView {
       void setSubjects(List<AssessmentSubjectsExpandable> subjects);
       void setSubjectToSpinner();
    }

}
