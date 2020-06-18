package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.custom_dialogs.ChooseImageDialog;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.services.camera.VideoMonitoringService;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ImageListDialog;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.ImageListDialog_;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.UpdateImageListListener;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.PermissionUtils;
import com.pratham.assessment.utilities.RealPathUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_image_answer_row)
public class ImageAnswerFragment extends Fragment {

    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.captured_img)
    ImageView captured_img;
    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;

    @ViewById(R.id.btn_show_captured_img)
    Button view_captured_img;

    List<ScienceQuestionChoice> answers;

    ChooseImageDialog chooseImageDialog;
    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;
    AssessmentAnswerListener assessmentAnswerListener;
    private Context context;
    ScienceAssessmentActivity scienceAssessmentActivity;
    String imageName = "";
    public static final int CAPTURE_IMAGE = 0;
    public static final int PICK_IMAGE_FROM_GALLERY = 1;
    public static final int SHOW_DIALOG = 2;
    String path;
    String fileName;
    Uri capturedImageUri;
    int capturedImageCnt = 0;
    public List imageList;
    boolean isCaptured = false;

    public ImageAnswerFragment() {
        // Required empty public constructor
    }


    @AfterViews
    public void init() {
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
            context = getActivity();
            scienceAssessmentActivity = (ScienceAssessmentActivity) getActivity();
        }
        setImageQuestion();
        answers = new ArrayList<>();
    }


    public static ImageAnswerFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        ImageAnswerFragment_ fragment = new ImageAnswerFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
            context = getActivity();
            scienceAssessmentActivity = (ScienceAssessmentActivity) getActivity();


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_image_answer_row, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setImageQuestion();
    }
*/

    public void setImageQuestion() {
    /*    String para="";
        if (scienceQuestion.isParaQuestion()) {
            para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
        }
        assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());
*/


        /*etAnswer.setTextColor(Assessment_Utility.selectedColor);
        etAnswer.setText(scienceQuestion.getUserAnswer());*/
        imageList = new ArrayList();
        chooseImageDialog = new ChooseImageDialog(getActivity());

        if (scienceQuestion.getIsAttempted() && !scienceQuestion.getUserAnswer().equalsIgnoreCase("")) {

//            List imgs = Arrays.asList(scienceQuestion.getUserAnswer().split(","));
            if (scienceQuestion.getMatchingNameList().size() > 0) {
                for (int i = 0; i < scienceQuestion.getMatchingNameList().size(); i++) {
                    imageList.add(scienceQuestion.getMatchingNameList().get(i).getQcid());

                }
            }
            view_captured_img.setVisibility(View.VISIBLE);
//            setImage(Uri.parse(scienceQuestion.getUserAnswer()));
     /*       Glide.with(context)
                    .load(scienceQuestion.getUserAnswer())
//                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(Drawable.createFromPath(scienceQuestion.getUserAnswer())))
                    .into(captured_img);*/
//            captured_img.setVisibility(View.VISIBLE);
        }
        setOdiaFont(getActivity(), question);

        question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {

            String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
            final String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;


            String path = scienceQuestion.getPhotourl();
            String[] imgPath = path.split("\\.");
            int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                try {
                    InputStream gif;
                    if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        Glide.with(getActivity()).asGif()
                                .load(path)
                                .apply(new RequestOptions()
                                        .placeholder(Drawable.createFromPath(localPath)))
                                .into(questionImage);
//                    zoomImg.setVisibility(View.VISIBLE);
                    } else {
                        gif = new FileInputStream(localPath);
                        questionImage.setVisibility(View.GONE);
                        questionGif.setVisibility(View.VISIBLE);
                        questionGif.setGifResource(gif);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Glide.with(getActivity())
                        .load(path)
                        .apply(new RequestOptions()
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(questionImage);
            }

            questionImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
                }
            });
            questionGif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
                }
            });

         /*   } else {
                String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                questionImage.setImageBitmap(bitmap);
            }*/
        } else questionImage.setVisibility(View.GONE);

//        fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE + "_" + capturedImageCnt + ".jpg";

//            String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Answers/" + fileName;
        path = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH;

        captured_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                Assessment_Utility.showZoomDialog(getActivity(), scienceQuestion.getUserAnswer(), scienceQuestion.getUserAnswer(), "");
            }
        });


      /*  etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                questionTypeListener.setAnswer("", etAnswer.getText().toString(), scienceQuestion.getQid(), null);
//                assessmentAnswerListener.setAnswerInActivity("", etAnswer.getText().toString(), scienceQuestion.getQid(), null);
            }
        });*/

    }


    @Click(R.id.btn_show_captured_img)
    public void onViewCaptured() {
        if (imageList.size() > 0) {
            showImageThumbnailDialog(imageList, false);
        } else {
            Toast.makeText(getActivity(), "No images", Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.btn_capture_img)
    public void onCaptureClick() {
//        assessmentAnswerListener.setImageCaptureResult(scienceQuestion);
        setImageCaptureResult();

//        chooseImageDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


   /* public void setImage(Uri uri) {
        captured_img.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(uri)
                .into(captured_img);
//        captured_img.setImageURI(uri);

    }*/

  /*  public void setImage(Bitmap bitmap) {
        captured_img.setVisibility(View.VISIBLE);
        captured_img.setImageBitmap(bitmap);

    }*/

    public void setImageCaptureResult() {
        final ChooseImageDialog chooseImageDialog = new ChooseImageDialog(context);
//        imageFileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + ".jpg";

//            String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Answers/" + fileName;
//        imageFilePath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH ;

        chooseImageDialog.btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isCaptured = true;
                    chooseImageDialog.cancel();
                    fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE + "_" + capturedImageCnt + ".jpg";

                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                        String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA};

                        if (!((ScienceAssessmentActivity) context).isPermissionsGranted(context, permissionArray)) {
                            Toast.makeText(context, "Give Camera permissions through settings and restart the app.", Toast.LENGTH_SHORT).show();
                        } else {
//                        imageName = Assessment_Utility.getFileName(scienceQuestion.getQid())
                            scienceQuestion.setUserAnswer(path + "/" + fileName);
//                        selectedImage = selectedImageTemp;
                          /*  Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, CAPTURE_IMAGE);*/

                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            File imagesFolder = new File(path);
                            if (!imagesFolder.exists()) imagesFolder.mkdirs();
                            File image = new File(imagesFolder, fileName);
//                            capturedImageUri = Uri.fromFile(image);
                            capturedImageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", image);
                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, capturedImageUri);
                            startActivityForResult(cameraIntent, CAPTURE_IMAGE);
                        }
                    } else {
//                    imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
                       /* scienceQuestion.setUserAnswer(fileName);
//                    selectedImage = selectedImageTemp;
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, CAPTURE_IMAGE);*/
                        scienceQuestion.setUserAnswer(path + "/" + fileName);
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        File imagesFolder = new File(path);
                        if (!imagesFolder.exists()) imagesFolder.mkdirs();
                        File image = new File(imagesFolder, fileName);
//                        capturedImageUri = Uri.fromFile(image);
                        capturedImageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", image);
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, capturedImageUri);
                        startActivityForResult(cameraIntent, CAPTURE_IMAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Camera open failed", Toast.LENGTH_SHORT).show();
//                    assessmentAnswerListener.showCameraError();
                }
            }
        });


        chooseImageDialog.btn_choose_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCaptured = false;
                chooseImageDialog.cancel();
             /*   if (Assessment_Constants.VIDEOMONITORING) {
                    assessmentAnswerListener.pauseVideoMonitoring();
                }*/


                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                    String[] permissionArray = new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};

                    if (!((ScienceAssessmentActivity) context).isPermissionsGranted(context, permissionArray)) {
                        Toast.makeText(context, "Give Storage permissions through settings and restart the app.", Toast.LENGTH_SHORT).show();
                    } else {
//                        imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
//                        scienceQuestion.setUserAnswer(fileName);
//                        selectedImage = selectedImageTemp;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY);
                    }
                } else {
//                    imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
//                    scienceQuestion.setUserAnswer(fileName);
//                    selectedImage = selectedImageTemp;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY);
                }
            }
        });
        chooseImageDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == -1 && requestCode == PICK_IMAGE_FROM_GALLERY) {
                Uri selectedImage = data.getData();
//                setImage(selectedImage);
                imageList.add(selectedImage);
                showImageThumbnailDialog(imageList, true);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
//                createDirectoryAndSaveFile(bitmap, imageFileName);
                String path;

                path = RealPathUtil.getUriRealPathAboveKitkat(context, selectedImage);
                if (path.equalsIgnoreCase("")) {
                    path = RealPathUtil.getRealPathFromURI_API19(context, selectedImage);
                }
                scienceQuestion.setUserAnswer(path);

                if (imageList.size() > 0) {
                    view_captured_img.setVisibility(View.VISIBLE);
//                    for (int i = 0; i < imageList.size(); i++) {
                   /* ScienceQuestionChoice answer = new ScienceQuestionChoice();
                    path = RealPathUtil.getUriRealPathAboveKitkat(context, (Uri) selectedImage);
                    answer.setQcid(path);
                    answers.add(answer);*/
//                    }
                }

            /*    if (!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                    assessmentAnswerListener.setAnswerInActivity("", scienceQuestion.getUserAnswer(), scienceQuestion.getQid(), null);
                else*/
//                assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), answers);


            } else if (resultCode == -1 && requestCode == CAPTURE_IMAGE) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                Uri selectedImage = data.getData();
                fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE + "_" + capturedImageCnt + ".jpg";

                String file = path + "/" + fileName;
                imageList.add(file);
                showImageThumbnailDialog(imageList, true);
//                setImage(capturedImageUri);
//                if (currentFragment instanceof ImageAnswerFragment)
//                    ((ImageAnswerFragment) currentFragment).setImage(photo);
//                selectedImage.setImageBitmap(photo);
                // String selectedImagePath = getPath(photo);
//                setImage();
//                createDirectoryAndSaveFile(photo, fileName);
                if (imageList.size() > 0) {
                    view_captured_img.setVisibility(View.VISIBLE);
                    view_captured_img.setVisibility(View.VISIBLE);
//                    for (int i = 0; i < imageList.size(); i++) {
                    /*ScienceQuestionChoice answer = new ScienceQuestionChoice();
                    fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE + "_" + capturedImageCnt + ".jpg";

                    answer.setQcid(path + "/" + fileName);
                    answers.add(answer);*/
//                    }
                }
//                path + "/" + fileName
           /*     if (!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                    assessmentAnswerListener.setAnswerInActivity("", scienceQuestion.getUserAnswer(), scienceQuestion.getQid(), null);
                else*/
//                assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), answers);
                capturedImageCnt++;
            } else if (resultCode == 2 && requestCode == SHOW_DIALOG) {
                try {
                    List<ScienceQuestionChoice> answers = new ArrayList<>();

                    List imageList = data.getParcelableArrayListExtra("imageList");
//                    for (int j = 0; j < imageList.size(); j++) {
//                        for (int k = 0; k < newImageList.size(); k++) {
                  /*  Iterator<List> iterator = imageList.iterator();
                    while (iterator.hasNext()) {
//                        ScienceQuestion scienceQuestion = (ScienceQuestion) iterator.next();
                        if (!newImageList.contains(iterator.next()))
                            iterator.remove();
                    }
*/

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
                                path = RealPathUtil.getUriRealPathAboveKitkat(context, (Uri) imageList.get(i));
                                if (path.equalsIgnoreCase("")) {
                                    path = RealPathUtil.getRealPathFromURI_API19(context, (Uri) imageList.get(i));
                                }
                                answer.setQcid(path);
//                                answer.setQcid(path);
                            }

//                            }
                            answers.add(answer);

                        }

                        Log.d("TAG", "onActivityResult: " + imageList.size());

                        assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), answers);

                    }
                    this.imageList = imageList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            assessmentAnswerListener.resumeVideoMonitoring();
           /* if (Assessment_Constants.VIDEOMONITORING)
                scienceAssessmentActivity.startCameraService();*/
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    private void showImageThumbnailDialog(List imageList, boolean showButton) {
//        ImageListDialog imageListDialog = new ImageListDialog(getActivity(), imageList);
//        imageListDialog.show();
        Intent intent = new Intent(context, ImageListDialog_.class);
        intent.putParcelableArrayListExtra("imageList", (ArrayList<? extends Parcelable>) imageList);
        intent.putExtra("showDeleteButton", showButton);
        startActivityForResult(intent, SHOW_DIALOG);
    }

    /*   private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
           try {vide
               File direct = new File(path);
               if (!direct.exists()) direct.mkdir();
               scienceQuestion.setUserAnswer(path + "/" + fileName);

               File file = new File(path + "/" + fileName);


   //           File  file1 = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH);

   //            if (!direct.exists()) direct.mkdir();

   //            File fileName = new File(direct, fileName);
              *//* if (fileName.exists())
                fileName.delete();*//*


            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/
    private String getRealPathFromURI(Uri contentURI) {

        String thePath = "no-path-found";
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return thePath;
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
            if (scienceQuestion.isParaQuestion()) {
                btn_view_hint.setVisibility(View.VISIBLE);
//                para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
            } else btn_view_hint.setVisibility(View.GONE);
//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        } else {
            btn_view_hint.setVisibility(View.GONE);

//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        }

      /*  if (answers != null && answers.size() > 0) {
            assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), answers);
        }*/
//        if(imageList.size()>0){

      /*  if (imageList.size() > 0) {
            for (int i = 0; i < imageList.size(); i++) {
                ScienceQuestionChoice answer = new ScienceQuestionChoice();
                if (isCaptured) {
                    fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE + "_" + capturedImageCnt + ".jpg";
                    answer.setQcid(path + "/" + fileName);
                } else {
                    path = RealPathUtil.getUriRealPathAboveKitkat(context, (Uri) imageList.get(i));
                  *//*  ScienceQuestionChoice answer = new ScienceQuestionChoice();
                    fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE + "_" + capturedImageCnt + ".jpg";
                    answer.setQcid(path + "/" + fileName);*//*
//                    answers.add(answer);
                    answer.setQcid(path);
                }
                answers.add(answer);
            }

            assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), answers);
//            }
        }*/


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


   /* public static void updateList(List imgList) {
        imageList = imgList;

    }*/
}
