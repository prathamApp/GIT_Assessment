package com.pratham.assessment.ui.choose_assessment.science;


import com.pratham.assessment.domain.AssessmentToipcsModal;

import java.util.ArrayList;
import java.util.List;

public interface TopicSelectListener {
    void getSelectedItems(ArrayList<String> topicIDList, String selectedLang, String selectedSub, List<AssessmentToipcsModal> topics);
    void getSelectedTopic(String topic, String selectedSub, String selectedLang, SelectTopicDialog selectTopicDialog);

}
