package com.pratham.assessment.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.ui.login.group_selection.fragment_select_group.FragmentSelectGroup;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Anki on 12/3/2018.
 */

public class SelectAgeGroupDialog extends Dialog {
    @BindView(R.id.iv_age_3_to_6)
    public ImageView iv_age_3_to_6;

    /* @BindView(R.id.close)
     public ImageView close;
 */
    @BindView(R.id.iv_age_8_to_14)
    public ImageView iv_age_8_to_14;

    @BindView(R.id.tv_age_3_to_6)
    public TextView tv_age_3_to_6;

    @BindView(R.id.tv_age_8_to_14)
    public TextView tv_age_8_to_14;

    Activity activity;

    public SelectAgeGroupDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        setContentView(R.layout.layout_select_age_group_dialog);
        ButterKnife.bind(this);
        this.activity = (Activity) context;

    }

    @OnClick({R.id.tv_age_3_to_6, R.id.iv_age_3_to_6})
    public void age3to6selected() {
        this.dismiss();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Assessment_Constants.GROUP_AGE_BELOW_7, true);
        Assessment_Utility.showFragment(activity, new FragmentSelectGroup(), R.id.frame_group,
                bundle, FragmentSelectGroup.class.getSimpleName());
    }

    @OnClick({R.id.tv_age_8_to_14, R.id.iv_age_8_to_14})
    public void age8to14selected() {
        this.dismiss();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Assessment_Constants.GROUP_AGE_ABOVE_7, true);
        Assessment_Utility.showFragment(activity, new FragmentSelectGroup(), R.id.frame_group,
                bundle, FragmentSelectGroup.class.getSimpleName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}