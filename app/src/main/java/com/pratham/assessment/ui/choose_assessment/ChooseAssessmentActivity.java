package com.pratham.assessment.ui.choose_assessment;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.ui.choose_assessment.fragments.LanguageFragment;
import com.pratham.assessment.ui.choose_assessment.fragments.TopicFragment;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.certificate.AssessmentCertificateActivity;
import com.pratham.assessment.ui.login_menu.MenuFragment;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    RelativeLayout rlSubject;
    @BindView(R.id.nav_frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.tv_choose_assessment)
    TextView tv_choose_assessment;

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

        eceLoginDialog = new ECELoginDialog(this);

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_Subject:
                        rlSubject.setVisibility(View.VISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        tv_choose_assessment.setText("Choose subject");
                        break;
                    case R.id.menu_certificate:
                        startActivity(new Intent(ChooseAssessmentActivity.this, AssessmentCertificateActivity.class));
                        break;
                    case R.id.menu_supervision_type:
                        showSupervisionDialog();
                        break;
                    case R.id.menu_language:
                        rlSubject.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        tv_choose_assessment.setText("Choose language");
                        Assessment_Utility.showFragment(ChooseAssessmentActivity.this, new LanguageFragment(), R.id.nav_frame_layout,
                                null, LanguageFragment.class.getSimpleName());

                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

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
    }

    private void showSupervisionDialog() {
        loggedCrl = null;
       /* if (Assessment_Constants.SELECTED_SUBJECT.equalsIgnoreCase("ece")) {
            eceLoginDialog.btn_unsupervised.setVisibility(View.GONE);
        } else eceLoginDialog.btn_unsupervised.setVisibility(View.VISIBLE);
*/

        eceLoginDialog.btn_supervised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Assessment_Constants.ASSESSMENT_TYPE = "supervised";
                String userName = eceLoginDialog.userNameET.getText().toString(), password = eceLoginDialog.passwordET.getText().toString();
                getLoggedInCrl(userName, password);
                if (loggedCrl != null) {
                    String loggedCrlId = loggedCrl.getCRLId();
                    Intent intent = new Intent(ChooseAssessmentActivity.this, SupervisedAssessmentActivity.class);
                    intent.putExtra("crlId", loggedCrlId);
//                    intent.putExtra("subId", sub);
                    startActivity(intent);
                    eceLoginDialog.dismiss();
                }
            }
        });


        eceLoginDialog.show();

    }

    @OnClick(R.id.menu_icon)
    public void openMenu() {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);
        else
            drawerLayout.openDrawer(Gravity.START);
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
        AssessmentSubjects ece = new AssessmentSubjects();
        ece.setSubjectid("0");
        ece.setSubjectname("ECE");
        contentTableList.add(ece);
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
      /*  presenter.endSession();
//        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SelectGroupActivity.class));*/
        showExitDialog();
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

        title.setText("Do you want to exit?");
        restart_btn.setText("No");
        exit_btn.setText("Yes");
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();

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

        rlSubject.setVisibility(View.GONE);

    /*    if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("practice")) {


        } else if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("supervised")) {
            if (sub.getSubjectid().equalsIgnoreCase("0")) {
                String assessmentSession = "" + UUID.randomUUID().toString();
                Assessment_Constants.assessmentSession = "test-" + assessmentSession;
                presenter.startAssessSession();
                crlId=loggedCrl.getCRLId();
                Intent intent = new Intent(ChooseAssessmentActivity.this, ECEActivity.class);
                intent.putExtra("resId", "9962");
                intent.putExtra("crlId",crlId );
                startActivity(intent);
            } else {

            }
        }*/








      /*  eceLoginDialog = new ECELoginDialog(this);
        if (sub.getSubjectid().equalsIgnoreCase("0")) {
            eceLoginDialog.btn_unsupervised.setVisibility(View.GONE);
        } else eceLoginDialog.btn_unsupervised.setVisibility(View.VISIBLE);
*/
       /* if (subId.equalsIgnoreCase("1304") || subId.equalsIgnoreCase("1302")) {
            eceLoginDialog.btn_unsupervised.setVisibility(View.GONE);
//            getLoggedInCrl(userName, password);
           *//* String userName = eceLoginDialog.userNameET.getText().toString(), password = eceLoginDialog.passwordET.getText().toString();
            crlId = AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getCrlDao().getCrlId(userName, password);*//*
        } else
            eceLoginDialog.btn_unsupervised.setVisibility(View.VISIBLE);
*/

        final String finalCrlId = crlId;
        eceLoginDialog.btn_unsupervised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (loggedCrl != null) {

                if (sub.getSubjectid().equalsIgnoreCase("0")) {
                    String assessmentSession = "" + UUID.randomUUID().toString();
                    Assessment_Constants.assessmentSession = "test-" + assessmentSession;
                    presenter.startAssessSession();
                    Intent intent = new Intent(ChooseAssessmentActivity.this, ECEActivity.class);
                    intent.putExtra("resId", "9962");
                    intent.putExtra("crlId", finalCrlId);
                    startActivity(intent);
                }/* else if (subId.equalsIgnoreCase("1302")) {
                    Intent intent = new Intent(ChooseAssessmentActivity.this, TestDisplayActivity.class);
                    intent.putExtra("subId", subId);
                    intent.putExtra("crlId", "");

                    startActivity(intent);
                }*/ else {
//                        Intent intent = new Intent(ChooseAssessmentActivity.this, CRLActivity.class);
                  /*  Intent intent = new Intent(ChooseAssessmentActivity.this, ScienceAssessmentActivity.class);
                    intent.putExtra("subId", sub);
                    intent.putExtra("crlId", "");
                    startActivity(intent);*/
                    Assessment_Utility.showFragment(ChooseAssessmentActivity.this, new TopicFragment(), R.id.nav_frame_layout,
                            null, TopicFragment.class.getSimpleName());

                }
                eceLoginDialog.dismiss();
                /*} else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChooseAssessmentActivity.this).create();
                    alertDialog.setTitle("Invalid Credentials");
                    alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eceLoginDialog.userNameET.setText("");
                            eceLoginDialog.passwordET.setText("");
                            eceLoginDialog.userNameET.requestFocus();
                        }
                    });
                    alertDialog.show();
                }*/
            }
        });


        eceLoginDialog.btn_supervised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = eceLoginDialog.userNameET.getText().toString(), password = eceLoginDialog.passwordET.getText().toString();

                getLoggedInCrl(userName, password);
                if (loggedCrl != null) {
                    String loggedCrlId = loggedCrl.getCRLId();
                    Intent intent = new Intent(ChooseAssessmentActivity.this, SupervisedAssessmentActivity.class);
                    intent.putExtra("crlId", loggedCrlId);
                    intent.putExtra("subId", sub);
                    startActivity(intent);
                    eceLoginDialog.dismiss();
                }
            }
        });
        eceLoginDialog.show();
    }

    @Override
    public void languageClicked(int pos, AssessmentLanguages languages) {
        Assessment_Constants.SELECTED_LANGUAGE = languages.getLanguagename();
        drawerLayout.closeDrawer(GravityCompat.START);
        Toast.makeText(this, "Language " + languages.getLanguagename(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void topicClicked(int pos, AssessmentTest test) {
        Toast.makeText(this, ""+test.getExamname(), Toast.LENGTH_SHORT).show();
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
}