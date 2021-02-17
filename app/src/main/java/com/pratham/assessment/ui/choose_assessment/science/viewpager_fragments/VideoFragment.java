package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.PermissionUtils;
import com.pratham.assessment.utilities.RealPathUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_video_row)
public class VideoFragment extends Fragment {

    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.iv_answer_image_play_icon)
    ImageView iv_answer_image_play_icon;
    @ViewById(R.id.btn_capture_video)
    Button btn_capture_video;
    @ViewById(R.id.vv_question)
    VideoView vv_question;
    @ViewById(R.id.vv_answer_play_video)
    VideoView vv_answer;
    @ViewById(R.id.rl_answer_video)
    RelativeLayout rl_answer_video;
    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;

    AssessmentAnswerListener assessmentAnswerListener;
    String fileName;
    String questionPath;
    public static final int VIDEO_CAPTURE = 101;

    String filePath;

    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;
    private String answerPath;
    private boolean VideoCaptured;
    String videoName = "";
    ScienceAssessmentActivity scienceAssessmentActivity;

    public VideoFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
            scienceAssessmentActivity = (ScienceAssessmentActivity) getActivity();

        }
        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        setVideoQuestion();

    }

    public static VideoFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        VideoFragment_ videoFragment = new VideoFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        videoFragment.setArguments(args);
        return videoFragment;
    }


    public void setVideoQuestion() {
        setOdiaFont(getActivity(), question);

        fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
        rl_answer_video.setVisibility(View.GONE);
//        questionPath = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded" + "/" + fileName;
        questionPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
        if (scienceQuestion.getQname().equalsIgnoreCase(""))
            question.setText(R.string.watch_the_video);
        else question.setText(Html.fromHtml(scienceQuestion.getQname()));
        rl_answer_video.setVisibility(View.GONE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(questionPath, MediaStore.Images.Thumbnails.MICRO_KIND);
        BitmapDrawable ob = new BitmapDrawable(getResources(), thumb);
        questionImage.setBackgroundDrawable(ob);

        if (!scienceQuestion.getUserAnswer().equalsIgnoreCase("")) {
            rl_answer_video.setVisibility(View.VISIBLE);
        }
        questionImage.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btn_capture_video)
    public void captureVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        setVideoResult(intent, VIDEO_CAPTURE);
    }


    @Click({R.id.iv_answer_image_play_icon, R.id.vv_answer_play_video})
    public void onAnswerVideoClicked() {
        MediaController mediaController = new MediaController(getActivity());
//        answerPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + scienceQuestion.getUserAnswer();
        answerPath = scienceQuestion.getUserAnswer();
        if (scienceQuestion.getIsAttempted()) {
            if (answerPath == null) {
                if (!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                    answerPath = scienceQuestion.getUserAnswer();
                else answerPath = "";
            }
            iv_answer_image_play_icon.setVisibility(View.GONE);
            vv_answer.setVisibility(View.VISIBLE);
            vv_answer.setVideoPath(answerPath);
            vv_answer.setMediaController(mediaController);
            mediaController.setAnchorView(vv_answer);
            vv_answer.setZOrderOnTop(true);
            vv_answer.setZOrderMediaOverlay(true);
            vv_answer.start();
        }
    }

    @Click({R.id.iv_question_image})
    public void onVideoClicked() {
        MediaController mediaController = new MediaController(getActivity());
        questionImage.setVisibility(View.GONE);
        vv_question.setVisibility(View.VISIBLE);
        vv_question.setVideoPath(questionPath);
        vv_question.setMediaController(mediaController);
        mediaController.setAnchorView(vv_question);
        vv_question.setZOrderOnTop(true);
        vv_question.setZOrderMediaOverlay(true);
        vv_question.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (vv_question != null)
            vv_question.pause();
        if (vv_answer != null)
            vv_answer.pause();
    }

    public void showAnswerVideo() {
        try {

            rl_answer_video.setVisibility(View.VISIBLE);
//        answerPath = Environment.getExternalStorageDirectory().toString() + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" +scienceQuestion.getUserAnswer();
//            answerPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + scienceQuestion.getUserAnswer();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
            BitmapDrawable ob = new BitmapDrawable(getResources(), thumb);
            iv_answer_image_play_icon.setBackgroundDrawable(ob);
            iv_answer_image_play_icon.setVisibility(View.VISIBLE);
            VideoCaptured = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVideoResult(Intent intent, int videoCapture) {

        try {
            if (hasCamera()) {
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                    String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA};

                    if (!((ScienceAssessmentActivity) getActivity()).isPermissionsGranted(getActivity(), permissionArray)) {
                        Toast.makeText(getActivity(), R.string.give_camera_permissions, Toast.LENGTH_LONG).show();
                    } else {
//                        videoName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO + ".mp4";
                        scienceQuestion.setUserAnswer(videoName);
                        intent.putExtra(MediaStore. EXTRA_DURATION_LIMIT, 3 * 60);
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    }
                } else {
//                    videoName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO + ".mp4";
                    scienceQuestion.setUserAnswer(videoName);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3 * 60);
                }
            }
//            filePath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + videoName;
            startActivityForResult(intent, videoCapture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean hasCamera() {
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == -1 && requestCode == VIDEO_CAPTURE) {

                Uri videoUri = data.getData();
                String path = RealPathUtil.getUriRealPathAboveKitkat(getActivity(), videoUri);
                if (path.equalsIgnoreCase("")) {
                    path = RealPathUtil.getRealPathFromURI_API19(getActivity(), videoUri);
                }
                Log.d("vid path", "onActivityResult: " + path);
                filePath = path;

        /*        AssetFileDescriptor videoAsset = getActivity().getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
                File direct = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH);
                if (!direct.exists()) direct.mkdir();
                File fileName = new File(direct, videoName);
                if (fileName.exists())
                    fileName.delete();
                OutputStream out = new FileOutputStream(fileName);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();*/
                showCapturedVideo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCapturedVideo() {
        try {
            showAnswerVideo();
          /*  videoName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO + ".mp4";
            filePath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + videoName;
         */
            assessmentAnswerListener.setAnswerInActivity("", filePath, scienceQuestion.getQid(), null);
//        checkAssessment(queCnt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE
        String para = "";
        if (scienceQuestion != null) {
            scienceQuestion = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getQuestionByQID(scienceQuestion.getQid());
            if (scienceQuestion.isParaQuestion()) {
                btn_view_hint.setVisibility(View.VISIBLE);
//                para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
            } else btn_view_hint.setVisibility(View.GONE);
//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        } else {
            btn_view_hint.setVisibility(View.GONE);
//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());
        }


    }

    @Click(R.id.btn_view_hint)
    public void showPara() {
        if (scienceQuestion != null) {
            if (scienceQuestion.isParaQuestion()) {
                String para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
                showZoomDialog(getActivity(), "", "", para);
            }
        }
    }
}
