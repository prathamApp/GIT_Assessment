package com.pratham.assessment.ui.choose_assessment.science.certificate;

import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.Score;

import java.util.List;

public interface AssessmentCertificateContract {

    public interface CertificatePresenter {

        void getSubjectData();
        void getStudent(String studentId);

        void getTopicData(String selectedSub);

        void generateCertificate(String selectedSub);

        void getScoreData(AssessmentPaperForPush paper);
    }

    public interface CertificateView {
        void setStudentName(String studentName);

        void setSubjectSpinner(String[] sub);

        void setTopicSpinner(String[] topics);

        void showCertificate(AssessmentPaperForPush paperList, List<Score> scoreData);

        void showNothing();

        void showPaperList(List<AssessmentPaperForPush> paperList);
    }
}
