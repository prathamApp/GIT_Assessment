package com.pratham.assessment.ui.choose_assessment.science.certificate;

import android.content.Context;
import android.util.Log;

import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.util.List;


public class AssessmentCertificatePresenterImpl implements AssessmentCertificateContract.CertificatePresenter {
    Context context;
    AssessmentCertificateContract.CertificateView view;


    public AssessmentCertificatePresenterImpl(Context context) {
        this.context = context;
        view = (AssessmentCertificateContract.CertificateView) context;
    }

    @Override
    public void getSubjectData() {
        List<AssessmentSubjects> subjects = AppDatabase.getDatabaseInstance(context).getSubjectDao().getAllSubjects();
        String[] sub = new String[subjects.size()];
        for (int i = 0; i < subjects.size(); i++)
            sub[i] = subjects.get(i).getSubjectname();
        view.setSubjectSpinner(sub);
    }


    @Override
    public void getStudent(String studentId) {
        String studentName = AppDatabase.getDatabaseInstance(context).getStudentDao().getFullName(studentId);
        view.setStudentName(studentName);


    }

    @Override
    public void getTopicData(String selectedSub) {
        String[] topics = AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().getExamNameBySubjectName(selectedSub);
        view.setTopicSpinner(topics);
    }

    @Override
    public void generateCertificate(String selectedSub) {
        String subId = AppDatabase.getDatabaseInstance(context).getSubjectDao().getIdByName(selectedSub);
        List<AssessmentPaperForPush> paperList = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPaperBySubId(subId, Assessment_Constants.currentStudentID);
        view.showPaperList(paperList);
        if (paperList.size() == 0) {
            view.showNothing();
        }
        Log.d("@@@@", paperList.size() + "");
    }

    @Override
    public void getScoreData(AssessmentPaperForPush paper) {
        String sessionId = paper.getSessionID();
        List<Score> scoreData = AppDatabase.getDatabaseInstance(context).getScoreDao().getAllNewScores(sessionId);
        view.showCertificate(paper, scoreData);
    }
}
