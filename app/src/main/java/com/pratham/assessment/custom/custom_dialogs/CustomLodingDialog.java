package com.pratham.assessment.custom.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

public class CustomLodingDialog extends Dialog {


    public CustomLodingDialog(@NonNull Context context) {
        super(context);
    }

    public CustomLodingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomLodingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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



}
