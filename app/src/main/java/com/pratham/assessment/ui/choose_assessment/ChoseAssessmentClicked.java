package com.pratham.assessment.ui.choose_assessment;

import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentTest;

public interface ChoseAssessmentClicked {
    public void subjectClicked(int position, AssessmentSubjects nodeId);
    void languageClicked(int pos, AssessmentLanguages languages);
    void topicClicked(int pos, AssessmentTest test);
}
