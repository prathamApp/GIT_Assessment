package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.content.Context;

import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.AssessmentSubjectsExpandable;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SubjectPresenter implements SubjectContract.SubjectPresenter {
    Context context;
    SubjectContract.SubjectView subjectView;

    public SubjectPresenter(Context context, SubjectContract.SubjectView view) {
        this.context = context;
        subjectView = view;
    }

    @Override
    public void getSubjectsFromDB(String selectedLang) {
        String langId = AppDatabase.getDatabaseInstance(context).getLanguageDao().getLangIdByName(selectedLang.toUpperCase());
        List<AssessmentSubjects> subjects = new ArrayList<>();
        List<AssessmentSubjects> AllSubjects;
        List<AssessmentSubjectsExpandable> assessmentSubjectsExpandables = new ArrayList<>();

//        subjects = AppDatabase.getDatabaseInstance(context).getSubjectDao().getAllSubjects();
        AllSubjects = AppDatabase.getDatabaseInstance(context).getSubjectDao().getAllSubjectsByLangId(langId);
        List<String> attemptedSubjectIds = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPapersByUniqueSubId(langId);
        for (int j = 0; j < AllSubjects.size(); j++) {
            for (int i = 0; i < attemptedSubjectIds.size(); i++) {
                if (attemptedSubjectIds.get(i).equalsIgnoreCase(AllSubjects.get(j).getSubjectid()))
                    subjects.add(AllSubjects.get(j));
            }
        }

        for (AssessmentSubjects assessmentSubjects : subjects) {
            List<AssessmentPaperForPush> assessmentPaperForPush = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPaperBySubIdAndLangId(assessmentSubjects.getSubjectid(), Assessment_Constants.currentStudentID, langId);
            Collections.sort(assessmentPaperForPush, new Comparator<AssessmentPaperForPush>() {
                @Override
                public int compare(AssessmentPaperForPush o1, AssessmentPaperForPush o2) {
                    Date date1 = Assessment_Utility.stringToDate(o1.getPaperEndTime());
                    Date date2 = Assessment_Utility.stringToDate(o2.getPaperEndTime());
                    if (date1 != null && date2 != null)
                        return date1.compareTo(date2);
                    else return 0;
                }
            });
            Collections.reverse(assessmentPaperForPush);
            assessmentSubjectsExpandables.add(new AssessmentSubjectsExpandable(assessmentSubjects.getSubjectid(), assessmentSubjects.getSubjectname(), assessmentPaperForPush));
        }


        subjectView.setSubjects(assessmentSubjectsExpandables);
    }
}
