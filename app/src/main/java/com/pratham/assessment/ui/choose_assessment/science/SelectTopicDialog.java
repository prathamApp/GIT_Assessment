package com.pratham.assessment.ui.choose_assessment.science;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentToipcsModal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectTopicDialog extends Dialog {

    @BindView(R.id.tv_update_topics)
    TextView updateTopics;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.txt_message)
    TextView topics;
    @BindView(R.id.spinner_topic)
    Spinner spinner_topic;
    @BindView(R.id.select_spinner_Subject)
    Spinner spinner_subject;
    @BindView(R.id.select_spinner_lang)
    Spinner spinner_lang;
    @BindView(R.id.txt_ok)
    TextView txt_ok;

    Context context;
    List<AssessmentToipcsModal> toipcsModalList;
    List<AssessmentLanguages> assessmentLanguagesList;
    List<AssessmentSubjects> assessmentSubjectsList;
    List<CheckBox> checkBoxes = new ArrayList<>();
    TopicSelectListener topicSelectListener;

    public SelectTopicDialog(@NonNull Context context, TopicSelectListener topicSelectListener, List topicList, List<AssessmentLanguages> languages, List<AssessmentSubjects> subjects) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        this.toipcsModalList = topicList;
        this.context = context;
        this.topicSelectListener = topicSelectListener;
        this.assessmentLanguagesList = languages;
        this.assessmentSubjectsList = subjects;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_topic_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        topics.setText("Select Topics");
        String[] topicNames = new String[toipcsModalList.size()];
        for (int i = 0; i < toipcsModalList.size(); i++) {
            topicNames[i] = toipcsModalList.get(i).getTopicname();
        }
        ArrayAdapter topicAdapter=new ArrayAdapter(context, android.R.layout.simple_spinner_item, topicNames);
        topicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_topic.setAdapter(topicAdapter);
        String[] lang = new String[assessmentLanguagesList.size()];
        for (int i = 0; i < assessmentLanguagesList.size(); i++) {
            lang[i] = assessmentLanguagesList.get(i).getLanguagename();
        }
        ArrayAdapter langAdapter=new ArrayAdapter(context, android.R.layout.simple_spinner_item, lang);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_lang.setAdapter(langAdapter);
        String[] sub = new String[assessmentSubjectsList.size()];
        for (int j = 0; j < assessmentSubjectsList.size(); j++) {
            sub[j] = assessmentSubjectsList.get(j).getSubjectname();
            if (assessmentSubjectsList.get(j).getSubjectname().equalsIgnoreCase("science")) {
                spinner_subject.setSelection(j);
            }
        }
        ArrayAdapter subAdapter=new ArrayAdapter(context, android.R.layout.simple_spinner_item, sub);
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_subject.setAdapter(subAdapter);


      /*  for (int i = 0; i < toipcsModalList.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(toipcsModalList.get(i).getTopicname());
            checkBox.setTag(toipcsModalList.get(i).getTopicid());
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(param);
            flowLayout.addView(checkBox);
            checkBoxes.add(checkBox);
        }*/
    }


    @OnClick(R.id.btn_close)
    public void closeDialog() {
        dismiss();
        ((Activity)context).finish();
    }


    @OnClick(R.id.txt_ok)
    public void ok() {
        String selectedTopic = spinner_topic.getSelectedItem().toString();
        String selectedSub = spinner_subject.getSelectedItem().toString();
        String selectedLang = spinner_lang.getSelectedItem().toString();
        topicSelectListener.getSelectedTopic(selectedTopic, selectedSub, selectedLang,this);
    }



}

