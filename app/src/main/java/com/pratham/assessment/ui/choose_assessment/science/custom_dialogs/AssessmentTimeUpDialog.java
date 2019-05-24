package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pratham.assessment.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AssessmentTimeUpDialog extends Dialog {

    @BindView(R.id.btn_ok_time_up)
    Button btn_ok;
    Context context;

    public AssessmentTimeUpDialog(@NonNull Context context) {
        super(context,android.R.style.);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_time_up_dialog);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_ok_time_up)
    public void closeDialog() {
        ((Activity) context).finish();
        dismiss();
    }
}

