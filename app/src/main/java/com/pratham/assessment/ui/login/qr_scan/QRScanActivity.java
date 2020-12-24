package com.pratham.assessment.ui.login.qr_scan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.dao.StatusDao;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.ui.choose_assessment.ChooseAssessmentActivity_;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.UUID;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

@EActivity(R.layout.activity_qrscan)
public class QRScanActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    @ViewById(R.id.content_frame)
    ViewGroup content_frame;
    @ViewById(R.id.button_rl)
    RelativeLayout button_ll;
    @ViewById(R.id.tv_stud_one)
    TextView tv_stud_one;
    @ViewById(R.id.btn_start_game)
    Button btn_start_game;
    @ViewById(R.id.btn_reset_btn)
    Button btn_reset_btn;

    //    private AppDatabase appDatabase;
    // QRScanContract.QRScanPresenter presenter;
    int totalStudents = 0;
    String stdFirstName, stdId;
    Dialog dialog;
    Boolean setStud = false;
    public ZXingScannerView mScannerView;
    int crlCheck;

    @AfterViews
    public void init() {
        mScannerView = new ZXingScannerView(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //  presenter = new QRScanPresenter(this, this);
        mScannerView.setResultHandler(this);
        content_frame.addView((mScannerView));

        //  Log.d("tag", "SD Path: " + COS_Constants.ext_path);
        initCamera();
        /* 1) In case migration needed and no problem with data loss then this would work

        appDatabase =  Room.databaseBuilder(this,
        AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
         */


        /* 2) In case migration needed and want to keep data as it is then this would work
            but the case is
            SQLite supports radio_button_bg limited subset of ALTER TABLE.
             The ALTER TABLE command in SQLite allows the user to rename radio_button_bg table or to add radio_button_bg new column to an existing table.
             It is not possible to rename radio_button_bg column, remove radio_button_bg column, or add or remove constraints from radio_button_bg table.
         */
    }

    /*  @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_qrscan);
          mScannerView = new ZXingScannerView(this);
          ButterKnife.bind(this);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

          //  presenter = new QRScanPresenter(this, this);
          mScannerView.setResultHandler(this);
          content_frame.addView((mScannerView));

          //  Log.d("tag", "SD Path: " + COS_Constants.ext_path);
          initCamera();
          *//* 1) In case migration needed and no problem with data loss then this would work

        appDatabase =  Room.databaseBuilder(this,
        AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
         *//*


     *//* 2) In case migration needed and want to keep data as it is then this would work
            but the case is
            SQLite supports radio_button_bg limited subset of ALTER TABLE.
             The ALTER TABLE command in SQLite allows the user to rename radio_button_bg table or to add radio_button_bg new column to an existing table.
             It is not possible to rename radio_button_bg column, remove radio_button_bg column, or add or remove constraints from radio_button_bg table.
         *//*
    }
*/
    public void AnimateTextView(Context c, final TextView iv_logo) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.zoom_in_new);
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.zoom_out_new);
        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                iv_logo.setAnimation(anim_out);
            }
        });
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showButton();
            }
        });
        //(holder.mTextView).setAnimation(anim_in);
        iv_logo.setAnimation(anim_in);
    }

    public void showButtonsAnimation(Context c, final RelativeLayout button_ll) {
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.zoom_in);
        anim_in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
        //(holder.mTextView).setAnimation(anim_in);
        button_ll.setAnimation(anim_in);
    }

    public void hideButtonsAnimation(Context c, final RelativeLayout button_ll, final TextView iv_logo) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.full_zoom_out);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv_stud_one.setText("");
                button_ll.setVisibility(View.GONE);
                iv_logo.setVisibility(View.GONE);
            }
        });
        //(holder.mTextView).setAnimation(anim_in);
        button_ll.setAnimation(anim_out);
        iv_logo.setAnimation(anim_out);
    }


    private void showButton() {
        button_ll.setVisibility(View.VISIBLE);
        showButtonsAnimation(this, button_ll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mScannerView.stopCamera();
    }

    public void initCamera() {
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(this);
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

/*    @OnClick(R.id.btn_stats)
    public void showStudentsUsage() {
        mScannerView.stopCamera();
        //startActivity(new Intent(this, TabUsage.class));
    }*/

    @Click(R.id.btn_reset_btn)
    public void resetQrList() {
        stdFirstName = "";
        stdId = "";
        hideButtonsAnimation(this, button_ll, tv_stud_one);
        //   ButtonClickSound.start();
        scanNextQRCode();
    }

/*    @OnClick(R.id.iv_admin)
    public void gotoNext() {
        showCrlDialog();
    }*/

    @Click(R.id.btn_start_game)
    public void gotoGame() {
        mScannerView.stopCamera();
        enterStudentData(stdId, stdFirstName);
        startSession();
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");

//        Assessment_Constants.currentStudentID = stdId;
        FastSave.getInstance().saveString("currentStudentID", stdId);


        // ButtonClickSound.start();
        //Assessment_Constants.GROUP_LOGIN = false;
        //startActivity(new Intent(this, RCGameActivity.class));
        startActivity(new Intent(this, ChooseAssessmentActivity_.class));
        finish();
/*        Intent dataConfirmationIntent = new Intent(this, DataConfirmation.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("playerModalArrayList", playerModalList);
        dataConfirmationIntent.putExtras(bundle);
        startActivity(dataConfirmationIntent);*/
    }


    private String[] decodeStudentId(String text, String s) {
        return text.split(s);
    }

    public void scanNextQRCode() {
        if (mScannerView != null) {
            mScannerView.stopCamera();
            mScannerView.startCamera();
            mScannerView.resumeCameraPreview(this);
        }
    }


    public void dialogClick() {

        tv_stud_one.setVisibility(View.VISIBLE);
        tv_stud_one.setText("" + stdFirstName);
        AnimateTextView(this, tv_stud_one);
        scanNextQRCode();
    }

    @Override
    public void handleResult(Result result) {
        try {
            boolean dulicateQR = false;
            mScannerView.stopCamera();
            Log.d("RawResult:::", "****" + result.getText());

            JSONObject jsonobject = new JSONObject(result.getText());
            stdId = jsonobject.getString("stuId");
            stdFirstName = jsonobject.getString("name");

            dialogClick();

        } catch (Exception e) {
            Toast.makeText(this, R.string.invalid_qr_code, Toast.LENGTH_SHORT).show();
            scanNextQRCode();
            BackupDatabase.backup(this);
            e.printStackTrace();
        }

    }

    private void startSession() {
        new AsyncTask<Object, Void, Object>() {
            String currentSession;

            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    StatusDao statusDao = appDatabase.getStatusDao();
                    currentSession = "" + UUID.randomUUID().toString();

                    Assessment_Constants.currentSession = currentSession;
                    FastSave.getInstance().saveString("currentSession", currentSession);

                    statusDao.updateValue("CurrentSession", "" + currentSession);

                    String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");

                    Session startSesion = new Session();
                    startSesion.setSessionID("" + currentSession);
                    String timerTime = AssessmentApplication.getCurrentDateTime(false, AppStartDateTime);

                    Log.d("doInBackground", "--------------------------------------------doInBackground : " + timerTime);
                    startSesion.setFromDate(timerTime);
                    startSesion.setToDate("NA");
                    startSesion.setSentFlag(0);
                    appDatabase.getSessionDao().insert(startSesion);

                    Attendance attendance = new Attendance();
                    attendance.setSessionID("" + currentSession);
                    attendance.setDate(timerTime);
                    attendance.setStudentID("" + stdId);
                    attendance.setGroupID("QR");
                    appDatabase.getAttendanceDao().insert(attendance);

                    BackupDatabase.backup(QRScanActivity.this);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private void enterStudentData(final String stdId, final String stdFirstName) {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
//appDatabase=AppDatabase.getDatabaseInstance()
                   /* appDatabase = Room.databaseBuilder(QRScanActivity.this,
                            AppDatabase.class, AppDatabase.DB_NAME)
                            .build();*/

                    Student student = new Student();

                    student.setStudentID("" + stdId);
                    student.setFullName("" + stdFirstName);
                    student.setNewFlag(1);
                    student.setStudentUID("QR");
                    student.setIsniosstudent("0");
                    student.setDeviceId(Assessment_Utility.getDeviceId(QRScanActivity.this));
                    String studentName = appDatabase.getStudentDao().checkStudent("" + stdId);

                    if (studentName == null) {
                        appDatabase.getStudentDao().insert(student);
                    }

                    BackupDatabase.backup(QRScanActivity.this);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }
}


/*

<ImageView
                android:id="@+id/iv_admin"
                android:layout_width="95dp"
                android:layout_height="95dp"
                android:src="@drawable/ic_admin"
                android:scaleType="fitXY"
                android:layout_centerInParent="true"
                android:layout_alignParentTop="true"
                android:layout_marginVertical="10dp"
                android:gravity="center"
                android:textSize="20sp"
                android:textStyle="bold" />

            <ImageView
                android:id="@+id/btn_stats"
                android:layout_width="95dp"
                android:layout_height="95dp"
                android:src="@drawable/ic_stats"
                android:scaleType="fitXY"
                android:layout_centerInParent="true"
                android:layout_marginVertical="10dp"
                android:gravity="center"
                android:textSize="20sp"
                android:textStyle="bold" />

* */