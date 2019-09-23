package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ZoomImageDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.utilities.Assessment_Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;

    @BindView(R.id.iv_answer_image_play_icon)
    ImageView iv_answer_image_play_icon;
    @BindView(R.id.btn_capture_video)
    Button btn_capture_video;
    @BindView(R.id.rl_answer_video)
    public RelativeLayout rl_answer_video;
    /*@BindView(R.id.vv_play_video)
    VideoView videoView;*/
    ScienceQuestion scienceQuestion;
    AssessmentAnswerListener assessmentAnswerListener;
    Context context;
    private static final int VIDEO_CAPTURE = 101;
    String fileName;
    String path;

    public VideoViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        assessmentAnswerListener = (ScienceAssessmentActivity) context;
    }

    public void setVideoQuestion(ScienceQuestion scienceQuestion1, int pos) {
        this.scienceQuestion = scienceQuestion1;
        fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
        path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded" + "/" + fileName;

        rl_answer_video.setVisibility(View.GONE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
        questionImage.setImageBitmap(thumb);
     /*   if (VideoCaptured) {
            rl_answer_video.setVisibility(View.VISIBLE);
        } else rl_answer_video.setVisibility(View.GONE);
*/
        question.setText(scienceQuestion.getQname());
      /*  if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
            Glide.with(context).asBitmap().
                    load(Assessment_Constants.loadOnlineImagePath + scienceQuestion.getPhotourl()).apply(new RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable bd = new BitmapDrawable(resource);
                            questionImage.setImageDrawable(bd);
                        }
                    });
        } else questionImage.setVisibility(View.GONE);*/
        questionImage.setVisibility(View.VISIBLE);


       /* btn_capture_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureVideo();
            }
        });*/


    }

    @OnClick(R.id.btn_capture_video)
    public void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        assessmentAnswerListener.setVideoResult(intent, VIDEO_CAPTURE, scienceQuestion);
       /* if (VideoCaptured) {
            rl_answer_video.setVisibility(View.VISIBLE);
        } else rl_answer_video.setVisibility(View.GONE);
*/
    }

    @OnClick({R.id.iv_answer_image_play_icon, R.id.vv_answer_play_video})
    public void onAnswerVideoClicked() {
        String fileName =scienceQuestion.getPaperid()+ "_" +scienceQuestion.getQid()+ ".mp4";
        String path = Environment.getExternalStorageDirectory().toString() + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + fileName;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MICRO_KIND);
        iv_answer_image_play_icon.setImageBitmap(thumb);

        ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, scienceQuestion.getQtid());
        zoomImageDialog.show();
    }

    @OnClick({R.id.iv_question_image})
    public void onVideoClicked() {
        ZoomImageDialog zoomImageDialog = new ZoomImageDialog(context, path, scienceQuestion.getQtid());
        zoomImageDialog.show();

    }




}
