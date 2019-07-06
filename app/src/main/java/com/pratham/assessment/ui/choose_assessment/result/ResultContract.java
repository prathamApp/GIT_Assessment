package com.pratham.assessment.ui.choose_assessment.result;

import com.pratham.assessment.domain.ResultModalClass;

import java.util.List;

public interface ResultContract {
    interface ResultPresenter {


        String getStudent(String studentId);

        String getSubjectName(String examId);

        String getTopicName(String examId);
    }

    interface ResultView {


    }

}
