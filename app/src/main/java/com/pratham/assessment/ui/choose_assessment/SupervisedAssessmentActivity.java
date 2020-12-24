package com.pratham.assessment.ui.choose_assessment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.ui.choose_assessment.ece.ECEActivity_;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity_;
import com.pratham.assessment.constants.Assessment_Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@EActivity(R.layout.activity_supervised_assessment)
public class SupervisedAssessmentActivity extends BaseActivity {
    @ViewById(R.id.submitBtn)
    Button submitBtn;
    @ViewById(R.id.btn_camera)
    ImageButton btn_camera;
    @ViewById(R.id.supervisor_name)
    TextView supervisor_name;
    @ViewById(R.id.iv_image)
    ImageView iv_image;

    String imageName = "";
    boolean isPhotoSaved = false;
    String supervisorId = "";
    //    String subId = "";
    private static final int CAMERA_REQUEST = 1888;

    @AfterViews
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        supervisorId = getIntent().getStringExtra("crlId");
        supervisorId = "" + AssessmentApplication.getUniqueID();

//        subId = getIntent().getStringExtra("subId");
        if (Assessment_Constants.SELECTED_SUBJECT.equals("ece")) {
            goToAssessment();
        } else {
           /* Crl crl = AppDatabase.getDatabaseInstance(this).getCrlDao().getCrl(supervisorId);
            supervisor_name.setText(crl.getFirstName() + " " + crl.getLastName());*/
        }
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
        imageName = supervisorId + ".jpg";
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("codes", String.valueOf(requestCode) + resultCode);
        try {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                iv_image.setVisibility(View.VISIBLE);
                iv_image.setImageBitmap(photo);
                iv_image.setScaleType(ImageView.ScaleType.FIT_XY);
                createDirectoryAndSaveFile(photo, imageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.submitBtn)
    public void submitSupervisorData() {
        String sName = "" + supervisor_name.getText();
        if (isPhotoSaved) {
            if (sName.length() != 0) {
                Assessment_Constants.ASSESSMENT_TYPE = Assessment_Constants.SUPERVISED;
                Assessment_Constants.supervisedAssessment = true;
                AddSupervisorToDB(supervisorId, sName, imageName);
            }
        } else {
            AnimateCamButton(this, btn_camera);
        }
    }

    public void AnimateCamButton(Context c, final ImageButton imageButton) {
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.side_shake);
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
                    String currentSession = FastSave.getInstance().getString("currentSession", "");

//                    supervisorData.setAssessmentSessionId(assessmentSession);
                    supervisorData.setAssessmentSessionId(currentSession);
                    supervisorData.setSupervisorPhoto(supervisorPhoto);

                    AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getSupervisorDataDao().insert(supervisorData);
                    BackupDatabase.backup(SupervisedAssessmentActivity.this);

                    AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getStatusDao().updateValue("currentSession", "" + currentSession);
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
                    goToAssessment();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

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
                AppDatabase.getDatabaseInstance(this).getSessionDao().insert(startSesion);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            presenter.startAssessSession();
            Intent intent = new Intent(SupervisedAssessmentActivity.this, ECEActivity_.class);
            intent.putExtra("resId", "9962");
            intent.putExtra("crlId", supervisorId);
            startActivity(intent);
            finish();
        }/* else if (subId.equalsIgnoreCase("1302")) {
            Intent intent = new Intent(SupervisedAssessmentActivity.this, TestDisplayActivity.class);
            intent.putExtra("subId", subId);
            intent.putExtra("crlId", supervisorId);

            startActivity(intent);
            finish();
        }*/ else {
            Intent intent = new Intent(SupervisedAssessmentActivity.this, ScienceAssessmentActivity_.class);
           /* Intent intent = new Intent(SupervisedAssessmentActivity.this, DownloadQuestionsActivity_.class);*/
            startActivity(intent);
            finish();
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
            isPhotoSaved = true;
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Assessment_Constants.ASSESSMENT_TYPE = "practice";
    }
}
