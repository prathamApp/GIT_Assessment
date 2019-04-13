package com.pratham.assessment.ui.choose_assessment.science;


import java.util.ArrayList;

public interface TopicSelectListener {
    void getSelectedItems(ArrayList<String> topicIDList, String selectedLang, String selectedSub);
    void getSelectedTopic(String topic, String selectedSub, String selectedLang);

    void getTopicDataBySubject(String selectedSub);
}
