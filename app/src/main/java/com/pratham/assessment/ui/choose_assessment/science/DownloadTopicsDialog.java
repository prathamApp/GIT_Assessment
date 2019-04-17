package com.pratham.assessment.ui.choose_assessment.science;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentToipcsModal;
import com.pratham.assessment.utilities.APIs;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DownloadTopicsDialog extends Dialog {

    @BindView(R.id.txt_clear_changes)
    TextView clear_changes;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.txt_message)
    TextView tv_topics;
    @BindView(R.id.flowLayout)
    GridLayout flowLayout;
    @BindView(R.id.ll_lang_sub)
    LinearLayout ll_filters;

    @BindView(R.id.spinner_lang)
    Spinner spinnerLang;

    @BindView(R.id.spinner_subject)
    Spinner spinnerSubject;

    String selectedLang, selectedSub;


    Context context;
    List<AssessmentToipcsModal> toipcsModalList;
    List<AssessmentLanguages> assessmentLanguagesList;
    List<AssessmentSubjects> assessmentSubjectsList;
    List<CheckBox> checkBoxes = new ArrayList<>();
    TopicSelectListener topicSelectListener;
    List<AssessmentToipcsModal> topics;

    public DownloadTopicsDialog(@NonNull Context context, TopicSelectListener topicSelectListener, List<AssessmentToipcsModal> tv_topics, List<AssessmentLanguages> languages, List<AssessmentSubjects> subjects) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        this.toipcsModalList = tv_topics;
        this.assessmentLanguagesList = languages;
        this.assessmentSubjectsList = subjects;
        this.context = context;
        this.topicSelectListener = topicSelectListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_village_dialog);
        ButterKnife.bind(this);
        ll_filters.setVisibility(View.VISIBLE);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        tv_topics.setText("Select Topics");

        String[] lang = new String[assessmentLanguagesList.size() + 1];
        lang[0] = "Select Language";
        for (int i = 0; i < assessmentLanguagesList.size(); i++) {
            lang[i + 1] = assessmentLanguagesList.get(i).getLanguagename();
        }
        spinnerLang.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, lang));

        String[] sub = new String[assessmentSubjectsList.size() + 1];
        sub[0] = "Select Subject";

        for (int j = 0; j < assessmentSubjectsList.size(); j++) {
            sub[j + 1] = assessmentSubjectsList.get(j).getSubjectname();
        }

        spinnerSubject.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sub));

        spinnerLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerLang.getSelectedItem().toString().equalsIgnoreCase("Select Language"))
                    Toast.makeText(context, "Please select language", Toast.LENGTH_SHORT).show();
                else selectedLang = spinnerLang.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerSubject.getSelectedItem().toString().equalsIgnoreCase("Select Subject"))
                    Toast.makeText(context, "Please select subject", Toast.LENGTH_SHORT).show();
                else {
                    selectedSub = spinnerSubject.getSelectedItem().toString();
                    String subId = AppDatabase.getDatabaseInstance(context).getSubjectDao().getIdByName(selectedSub);
                    getTopicData(subId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       /* for (int i = 0; i < toipcsModalList.size(); i++) {
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
        ((Activity) context).finish();

    }

    @OnClick(R.id.txt_clear_changes)
    public void clearChanges() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setChecked(false);
        }
    }

    @OnClick(R.id.txt_ok)
    public void ok() {
        if (spinnerLang.getSelectedItem().toString().equalsIgnoreCase("Select Language")
                || spinnerSubject.getSelectedItem().toString().equalsIgnoreCase("Select Subject")) {
            Toast.makeText(context, "Please select language and subject", Toast.LENGTH_SHORT).show();
        } else {

            new AlertDialog.Builder(context)
                    .setTitle("Download Questions")
                    .setMessage("Download selected topic questions?")
                    .setPositiveButton(android.R.string.yes, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ArrayList<String> topicIDList = new ArrayList();
                            for (int i = 0; i < checkBoxes.size(); i++) {
                                if (checkBoxes.get(i).isChecked()) {
                                    topicIDList.add(checkBoxes.get(i).getTag().toString());
                                }
                            }
                            if (!topicIDList.isEmpty()) {
                                selectedLang = spinnerLang.getSelectedItem().toString();
//                            spinnerSubject.setSelection();
                                selectedSub = spinnerSubject.getSelectedItem().toString();
                                dismiss();
                                topicSelectListener.getSelectedItems(topicIDList, selectedLang, selectedSub, topics);
                            } else
                                Toast.makeText(context, "Please select topic", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    private void getTopicData(String selectedSub) {

//        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
        topics = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading language");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentSubjectWiseTopicAPI + selectedSub)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response != null) {
                                for (int i = 0; i < response.length(); i++) {
                                    AssessmentToipcsModal assessmentToipcsModal = new AssessmentToipcsModal();
                                    assessmentToipcsModal.setTopicid(response.getJSONObject(i).getString("topicid"));
                                    assessmentToipcsModal.setTopicname(response.getJSONObject(i).getString("topicname"));
                                    assessmentToipcsModal.setSubjectid(response.getJSONObject(i).getString("subjectid"));
                                    topics.add(assessmentToipcsModal);

                                }
                                progressDialog.dismiss();
                                setTopicsToCheckBox(topics);
                            } else {
                                Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }

    private void setTopicsToCheckBox(List<AssessmentToipcsModal> topics) {
        for (int i = 0; i < topics.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(topics.get(i).getTopicname());
            checkBox.setTag(topics.get(i).getTopicid());
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(param);
            flowLayout.addView(checkBox);
            checkBoxes.add(checkBox);
        }

    }
}

