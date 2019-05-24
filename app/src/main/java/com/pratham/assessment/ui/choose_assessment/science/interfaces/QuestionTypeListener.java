package com.pratham.assessment.ui.choose_assessment.science.interfaces;

import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

public interface QuestionTypeListener {

    public void setType(int QType, String answer);

    void setAnswer(String ansId, String answer, String qid, List<ScienceQuestionChoice> list);
}
