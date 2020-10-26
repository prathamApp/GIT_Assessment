package com.pratham.assessment.ui.choose_assessment;


import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.NIOSExam;

import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.AssessmentApplication.sharedPreferences;
import static com.pratham.assessment.utilities.Assessment_Constants.CURRENT_VERSION;

public interface ChooseAssessmentContract {

    public interface ChooseAssessmentView {
        void clearContentList();

        void addContentToViewList(List<AssessmentSubjects> contentTable);

        void showNoExamLayout(boolean show);

        void notifyAdapter();

    }

    public interface ChooseAssessmentPresenter {

        public void versionObtained(String latestVersion);

        public void startActivity(String activityName);

        void copyListData();

        void clearNodeIds();

        void endSession();

//        void startAssessSession();
    }

}
