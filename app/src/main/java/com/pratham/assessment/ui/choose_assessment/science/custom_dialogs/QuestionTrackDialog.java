package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.adapters.QuestionTrackerAdapter;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/


public class QuestionTrackDialog extends Dialog {

    //    @ViewById(R.id.txt_cancel)
    TextView cancel;
    //    @ViewById(R.id.btn_close)
    ImageButton btn_close;
    //    @ViewById(R.id.txt_message)
    TextView tv_topics;
    //    @ViewById(R.id.rv_questions)
    RecyclerView rvQuestion;


    Context context;
    List<ScienceQuestion> scienceQuestionList;


    public QuestionTrackDialog(@NonNull Context context, List<ScienceQuestion> scienceQuestions) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        this.scienceQuestionList = scienceQuestions;
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_tracker_dialog);
//        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        cancel = findViewById(R.id.txt_cancel);
        btn_close = findViewById(R.id.btn_close);
        tv_topics = findViewById(R.id.txt_message);
        tv_topics.setText("Question Tracker");

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

       /* QuestionTrackerAdapter questionTrackerAdapter = new QuestionTrackerAdapter(this, context, scienceQuestionList);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(context, 5);
        rvQuestion.setLayoutManager(linearLayoutManager);
        rvQuestion.setAdapter(questionTrackerAdapter);*/

    }


  /*  @Click(R.id.btn_close)
    public void closeDialog() {
        dismiss();

    }
*/
/*
    @Click(R.id.txt_cancel)
    public void cancel() {
        dismiss();
    }

    @Click(R.id.txt_save)
    public void ok() {

    }
*/


}

