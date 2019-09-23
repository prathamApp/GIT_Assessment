package com.pratham.assessment.ui.choose_assessment.science.custom_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.custom.gif_viewer.GifViewZoom;
import com.pratham.assessment.custom.zoom_image.ZoomageView;

import java.io.FileInputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ZoomImageDialog extends Dialog {

    @BindView(R.id.btn_ok_img)
    ImageButton btn_ok;
    @BindView(R.id.iv_zoom_img)
    ZoomageView zoomImg;
    @BindView(R.id.iv_img)
    GifViewZoom gifView;
    @BindView(R.id.vv_video)
    VideoView videoView;
    private Context context;
    private String path;
    private String localPath;
    private String qtid = "";

    public ZoomImageDialog(@NonNull Context context, String path, String localPath) {
//        super(context,android.R.style.Theme_NoTitleBar_Fullscreen);
        super(context, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
//        super(context,android.R.style.Theme_Holo_NoActionBar_Fullscreen);
        this.context = context;
        this.path = path;
        this.localPath = localPath;
    }

    public ZoomImageDialog(Context context, String path, String qtid, String localPath) {
        super(context);
        this.context = context;
        this.path = path;
        this.qtid = qtid;
        this.localPath = localPath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_image_dialog);
        ButterKnife.bind(this);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (!qtid.equalsIgnoreCase("")) {
            //Creating MediaController
            MediaController mediaController = new MediaController(context);
//            mediaController.setAnchorView(videoView);

            //specify the location of media file

            //Setting MediaController and URI, then starting the videoView
           /* videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();*/
            zoomImg.setVisibility(View.GONE);
            gifView.setVisibility(View.GONE);

            videoView.setVideoPath(path);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.setZOrderOnTop(true);
            videoView.setZOrderMediaOverlay(true);
            videoView.start();

        } else {
            if (path != null) {
                String[] imgPath = path.split("\\.");
                int len;
                if (imgPath.length > 0)
                    len = imgPath.length - 1;
                else len = 0;
                if (imgPath[len].equalsIgnoreCase("gif")) {
                    if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        Glide.with(context).asGif()
                                .load(path)
                                .apply(new RequestOptions()
                                        .placeholder(Drawable.createFromPath(localPath)))
                                .into(zoomImg);
                        zoomImg.setVisibility(View.VISIBLE);
                        gifView.setVisibility(View.GONE);
                    } else {
                        try {
                            InputStream gif = new FileInputStream(localPath);
                            zoomImg.setVisibility(View.GONE);
                            gifView.setVisibility(View.VISIBLE);
                            gifView.setGifResource(gif);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Glide.with(context)
                            .load(path)
                            .apply(new RequestOptions()
                                    .placeholder(Drawable.createFromPath(localPath)))
                            .into(zoomImg);
                    zoomImg.setVisibility(View.VISIBLE);
                    gifView.setVisibility(View.GONE);

                }
            }
        }
    }

    @OnClick(R.id.btn_ok_img)
    public void closeDialog() {
        dismiss();
    }
}

