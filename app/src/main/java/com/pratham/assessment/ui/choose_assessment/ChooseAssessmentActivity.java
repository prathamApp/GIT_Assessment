package com.pratham.assessment.ui.choose_assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.custom.toggle_button.SwipeableButton;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.ui.choose_assessment.fragments.LanguageFragment;
import com.pratham.assessment.ui.choose_assessment.fragments.TopicFragment;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.certificate.AssessmentCertificateActivity;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity;
import com.pratham.assessment.ui.splash_activity.SplashActivity;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static com.pratham.assessment.utilities.Assessment_Constants.LANGUAGE;
import static com.pratham.assessment.utilities.Assessment_Utility.dpToPx;

public class ChooseAssessmentActivity extends BaseActivity implements
        ChoseAssessmentClicked, ChooseAssessmentContract.ChooseAssessmentView {

    ChooseAssessmentContract.ChooseAssessmentPresenter presenter;

    @BindView(R.id.rl_Profile)
    RelativeLayout rl_Profile;
    @BindView(R.id.btn_Profile)
    ImageButton btn_Profile;
    @BindView(R.id.spinner_choose_lang)
    Spinner spinner_choose_lang;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.rl_choose_sub)
    public RelativeLayout rlSubject;
    @BindView(R.id.nav_frame_layout)
    public FrameLayout frameLayout;
    @BindView(R.id.tv_choose_assessment)
    TextView tv_choose_assessment;
    @BindView(R.id.menu_icon)
    ImageButton menu_icon;

    @BindView(R.id.toggle_btn)
    public SwipeableButton toggle_btn;

    private RecyclerView recyclerView;
    List<AssessmentSubjects> contentTableList;
    ChooseAssessmentAdapter chooseAssessAdapter;
    ECELoginDialog eceLoginDialog;
    Crl loggedCrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_assessment);
        ButterKnife.bind(this);
        String studentName = AppDatabase.getDatabaseInstance(this).getStudentDao().getFullName(Assessment_Constants.currentStudentID);
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


        presenter = new ChooseAssessmentPresenter(ChooseAssessmentActivity.this, this);
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
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });


    }


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
        Intent intent = new Intent(ChooseAssessmentActivity.this, SupervisedAssessmentActivity.class);
        intent.putExtra("crlId", "");
//                    intent.putExtra("subId", sub);
        startActivity(intent);
//                    eceLoginDialog.dismiss();
            /*    }
            }
        });*/


//        eceLoginDialog.show();

    }

    @OnClick(R.id.menu_icon)

    public void openMenu() {
        if (toggle_btn.getVisibility() != View.VISIBLE)
            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
        else menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));

        getSupportFragmentManager().popBackStack();
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 0) {
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

    @OnClick({R.id.btn_Profile, R.id.rl_Profile})
    public void gotoProfileActivity() {
//        ButtonClickSound.start();


//        startActivity(new Intent(this, ResultActivity.class));
//        startActivity(new Intent(this, ProfileActivity.class));
        startActivity(new Intent(this, AssessmentCertificateActivity.class));
    }

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
        title.setText("Do you want to exit?");
        restart_btn.setText("No");
        exit_btn.setText("Yes");
        cancel_btn.setText("Restart");
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ChooseAssessmentActivity.this, SplashActivity.class));
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
        loggedCrl = null;
        String crlId = "";
        tv_choose_assessment.setText("Choose topic");
        menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));

        rlSubject.setVisibility(View.GONE);
        toggle_btn.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);

        if (sub.getSubjectname().equalsIgnoreCase("ece")) {
            if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("") || Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("practice")) {
                rlSubject.setVisibility(View.VISIBLE);
                toggle_btn.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                Toast.makeText(this, "Switch on supervision mode", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ChooseAssessmentActivity.this, SupervisedAssessmentActivity.class);
                intent.putExtra("crlId", "");
//                    intent.putExtra("subId", sub);
                startActivity(intent);
               /* Intent intent = new Intent(ChooseAssessmentActivity.this, ECEActivity.class);
                intent.putExtra("resId", "9962");
                intent.putExtra("crlId", "");
                startActivity(intent);*/
            }
        } else {
            Assessment_Utility.showFragment(ChooseAssessmentActivity.this, new TopicFragment(), R.id.nav_frame_layout,
                    null, TopicFragment.class.getSimpleName());
        }


    }

    @Override
    public void languageClicked(int pos, AssessmentLanguages languages) {
        Assessment_Constants.SELECTED_LANGUAGE = languages.getLanguageid();
        FastSave.getInstance().saveString(LANGUAGE, languages.getLanguageid());
        setLanguageInNav();
        drawerLayout.closeDrawer(GravityCompat.START);
        Toast.makeText(this, "Language " + languages.getLanguagename(), Toast.LENGTH_SHORT).show();
        getSupportFragmentManager().popBackStackImmediate();
        frameLayout.setVisibility(View.GONE);
        rlSubject.setVisibility(View.VISIBLE);
        toggle_btn.setVisibility(View.VISIBLE);
        if (toggle_btn.getVisibility() != View.VISIBLE)
            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
        else menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));


    }

    @Override
    public void topicClicked(int pos, AssessmentTest test) {
//        Toast.makeText(this, "" + test.getExamname(), Toast.LENGTH_SHORT).show();
        Assessment_Constants.SELECTED_EXAM_ID = test.getExamid();
       /* List<AssessmentTest> tests = AppDatabase.getDatabaseInstance(this).getTestDao().getTopicByExamId(Assessment_Constants.SELECTED_EXAM_ID);
        if (tests.size() <= 0) {
            downloadPaperPattern();
        }*/
        if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("supervised"))
            showSupervisionDialog();
        else {
            Intent intent = new Intent(ChooseAssessmentActivity.this, ScienceAssessmentActivity.class);
            startActivity(intent);
        }
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
        toggle_btn.setVisibility(View.VISIBLE);

        if (toggle_btn.getVisibility() == View.VISIBLE)
            menu_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu));

        getSupportFragmentManager().popBackStack();
        if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("supervised")) {
            toggle_btn.setChecked(true);
        } else if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("practice")) {
            toggle_btn.setChecked(false);
        } else toggle_btn.setChecked(false);


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
}