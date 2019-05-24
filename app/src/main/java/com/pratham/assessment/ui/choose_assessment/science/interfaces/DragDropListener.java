package com.pratham.assessment.ui.choose_assessment.science.interfaces;

import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

public interface DragDropListener {
    void setList(List<ScienceQuestionChoice> list,String qid);
}
