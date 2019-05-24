package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentPaper;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.AssessmentTestModal;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.TestSelectListener;
import com.pratham.assessment.utilities.APIs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectExamDialog extends Dialog {

    @BindView(R.id.tv_update_tests)
    TextView updateTests;
    @BindView(R.id.btn_close)
    ImageButton btn_close;
    @BindView(R.id.txt_message)
    TextView msg;
    @BindView(R.id.select_exam_spinner)
    Spinner spinner_test;

    @BindView(R.id.txt_save)
    TextView txt_ok;
    @BindView(R.id.ll_select_paper)
    LinearLayout ll_selectPaper;
    @BindView(R.id.ll_start_assessment)
    LinearLayout ll_selectAssessment;

    @BindView(R.id.tv_no_of_que)
    TextView noOfQue;
    @BindView(R.id.tv_out_of_marks)
    TextView outOfMarks;
    @BindView(R.id.tv_time_allotted)
    TextView timeAlloted;


    String examId;

    private Context context;
    private List<AssessmentTest> assessmentTests = new ArrayList<>();
    List<AssessmentTestModal> assessmentTestModals = new ArrayList<>();

    private TestSelectListener testSelectListener;
    private AssessmentTest assessmentTest = new AssessmentTest();
    ProgressDialog progressDialog;

    public SelectExamDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        this.context = context;
//        this.assessmentTests = assessmentTests;
        testSelectListener = (TestSelectListener) context;
        SelectExamDialog.this.show();
        examId = "";


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_select_exam_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        msg.setText("Select Test");
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        assessmentTests = AppDatabase.getDatabaseInstance(context).getTestDao().getAllAssessmentTests();
        if (assessmentTests.isEmpty())
            getExamData();
        else {
            showTests();
        }

    }


    @OnClick(R.id.btn_close)
    public void closeDialog() {
        dismiss();
    }


    @OnClick(R.id.txt_save)
    public void ok() {
        final String selectedTest = spinner_test.getSelectedItem().toString();

        new AlertDialog.Builder(context)
                .setTitle("Download Questions")
                .setMessage("Download " + selectedTest + " papers?")
                .setPositiveButton(android.R.string.yes, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        for (int i = 0; i < assessmentTests.size(); i++) {
                            if (assessmentTests.get(i).getExamname().equalsIgnoreCase(selectedTest)) {
                                assessmentTest = assessmentTests.get(i);
                                break;
                            }
                        }
                        if (assessmentTest != null)
                            examId = assessmentTest.getExamid();
                        else examId="";

                        downloadPaper();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


//        testSelectListener.getSelectedTest(assessmentTest);

    }

    private void downloadPaper() {

        if (!examId.equalsIgnoreCase(""))
            getPaperData(examId);
    }

    @OnClick(R.id.tv_update_tests)
    public void setUpdateTests() {
        this.dismiss();
        getExamData();

    }


    private void getExamData() {

        progressDialog.setMessage("Loading Exams");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentExamAPI)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<AssessmentTestModal>>() {
                        }.getType();
                        assessmentTests.clear();
                        assessmentTestModals = gson.fromJson(response, listType);
                        for (int i = 0; i < assessmentTestModals.size(); i++) {
                            assessmentTests.addAll(assessmentTestModals.get(i).getLstsubjectexam());
                            for (int j = 0; j < assessmentTests.size(); j++) {
                                assessmentTests.get(j).setSubjectid(assessmentTestModals.get(i).getSubjectid());
                                assessmentTests.get(j).setSubjectname(assessmentTestModals.get(i).getSubjectname());
                            }
                        }
                        if (!assessmentTests.isEmpty()) {
                            AppDatabase.getDatabaseInstance(context).getTestDao().insertAllTest(assessmentTests);
                        }
                        progressDialog.dismiss();

                        showTests();

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showTests() {

        String[] testNames = new String[assessmentTests.size()];
        for (int i = 0; i < assessmentTests.size(); i++) {
            testNames[i] = assessmentTests.get(i).getExamname();
        }
        ArrayAdapter langAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, testNames);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_test.setAdapter(langAdapter);
        spinner_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTest = view.toString();


                //  getPaperData(examId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getPaperData(String examId) {

        progressDialog.setMessage("Loading Paper");
        progressDialog.setCancelable(false);
        AndroidNetworking.get(APIs.AssessmentPaperAPI + examId)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<AssessmentPaper>>() {
                        }.getType();
//                        assessmentTests.clear();
                        List<AssessmentPaper> assessmentPapers = gson.fromJson(response, listType);
                        List<ScienceQuestion> scienceQuestionList = new ArrayList<>();
                        List<ScienceQuestionChoice> scienceQuestionChoiceList = new ArrayList<>();
                        for (int i = 0; i < assessmentPapers.size(); i++) {
                            scienceQuestionList.addAll(assessmentPapers.get(i).getLstpaperquestion());
                            if (!scienceQuestionList.isEmpty()) {
                                for (int j = 0; j < scienceQuestionList.size(); j++)
                                    scienceQuestionChoiceList.addAll(scienceQuestionList.get(j).getLstquestionchoice());
                            }
                        }
                        addPapersToDB(assessmentPapers, scienceQuestionList, scienceQuestionChoiceList);
                       /* if (!assessmentTests.isEmpty()) {
                            AppDatabase.getDatabaseInstance(context).getTestDao().insertAllTest(assessmentTests);
                        }
                        SelectExamDialog.this.show();
                        showTests();
*/
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void addPapersToDB(List<AssessmentPaper> assessmentPapers, List<ScienceQuestion> scienceQuestionList, List<ScienceQuestionChoice> scienceQuestionChoiceList) {
        AppDatabase.getDatabaseInstance(context).getAssessmentPaperDao().insertAllPapers(assessmentPapers);
        AppDatabase.getDatabaseInstance(context).getScienceQuestionDao().insertAllQuestions(scienceQuestionList);
        AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().insertAllQuestionChoices(scienceQuestionChoiceList);
        showRandomPaper();


    }

    private void showRandomPaper() {
        List<AssessmentPaper> assessmentPapers = AppDatabase.getDatabaseInstance(context).getAssessmentPaperDao().getAssessmentPapersByExamId(examId);
        Random rand = new Random();
        AssessmentPaper assessmentPaper = assessmentPapers.get(rand.nextInt(assessmentPapers.size()));
//        noOfQue.setText("Total No of question : "+assessmentPaper.);
        outOfMarks.setText("Out of Marks : "+assessmentPaper.getOutofmarks());
        timeAlloted.setText("Time Alloted : "+assessmentPaper.getExamtime()+" min");
        Log.d("aaa", "" + assessmentPapers.size());

        ll_selectPaper.setVisibility(View.GONE);
        ll_selectAssessment.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.txt_back)
    public void showTestSpinner() {
        msg.setText("Test");
        ll_selectPaper.setVisibility(View.VISIBLE);
        ll_selectAssessment.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_start_Assessment)
    public void startAssessment() {
        this.dismiss();
        Intent intent = new Intent(context, ScienceAssessmentActivity.class);
//        intent.putExtra("nodeId", nodeId);
        context.startActivity(intent);
    }

}
