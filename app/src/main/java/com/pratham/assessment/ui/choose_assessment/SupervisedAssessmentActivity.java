package com.pratham.assessment.ui.choose_assessment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.display_english_list.TestDisplayActivity;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pratham.assessment.utilities.Assessment_Constants.assessmentSession;

public class SupervisedAssessmentActivity extends AppCompatActivity {
    @BindView(R.id.submitBtn)
    Button submitBtn;
    @BindView(R.id.btn_camera)
    ImageButton btn_camera;
    @BindView(R.id.supervisor_name)
    TextView supervisor_name;
    @BindView(R.id.iv_image)
    ImageView iv_image;

    String imageName = "";
    boolean isPhotoSaved = false;
    String supervisorId = "";
    String nodeId = "";
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervised_assessment);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supervisorId = getIntent().getStringExtra("loggedCrlId");
        nodeId = getIntent().getStringExtra("nodeId");
        if (nodeId.equals("1302") || nodeId.equals("1304")) {
            goToAssessment();
        } else {
            Crl crl = AppDatabase.getDatabaseInstance(this).getCrlDao().getCrl(supervisorId);
            supervisor_name.setText(crl.getFirstName() + " " + crl.getLastName());
        }
    }

    @OnClick(R.id.btn_camera)
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

    @OnClick(R.id.submitBtn)
    public void submitSupervisorData() {
        String sName = "" + supervisor_name.getText();
        if (isPhotoSaved) {
            if (sName.length() != 0) {
                Assessment_Constants.supervisedAssessment = true;
                AddSupervisorToDB(supervisorId, sName, imageName);
            }
        } else {
            AnimateCamButtom(this, btn_camera);
        }
    }

    public void AnimateCamButtom(Context c, final ImageButton imageButton) {
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.side_shake);
        imageButton.startAnimation(animShake);
    }

    public void AddSupervisorToDB(final String supervisorID, final String sName,
                                  final String supervisorPhoto) {
        new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    assessmentSession = "test-" + AssessmentApplication.getUniqueID();
                    SupervisorData supervisorData = new SupervisorData();
                    supervisorData.setSupervisorId(supervisorID);
                    supervisorData.setSupervisorName(sName);
                    supervisorData.setAssessmentSessionId(assessmentSession);
                    supervisorData.setSupervisorPhoto(supervisorPhoto);

                    AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getSupervisorDataDao().insert(supervisorData);
                    BackupDatabase.backup(SupervisedAssessmentActivity.this);

                    AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getStatusDao().updateValue("AssessmentSession", "" + assessmentSession);
                    assessmentSession = assessmentSession;
//                    Assessment_Constants.assessmentFlag = true;

                    String AppStartDateTime = AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getStatusDao().getValue("AppStartDateTime");
                    Session startSesion = new Session();
                    startSesion.setSessionID("" + assessmentSession);
                    String timerTime = AssessmentApplication.getCurrentDateTime(false, AppStartDateTime);
                    Log.d("doInBackground", "--------------------------------------------doInBackground : " + timerTime);
                    startSesion.setFromDate(timerTime);
                    startSesion.setToDate("NA");
                    startSesion.setSentFlag(0);
                    Assessment_Constants.currentsupervisorID = "" + supervisorID;
                    AppDatabase.getDatabaseInstance(SupervisedAssessmentActivity.this).getSessionDao().insert(startSesion);

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
        if (nodeId.equalsIgnoreCase("1304")) {
            Intent intent = new Intent(SupervisedAssessmentActivity.this, ECEActivity.class);
            intent.putExtra("resId", "9962");
            intent.putExtra("crlId", supervisorId);
            startActivity(intent);
            finish();
        } else if (nodeId.equalsIgnoreCase("1302")) {
            Intent intent = new Intent(SupervisedAssessmentActivity.this, TestDisplayActivity.class);
            intent.putExtra("nodeId", nodeId);
            intent.putExtra("crlId", supervisorId);

            startActivity(intent);
            finish();
        } else {
//                        Intent intent = new Intent(ChooseAssessmentActivity.this, CRLActivity.class);
            Intent intent = new Intent(SupervisedAssessmentActivity.this, ScienceAssessmentActivity.class);
            intent.putExtra("nodeId", nodeId);
            intent.putExtra("crlId", supervisorId);

            startActivity(intent);
            finish();
        }
    }


    public void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        try {

            File direct = new File(Environment.getExternalStorageDirectory().toString() + "/.assessmentInternal");
            if (!direct.exists()) direct.mkdir();
            direct = new File(Environment.getExternalStorageDirectory().toString() + "/.assessmentInternal/supervisorImages");
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


}
