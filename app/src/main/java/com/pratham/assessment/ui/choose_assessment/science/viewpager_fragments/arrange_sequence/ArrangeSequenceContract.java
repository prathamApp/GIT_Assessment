package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.arrange_sequence;

import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

public interface ArrangeSequenceContract {

    interface ArrangeSeqPresenter {
        void setView(ArrangeSeqView view);

        void getShuffledList(ScienceQuestion scienceQuestion);
    }

    interface ArrangeSeqView {
        void setShuffledList(List<ScienceQuestionChoice> shuffledList);

    }



}
