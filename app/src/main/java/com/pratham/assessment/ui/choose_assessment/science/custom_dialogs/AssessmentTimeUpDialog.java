package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pratham.assessment.R;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTrackerListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.ViewById;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/


public class AssessmentTimeUpDialog extends Dialog {

    //    @ViewById(R.id.btn_ok_time_up)
    Button btn_ok;
    Context context;
    QuestionTrackerListener questionTrackerListener;

    public AssessmentTimeUpDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        this.context = context;
        questionTrackerListener = (QuestionTrackerListener) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_time_up_dialog);
//        ButterKnife.bind(this);
        btn_ok = findViewById(R.id.btn_ok_time_up);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                questionTrackerListener.onSaveAssessmentClick();
            }
        });
    }

    /*@Click(R.id.btn_ok_time_up)
    public void closeDialog() {
//        ((Activity) context).finish();
        dismiss();
        questionTrackerListener.onSaveAssessmentClick();
    }*/
}

