package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.pratham.assessment.R;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/


public class CountDownDialog extends Dialog {

    //    @BindView(R.id.tv_timer)
    TextView timer;

    Context context;


    public CountDownDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_countdown_dialog);
//        ButterKnife.bind(this)
        timer = findViewById(R.id.tv_timer);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        CountDownTimer mCountDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int time = (int) (millisUntilFinished / 1000);
                timer.setText("" + time);
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };
        mCountDownTimer.start();

    }
}

