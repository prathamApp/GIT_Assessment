package com.pratham.assessment.ui.choose_assessment;


import com.pratham.assessment.domain.ContentTable;

public interface ChooseAssessmentContract {

    public interface ChooseAssessmentView{
        void clearContentList();

        void addContentToViewList(ContentTable contentTable);

        void notifyAdapter();
    }

    public interface ChooseAssessmentPresenter{
        public void startActivity(String activityName);

        void copyListData();

        void clearNodeIds();

        void endSession();

        void startAssessSession();
    }

}
