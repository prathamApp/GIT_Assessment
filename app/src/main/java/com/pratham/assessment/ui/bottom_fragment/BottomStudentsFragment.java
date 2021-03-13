package com.pratham.assessment.ui.bottom_fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.gson.Gson;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.AvatarModal;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Status;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.interfaces.SplashInterface;
import com.pratham.assessment.services.AppExitService;
import com.pratham.assessment.services.LocationService;
import com.pratham.assessment.ui.bottom_fragment.add_student.AddStudentFragment;
import com.pratham.assessment.ui.bottom_fragment.add_student.EnrollmentNoFragment;
import com.pratham.assessment.ui.choose_assessment.ChooseAssessmentActivity_;
import com.pratham.assessment.ui.splash_activity.SplashActivity;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.pratham.assessment.ui.splash_activity.SplashPresenter.doInitialEntries;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/

@EFragment(R.layout.student_list_fragment)
public class BottomStudentsFragment extends BottomSheetDialogFragment implements StudentClickListener, SplashInterface {


    @ViewById(R.id.students_recyclerView)
    RecyclerView rl_students;
    @ViewById(R.id.add_student)
    Button add_student;
   /* @BindView(R.id.btn_download_all_data)
    Button btn_download_all_data;*/

    private ArrayList avatarList = new ArrayList();
    private List<Student> studentDBList, studentList;
    StudentsAdapter adapter;
    Gson gson;

    @AfterViews
    public void init() {
        try {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            addAvatarsInList();
            studentList = new ArrayList<>();
            Status status = new Status();
            setAppVersion(status);
            gson = new Gson();
         /*   adapter = new StudentsAdapter(getActivity(), this, studentList, avatarList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            rl_students.setLayoutManager(mLayoutManager);
            rl_students.setAdapter(adapter);
*/
//        btn_download_all_data.setVisibility(View.GONE);

      /*  if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork())
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                btn_download_all_data.setVisibility(View.VISIBLE);
            }*/
            showStudents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


  /*  @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_list_fragment, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        addAvatarsInList();
        studentList = new ArrayList<>();
        gson = new Gson();
        adapter = new StudentsAdapter(getActivity(), this, studentList, avatarList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rl_students.setLayoutManager(mLayoutManager);
        rl_students.setAdapter(adapter);

//        btn_download_all_data.setVisibility(View.GONE);

      *//*  if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork())
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                btn_download_all_data.setVisibility(View.VISIBLE);
            }*//*
        showStudents();
    }*/

    private void setStudentsToRecycler() {
        adapter = new StudentsAdapter(getActivity(), this, studentList, avatarList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        if (rl_students != null) {
            rl_students.setLayoutManager(mLayoutManager);
            rl_students.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void addAvatarsInList() {
      /*  for (int i = 0; i < 6; i++) {
            AvatarModal avatarModal = new AvatarModal();
            avatarModal.setAvatarName(""+Assessment_Utility.getRandomAvatar(getActivity()));
            avatarModal.setClickFlag(false);
            avatarList.add(avatarModal);
        }*/
        AvatarModal avatarModal = new AvatarModal();
        avatarModal.setAvatarName("g1.png");
        avatarModal.setClickFlag(false);
        avatarList.add(avatarModal);
        AvatarModal avatarModal1 = new AvatarModal();

        avatarModal1.setAvatarName("b1.png");
        avatarModal1.setClickFlag(false);
        avatarList.add(avatarModal1);
        AvatarModal avatarModal2 = new AvatarModal();

        avatarModal2.setAvatarName("g2.png");
        avatarModal2.setClickFlag(false);
        avatarList.add(avatarModal2);
        AvatarModal avatarModal3 = new AvatarModal();

        avatarModal3.setAvatarName("b2.png");
        avatarModal3.setClickFlag(false);
        avatarList.add(avatarModal3);
        AvatarModal avatarModal4 = new AvatarModal();

        avatarModal4.setAvatarName("g3.png");
        avatarModal4.setClickFlag(false);
        avatarList.add(avatarModal4);
        AvatarModal avatarModal5 = new AvatarModal();

        avatarModal5.setAvatarName("b3.png");
        avatarModal5.setClickFlag(false);
        avatarList.add(avatarModal5);

        Collections.shuffle(avatarList);

    }


    @Override
    public void onPause() {
        super.onPause();
        SplashActivity.fragmentBottomPauseFlg = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            getActivity().onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ProgressDialog progress;

  /*  @OnClick(R.id.btn_download_all_data)
    public void onBtnDownload() {
        if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                setProgressDailog();
                new DownloadData().doInBackground();
            } else {
                Toast.makeText(getActivity(), "Connect to Kolibri", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

  /*  @SuppressLint("InvalidWakeLockTag")
    private void setProgressDailog() {
        progress = new Dialog(getActivity());
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setContentView(R.layout.dialog_file_downloading);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
//        PowerManager pm = (PowerManager) AssessmentApplication.getInstance().getSystemService(AssessmentApplication.getInstance().POWER_SERVICE);
//        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNjfdhotDimScreen");
        progressLayout = progress.findViewById(R.id.dialog_progressLayout);
        roundProgress = progress.findViewById(R.id.dialog_roundProgress);
        dialog_file_name = progress.findViewById(R.id.dialog_file_name);
        iv_file_trans = progress.findViewById(R.id.iv_file_trans);
        Glide.with(this).load(R.drawable.splash_group).into(iv_file_trans);
        dialog_file_name.setText("Downloading please wait");
        progressLayout.setCurProgress(0);
        progress.show();
    }
*/
   /* @Subscribe
    public void messageRecieved(EventMessage message) {
        if (message != null) {
            if (message.getMessage().equalsIgnoreCase(Assessment_Constants.DATA_FILE_PROGRESS)) {
                if (progress != null)
                    progressLayout.setCurProgress((int) message.getProgress());
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.UNZIPPING_DATA_FILE)) {
                if (progress != null) {
                    dialog_file_name.setText("Unzipping please wait..");
*//*                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            roundProgress.setVisibility(View.VISIBLE);
                            progressLayout.setVisibility(View.GONE);
                        }
                    });*//*
                }
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.DATA_ZIP_COMPLETE)) {
//                wl.release();
                Log.d("pushorassign", "Zipping Completed.. ");
                if (progress != null)
                    dialog_file_name.setText("Zipping Completed..");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog_file_name.setText("Loading Data..");
                        Log.d("pushorassign", "Loading Data..");
                        populateDB();
                    }
                }, 1000);
            } else if (message.getMessage().equalsIgnoreCase(Assessment_Constants.DATA_DOWNLOAD_ERROR)) {
//                wl.release();
                if (progress != null)
                    dialog_file_name.setText("Download Error..");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                }, 1000);
            }
        }
    }*/

    @SuppressLint("StaticFieldLeak")
    private void populateDB() {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (!FastSave.getInstance().getBoolean(Assessment_Constants.INITIAL_ENTRIES, false))
                        doInitialEntries(getActivity());
//                    if (!FastSave.getInstance().getBoolean(Assessment_Constants.KEY_MENU_COPIED, false))
                    populateMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (progress != null)
                            progress.dismiss();
                    }
                }, 1000);
            }
        }.execute();
    }

    public void populateMenu() {
        try {
            File folder_file, db_file;
            folder_file = new File(/*AssessmentApplication.pradigiPath*/"" + "/.Assessment/English/");
            if (folder_file.exists()) {
                Log.d("-CT-", "doInBackground AssessmentApplication.contentSDPath: " + AssessmentApplication.contentSDPath);
                db_file = new File(folder_file + "/" + AppDatabase.DB_NAME);
//                    db_file = new File(folder_file.getAbsolutePath() + "/" + AppDatabase.DB_NAME);
                if (db_file.exists()) {
                    SQLiteDatabase db = SQLiteDatabase.openDatabase(db_file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                    if (db != null) {
                        Cursor content_cursor;
                        try {
                            content_cursor = db.rawQuery("SELECT * FROM ContentTable", null);
                            //populate contents
                            List<ContentTable> contents = new ArrayList<>();
                            if (content_cursor.moveToFirst()) {
                                while (!content_cursor.isAfterLast()) {
                                    ContentTable detail = new ContentTable();
                                    detail.setNodeId(content_cursor.getString(content_cursor.getColumnIndex("nodeId")));
                                    detail.setNodeType(content_cursor.getString(content_cursor.getColumnIndex("nodeType")));
                                    detail.setNodeTitle(content_cursor.getString(content_cursor.getColumnIndex("nodeTitle")));
                                    detail.setNodeKeywords(content_cursor.getString(content_cursor.getColumnIndex("nodeKeywords")));
                                    detail.setNodeAge(content_cursor.getString(content_cursor.getColumnIndex("nodeAge")));
                                    detail.setNodeDesc(content_cursor.getString(content_cursor.getColumnIndex("nodeDesc")));
                                    detail.setNodeServerImage(content_cursor.getString(content_cursor.getColumnIndex("nodeServerImage")));
                                    detail.setNodeImage(content_cursor.getString(content_cursor.getColumnIndex("nodeImage")));
                                    detail.setResourceId(content_cursor.getString(content_cursor.getColumnIndex("resourceId")));
                                    detail.setResourceType(content_cursor.getString(content_cursor.getColumnIndex("resourceType")));
                                    detail.setResourcePath(content_cursor.getString(content_cursor.getColumnIndex("resourcePath")));
                                    detail.setLevel("" + content_cursor.getInt(content_cursor.getColumnIndex("level")));
                                    detail.setContentLanguage(content_cursor.getString(content_cursor.getColumnIndex("contentLanguage")));
                                    detail.setParentId(content_cursor.getString(content_cursor.getColumnIndex("parentId")));
                                    detail.setContentType(content_cursor.getString(content_cursor.getColumnIndex("contentType")));
                                    detail.setIsDownloaded("true");
                                    detail.setOnSDCard(false);
                                    contents.add(detail);
                                    content_cursor.moveToNext();
                                }
                            }
                            AppDatabase.appDatabase.getContentTableDao().addContentList(contents);
                            content_cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            getActivity().startService(new Intent(getActivity(), AppExitService.class));
            BackupDatabase.backup(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void doInitialEntries(AppDatabase appDatabase) {
        try {
            Status status;
            status = new Status();

            String key = "DeviceId",
                    value = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            setStatusTableEntries(status, key, value);
           *//* if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey("DeviceId") != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey("DeviceId").equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status.setStatusKey(key);
                status.setValue("" + value);
                status.setDescription("" + Build.SERIAL);
                appDatabase.getStatusDao().insert(status);
            }*//*

            key = "CRLID";
            value = "default";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }

*//*
            key = "DeviceName";
            value = Assessment_Utility.getDeviceName();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*
              *//*  status = new Status();
                status.setStatusKey("DeviceName");
                status.setValue(Assessment_Utility.getDeviceName());
                appDatabase.getStatusDao().insert(status);
*//*

            key = "gpsFixDuration";
            value = "";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }

*//*

            key = "prathamCode";
            value = "";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }

*//*




          *//*  status = new Status();
            status.setStatusKey("gpsFixDuration");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("prathamCode");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);
*//*
            key = "apkType";
            if (AssessmentApplication.isTablet)
                value = "Tablet";
            else value = "Smart phone";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }

*//*

         *//*   status = new Status();
            status.setStatusKey("apkType");
            if (AssessmentApplication.isTablet)
                status.setValue("Tablet");
            else status.setValue("Smart phone");

            appDatabase.getStatusDao().insert(status);
*//*
            key = "Latitude";
            value = "";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }*//*
            key = "Longitude";
            value = "";
            setStatusTableEntries(status, key, value);

         *//*   if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }*//*


            key = "GPSDateTime";
            value = "";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "CurrentSession";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }

*//*


            key = "SdCardPath";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "AppLang";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "AppStartDateTime";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "ActivatedForGroups";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "programId";
            value = "1";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "group1";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*
            key = "group2";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*
            key = "group3";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*


            key = "group4";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*
            key = "group5";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }

*//*

       *//*     status = new Status();
            status.setStatusKey("Latitude");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("Longitude");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("GPSDateTime");
            status.setValue("");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("CurrentSession");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("SdCardPath");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AppLang");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AppStartDateTime");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            //new Entries
            status = new Status();
            status.setStatusKey("ActivatedForGroups");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("programId");
            status.setValue("1");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group1");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group2");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group3");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group4");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("group5");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);
*//*

            key = "village";
            value = "NA";
            setStatusTableEntries(status, key, value);
            *//*if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "ActivatedDate";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "AssessmentSession";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "AndroidID";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "DBVersion";
            value = "NA";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "SerialID";
            value = Assessment_Utility.getDeviceSerialID();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "OsVersionName";
            value = Assessment_Utility.getOSVersion();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "OsVersionNum";
            value = Assessment_Utility.getOSVersionNo();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }

*//*

        *//*    status = new Status();
            status.setStatusKey("village");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("ActivatedDate");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AssessmentSession");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("AndroidID");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("DBVersion");
            status.setValue("NA");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("SerialID");
            status.setValue(Assessment_Utility.getDeviceSerialID());
            appDatabase.getStatusDao().insert(status);


            status = new Status();
            status.setStatusKey("OsVersionName");
            status.setValue(Assessment_Utility.getOSVersion());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("OsVersionNum");
            status.setValue(Assessment_Utility.getOSVersionNo() + "");
            appDatabase.getStatusDao().insert(status);
*//*

            key = "AvailableStorage";
            value = Assessment_Utility.getAvailableStorage();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*
            key = "ScreenResolution";
            value = Assessment_Utility.getScreenResolution((AppCompatActivity) getActivity());
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "Manufacturer";
            value = Assessment_Utility.getManufacturer();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "Model";
            value = Assessment_Utility.getModel();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "ApiLevel";
            value = Assessment_Utility.getApiLevel() + "";
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            key = "InternalStorageSize";
            value = Assessment_Utility.getInternalStorageSize();
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            key = "wifiMAC";
            value = macAddress;
            setStatusTableEntries(status, key, value);
*//*
            if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                appDatabase.getStatusDao().insert(status);
            }
*//*

*//*
            status = new Status();
            status.setStatusKey("AvailableStorage");
            status.setValue(Assessment_Utility.getAvailableStorage());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("ScreenResolution");
            status.setValue(Assessment_Utility.getScreenResolution((AppCompatActivity) getActivity()));
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("Manufacturer");
            status.setValue(Assessment_Utility.getManufacturer());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("Model");
            status.setValue(Assessment_Utility.getModel());
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("ApiLevel");
            status.setValue(Assessment_Utility.getApiLevel() + "");
            appDatabase.getStatusDao().insert(status);

            status = new Status();
            status.setStatusKey("InternalStorageSize");
            status.setValue(Assessment_Utility.getInternalStorageSize() + "");
            appDatabase.getStatusDao().insert(status);

            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            status.setStatusKey("wifiMAC");
            status.setValue(macAddress);
            appDatabase.getStatusDao().insert(status);*//*

            setAppName(status);
            setAppVersion(status);

            addStartTime(status);
//            getSdCardPath();
            requestLocation();

            FastSave.getInstance().saveBoolean(Assessment_Constants.INITIAL_ENTRIES, true);
            BackupDatabase.backup(getActivity());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private void setAppName(Status status) {
        String appname = "";
        CharSequence c;
        ActivityManager am = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = getActivity().getPackageManager();
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
        try {
            c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
            appname = c.toString();
            Log.w("LABEL", c.toString());
        } catch (Exception e) {
        }


        String key = "appName";
        String value = appname;
        setStatusTableEntries(status, key, value);
      /*  if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                    && !AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
            } else {
                status = new Status();
                status.setStatusKey(key);
                status.setValue(value);
                AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().insert(status);
            }*/

           /* status = new Status();
            status.setStatusKey("appName");
            status.setValue(appname);
            AppDatabase.appDatabase.getStatusDao().insert(status);*/

   /* } else

    {

        String key = "appName";
        String value = appname;
        if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null
                && AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key).equalsIgnoreCase("")) {
            AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
        } else {
            status = new Status();
            status.setStatusKey("appName");
            status.setValue(appname);
            AppDatabase.appDatabase.getStatusDao().insert(status);
        }
    }*/

    }

    private void setStatusTableEntries(Status status, String key, String value) {

        if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey(key) != null) {
            AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue(key, value);
        } else {
            status.setStatusKey(key);
            status.setValue(value);
            AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().insert(status);
        }

    }

    private void requestLocation() {
        new LocationService(getActivity()).checkLocation();
    }

    private void setAppVersion(Status status) {
        PackageInfo pInfo = null;
        String verCode = "";
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            verCode = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setStatusTableEntries(status, "apkVersion", verCode);

     /*   if (AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey("apkVersion") == null) {
            status = new Status();

            status.setStatusKey("apkVersion");

            status.setValue(verCode);
            AppDatabase.appDatabase.getStatusDao().insert(status);

        } else {
            status.setStatusKey("apkVersion");

            PackageInfo pInfo = null;
            String verCode = "";
            try {
                pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                verCode = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            status.setValue(verCode);
            AppDatabase.appDatabase.getStatusDao()
                    .updateValue(AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getKey("apkVersion"), verCode);

        }*/
    }

    private void addStartTime(Status status) {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    String appStartTime = AssessmentApplication.getCurrentDateTime();
//                    StatusDao statusDao = AppDatabase.appDatabase.getStatusDao();
                    setStatusTableEntries(status, "AppStartDateTime", appStartTime);
                    BackupDatabase.backup(getActivity());
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    private void showStudents() {
        try {

            new AsyncTask<Void, Integer, Void>() {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Loading... Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                @Override
                protected Void doInBackground(Void... voids) {

                    studentList.clear();
                    studentDBList = AppDatabase.getDatabaseInstance(getActivity()).getStudentDao().getAllStudents();
                    if (studentDBList != null) {
                        for (int i = 0; i < studentDBList.size(); i++) {
                            Student studentAvatar = new Student();
                            studentAvatar.setStudentID(studentDBList.get(i).getStudentID());
                            studentAvatar.setFullName(studentDBList.get(i).getFullName());
                            studentAvatar.setGroupId(studentDBList.get(i).getGroupId());
                            studentAvatar.setStudentUID(studentDBList.get(i).getStudentUID());
                            studentAvatar.setIsniosstudent(studentDBList.get(i).getIsniosstudent());
                            if (studentDBList.get(i).getAvatarName() != null)
                                studentAvatar.setAvatarName(studentDBList.get(i).getAvatarName());
                            else
                                studentAvatar.setAvatarName("" + Assessment_Utility.getRandomAvatarName(getActivity()));
                            studentList.add(studentAvatar);
                        }
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    setStudentsToRecycler();
                    BackupDatabase.backup(getActivity());
                }

            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.add_student)
    public void addStudent() {
        SplashActivity.fragmentAddStudentOpenFlg = true;
        AddStudentFragment addStudentFragment = AddStudentFragment.newInstance(this);
        addStudentFragment.show(getActivity().getSupportFragmentManager(), AddStudentFragment.class.getSimpleName());

    }

    @Click(R.id.enter_enrollment_no)
    public void addStudentByEnrollmentNo() {
        EnrollmentNoFragment enrollmentNoFragment = EnrollmentNoFragment.newInstance(this);
        enrollmentNoFragment.show(getActivity().getSupportFragmentManager(), EnrollmentNoFragment.class.getSimpleName());

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("BottomSheetCancel", "onCancel: aaaaaaaaaaaaaaaaaaaa");
    }

    @Override
    public void onResume() {
        super.onResume();
        SplashActivity.fragmentBottomPauseFlg = false;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void messageReceived(String msg) {
        if (msg.equalsIgnoreCase("reload"))
            showStudents();
    }
/*
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
*/

    public ProgressDialog progressDialog;

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getActivity().getString(R.string.loading_please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        try {
            if (progressDialog != null)
                progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStudentClick(final String studentName, final String studentId, String
            groupId, String isniosstudent) {

        new AsyncTask<Object, Void, Object>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    String currentSession = "" + UUID.randomUUID().toString();
                    AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().updateValue("CurrentSession", "" + currentSession);

                    Attendance attendance = new Attendance();
                    attendance.setSessionID(currentSession);
                    attendance.setStudentID(studentId);
                    attendance.setDate(AssessmentApplication.getCurrentDateTime());
                    if (groupId != null && !groupId.equalsIgnoreCase(""))
                        attendance.setGroupID(groupId);
                    else attendance.setGroupID("PS");

                    attendance.setSentFlag(0);
                    Assessment_Constants.currentStudentID = studentId;
                    FastSave.getInstance().saveString("currentStudentID", studentId);

                    Assessment_Constants.currentStudentName = studentName;
                    FastSave.getInstance().saveString("currentStudentName", studentName);

                    AppDatabase.getDatabaseInstance(getActivity()).getAttendanceDao().insert(attendance);
                    Assessment_Constants.currentSession = currentSession;
                    FastSave.getInstance().saveString("currentSession", currentSession);


                    Session startSesion = new Session();
                    startSesion.setSessionID("" + currentSession);
                    startSesion.setFromDate("" + AssessmentApplication.getCurrentDateTime());
                    startSesion.setToDate("NA");
                    startSesion.setSentFlag(0);
                    AppDatabase.getDatabaseInstance(getActivity()).getSessionDao().insert(startSesion);
                    if (isniosstudent != null && !isniosstudent.equalsIgnoreCase(""))
                        if (isniosstudent.equalsIgnoreCase("1"))
                            FastSave.getInstance().saveBoolean("enrollmentNoLogin", true);
                        else FastSave.getInstance().saveBoolean("enrollmentNoLogin", false);

//                    getStudentData(Assessment_Constants.STUDENT_PROGRESS_INTERNET, Assessment_Constants.STUDENT_PROGRESS_API, Assessment_Constants.currentStudentID);

                  /*  try {
                        if (bgMusic != null && bgMusic.isPlaying()) {
                            bgMusic.stop();
                            bgMusic.setLooping(false);
                            bgMusic.release();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dismissProgressDialog();
                BackupDatabase.backup(getActivity());
//                Assessment_Constants.ASSESSMENT_TYPE = "";
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), ChooseAssessmentActivity_.class));
                    getActivity().finish();
                }

//                startActivity(new Intent(getActivity(), ChooseLevelActivity.class));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /* private void getStudentData(final String requestType, String url, String studentID) {
         try {
             String url_id;
             url_id = url + studentID;
             AndroidNetworking.get(url_id)
                     .addHeaders("Content-Type", "application/json")
                     .build()
                     .getAsString(new StringRequestListener() {
                         @Override
                         public void onResponse(String response) {
                             receivedContent(requestType, response);
                         }

                         @Override
                         public void onError(ANError anError) {
                             try {
                                 Log.d("Error:", anError.getErrorDetail());
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                             receivedError();
                         }
                     });
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 */
    private void receivedError() {
//        startActivity(new Intent(getActivity(), HomeActivity.class));
//        getActivity().finish();
    }

    /*@SuppressLint("StaticFieldLeak")
    private void receivedContent(String requestType, String response) {
        if (requestType.equalsIgnoreCase(Assessment_Constants.STUDENT_PROGRESS_INTERNET)) {
//            new AsyncTask<Object, Void, Object>() {
//                @Override
//                protected Object doInBackground(Object[] objects) {
            try {
                Type listType = new TypeToken<ArrayList<ContentProgress>>() {
                }.getType();
                List<ContentProgress> contentProgressList = gson.fromJson(response, listType);
                if (contentProgressList != null && contentProgressList.size() > 0) {
                    for (int i = 0; i < contentProgressList.size(); i++) {
                        contentProgressList.get(i).setSentFlag(1);
                        contentProgressList.get(i).setLabel("" + Assessment_Constants.RESOURCE_PROGRESS);
                    }
                    AppDatabase.appDatabase.getContentProgressDao().addContentProgressList(contentProgressList);
                    BackupDatabase.backup(getActivity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Object o) {
//                    super.onPostExecute(o);
            getStudentData(Assessment_Constants.LEARNT_WORDS_INTERNET, Assessment_Constants.LEARNT_WORDS_API, Assessment_Constants.currentStudentID);
//                }
//            }.execute();
        } else if (requestType.equalsIgnoreCase(Assessment_Constants.LEARNT_WORDS_INTERNET)) {
//            new AsyncTask<Object, Void, Object>() {
//                @Override
//                protected Object doInBackground(Object[] objects) {
//
            try {
                Type listType = new TypeToken<ArrayList<LearntWords>>() {
                }.getType();
                List<LearntWords> learntWordsList = gson.fromJson(response, listType);
                if (learntWordsList != null && learntWordsList.size() > 0) {
                    for (int i = 0; i < learntWordsList.size(); i++) {
                        learntWordsList.get(i).setSentFlag(1);
                        learntWordsList.get(i).setWord("" + learntWordsList.get(i).getWord().toLowerCase());
                        if (!checkWord(learntWordsList.get(i).getStudentId(), learntWordsList.get(i).getWordUUId(), learntWordsList.get(i).getWord(), learntWordsList.get(i).getWordType()))
                            AppDatabase.appDatabase.getLearntWordDao().insert(learntWordsList.get(i));
                    }
                    BackupDatabase.backup(getActivity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Object o) {
//                    super.onPostExecute(o);
//                    startActivity(new Intent(getActivity(), HomeActivity.class));
//                    getActivity().finish();
//                }
//            }.execute();
        }
    }
*/
   /* private boolean checkWord(String studentId, String wordUUId, String wordCheck, String wordType) {
        try {
            String word = AppDatabase.appDatabase.getLearntWordDao().checkLearntData(studentId, "" + wordUUId, wordCheck.toLowerCase(), wordType);
            if (word != null)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
*/
    @Override
    public void onChildAdded() {
        showStudents();
    }

    /*public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + avatar) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + avatar) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
*/

}
