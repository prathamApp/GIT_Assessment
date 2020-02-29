package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

public interface CertificateContract {
    public interface CertificatePresenter {
        void getStudentName();

//        void getScoreData(String paperId);

        float getRating(String level, String paperId);

//        void getPaper(String examId,String subjectid);

        void setView(CertificateContract.CertificateView certificateView);
    }

    interface CertificateView {
        void setStudentName(String name);

    }
}
