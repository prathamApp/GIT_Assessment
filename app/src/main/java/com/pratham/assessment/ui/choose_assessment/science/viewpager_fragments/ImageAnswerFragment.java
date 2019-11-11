package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.PermissionUtils;
import com.pratham.assessment.utilities.RealPathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

public class ImageAnswerFragment extends Fragment {

    @BindView(R.id.tv_question)
    TextView question;
    @BindView(R.id.iv_question_image)
    ImageView questionImage;
    @BindView(R.id.iv_question_gif)
    GifView questionGif;
    @BindView(R.id.captured_img)
    ImageView captured_img;

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
    String path;
    String fileName;

    public ImageAnswerFragment() {
        // Required empty public constructor
    }


    public static ImageAnswerFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        ImageAnswerFragment fragment = new ImageAnswerFragment();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
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


    public void setImageQuestion() {
        /*etAnswer.setTextColor(Assessment_Utility.selectedColor);
        etAnswer.setText(scienceQuestion.getUserAnswer());*/
        chooseImageDialog = new ChooseImageDialog(getActivity());

        if (scienceQuestion.getIsAttempted() && !scienceQuestion.getUserAnswer().equalsIgnoreCase("")) {
//            setImage(Uri.parse(scienceQuestion.getUserAnswer()));
            Glide.with(context)
                    .load(scienceQuestion.getUserAnswer())
//                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(Drawable.createFromPath(scienceQuestion.getUserAnswer())))
                    .into(captured_img);
            captured_img.setVisibility(View.VISIBLE);
        }

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
                    showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath);
                }
            });
            questionGif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath);
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

        fileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + ".jpg";

//            String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Answers/" + fileName;
        path = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH;

        captured_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                Assessment_Utility.showZoomDialog(getActivity(), scienceQuestion.getUserAnswer(), scienceQuestion.getUserAnswer());
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

    @OnClick(R.id.btn_capture_img)
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setImage(Uri uri) {
        captured_img.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(uri)
                .into(captured_img);
//        captured_img.setImageURI(uri);

    }

    public void setImage(Bitmap bitmap) {
        captured_img.setVisibility(View.VISIBLE);
        captured_img.setImageBitmap(bitmap);

    }

    public void setImageCaptureResult() {
        final ChooseImageDialog chooseImageDialog = new ChooseImageDialog(context);
//        imageFileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + ".jpg";

//            String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Answers/" + fileName;
//        imageFilePath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH ;

        chooseImageDialog.btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog.cancel();
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                    String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA};

                    if (!((ScienceAssessmentActivity) context).isPermissionsGranted(context, permissionArray)) {
                        Toast.makeText(context, "Give Camera permissions through settings and restart the app.", Toast.LENGTH_SHORT).show();
                    } else {
//                        imageName = Assessment_Utility.getFileName(scienceQuestion.getQid())
                        scienceQuestion.setUserAnswer(fileName);
//                        selectedImage = selectedImageTemp;
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, CAPTURE_IMAGE);
                    }
                } else {
//                    imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
                    scienceQuestion.setUserAnswer(fileName);
//                    selectedImage = selectedImageTemp;
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, CAPTURE_IMAGE);
                }
            }
        });


        chooseImageDialog.btn_choose_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageDialog.cancel();
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                    String[] permissionArray = new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};

                    if (!((ScienceAssessmentActivity) context).isPermissionsGranted(context, permissionArray)) {
                        Toast.makeText(context, "Give Storage permissions through settings and restart the app.", Toast.LENGTH_SHORT).show();
                    } else {
//                        imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
                        scienceQuestion.setUserAnswer(fileName);
//                        selectedImage = selectedImageTemp;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY);
                    }
                } else {
//                    imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
                    scienceQuestion.setUserAnswer(fileName);
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
//            if (currentFragment instanceof ImageAnswerFragment)
//                ((ImageAnswerFragment) currentFragment).setImage(selectedImage);
                setImage(selectedImage);
//                this.selectedImage.setImageURI(selectedImage);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
//                createDirectoryAndSaveFile(bitmap, imageFileName);
                String path;
                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    path = RealPathUtil.getRealPathFromURI_API19(context, selectedImage);
                } else if (android.os.Build.VERSION.SDK_INT > 10) {
                    path = RealPathUtil.getRealPathFromURI_API11to18(context, selectedImage);
                } else path = RealPathUtil.getRealPathFromURI_BelowAPI11(context, selectedImage);

                scienceQuestion.setUserAnswer(path);

                if (!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                    assessmentAnswerListener.setAnswerInActivity("", scienceQuestion.getUserAnswer(), scienceQuestion.getQid(), null);
                else
                    assessmentAnswerListener.setAnswerInActivity("", path, scienceQuestion.getQid(), null);
        } else if (resultCode == -1 && requestCode == CAPTURE_IMAGE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                if (currentFragment instanceof ImageAnswerFragment)
//                    ((ImageAnswerFragment) currentFragment).setImage(photo);
//                selectedImage.setImageBitmap(photo);
                // String selectedImagePath = getPath(photo);
                setImage(photo);
                createDirectoryAndSaveFile(photo, fileName);
                if (!scienceQuestion.getUserAnswer().equalsIgnoreCase(""))
                    assessmentAnswerListener.setAnswerInActivity("", scienceQuestion.getUserAnswer(), scienceQuestion.getQid(), null);
                else
                    assessmentAnswerListener.setAnswerInActivity("", path + "/" + fileName, scienceQuestion.getQid(), null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        try {
            File direct = new File(path);
            if (!direct.exists()) direct.mkdir();
            scienceQuestion.setUserAnswer(path + "/" + fileName);

            File file = new File(path + "/" + fileName);


//           File  file1 = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH);

//            if (!direct.exists()) direct.mkdir();

//            File fileName = new File(direct, fileName);
           /* if (fileName.exists())
                fileName.delete();*/


            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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

}
