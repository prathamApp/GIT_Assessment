package com.pratham.assessment.ui.choose_assessment.result;

import android.content.Context;

import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentPaperForPush;

import org.androidannotations.annotations.EBean;

@EBean
public class ResultPresenter implements ResultContract.ResultPresenter {

    AssessmentPaperForPush paper;
    Context context;

    public ResultPresenter(Context context) {
        this.context = context;
        this.paper = paper;
    }

  /*  @Override
    public List<ResultModalClass> getData() {
        List<ResultModalClass> resultList = new ArrayList<>();
        List<Score> scores = paper.getScoreList();
        for (int i = 0; i < scores.size(); i++) {
            ResultModalClass result = new ResultModalClass();
            result.setQuestion(getQuestion("" + scores.get(i).getQuestionId()));
            result.setUserAnswer(scores.get(i).getUserAnswer());
            result.setCorrectAnswer(getAnswer(""+scores.get(i).getQuestionId()));
            result.setCorrect(scores.get(i).getIsCorrect());
            resultList.add(result);
        }
        return resultList;
    }*/

    @Override
    public String getStudent(String studentId) {
        String studentName=AppDatabase.getDatabaseInstance(context).getStudentDao().getFullName(studentId);
        return studentName;

    }

    @Override
    public String getSubjectName(String examId) {
        String subName=AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().getSubjectNameById(examId);
        return subName;
    }

    @Override
    public String getTopicName(String examId) {
        String examName=AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().getExamNameById(examId);
        return examName;
    }

    private String getAnswer(String questionId) {
        String answer = AppDatabase.getDatabaseInstance(context).getScienceQuestionDao().getAnswerNameByQID(questionId);
        return answer;
    }

    private String getQuestion(String questionId) {
        String question = AppDatabase.getDatabaseInstance(context).getScienceQuestionDao().getQuestionNameByQID(questionId);
        return question;
    }
}
