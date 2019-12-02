package com.pratham.assessment.ui.choose_assessment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.pratham.assessment.R;
import com.pratham.assessment.utilities.Assessment_Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ECELoginDialog extends Dialog {

    @BindView(R.id.userName)
    android.support.design.widget.TextInputEditText userNameET;

    @BindView(R.id.password)
    android.support.design.widget.TextInputEditText passwordET;
    @BindView(R.id.btn_supervised)
    Button btn_supervised;
    @BindView(R.id.btn_unsupervised)
    Button btn_unsupervised;
    Context context;

    public ECELoginDialog(Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.assessment_login_dialog);
        this.context = context;
        ButterKnife.bind(this);
        /*userNameET.setText("pravinthorat");
        passwordET.setText("pratham123");*/
        userNameET.setText("");
        passwordET.setText("");
    }

    /*   @OnClick(R.id.btn_supervised)
       public void onSupervisedClicked() {
           Assessment_Constants.ASSESSMENT_TYPE = "supervised";
       }
   */
    @OnClick(R.id.btn_unsupervised)
    public void onUnSupervisedClicked() {
        if (Assessment_Constants.SELECTED_SUBJECT.equalsIgnoreCase("")) {
            Toast.makeText(context, "Select subject", Toast.LENGTH_SHORT).show();
        } else
            Assessment_Constants.ASSESSMENT_TYPE = "practice";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
    }


}
