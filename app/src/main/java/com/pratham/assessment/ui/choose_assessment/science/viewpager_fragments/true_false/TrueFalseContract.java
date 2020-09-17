package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.true_false;

import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

public interface TrueFalseContract {
    interface TrueFalsePresenter {
        void getOptions(String qid);

        void setView(TrueFalseView view);

    }

    interface TrueFalseView {
       void setOptions(List<ScienceQuestionChoice> options);
    }
}
