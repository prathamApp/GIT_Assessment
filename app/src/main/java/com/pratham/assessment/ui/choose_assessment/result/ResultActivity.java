package com.pratham.assessment.ui.choose_assessment.result;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ResultModalClass;
import com.pratham.assessment.domain.ResultOuterModalClass;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.CertificateFragment;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends BaseActivity implements ResultContract.ResultView {
    List<ResultModalClass> resultList;
    ResultOuterModalClass outerModalClass;
 /*   String outOfMarks, marksObtained, studentId, examStartTime, examEndTime, examId, subjectId, paperId;
    @BindView(R.id.rv_question_answers)
    RecyclerView rv_question_answers;
    @BindView(R.id.tv_out_of_marks)
    TextView tv_out_of_marks;
    @BindView(R.id.tv_marks_obtained)
    TextView tv_marks_obtained;
    @BindView(R.id.tv_topic)
    TextView tv_topic;
    @BindView(R.id.tv_subject)
    TextView tv_subject;

    ResultContract.ResultPresenter presenter;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
//        final Toolbar mToolbar = findViewById(R.id.toolbar);
//        resultList = (List<ResultModalClass>) getIntent().getSerializableExtra("result");
        outerModalClass = (ResultOuterModalClass) getIntent().getSerializableExtra("result");
    /*    resultList = outerModalClass.getResultList();
        outOfMarks = outerModalClass.getOutOfMarks();
        marksObtained = outerModalClass.getMarksObtained();
        studentId = outerModalClass.getStudentId();
        examStartTime = outerModalClass.getExamStartTime();
        examEndTime = outerModalClass.getExamEndTime();
        examId = outerModalClass.getExamId();
        subjectId = outerModalClass.getSubjectId();
        paperId = outerModalClass.getPaperId();
        tv_marks_obtained.setText(marksObtained);
        tv_out_of_marks.setText(outOfMarks);*/

        /*outOfMarks = getIntent().getStringExtra("outOfMarks");
        marksObtained = getIntent().getStringExtra("marksObtained");
        studentId = getIntent().getStringExtra("studentId");
        examStartTime = getIntent().getStringExtra("examStartTime");
        examEndTime = getIntent().getStringExtra("examEndTime");
        examId = getIntent().getStringExtra("examId");
        subjectId = getIntent().getStringExtra("subjectId");
        paperId = getIntent().getStringExtra("paperId");
        tv_marks_obtained.setText(marksObtained);
        tv_out_of_marks.setText(outOfMarks);*/


        Bundle bundle = new Bundle();
        bundle.putSerializable("result", outerModalClass);
        Assessment_Utility.showFragment(this, new ResultFragment(), R.id.certificate_frame, bundle, ResultFragment.class.getSimpleName());


/*        presenter = new ResultPresenter(this);
        String studentName = presenter.getStudent(studentId);
        mToolbar.setTitle(studentName);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        String subName = presenter.getSubjectName(examId);
        String topicName = presenter.getTopicName(examId);
        tv_topic.setText(topicName);
        tv_subject.setText(subName);
        ResultAdapter resultAdapter = new ResultAdapter(this, resultList);
        rv_question_answers.setAdapter(resultAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_question_answers.setLayoutManager(linearLayoutManager);
        resultAdapter.notifyDataSetChanged();

        AppBarLayout mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            //            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
            }
        });*/
    }

  /*  @OnClick(R.id.btn_done)
    public void onDoneClick() {
//        certificate_frame.setVisibility(View.VISIBLE);
      *//*  AssessmentPaperForPush assessmentPaperForPush = AppDatabase.getDatabaseInstance(this).getAssessmentPaperForPushDao().getAssessmentPapersByPaperId(paperId);
        Bundle bundle = new Bundle();
        bundle.putSerializable("assessmentPaperForPush", assessmentPaperForPush);
        Assessment_Utility.showFragment(this, new CertificateFragment(), R.id.certificate_frame, bundle, CertificateFragment.class.getSimpleName());
*//*
        finish();
    }
*/

    @Override
    public void onBackPressed() {
       int fragmentCnt= getSupportFragmentManager().getBackStackEntryCount();
       if(fragmentCnt>1)
            finish();
    }
}
