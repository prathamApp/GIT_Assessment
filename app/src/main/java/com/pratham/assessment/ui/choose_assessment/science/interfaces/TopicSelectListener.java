package com.pratham.assessment.ui.choose_assessment.science.interfaces;


import com.pratham.assessment.domain.AssessmentToipcsModal;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.SelectTopicDialog;

import java.util.ArrayList;
import java.util.List;

public interface TopicSelectListener {
    void getSelectedItems(List<String> topicIDList, String selectedLang, List<AssessmentToipcsModal> topics);
    void getSelectedTopic(String topic, String selectedLang, SelectTopicDialog selectTopicDialog);

}
