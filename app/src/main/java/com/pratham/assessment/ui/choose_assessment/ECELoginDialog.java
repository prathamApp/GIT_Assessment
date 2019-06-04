package com.pratham.assessment.ui.choose_assessment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import com.pratham.assessment.R;

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

    public ECELoginDialog(Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.assessment_login_dialog);
        ButterKnife.bind(this);
       /* userNameET.setText("pravinthorat");
        passwordET.setText("pratham123");*/
        userNameET.setText("");
        passwordET.setText("");
    }

/*@OnClick(R.id.btn_clearData)
    public void onClear(){
    userNameET.setText("");
    passwordET.setText("");
    userNameET.requestFocus();
}*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}
