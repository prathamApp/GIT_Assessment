package com.pratham.assessment.ui.choose_assessment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.service.textservice.SpellCheckerService;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.async.PushDataToServer;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Status;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.ui.choose_assessment.fragments.LanguageFragment_;
import com.pratham.assessment.ui.choose_assessment.fragments.TopicFragment;
import com.pratham.assessment.ui.choose_assessment.fragments.TopicFragment_;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity_;
import com.pratham.assessment.ui.choose_assessment.science.certificate.AssessmentCertificateActivity;
import com.pratham.assessment.ui.splash_activity.SplashActivity_;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.FileUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.pratham.assessment.utilities.Assessment_Constants.EXAMID;
import static com.pratham.assessment.utilities.Assessment_Constants.LANGUAGE;
import static com.pratham.assessment.utilities.Assessment_Constants.PUSH_DATA_FROM_DRAWER;
import static com.pratham.assessment.utilities.Assessment_Constants.VIDEOMONITORING;
import static com.pratham.assessment.utilities.Assessment_Utility.dpToPx;

@EActivity(R.layout.activity_choose_assessment)
public class ChooseAssessmentActivity extends BaseActivity implements
        ChoseAssessmentClicked, ChooseAssessmentContract.ChooseAssessmentView {
    @Bean(ChooseAssessmentPresenter.class)
    ChooseAssessmentContract.ChooseAssessmentPresenter presenter;

    @ViewById(R.id.rl_Profile)
    RelativeLayout rl_Profile;
    @ViewById(R.id.btn_Profile)
    ImageButton btn_Profile;
    @ViewById(R.id.spinner_choose_lang)
    Spinner spinner_choose_lang;
    @ViewById(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @ViewById(R.id.navigation)
    NavigationView navigation;
    @ViewById(R.id.rl_choose_sub)
    public RelativeLayout rlSubject;
    @ViewById(R.id.nav_frame_layout)
    public FrameLayout frameLayout;
    @ViewById(R.id.tv_choose_assessment)
    TextView tv_choose_assessment;
    @ViewById(R.id.menu_icon)
    ImageButton menu_icon;

    /* @ViewById(R.id.toggle_btn)
     public SwipeableButton toggle_btn;*/
    Context context;
    private RecyclerView recyclerView;
    List<AssessmentSubjects> contentTableList;
    ChooseAssessmentAdapter chooseAssessAdapter;
    ECELoginDialog eceLoginDialog;
    Crl loggedCrl;
    boolean videoMonitoring = false;
    @Bean(PushDataToServer.class)
    PushDataToServer pushDataToServer;

    @AfterViews
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        try {
            Bundle bundle = new Bundle();
            bundle = this.getIntent().getExtras();
            String studId = String.valueOf(bundle.getString("studentId"));
            String appName = String.valueOf(bundle.getString("appName"));
            String studName = String.valueOf(bundle.getString("studentName"));
            String subject = String.valueOf(bundle.getString("subjectName"));
            String language = String.valueOf(bundle.getString("subjectLanguage"));
            String subLevel = String.valueOf(bundle.getString("subjectLevel"));
       /*     Toast.makeText(this, "Id : " + studId + "\nAppName : "
                    + appName + "\nStudentName : " + studName + "\nSubject : " + subject + "\nLanguage : " + language
                    + "\nLevel : " + subLevel, Toast.LENGTH_LONG).show();
       */
            String langCode = "1";
            if (!language.equalsIgnoreCase("")) {
                switch (language.toLowerCase()) {
                    case "english":
                        langCode = "1";
                        break;
                    case "hindi":
                        langCode = "2";
                        break;
                    case "marathi":
                        langCode = "3";
                        break;
                    case "gujarati":
                        langCode = "7";
                        break;
                    case "kannada":
                        langCode = "8";
                        break;
                    case "assamese":
                        langCode = "9";
                        break;
                    case "bengali":
                        langCode = "10";
                        break;
                    case "odia":
                        langCode = "12";
                        break;
                    case "telugu":
                        langCode = "14";
                        break;
                    default:
                        langCode = "1";
                }
            }
            Assessment_Constants.SELECTED_LANGUAGE = langCode;
            FastSave.getInstance().saveString("language", langCode);
            if (!subject.equalsIgnoreCase("")) {
                Assessment_Constants.SELECTED_SUBJECT_ID = subject;
                FastSave.getInstance().saveString("SELECTED_SUBJECT_ID", subject);
            }
            if (!studId.equalsIgnoreCase("")) {
                Assessment_Constants.currentStudentID = studId;
                FastSave.getInstance().saveString("currentStudentID", studId);
                createDataBase();
            }
            if (!studName.equalsIgnoreCase("")) {
                Assessment_Constants.currentStudentName = studName;
                FastSave.getInstance().saveString("currentStudentName", studName);
            }

        } catch (Exception e) {
            Log.d("Exception@@@", e.getMessage());
            e.printStackTrace();
        }

//        setLocale(this, "langCode");

        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        String studentName = AppDatabase.getDatabaseInstance(this).getStudentDao().getFullName(Assessment_Constants.currentStudentID);
        String studentName = AppDatabase.getDatabaseInstance(this).getStudentDao().getFullName(currentStudentID);
        View view = navigation.getHeaderView(0);
        TextView name = view.findViewById(R.id.userName);
        name.setText(Html.fromHtml(studentName));

        Assessment_Constants.SELECTED_LANGUAGE = FastSave.getInstance().getString(LANGUAGE, "1");

        Menu menu = navigation.getMenu();
//        MenuItem nav_video = menu.findItem(R.id.menu_video_monitoring);
        MenuItem nav_push = menu.findItem(R.id.menu_push_data);
        if (!AssessmentApplication.isTablet) {
//            nav_video.setVisible(true);
            nav_push.setVisible(true);
        } else {
//            nav_video.setVisible(false);
            nav_push.setVisible(false);
        }

        MenuItem nav_certificate = menu.findItem(R.id.menu_certificate);
        if (FastSave.getInstance().getBoolean("enrollmentNoLogin", false)) {
            nav_certificate.setVisible(false);
        } else nav_certificate.setVisible(true);


       /* if (!AssessmentApplication.isTablet) {
            if (Assessment_Constants.VIDEOMONITORING) {
                videoMonitoring = true;
                nav_video.setTitle("Video monitoring(ON)");
                Toast.makeText(ChooseAssessmentActivity.this, "Video monitoring : ON", Toast.LENGTH_SHORT).show();

            } else {
                videoMonitoring = false;
                nav_video.setTitle("Video monitoring(OFF)");
                Toast.makeText(ChooseAssessmentActivity.this, "Video monitoring : OFF", Toast.LENGTH_SHORT).show();
            }
        }*/


      /*  toggle_btn.setOnSwipedOnListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Assessment_Constants.ASSESSMENT_TYPE = "supervised";
                Assessment_Constants.supervisedAssessment = true;
//                showSupervisionDialog();
                return null;
            }
        });

        toggle_btn.setOnSwipedOffListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Assessment_Constants.ASSESSMENT_TYPE = "practice";
                Assessment_Constants.supervisedAssessment = false;

                return null;
            }
        });*/

        eceLoginDialog = new ECELoginDialog(this);


//        presenter = new ChooseAssessmentPresenter(ChooseAssessmentActivity.this);
        contentTableList = new ArrayList<>();


        recyclerView = findViewById(R.id.choose_subject_recycler);
        chooseAssessAdapter = new ChooseAssessmentAdapter(this, contentTableList, this);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseAssessAdapter);

        presenter.copyListData();


        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_Subject:
                        menu_icon.setImageDrawable(getDrawable(R.drawable.ic_menu));
                        rlSubject.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        tv_choose_assessment.setText("Choose subject");
//                        toggle_btn.setVisibility(View.VISIBLE);
                        clearContentList();
                        presenter.copyListData();

                        break;

                    case R.id.menu_certificate:
                        startActivity(new Intent(ChooseAssessmentActivity.this, AssessmentCertificateActivity.class));
                        break;

                    case R.id.menu_language:
//                        toggle_btn.setVisibility(View.GONE);
                        menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));

                      /*  if (toggle_btn.getVisibility() != View.VISIBLE)
                            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
                        else
                            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
*/
                        rlSubject.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        Assessment_Utility.showFragment(ChooseAssessmentActivity.this, new LanguageFragment_(), R.id.nav_frame_layout,
                                null, LanguageFragment_.class.getSimpleName());
                        break;
                    case R.id.menu_download_offline_language:
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setComponent(new ComponentName("com.google.android.googlequicksearchbox",
                                "com.google.android.voicesearch.greco3.languagepack.InstallActivity"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
/*                    case R.id.menu_video_monitoring:
                        Menu menu = navigation.getMenu();
                        MenuItem nav_video = menu.findItem(R.id.menu_video_monitoring);
                        if (!videoMonitoring) {
                            videoMonitoring = true;
                            Assessment_Constants.VIDEOMONITORING = true;
                            nav_video.setTitle("Video monitoring(ON)");
                            Toast.makeText(ChooseAssessmentActivity.this, "Video monitoring : ON", Toast.LENGTH_SHORT).show();
                        } else {
                            videoMonitoring = false;
                            Assessment_Constants.VIDEOMONITORING = false;
                            nav_video.setTitle("Video monitoring(OFF)");
                            Toast.makeText(ChooseAssessmentActivity.this, "Video monitoring : OFF", Toast.LENGTH_SHORT).show();

                        }
                        break;*/
                    case R.id.menu_push_data:
                        PUSH_DATA_FROM_DRAWER = true;
                        pushDataToServer.setValue(ChooseAssessmentActivity.this, false);
                        pushDataToServer.doInBackground();
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });


    }


    /*public void setLocale(Activity context, String langCode) {
        Locale locale = new Locale("mr");
        Locale.setDefault(locale);
        // Create a new configuration object
        Configuration config = new Configuration();
        // Set the locale of the new configuration
        config.locale = locale;
        // Update the configuration of the Accplication context
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics());
    }*/



  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_assessment);
        ButterKnife.bind(this);
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        String studentName = AppDatabase.getDatabaseInstance(this).getStudentDao().getFullName(Assessment_Constants.currentStudentID);
        String studentName = AppDatabase.getDatabaseInstance(this).getStudentDao().getFullName(currentStudentID);
        View view = navigation.getHeaderView(0);
        TextView name = view.findViewById(R.id.userName);
        name.setText(studentName);

        Assessment_Constants.SELECTED_LANGUAGE = FastSave.getInstance().getString(LANGUAGE, "1");


        toggle_btn.setOnSwipedOnListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Assessment_Constants.ASSESSMENT_TYPE = "supervised";
//                showSupervisionDialog();
                return null;
            }
        });

        toggle_btn.setOnSwipedOffListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Assessment_Constants.ASSESSMENT_TYPE = "practice";
                return null;
            }
        });

        eceLoginDialog = new ECELoginDialog(this);


        presenter = new ChooseAssessmentPresenter(ChooseAssessmentActivity.this);
        contentTableList = new ArrayList<>();


        recyclerView = findViewById(R.id.choose_subject_recycler);
        chooseAssessAdapter = new ChooseAssessmentAdapter(this, contentTableList, this);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseAssessAdapter);

        presenter.copyListData();


        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_Subject:
                        menu_icon.setImageDrawable(getDrawable(R.drawable.ic_menu));
                        rlSubject.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        tv_choose_assessment.setText("Choose subject");
                        toggle_btn.setVisibility(View.VISIBLE);
                        clearContentList();
                        presenter.copyListData();

                        break;

                    case R.id.menu_certificate:
                        startActivity(new Intent(ChooseAssessmentActivity.this, AssessmentCertificateActivity.class));
                        break;

                    case R.id.menu_language:
                        toggle_btn.setVisibility(View.GONE);

                        if (toggle_btn.getVisibility() != View.VISIBLE)
                            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
                        else
                            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));

                        rlSubject.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        Assessment_Utility.showFragment(ChooseAssessmentActivity.this, new LanguageFragment(), R.id.nav_frame_layout,
                                null, LanguageFragment.class.getSimpleName());
                        break;
                    case R.id.menu_download_offline_language:
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setComponent(new ComponentName("com.google.android.googlequicksearchbox",
                                "com.google.android.voicesearch.greco3.languagepack.InstallActivity"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });


    }*/


    private void showSupervisionDialog() {
        loggedCrl = null;
       /* if (Assessment_Constants.SELECTED_SUBJECT.equalsIgnoreCase("ece")) {
            eceLoginDialog.btn_unsupervised.setVisibility(View.GONE);
        } else eceLoginDialog.btn_unsupervised.setVisibility(View.VISIBLE);
*/

       /* eceLoginDialog.btn_supervised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*/
      /*          String userName = eceLoginDialog.userNameET.getText().toString(), password = eceLoginDialog.passwordET.getText().toString();
                getLoggedInCrl(userName, password);
                if (loggedCrl != null) {
                    String loggedCrlId = loggedCrl.getCRLId();*/
        /*Intent intent = new Intent(ChooseAssessmentActivity.this, SupervisedAssessmentActivity_.class);
        intent.putExtra("crlId", "");
//                    intent.putExtra("subId", sub);
        startActivity(intent);*/
//                    eceLoginDialog.dismiss();
            /*    }
            }
        });*/


//        eceLoginDialog.show();

    }

    @Click(R.id.menu_icon)
    public void openMenu() {
     /*   if (toggle_btn.getVisibility() != View.VISIBLE)
            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
        else menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
*/
        getSupportFragmentManager().popBackStack();
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 0) {
            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));
            if (drawerLayout.isDrawerOpen(Gravity.START))
                drawerLayout.closeDrawer(Gravity.START);
            else
                drawerLayout.openDrawer(Gravity.START);
        } else {
            resetActivity();
        }

    }




   /* private void makeSubjectsNonClickable(boolean clickable) {
        if (clickable) {
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                recyclerView.getChildAt(i).setEnabled(true);
                recyclerView.getChildAt(i).setClickable(true);
            }
        } else {
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                recyclerView.getChildAt(i).setEnabled(false);
                recyclerView.getChildAt(i).setClickable(false);
            }
        }
    }*/


    @Override
    public void clearContentList() {
        contentTableList.clear();
    }

    @Override
    public void addContentToViewList(List<AssessmentSubjects> contentTable) {

        contentTableList.addAll(contentTable);
       /* AssessmentSubjects ece = new AssessmentSubjects();
        ece.setSubjectid("0");
        ece.setSubjectname("ECE");
        contentTableList.add(ece);*/
        for (int i = 0; i < contentTableList.size(); i++) {
            if (contentTableList.get(i).getSubjectname().equalsIgnoreCase("english"))
                contentTableList.remove(contentTableList.get(i));
        }
        Collections.sort(contentTableList, new Comparator<AssessmentSubjects>() {
            @Override
            public int compare(AssessmentSubjects o1, AssessmentSubjects o2) {
                return o1.getSubjectid().compareTo(o2.getSubjectid());
            }
        });
        Log.d("sorted", contentTableList.toString());
    }

    @Override
    public void notifyAdapter() {
        chooseAssessAdapter.notifyDataSetChanged();
        /*if (COS_Utility.isDataConnectionAvailable(ChooseLevelActivity.this))
                    getAPIContent(COS_Constants.INTERNET_DOWNLOAD, COS_Constants.INTERNET_DOWNLOAD_API);
                else {
                    levelAdapter.notifyDataSetChanged();
         }*/
    }

    /*@Click({R.id.btn_Profile, R.id.rl_Profile})
    public void gotoProfileActivity() {
//        ButtonClickSound.start();


//        startActivity(new Intent(this, ResultActivity.class));
//        startActivity(new Intent(this, ProfileActivity.class));
        startActivity(new Intent(this, AssessmentCertificateActivity.class));
    }*/

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments >= 1) {
            getSupportFragmentManager().popBackStack();
            resetActivity();
        } else {
//            startActivity(new Intent(this, MenuActivity.class));
            showExitDialog();
        }
      /*  presenter.endSession();
//        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SelectGroupActivity.class));*/
//        showExitDialog();
    }

    public void showExitDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView title = dialog.findViewById(R.id.dia_title);
        Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);
        Button cancel_btn = dialog.findViewById(R.id.dia_btn_cancel);
        cancel_btn.setVisibility(View.VISIBLE);
        title.setText(R.string.do_you_want_to_exit);
        restart_btn.setText(R.string.no);
        exit_btn.setText(R.string.yes);
        cancel_btn.setText(R.string.restart);
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String curSession = AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getStatusDao().getValue("CurrentSession");
                String toDateTemp = AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getSessionDao().getToDate(curSession);

                Log.d("AppExitService:", "curSession : " + curSession + "      toDateTemp : " + toDateTemp);
                VIDEOMONITORING = false;
                if (toDateTemp != null) {
                    if (toDateTemp.equalsIgnoreCase("na"))
                        AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getSessionDao().UpdateToDate(curSession, Assessment_Utility.getCurrentDateTime());
                }
                finishAffinity();

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VIDEOMONITORING = false;
                finish();
                startActivity(new Intent(ChooseAssessmentActivity.this, SplashActivity_.class));
            }
        });
        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void subjectClicked(final int position, final AssessmentSubjects sub) {
        Assessment_Constants.SELECTED_SUBJECT = sub.getSubjectname();
        Assessment_Constants.SELECTED_SUBJECT_ID = sub.getSubjectid();
        FastSave.getInstance().saveString("SELECTED_SUBJECT_ID", sub.getSubjectid());
        loggedCrl = null;
        String crlId = "";
        tv_choose_assessment.setText("Choose topic");
        menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));

        rlSubject.setVisibility(View.GONE);
//        toggle_btn.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        if (sub.getSubjectname().equalsIgnoreCase("ece")) {
            if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("") || Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase(Assessment_Constants.PRACTICE)) {
                rlSubject.setVisibility(View.VISIBLE);
//                toggle_btn.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                Toast.makeText(this, "Switch on supervision mode", Toast.LENGTH_SHORT).show();
            } else {
               /* Intent intent = new Intent(ChooseAssessmentActivity.this, SupervisedAssessmentActivity_.class);
                intent.putExtra("crlId", "");
//                    intent.putExtra("subId", sub);
                startActivity(intent);*/
               /* Intent intent = new Intent(ChooseAssessmentActivity.this, ECEActivity.class);
                intent.putExtra("resId", "9962");
                intent.putExtra("crlId", "");
                startActivity(intent);*/
            }
        } else {
            Assessment_Utility.showFragment(ChooseAssessmentActivity.this, new TopicFragment_(), R.id.nav_frame_layout,
                    null, TopicFragment.class.getSimpleName());
        }


    }

    @Override
    public void languageClicked(int pos, AssessmentLanguages languages) {
        Assessment_Constants.SELECTED_LANGUAGE = languages.getLanguageid();
        FastSave.getInstance().saveString(LANGUAGE, languages.getLanguageid());
        setLanguageInNav();
        clearContentList();
        presenter.copyListData();
        drawerLayout.closeDrawer(GravityCompat.START);
        Toast.makeText(this, "Language " + languages.getLanguagename(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().popBackStackImmediate();
        frameLayout.setVisibility(View.GONE);
        rlSubject.setVisibility(View.VISIBLE);
//        toggle_btn.setVisibility(View.VISIBLE);
        /*if (toggle_btn.getVisibility() != View.VISIBLE)
            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
        else*/
        menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));


    }

    @Override
    public void topicClicked(int pos, AssessmentTest test) {
//        Toast.makeText(this, "" + test.getExamname(), Toast.LENGTH_SHORT).show();
        Assessment_Constants.SELECTED_EXAM_ID = test.getExamid();
        FastSave.getInstance().saveString(EXAMID, test.getExamid());
       /* List<AssessmentTest> tests = AppDatabase.getDatabaseInstance(this).getTestDao().getTopicByExamId(Assessment_Constants.SELECTED_EXAM_ID);
        if (tests.size() <= 0) {
            downloadPaperPattern();
        }*/
   /*     if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("supervised"))
            showSupervisionDialog();
        else {*/
        Intent intent = new Intent(ChooseAssessmentActivity.this, ScienceAssessmentActivity_.class);
//        Intent intent = new Intent(ChooseAssessmentActivity.this, DownloadQuestionsActivity_.class);
        startActivity(intent);
//        }
    }


    private void getLoggedInCrl(String userName, String password) {
        Crl loggedCrl = AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getCrlDao().checkUserValidation(userName, password);
        if (loggedCrl == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(ChooseAssessmentActivity.this).create();
            alertDialog.setTitle("Invalid Credentials");
            alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    eceLoginDialog.userNameET.setText("");
                    eceLoginDialog.passwordET.setText("");
                    eceLoginDialog.userNameET.requestFocus();
                    ChooseAssessmentActivity.this.loggedCrl = null;
                }
            });
            alertDialog.show();
        } else
            this.loggedCrl = loggedCrl;

    }


    @Override
    protected void onResume() {
        super.onResume();
        setLanguageInNav();
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments >= 1) {
        } else {
//            startActivity(new Intent(this, MenuActivity.class));
            resetActivity();
        }

    }

    private void setLanguageInNav() {
        Menu menu = navigation.getMenu();
        MenuItem nav_lang = menu.findItem(R.id.menu_language);
        String lang = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangNameById(Assessment_Constants.SELECTED_LANGUAGE);
        String languageMenu = "";
        if (lang != null) {
            if (!lang.equals("")) {
                languageMenu = "Language(" + lang + ")";
            }
        } else languageMenu = "Language( ENGLISH )";

        nav_lang.setTitle(languageMenu);
    }

    public void resetActivity() {
//        toggle_btn.setVisibility(View.VISIBLE);

        /*  if (toggle_btn.getVisibility() == View.VISIBLE)*/
        menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));

        getSupportFragmentManager().popBackStack();
      /*  if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("supervised")) {
            toggle_btn.setChecked(true);
        } else if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("practice")) {
            toggle_btn.setChecked(false);
        } else toggle_btn.setChecked(false);*/


        rlSubject.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        tv_choose_assessment.setText("Choose subject");
    }


    /* private void getExamData() {
         final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setMessage("Loading Exams");
         progressDialog.setCancelable(false);
         progressDialog.show();
         AndroidNetworking.get(APIs.AssessmentExamAPI + Assessment_Constants.SELECTED_SUBJECT_ID)
                 .build()
                 .getAsString(new StringRequestListener() {
                     @Override
                     public void onResponse(String response) {
                         Gson gson = new Gson();
                         Type listType = new TypeToken<List<AssessmentTestModal>>() {
                         }.getType();
                         List<AssessmentTestModal> assessmentTestModals = gson.fromJson(response, listType);
                         List<AssessmentTest> assessmentTests = new ArrayList<>();
                         for (int i = 0; i < assessmentTestModals.size(); i++) {
                             assessmentTests.addAll(assessmentTestModals.get(i).getLstsubjectexam());
                             for (int j = 0; j < assessmentTests.size(); j++) {
                                 assessmentTests.get(j).setSubjectid(assessmentTestModals.get(i).getSubjectid());
                                 assessmentTests.get(j).setSubjectname(assessmentTestModals.get(i).getSubjectname());
                             }
                         }
                         if (!assessmentTests.isEmpty()) {
                             AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getTestDao().insertAllTest(assessmentTests);
                             progressDialog.dismiss();
                             Toast.makeText(ChooseAssessmentActivity.this, "Exams updated", Toast.LENGTH_SHORT).show();
                            *//* flowLayout.removeAllViews();
                            setTopicsToCheckBox(assessmentTests);*//*
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ChooseAssessmentActivity.this, "No Exams..", Toast.LENGTH_SHORT).show();
                            frameLayout.setVisibility(View.GONE);
                            rlSubject.setVisibility(View.VISIBLE);
                            toggle_btn.setVisibility(View.VISIBLE);

//                           btnOk.setEnabled(false);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Toast.makeText(ChooseAssessmentActivity.this, "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }*/

    public void createDataBase() {
        Log.d("$$$", "createDataBase");

        try {
            boolean dbExist = checkDataBase();
//            if (!dbExist) {
            try {
                Log.d("$$$", "createDataBasebefore");

                appDatabase = AppDatabase.getDatabaseInstance(this);

                Log.d("$$$", "createDataBaseAfter");

                          /*  Room.databaseBuilder(this,
                            AppDatabase.class, AppDatabase.DB_NAME)
                            .allowMainThreadQueries()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }
                            })
                            .build();*/
                if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrathamBackups" + "/assessment_database").exists()) {
                    try {
                        copyDataBase();
                        updateNewEntriesInStatusTable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
//                        showButton();
                    //populateMenu();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* } else {
                updateNewEntriesInStatusTable();
                appDatabase = AppDatabase.getDatabaseInstance(this);
               *//* appDatabase = Room.databaseBuilder(this,
                        AppDatabase.class, AppDatabase.DB_NAME)
                        .allowMainThreadQueries()
                        .build();*//*
//                showButton();
//                getSdCardPath();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(getDatabasePath(AppDatabase.DB_NAME).getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    public void updateNewEntriesInStatusTable() {
        Status status;
        try {

            status = new Status();
            String OsVersionName = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("OsVersionName");
            if (OsVersionName == null || OsVersionName.equalsIgnoreCase("")) {
                status.setStatusKey("OsVersionName");
                status.setValue(Assessment_Utility.getOSVersion());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("OsVersionName", Assessment_Utility.getOSVersion());
            }

            status = new Status();
            String OsVersionNum = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("OsVersionNum");
            if (OsVersionNum == null || OsVersionNum.equalsIgnoreCase("")) {

                status.setStatusKey("OsVersionNum");
                status.setValue(Assessment_Utility.getOSVersionNo() + "");
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("OsVersionNum", Assessment_Utility.getOSVersionNo() + "");

            }
            status = new Status();
            String AvailableStorage = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("AvailableStorage");
            if (AvailableStorage == null || AvailableStorage.equalsIgnoreCase("")) {
                status.setStatusKey("AvailableStorage");
                status.setValue(Assessment_Utility.getAvailableStorage());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("AvailableStorage", Assessment_Utility.getAvailableStorage());

            }
            status = new Status();
            String ScreenResolution = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("ScreenResolution");
            if (ScreenResolution == null || ScreenResolution.equalsIgnoreCase("")) {
                status.setStatusKey("ScreenResolution");
                status.setValue(Assessment_Utility.getScreenResolution((AppCompatActivity) context));
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("ScreenResolution", Assessment_Utility.getScreenResolution((AppCompatActivity) context));
            }

            status = new Status();
            String Manufacturer = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("Manufacturer");
            if (Manufacturer == null || Manufacturer.equalsIgnoreCase("")) {
                status.setStatusKey("Manufacturer");
                status.setValue(Assessment_Utility.getManufacturer());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("Manufacturer", Assessment_Utility.getManufacturer());
            }

            status = new Status();
            String Model = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("Model");
            if (Model == null || Model.equalsIgnoreCase("")) {

                status.setStatusKey("Model");
                status.setValue(Assessment_Utility.getModel());
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("Model", Assessment_Utility.getModel());
            }


            status = new Status();
            String ApiLevel = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("ApiLevel");
            if (ApiLevel == null || ApiLevel.equalsIgnoreCase("")) {

                status.setStatusKey("ApiLevel");
                status.setValue(Assessment_Utility.getApiLevel() + "");
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("ApiLevel", Assessment_Utility.getApiLevel() + "");
            }

            status = new Status();
            String InternalStorageSize = AppDatabase.getDatabaseInstance(context).getStatusDao().getKey("InternalStorageSize");
            if (InternalStorageSize == null || InternalStorageSize.equalsIgnoreCase("")) {

                status.setStatusKey("InternalStorageSize");
                status.setValue(Assessment_Utility.getInternalStorageSize() + "");
                AppDatabase.getDatabaseInstance(context).getStatusDao().insert(status);
            } else {
                AppDatabase.getDatabaseInstance(context).getStatusDao().updateValue("InternalStorageSize", Assessment_Utility.getInternalStorageSize() + "");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyDataBase() {

        try {
            new AsyncTask<Void, Integer, Void>() {
                ProgressDialog progressDialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(ChooseAssessmentActivity.this);
                    progressDialog.show();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    File folder_file, db_file;
                    try {
                        ArrayList<String> sdPath = FileUtils.getExtSdCardPaths(context);
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PrathamBackups" + "/assessment_database", null, SQLiteDatabase.OPEN_READONLY);
                        if (db != null) {
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Score Where sentFlag=0", null);
                                List<Score> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Score detail = new Score();
                                        detail.setScoreId(content_cursor.getInt(content_cursor.getColumnIndex("ScoreId")));
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setStudentID(content_cursor.getString(content_cursor.getColumnIndex("StudentID")));
                                        detail.setDeviceID(content_cursor.getString(content_cursor.getColumnIndex("DeviceID")));
                                        detail.setResourceID(content_cursor.getString(content_cursor.getColumnIndex("ResourceID")));
                                        detail.setQuestionId(content_cursor.getInt(content_cursor.getColumnIndex("QuestionId")));
                                        detail.setScoredMarks(content_cursor.getInt(content_cursor.getColumnIndex("ScoredMarks")));
                                        detail.setTotalMarks(content_cursor.getInt(content_cursor.getColumnIndex("TotalMarks")));
                                        detail.setStartDateTime(content_cursor.getString(content_cursor.getColumnIndex("StartDateTime")));
                                        detail.setEndDateTime(content_cursor.getString(content_cursor.getColumnIndex("EndDateTime")));
                                        detail.setLevel(content_cursor.getInt(content_cursor.getColumnIndex("Level")));
                                        detail.setLabel(content_cursor.getString(content_cursor.getColumnIndex("Label")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        detail.setIsAttempted(content_cursor.getString(content_cursor.getColumnIndex("isAttempted")).equalsIgnoreCase("true") ? true : false);
                                        detail.setIsCorrect(content_cursor.getString(content_cursor.getColumnIndex("isCorrect")).equalsIgnoreCase("true") ? true : false);
                                        detail.setUserAnswer(content_cursor.getString(content_cursor.getColumnIndex("userAnswer")));
                                        detail.setExamId(content_cursor.getString(content_cursor.getColumnIndex("examId")));
                                        detail.setPaperId(content_cursor.getString(content_cursor.getColumnIndex("paperId")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getScoreDao().addScoreList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Session Where sentFlag=0", null);
                                List<Session> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Session detail = new Session();
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setFromDate(content_cursor.getString(content_cursor.getColumnIndex("fromDate")));
                                        detail.setToDate(content_cursor.getString(content_cursor.getColumnIndex("toDate")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getSessionDao().addSessionList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Attendance Where sentFlag=0", null);
                                List<Attendance> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Attendance detail = new Attendance();
                                        detail.setAttendanceID(content_cursor.getInt(content_cursor.getColumnIndex("attendanceID")));
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setDate(content_cursor.getString(content_cursor.getColumnIndex("Date")));
                                        detail.setGroupID(content_cursor.getString(content_cursor.getColumnIndex("GroupID")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getAttendanceDao().addAttendanceList(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM AssessmentPaperForPush Where sentFlag=0", null);
                                List<AssessmentPaperForPush> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        AssessmentPaperForPush detail = new AssessmentPaperForPush();
                                        detail.setPaperStartTime(content_cursor.getString(content_cursor.getColumnIndex("paperStartTime")));
                                        detail.setPaperEndTime(content_cursor.getString(content_cursor.getColumnIndex("paperEndTime")));
                                        detail.setLanguageId(content_cursor.getString(content_cursor.getColumnIndex("languageId")));
                                        detail.setExamId(content_cursor.getString(content_cursor.getColumnIndex("examId")));
                                        detail.setSubjectId(content_cursor.getString(content_cursor.getColumnIndex("subjectId")));
                                        detail.setOutOfMarks(content_cursor.getString(content_cursor.getColumnIndex("outOfMarks")));
                                        detail.setPaperId(content_cursor.getString(content_cursor.getColumnIndex("paperId")));
                                        detail.setTotalMarks(content_cursor.getString(content_cursor.getColumnIndex("totalMarks")));
                                        detail.setExamTime(content_cursor.getString(content_cursor.getColumnIndex("examTime")));
                                        detail.setCorrectCnt(content_cursor.getInt(content_cursor.getColumnIndex("CorrectCnt")));
                                        detail.setWrongCnt(content_cursor.getInt(content_cursor.getColumnIndex("wrongCnt")));
                                        detail.setSkipCnt(content_cursor.getInt(content_cursor.getColumnIndex("SkipCnt")));
                                        detail.setSessionID(content_cursor.getString(content_cursor.getColumnIndex("SessionID")));
                                        detail.setStudentId(content_cursor.getString(content_cursor.getColumnIndex("studentId")));
                                        detail.setExamName(content_cursor.getString(content_cursor.getColumnIndex("examName")));
                                        detail.setQuestion1Rating(content_cursor.getString(content_cursor.getColumnIndex("question1Rating")));
                                        detail.setQuestion2Rating(content_cursor.getString(content_cursor.getColumnIndex("question2Rating")));
                                        detail.setQuestion3Rating(content_cursor.getString(content_cursor.getColumnIndex("question3Rating")));
                                        detail.setQuestion4Rating(content_cursor.getString(content_cursor.getColumnIndex("question4Rating")));
                                        detail.setQuestion5Rating(content_cursor.getString(content_cursor.getColumnIndex("question5Rating")));
                                        detail.setQuestion6Rating(content_cursor.getString(content_cursor.getColumnIndex("question6Rating")));
                                        detail.setQuestion7Rating(content_cursor.getString(content_cursor.getColumnIndex("question7Rating")));
                                        detail.setQuestion8Rating(content_cursor.getString(content_cursor.getColumnIndex("question8Rating")));
                                        detail.setQuestion9Rating(content_cursor.getString(content_cursor.getColumnIndex("question9Rating")));
                                        detail.setQuestion10Rating(content_cursor.getString(content_cursor.getColumnIndex("question10Rating")));
                                        detail.setFullName(content_cursor.getString(content_cursor.getColumnIndex("FullName")));
                                        detail.setGender(content_cursor.getString(content_cursor.getColumnIndex("Gender")));
                                        detail.setIsniosstudent(content_cursor.getString(content_cursor.getColumnIndex("isniosstudent")));
                                        detail.setAge(content_cursor.getInt(content_cursor.getColumnIndex("Age")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().insertAllPapersForPush(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM SupervisorData Where sentFlag=0", null);
                                List<SupervisorData> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        SupervisorData detail = new SupervisorData();
                                        detail.setsId(content_cursor.getInt(content_cursor.getColumnIndex("sId")));
                                        detail.setAssessmentSessionId(content_cursor.getString(content_cursor.getColumnIndex("assessmentSessionId")));
                                        detail.setSupervisorId(content_cursor.getString(content_cursor.getColumnIndex("supervisorId")));
                                        detail.setSupervisorName(content_cursor.getString(content_cursor.getColumnIndex("supervisorName")));
                                        detail.setSupervisorPhoto(content_cursor.getString(content_cursor.getColumnIndex("supervisorPhoto")));
                                        detail.setSentFlag(content_cursor.getInt(content_cursor.getColumnIndex("sentFlag")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getSupervisorDataDao().insertAll(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Cursor content_cursor;
                                content_cursor = db.rawQuery("SELECT * FROM Logs Where sentFlag=0", null);
                                List<Modal_Log> contents = new ArrayList<>();
                                if (content_cursor.moveToFirst()) {
                                    while (!content_cursor.isAfterLast()) {
                                        Modal_Log detail = new Modal_Log();
                                        detail.setLogId(content_cursor.getInt(content_cursor.getColumnIndex("logId")));
                                        detail.setDeviceId(content_cursor.getString(content_cursor.getColumnIndex("deviceId")));
                                        detail.setCurrentDateTime(content_cursor.getString(content_cursor.getColumnIndex("currentDateTime")));
                                        detail.setErrorType(content_cursor.getString(content_cursor.getColumnIndex("errorType")));
                                        detail.setExceptionMessage(content_cursor.getString(content_cursor.getColumnIndex("exceptionMessage")));
                                        detail.setExceptionStackTrace(content_cursor.getString(content_cursor.getColumnIndex("exceptionStackTrace")));
                                        detail.setGroupId(content_cursor.getString(content_cursor.getColumnIndex("groupId")));
                                        detail.setLogDetail(content_cursor.getString(content_cursor.getColumnIndex("LogDetail")));
                                        detail.setMethodName(content_cursor.getString(content_cursor.getColumnIndex("methodName")));
                                        contents.add(detail);
                                        content_cursor.moveToNext();
                                    }
                                }
                                AppDatabase.getDatabaseInstance(context).getLogsDao().insertAllLogs(contents);
                                content_cursor.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (!AssessmentApplication.isTablet) {
                                try {
                                    Cursor content_cursor;
                                    content_cursor = db.rawQuery("SELECT * FROM Student Where newFlag=0", null);
                                    List<Student> contents = new ArrayList<>();
                                    if (content_cursor.moveToFirst()) {
                                        while (!content_cursor.isAfterLast()) {
                                            Student detail = new Student();
                                            detail.setStudentID(content_cursor.getString(content_cursor.getColumnIndex("StudentID")));
                                            detail.setStudentUID(content_cursor.getString(content_cursor.getColumnIndex("StudentUID")));
                                            detail.setFirstName(content_cursor.getString(content_cursor.getColumnIndex("FirstName")));
                                            detail.setMiddleName(content_cursor.getString(content_cursor.getColumnIndex("MiddleName")));
                                            detail.setLastName(content_cursor.getString(content_cursor.getColumnIndex("LastName")));
                                            detail.setFullName(content_cursor.getString(content_cursor.getColumnIndex("FullName")));
                                            detail.setGender(content_cursor.getString(content_cursor.getColumnIndex("Gender")));
                                            detail.setRegDate(content_cursor.getString(content_cursor.getColumnIndex("regDate")));
                                            detail.setAge(content_cursor.getInt(content_cursor.getColumnIndex("Age")));
                                            detail.setVillageName(content_cursor.getString(content_cursor.getColumnIndex("villageName")));
                                            detail.setNewFlag(content_cursor.getInt(content_cursor.getColumnIndex("newFlag")));
                                            detail.setDeviceId(content_cursor.getString(content_cursor.getColumnIndex("DeviceId")));
                                            detail.setIsniosstudent(content_cursor.getString(content_cursor.getColumnIndex("isniosstudent")));
                                            contents.add(detail);
                                            content_cursor.moveToNext();
                                        }
                                    }
                                    AppDatabase.getDatabaseInstance(context).getStudentDao().insertAll(contents);
                                    content_cursor.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            BackupDatabase.backup(context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //addStartTime();
                    super.onPostExecute(aVoid);
                    progressDialog.dismiss();
                    BackupDatabase.backup(context);
                }

            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}