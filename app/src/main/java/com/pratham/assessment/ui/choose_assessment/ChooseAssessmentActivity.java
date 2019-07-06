package com.pratham.assessment.ui.choose_assessment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.certificate.AssessmentCertificateActivity;
import com.pratham.assessment.ui.display_english_list.TestDisplayActivity;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity;
import com.pratham.assessment.utilities.Assessment_Constants;

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
        rl_Profile.setVisibility(View.VISIBLE);
        presenter = new ChooseAssessmentPresenter(ChooseAssessmentActivity.this, this);
        contentTableList = new ArrayList<>();

        recyclerView = findViewById(R.id.choose_assessment_recycler);
        chooseAssessAdapter = new ChooseAssessmentAdapter(this, contentTableList, this);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseAssessAdapter);

        presenter.copyListData();
    }

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
        presenter.endSession();
//        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SelectGroupActivity.class));
    }

    @Override
    public void assessmentClicked(final int position, final String subId) {
        loggedCrl = null;
        eceLoginDialog = new ECELoginDialog(this);
        String crlId = "";
       if(subId.equalsIgnoreCase("0")){
           eceLoginDialog.btn_unsupervised.setVisibility(View.GONE);
       }else  eceLoginDialog.btn_unsupervised.setVisibility(View.VISIBLE);

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
                String assessmentSession = "" + UUID.randomUUID().toString();
                Assessment_Constants.assessmentSession = "test-" + assessmentSession;
                presenter.startAssessSession();
                if (subId.equalsIgnoreCase("0")) {
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
                    Intent intent = new Intent(ChooseAssessmentActivity.this, ScienceAssessmentActivity.class);
                    intent.putExtra("subId", subId);
                    intent.putExtra("crlId", "");
                    startActivity(intent);
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
                    intent.putExtra("subId", subId);
                    startActivity(intent);
                    eceLoginDialog.dismiss();
                }
            }
        });
        eceLoginDialog.show();
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