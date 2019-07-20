package com.pratham.assessment.ui.choose_assessment.result;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ResultModalClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends BaseActivity implements ResultContract.ResultView {
    List<ResultModalClass> resultList;
    String outOfMarks, marksObtained, studentId, examStartTime, examEndTime, examId,subjectId;
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
    ResultContract.ResultPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        final Toolbar mToolbar = findViewById(R.id.toolbar);
        resultList = (List<ResultModalClass>) getIntent().getSerializableExtra("result");
        outOfMarks = getIntent().getStringExtra("outOfMarks");
        marksObtained = getIntent().getStringExtra("marksObtained");
        studentId = getIntent().getStringExtra("studentId");
        examStartTime = getIntent().getStringExtra("examStartTime");
        examEndTime = getIntent().getStringExtra("examEndTime");
        examId = getIntent().getStringExtra("examId");
        subjectId = getIntent().getStringExtra("subjectId");
        tv_marks_obtained.setText(marksObtained);
        tv_out_of_marks.setText(outOfMarks);
        presenter = new ResultPresenter(this);
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
        });
    }

    @OnClick(R.id.btn_done)
    public void onDoneClick() {
        finish();
    }

}
