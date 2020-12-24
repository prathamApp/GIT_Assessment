package com.pratham.assessment.custom.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pratham.assessment.R;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

public class PushDataDialog extends Dialog {

    //    @BindView(R.id.ok_btn)
    Button ok_btn;
    //    @BindView(R.id.txt_push_dialog_msg)
    TextView txt_push_dialog_msg;
    //    @BindView(R.id.txt_push_cnt)
    TextView txt_push_cnt;

    public PushDataDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.app_push_data_dialog);
//        ButterKnife.bind(this);
        ok_btn = findViewById(R.id.ok_btn);
        txt_push_dialog_msg = findViewById(R.id.txt_push_dialog_msg);
        txt_push_cnt = findViewById(R.id.txt_push_cnt);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public PushDataDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PushDataDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        // Set the dialog to not focusable.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Show the dialog with NavBar hidden.
        super.show();

        // Set the dialog to focusable again.
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }


   /* public static void setInternetStatus(String msg) {
        txt_push_dialog_msg.setText(msg);
    }

    public static void setDataStatus(String msg1, String msg2) {
        txt_push_dialog_msg.setText(msg1);
        txt_push_cnt.setText(msg2);
        ok_btn.setVisibility(View.VISIBLE);
    }*/

  /*  @OnClick(R.id.ok_btn)
    public void okClick() {
        dismiss();
    }
*/
}
