package com.pratham.assessment.ui.certificate;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.domain.CertificateModelClass;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Ameya on 23-Nov-17.
 */

public interface CertificateContract {

    interface CertificateView{
        void setStudentName(String studName);

        void addContentToViewList(CertificateModelClass contentTable);

        void doubleQuestionCheck();

        void initializeTheIndex();

        void notifyAdapter();

        //void setSupervisorData(String sName, String sImage);
    }

    interface CertificatePresenter {
        void getStudentName();

        void proceed(JSONArray certiData, String nodeId);

//        void getSupervisorData(String certiMode);

        void fillAdapter(Assessment assessmentProfile, JSONArray certiData);

        JSONArray fetchAssessmentList();

        float getStarRating(float perc);

        void recordTestData(JSONObject jsonObjectAssessment, String certiTitle);
    }

}
