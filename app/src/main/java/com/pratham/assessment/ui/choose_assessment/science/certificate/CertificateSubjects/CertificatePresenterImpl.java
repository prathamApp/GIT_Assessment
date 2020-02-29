package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.content.Context;

import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;

import org.androidannotations.annotations.EBean;

@EBean
public class CertificatePresenterImpl implements CertificateContract.CertificatePresenter {
    Context context;
    CertificateContract.CertificateView certificateView;

    public CertificatePresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getStudentName() {
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        String studentName = AppDatabase.getDatabaseInstance(context).getStudentDao().getFullName(Assessment_Constants.currentStudentID);
        String studentName = AppDatabase.getDatabaseInstance(context).getStudentDao().getFullName(currentStudentID);
        certificateView.setStudentName(studentName);
    }

   /* @Override
    public void getScoreData(String paperId) {
        List<Score> scores = AppDatabase.getDatabaseInstance(context).getScoreDao().getScoreByPaperId(paperId);


        //        Log.d("@@", "getScoreData: " + scores);
        calculateLevel(scores);
    }*/

    @Override
    public float getRating(String level, String paperId) {
        int q1Total = AppDatabase.getDatabaseInstance(context).getScoreDao().getLevelCnt(level, paperId);
        int q1CorrectCnt = AppDatabase.getDatabaseInstance(context).getScoreDao().getLevelCorrectCnt(level, paperId, true);
        float q1Rating = CalculateRating(q1Total, q1CorrectCnt);
        return q1Rating;
    }

    @Override
    public void setView(CertificateContract.CertificateView certificateView) {
        this.certificateView = certificateView;

    }

    private float CalculateRating(int q1Total, int q1CorrectCnt) {
        float ratings = 0;
        try {
            float perc = (float) q1CorrectCnt / (float) q1Total;
            if (perc < 0.2)
                ratings = (float) 1;
            else if (perc >= 0.2 && perc < 0.4)
                ratings = (float) 2;
            else if (perc >= 0.4 && perc < 0.6)
                ratings = (float) 3;
            else if (perc >= 0.6 && perc < 0.8)
                ratings = (float) 4;
            else if (perc >= 0.8)
                ratings = (float) 5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratings;
    }

    /*private void calculateLevel(List<Score> scores) {
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i).getLevel() == 0)
                easyCnt++;
            else if (scores.get(i).getLevel() == 1)
                normalCnt++;
            else if (scores.get(i).getLevel() == 2)
                difficultCnt++;
        }
        Log.d("@@@", easyCnt + " " + normalCnt + " " + difficultCnt);
    }*/

   /* @Override
    public void getPaper(String examId, String subjectid) {
        List<AssessmentPaperForPush> paperList = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPapersByExamIdAndSubId(examId, subjectid, Assessment_Constants.currentStudentID);
        Log.d("AAAAA", paperList.size() + "");
        certificateView.showCertificateData(paperList);
    }*/
}
