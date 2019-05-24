package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pratham.assessment.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ZoomImageDialog extends Dialog {

    @BindView(R.id.btn_ok_img)
    Button btn_ok;
    @BindView(R.id.iv_zoom_img)
    ImageView zoomImg;
    Context context;
    String path;

    public ZoomImageDialog(@NonNull Context context, String path) {
        super(context);
        this.context = context;
        this.path = path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_image_dialog);
        ButterKnife.bind(this);

        Glide.with(context)
                .load(path)
                .into(zoomImg);
        zoomImg.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.btn_ok_img)
    public void closeDialog() {
        dismiss();
    }
}

