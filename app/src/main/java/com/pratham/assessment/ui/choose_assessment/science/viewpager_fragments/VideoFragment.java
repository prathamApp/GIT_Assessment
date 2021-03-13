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
import android.os.Parcelable;
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
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.custom.custom_dialogs.ChooseImageDialog;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ImageListDialog_;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.utilities.PermissionUtils;
import com.pratham.assessment.utilities.RealPathUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.constants.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA;
import static com.pratham.assessment.constants.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_video_row)
public class VideoFragment extends Fragment {

    public static final int SHOW_DIALOG = 2;


    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.iv_answer_image_play_icon)
    ImageView iv_answer_image_play_icon;
    @ViewById(R.id.btn_record_video)
    Button btn_capture_video;
    @ViewById(R.id.btn_show_recorded_video)
    Button btn_show_recorded_video;
    @ViewById(R.id.vv_question)
    VideoView vv_question;
    @ViewById(R.id.vv_answer_play_video)
    VideoView vv_answer;
    @ViewById(R.id.rl_answer_video)
    RelativeLayout rl_answer_video;
    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;
    private int videoCnt = 0;
    AssessmentAnswerListener assessmentAnswerListener;
    String fileName;
    String questionPath;
    public static final int VIDEO_CAPTURE = 101;
    public List imageList;

    String filePath;

    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;
    private String answerPath;
    private boolean VideoCaptured;
    String videoName = "";
    ScienceAssessmentActivity scienceAssessmentActivity;
    public static final int PICK_VIDEO_FROM_GALLERY = 1;

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
        imageList = new ArrayList();

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

       /* if (!scienceQuestion.getUserAnswer().equalsIgnoreCase("")) {
            rl_answer_video.setVisibility(View.VISIBLE);
        }*/
        if (scienceQuestion.getIsAttempted() && scienceQuestion.getMatchingNameList() != null) {
            if (scienceQuestion.getMatchingNameList().size() > 0) {
                for (int i = 0; i < scienceQuestion.getMatchingNameList().size(); i++) {
                    imageList.add(scienceQuestion.getMatchingNameList().get(i).getQcid());

                }
            }
            btn_show_recorded_video.setVisibility(View.VISIBLE);
        }

        if (scienceQuestion.getPhotourl() != null && !scienceQuestion.getPhotourl().equalsIgnoreCase(""))
            questionImage.setVisibility(View.VISIBLE);
    }

    @Click(R.id.btn_record_video)
    public void captureVideo() {

        ChooseImageDialog chooseImageDialog = new ChooseImageDialog(getActivity());
        chooseImageDialog.btn_take_photo.setText(R.string.capture_video);
        chooseImageDialog.btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                setVideoResult(intent, VIDEO_CAPTURE);

            }
        });

        chooseImageDialog.btn_choose_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog.dismiss();
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                    String[] permissionArray = new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};

                    if (!((ScienceAssessmentActivity) getActivity()).isPermissionsGranted(getActivity(), permissionArray)) {
                        Toast.makeText(getActivity(), R.string.give_storage_permissions, Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent_upload = new Intent();
                        intent_upload.setType("video/*");
                        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent_upload, PICK_VIDEO_FROM_GALLERY);
                    }
                } else {
                    Intent intent_upload = new Intent();
                    intent_upload.setType("video/*");
                    intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_upload, PICK_VIDEO_FROM_GALLERY);
                }
            }
        });
        chooseImageDialog.show();
    }

    @Click(R.id.btn_show_recorded_video)
    public void onViewCaptured() {
        if (imageList.size() > 0) {
            showImageThumbnailDialog(imageList, false);
        } else {
            Toast.makeText(getActivity(), "Nothing to show.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImageThumbnailDialog(List imageList, boolean showButton) {
//        ImageListDialog imageListDialog = new ImageListDialog(getActivity(), imageList);
//        imageListDialog.show();
        Intent intent = new Intent(getActivity(), ImageListDialog_.class);
        intent.putParcelableArrayListExtra("imageList", (ArrayList<? extends Parcelable>) imageList);
        intent.putExtra("showDeleteButton", showButton);
        intent.putExtra(DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA, DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO);
        startActivityForResult(intent, SHOW_DIALOG);
    }

    @Click({R.id.iv_answer_image_play_icon, R.id.vv_answer_play_video})
    public void onAnswerVideoClicked() {
        MediaController mediaController = new MediaController(getActivity());
//        answerPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + scienceQuestion.getUserAnswer();
        answerPath = filePath;
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
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3 * 60);
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

    /* @Override
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


                 showCapturedVideo();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 */
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == -1 && requestCode == PICK_VIDEO_FROM_GALLERY) {
                Uri selectedImage = data.getData();
                String path;

                path = RealPathUtil.getUriRealPathAboveKitkat(getActivity(), selectedImage);
                if (path.equalsIgnoreCase("")) {
                    path = RealPathUtil.getRealPathFromURI_API19(getActivity(), selectedImage);
                }
                imageList.add(selectedImage);
                scienceQuestion.setUserAnswer(path);
//                assessmentAnswerListener.setAnswerInActivity("", path, scienceQuestion.getQid(), null);
                filePath = path;
//                showCapturedVideo();
                showImageThumbnailDialog(imageList, true);
                if (imageList.size() > 0) {
                    btn_show_recorded_video.setVisibility(View.VISIBLE);
                } else btn_show_recorded_video.setVisibility(View.GONE);

            } else if (resultCode == -1 && requestCode == VIDEO_CAPTURE) {

                Uri videoUri = data.getData();
                String path = RealPathUtil.getUriRealPathAboveKitkat(getActivity(), videoUri);
                if (path.equalsIgnoreCase("")) {
                    path = RealPathUtil.getRealPathFromURI_API19(getActivity(), videoUri);
                }
                Log.d("vid path", "onActivityResult: " + path);
                filePath = path;
                imageList.add(path);
                showImageThumbnailDialog(imageList, true);

                if (imageList.size() > 0) {
                    btn_show_recorded_video.setVisibility(View.VISIBLE);
                } else btn_show_recorded_video.setVisibility(View.GONE);

//                showCapturedVideo();
            } else if (resultCode == 2 && requestCode == SHOW_DIALOG) {
                try {
                    List<ScienceQuestionChoice> answers = new ArrayList<>();

                    List imageList = data.getParcelableArrayListExtra("imageList");
//                    for (int j = 0; j < imageList.size(); j++) {
//                        for (int k = 0; k < newImageList.size(); k++) {
                 /*   Iterator<List> iterator = imageList.iterator();
                    while (iterator.hasNext()) {
//                        ScienceQuestion scienceQuestion = (ScienceQuestion) iterator.next();
                        if (!newImageList.contains(iterator.next()))
                            iterator.remove();
                    }*/


//                        }
//                    }
                    if (imageList.size() > 0) {
                        for (int i = 0; i < imageList.size(); i++) {
                            ScienceQuestionChoice answer = new ScienceQuestionChoice();
                            if (imageList.get(i) instanceof String) {
//                                fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE + "_" + capturedImageCnt + ".jpg";
                                answer.setQcid((String) imageList.get(i));
                            }
//                            if (imageList.get(i) instanceof Uri) {
                            else {
                                String path;
                                path = RealPathUtil.getUriRealPathAboveKitkat(getActivity(), (Uri) imageList.get(i));
                                if (path.equalsIgnoreCase("")) {
                                    path = RealPathUtil.getRealPathFromURI_API19(getActivity(), (Uri) imageList.get(i));
                                }
                                answer.setQcid(imageList.get(i).toString());
//                                answer.setQcid(path);
                            }

//                            }
                            answers.add(answer);

                        }

                        Log.d("TAG", "onActivityResult: " + imageList.size());

                        assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), answers);
                        if (imageList.size() > 0) {
                            btn_show_recorded_video.setVisibility(View.VISIBLE);
                        } else btn_show_recorded_video.setVisibility(View.GONE);

                    }
                    this.imageList = imageList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (
                Exception e) {
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
            ScienceQuestion scienceQuestion = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getQuestionByQID(this.scienceQuestion.getQid());
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
