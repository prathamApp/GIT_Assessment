package com.pratham.assessment.ui.choose_assessment.science;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.SelectExamDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.TestSelectListener;

import java.util.ArrayList;
import java.util.List;

public class CRLActivity extends AppCompatActivity implements TestSelectListener {
    ProgressDialog progressDialog;
    List<AssessmentTest> assessmentTestList = new ArrayList<>();
    List<AssessmentTest> assessmentTests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crl);
        SelectExamDialog selectExamDialog = new SelectExamDialog(this);
    }





    @Override
    public void getSelectedTest(AssessmentTest assessmentTest) {

    }
}
