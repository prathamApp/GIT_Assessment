package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

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
    @BindView(R.id.vv_video)
    VideoView videoView;
    Context context;
    String path;
    String qtid = "";

    public ZoomImageDialog(@NonNull Context context, String path) {
        super(context);
        this.context = context;
        this.path = path;
    }

    public ZoomImageDialog(Context context, String path, String qtid) {
        super(context);
        this.context = context;
        this.path = path;
        this.qtid = qtid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_image_dialog);
        ButterKnife.bind(this);
        if (!qtid.equalsIgnoreCase("")) {
            //Creating MediaController
            MediaController mediaController = new MediaController(context);
//            mediaController.setAnchorView(videoView);

            //specify the location of media file
            Uri uri = Uri.parse(path);

            //Setting MediaController and URI, then starting the videoView
           /* videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();*/
            zoomImg.setVisibility(View.GONE);

            videoView.setVideoPath(path);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.setZOrderOnTop(true);
            videoView.setZOrderMediaOverlay(true);
            videoView.start();

        } else {
            Glide.with(context)
                    .load(path)
                    .into(zoomImg);
            zoomImg.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.btn_ok_img)
    public void closeDialog() {
        dismiss();
    }
}

