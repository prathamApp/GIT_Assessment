package com.pratham.assessment.custom.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;


import com.pratham.assessment.R;



/**
 * Created by abc on 7/18/2018.
 */

public class ChooseImageDialog extends Dialog {
    //    @BindView(R.id.btn_choose_from_gallery)
    public Button btn_take_photo;

    //    @BindView(R.id.btn_choose_from_gallery)
    public Button btn_choose_from_gallery;


    public ChooseImageDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.layout_image_choose_dialog);
        btn_choose_from_gallery = findViewById(R.id.btn_choose_from_gallery);
        btn_take_photo = findViewById(R.id.btn_take_photo);

//        ButterKnife.bind(this);
    }
}
