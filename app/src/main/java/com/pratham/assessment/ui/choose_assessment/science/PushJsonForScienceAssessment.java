package com.pratham.assessment.ui.choose_assessment.science;

import android.content.Context;
import android.os.AsyncTask;

import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceAssessmentAnswer;

import org.json.JSONObject;

import java.util.List;

public class PushJsonForScienceAssessment extends AsyncTask {
    Context context;


    public PushJsonForScienceAssessment(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        List<ScienceAssessmentAnswer> answerList = AppDatabase.getDatabaseInstance(context).getScienceAssessmentAnswerDao().getAllAssessmentAnswersForPush();
        if (!answerList.isEmpty()) {
            JSONObject answerObject=new JSONObject();
            String pushStr="{";




        }
        return null;
    }
}
