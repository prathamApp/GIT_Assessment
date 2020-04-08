package com.pratham.assessment.ui.choose_assessment.science.interfaces;

import android.content.Intent;

import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.List;

public interface AssessmentAnswerListener {

    void setAnswerInActivity(String ansId, String answer, String qid, List<ScienceQuestionChoice> list);

    void removeSupervisorFragment();

    //    void setVideoResult(Intent intent, int videoCapture, ScienceQuestion scienceQuestion);
//    void setImageCaptureResult(ScienceQuestion scienceQuestion);
    void setAudio(String fileName, boolean isRecording);

  /*  void pauseVideoMonitoring();

    void showCameraError();

    void resumeVideoMonitoring();*/
}
