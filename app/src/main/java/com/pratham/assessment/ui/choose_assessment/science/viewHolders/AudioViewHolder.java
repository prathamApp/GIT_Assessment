package com.pratham.assessment.ui.choose_assessment.science.viewHolders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.AudioUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;
    @BindView(R.id.iv_question_audio)
    ImageView iv_question_audio;
    @BindView(R.id.iv_start_audio)
    ImageView iv_start_audio;
    AssessmentAnswerListener assessmentAnswerListener;
    ScienceQuestion scienceQuestion;
    Context context;
    boolean isRecording;
    boolean isPlaying;
    MediaPlayer mp = new MediaPlayer();
    String path;


    public AudioViewHolder(@NonNull View itemView, Context context, ScienceAdapter scienceAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        assessmentAnswerListener = (ScienceAssessmentActivity) context;
    }

    public void setAudioQuestion(final ScienceQuestion scienceQuestion1, int pos) {
        this.scienceQuestion = scienceQuestion1;

        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
           /* Glide.with(context).asBitmap().
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
                    });*/
            String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
            path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded" + "/" + fileName;

           /* Glide.with(context).asBitmap().
                    load( Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded"+fileName).apply(new RequestOptions()
                    .fitCenter()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .override(Target.SIZE_ORIGINAL))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            Drawable bd = new BitmapDrawable(resource);
                            questionImage.setImageDrawable(bd);
                        }
                    });*/


        } else questionImage.setVisibility(View.GONE);

        try {

            File direct = new File(Environment.getExternalStorageDirectory().toString() + "/.Assessment");
            if (!direct.exists()) direct.mkdir();
            direct = new File(Environment.getExternalStorageDirectory().toString() + "/.Assessment/Science/Content/Answers");
            if (!direct.exists()) direct.mkdir();
    /*        iv_question_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileName = scienceQuestion.getPaperid() + "_" + scienceQuestion.getQid() + ".mp3";

                    String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Answers/" + fileName;
                    if (isRecording) {
                        isRecording = false;
                        iv_start_audio.setImageResource(R.drawable.ic_play_circle);

                    } else {
                        isRecording = true;
                        iv_start_audio.setImageResource(R.drawable.ic_pause);

                    }
                    assessmentAnswerListener.setAudio(path, isRecording);
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_start_audio)
    public void startAudio() {
        try {
            String fileName =scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid()  + ".mp3";

            String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Answers/" + fileName;
            if (isRecording) {
                isRecording = false;
                iv_start_audio.setImageResource(R.drawable.ic_pause);

                AudioUtil.startRecording(path);


            } else {
                isRecording = true;
                AudioUtil.stopRecording();
                iv_start_audio.setImageResource(R.drawable.ic_mic_24dp);

            }
//            assessmentAnswerListener.setAudio(path, isRecording);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_question_audio)
    public void playQuestionAudio() {
        String fileName =scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid()  + ".mp3";
        try {
            String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded/" + fileName;
            if (isPlaying) {
                isPlaying = false;
                iv_question_audio.setImageResource(R.drawable.ic_play_circle);
                AudioUtil.playRecording(path, (Activity) context);


            } else {
                isPlaying = true;
                iv_question_audio.setImageResource(R.drawable.ic_pause);
                AudioUtil.stopPlayingAudio();

            }
//            assessmentAnswerListener.setAudio(path, isRecording);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String qid, String photoUrl) {
        String[] splittedPath = photoUrl.split("/");
        String fileName = qid + "_" + splittedPath[splittedPath.length - 1];
        return fileName;
    }
}
