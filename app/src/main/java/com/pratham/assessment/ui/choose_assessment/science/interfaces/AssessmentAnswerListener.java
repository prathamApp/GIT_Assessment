package com.pratham.assessment.ui.choose_assessment.science.interfaces;

import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

public interface AssessmentAnswerListener {

    void setAnswerInActivity(String ansId, String answer, String qid, List<ScienceQuestionChoice> list);

    void removeSupervisorFragment();

//    void setParagraph(String para, boolean isParaQuestion);
}
