package com.pratham.assessment.ui.choose_assessment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.constants.Assessment_Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.UUID;

@EFragment(R.layout.activity_supervised_assessment)
public class SupervisedAssessmentFragment extends Fragment {
    @ViewById(R.id.submitBtn)
    Button submitBtn;
    @ViewById(R.id.btn_camera)
    ImageButton btn_camera;
    @ViewById(R.id.supervisor_name)
    TextView supervisor_name;
    @ViewById(R.id.iv_image)
    ImageView iv_image;
    Context context;
    String imageName = "";
    boolean isPhotoSaved = false;
    String supervisorId = "";
    //    String subId = "";
    private static final int CAMERA_REQUEST = 1888;
    AssessmentAnswerListener assessmentAnswerListener;
    String paperId = "";
    Uri capturedImageUri;
    String path;
    String fileName;
    String currentSession;
    String sName;

    @AfterViews
    public void init() {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        supervisorId = getIntent().getStringExtra("crlId");
        supervisorId = "" + AssessmentApplication.getUniqueID();
        context = getActivity();
        currentSession = FastSave.getInstance().getString("currentSession", "");

        assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
        paperId = getArguments().getString("paperId");
//        assessmentAnswerListener = (DownloadQuestionsActivity) getActivity();
//        subId = getIntent().getStringExtra("subId");
        if (Assessment_Constants.SELECTED_SUBJECT.equals("ece")) {
            goToAssessment();
        } else {
           /* Crl crl = AppDatabase.getDatabaseInstance(this).getCrlDao().getCrl(supervisorId);
            supervisor_name.setText(crl.getFirstName() + " " + crl.getLastName());*/
        }
    }

    public SupervisedAssessmentFragment() {
        // Required empty public constructor
    }

    /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervised_assessment);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        supervisorId = getIntent().getStringExtra("crlId");
        supervisorId = "" + AssessmentApplication.getUniqueID();

//        subId = getIntent().getStringExtra("subId");
        if (Assessment_Constants.SELECTED_SUBJECT.equals("ece")) {
            goToAssessment();
        } else {
           *//* Crl crl = AppDatabase.getDatabaseInstance(this).getCrlDao().getCrl(supervisorId);
            supervisor_name.setText(crl.getFirstName() + " " + crl.getLastName());*//*
        }
    }*/

    @Click(R.id.btn_camera)
    public void openCamera() {
        try {
            fileName = paperId + "_" + Assessment_Constants.DOWNLOAD_MEDIA_TYPE_SUPERVISOR + ".jpg";
            path = AssessmentApplication.assessPath + Assessment_Constants.STORE_SUPERVISOR_IMAGE_PATH + "/";

            imageName = fileName;
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File imagesFolder = new File(path);
            if (!imagesFolder.exists()) imagesFolder.mkdirs();
            File image = new File(imagesFolder, fileName);
//                            capturedImageUri = Uri.fromFile(image);
            capturedImageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", image);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, capturedImageUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("codes", String.valueOf(requestCode) + resultCode);
        try {
            if (requestCode == CAMERA_REQUEST && resultCode == -1) {
              /*  Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {*/
                File f = new File(path + fileName);
                if (f.exists())
                    iv_image.setVisibility(View.VISIBLE);
               /* Bitmap photo = (Bitmap) data.getExtras().get("data");
                iv_image.setImageBitmap(photo);
                iv_image.setScaleType(ImageView.ScaleType.FIT_XY);
                createDirectoryAndSaveFile(photo, imageName);*/
                Glide.with(context)
                        .load(capturedImageUri)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(iv_image);
                DownloadMedia downloadMedia = new DownloadMedia();
                downloadMedia.setPaperId(supervisorId);
                downloadMedia.setQtId(currentSession);
                downloadMedia.setqId(sName);
                String fileName = AssessmentApplication.assessPath + Assessment_Constants.STORE_SUPERVISOR_IMAGE_PATH + "/" + imageName;
                downloadMedia.setPhotoUrl(fileName);
                downloadMedia.setMediaType(Assessment_Constants.DOWNLOAD_MEDIA_TYPE_SUPERVISOR);
                int cnt = AppDatabase.getDatabaseInstance(getActivity()).getDownloadMediaDao().deleteByPaperIdAndQtid(supervisorId, currentSession);
                AppDatabase.getDatabaseInstance(getActivity()).getDownloadMediaDao().insert(downloadMedia);
                isPhotoSaved = true;
//                }
            }
//        captured_img.setImageURI(uri);
        } catch (Exception e) {
            isPhotoSaved = false;
            e.printStackTrace();
        }
    }

    @Click(R.id.submitBtn)
    public void submitSupervisorData() {
        sName = "" + supervisor_name.getText().toString();
        if (isPhotoSaved) {
            if (sName.length() > 0) {
                Assessment_Constants.ASSESSMENT_TYPE = Assessment_Constants.SUPERVISED;
                Assessment_Constants.supervisedAssessment = true;
                AddSupervisorToDB(supervisorId, sName, imageName);
            } else Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
        } else {
            AnimateCamButton(context, btn_camera);
        }
    }

    public void AnimateCamButton(Context c, final ImageButton imageButton) {
        final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.side_shake);
        imageButton.startAnimation(animShake);
    }

    public void AddSupervisorToDB(final String supervisorID, final String sName,
                                  final String supervisorPhoto) {
        new AsyncTask<Object, Void, Object>() {


            @Override
            protected Object doInBackground(Object[] objects) {
                try {
//                    String uID = "" + AssessmentApplication.getUniqueID();
//                    FastSave.getInstance().saveString("assessmentSession", uID);

//                    assessmentSession = "" + uID;
                    SupervisorData supervisorData = new SupervisorData();
                    supervisorData.setSupervisorId(supervisorID);
                    supervisorData.setSupervisorName(sName);

//                    supervisorData.setAssessmentSessionId(assessmentSession);
                    supervisorData.setAssessmentSessionId(currentSession);


                    supervisorData.setSupervisorPhoto(supervisorPhoto);


                    AppDatabase.getDatabaseInstance(context).getSupervisorDataDao().insert(supervisorData);
                    BackupDatabase.backup(context);

                    AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("currentSession", "" + currentSession);
//                    Assessment_Constants.assessmentFlag = true;

                /*    String AppStartDateTime = AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getStatusDao().getValue("AppStartDateTime");
                    Session startSesion = new Session();
                    startSesion.setSessionID("" + currentSession);
                    String timerTime = AssessmentApplication.getCurrentDateTime(false, AppStartDateTime);
                    Log.d("doInBackground", "--------------------------------------------doInBackground : " + timerTime);
                    startSesion.setFromDate(timerTime);
                    startSesion.setToDate("NA");
                    startSesion.setSentFlag(0);
//                    Assessment_Constants.currentsupervisorID = "" + supervisorID;
                    AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getSessionDao().insert(startSesion);
*/
//                    getStudents();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                goToAssessment();

            }
        }.execute();
    }

    private void goToAssessment() {
        if (Assessment_Constants.SELECTED_SUBJECT.equalsIgnoreCase("ece")) {
            String unique_assessmentSession = "" + UUID.randomUUID().toString();
            Assessment_Constants.assessmentSession = "test-" + unique_assessmentSession;
            FastSave.getInstance().saveString("assessmentSession", unique_assessmentSession);

            try {
                Session startSesion = new Session();
//                startSesion.setSessionID("" + Assessment_Constants.assessmentSession);
                startSesion.setSessionID("" + unique_assessmentSession);
                String timerTime = AssessmentApplication.getCurrentDateTime(false, AssessmentApplication.getCurrentDateTime());
                startSesion.setFromDate(timerTime);
                startSesion.setToDate("NA");
                startSesion.setSentFlag(0);
                AppDatabase.getDatabaseInstance(context).getSessionDao().insert(startSesion);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            presenter.startAssessSession();
           /* Intent intent = new Intent(context, ECEActivity_.class);
            intent.putExtra("resId", "9962");
            intent.putExtra("crlId", supervisorId);
            startActivity(intent);*/
//            finish();
            if (getActivity() != null)
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            assessmentAnswerListener.removeSupervisorFragment();
        }/* else if (subId.equalsIgnoreCase("1302")) {
            Intent intent = new Intent(SupervisedAssessmentActivity.this, TestDisplayActivity.class);
            intent.putExtra("subId", subId);
            intent.putExtra("crlId", supervisorId);

            startActivity(intent);
            finish();
        }*/ else {
          /*  Intent intent = new Intent(context, ScienceAssessmentActivity_.class);
            startActivity(intent);*/
            //finish();
            assessmentAnswerListener.removeSupervisorFragment();
            if (getActivity() != null)
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();

            /*Intent intent = new Intent(SupervisedAssessmentActivity.this, ScienceAssessmentActivity.class);
            intent.putExtra("crlId", supervisorId);
            startActivity(intent);
            finish();*/
        }
    }


    public void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        try {

         /*   File direct = new File(Environment.getExternalStorageDirectory().toString() + "/.assessmentInternal");
            if (!direct.exists()) direct.mkdir();
            direct = new File(Environment.getExternalStorageDirectory().toString() + "/.assessmentInternal/supervisorImages");
            if (!direct.exists()) direct.mkdir();
*/
            File direct = new File(AssessmentApplication.assessPath + Assessment_Constants.ASSESSMENT_FOLDER_PATH);
            if (!direct.exists()) direct.mkdir();
            direct = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_SUPERVISOR_IMAGE_PATH);
            if (!direct.exists()) direct.mkdir();

            File file = new File(direct, fileName);

            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
