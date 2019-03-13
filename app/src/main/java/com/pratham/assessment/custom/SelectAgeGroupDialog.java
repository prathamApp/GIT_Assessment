package com.pratham.assessment.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.pratham.assessment.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anki on 12/3/2018.
 */

public class SelectAgeGroupDialog extends Dialog {
    @BindView(R.id.iv_age_3_to_6)
    public ImageView iv_age_3_to_6;

    @BindView(R.id.close)
    public ImageView close;

    @BindView(R.id.iv_age_8_to_14)
    public ImageView iv_age_8_to_14;


    public SelectAgeGroupDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.layout_select_age_group_dialog);
        ButterKnife.bind(this);

    }
}
