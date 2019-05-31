package com.pratham.assessment.ui.choose_assessment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.ui.choose_assessment.science.CRLActivity;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.display_english_list.TestDisplayActivity;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity;
import com.pratham.assessment.ui.profile.ProfileActivity;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.security.cert.CRL;
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
    List<ContentTable> contentTableList;
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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseAssessAdapter);

        presenter.copyListData();
    }

    @Override
    public void clearContentList() {
        contentTableList.clear();
    }

    @Override
    public void addContentToViewList(ContentTable contentTable) {

        contentTableList.add(contentTable);

        Collections.sort(contentTableList, new Comparator<ContentTable>() {
            @Override
            public int compare(ContentTable o1, ContentTable o2) {
                return o1.getNodeId().compareTo(o2.getNodeId());
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
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @Override
    public void onBackPressed() {
        presenter.endSession();
//        super.onBackPressed();
        finish();
        startActivity(new Intent(this, SelectGroupActivity.class));
    }

    @Override
    public void assessmentClicked(final int position, final String nodeId) {
        eceLoginDialog = new ECELoginDialog(this);
        final String userName = eceLoginDialog.userNameET.getText().toString(), password = eceLoginDialog.passwordET.getText().toString();

        eceLoginDialog.btn_unsupervised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoggedInCrl(userName, password);
                if (loggedCrl != null) {
                    String assessmentSession = "" + UUID.randomUUID().toString();
                    Assessment_Constants.assessmentSession = "test-" + assessmentSession;
                    presenter.startAssessSession();
                    String crlId = AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getCrlDao().getCrlId(userName, password);
                    if (nodeId.equalsIgnoreCase("1304")) {
                        Intent intent = new Intent(ChooseAssessmentActivity.this, ECEActivity.class);
                        intent.putExtra("resId", "9962");
                        intent.putExtra("crlId", crlId);
                        startActivity(intent);
                    } else if (nodeId.equalsIgnoreCase("1302")) {
                        Intent intent = new Intent(ChooseAssessmentActivity.this, TestDisplayActivity.class);
                        intent.putExtra("nodeId", nodeId);
                        startActivity(intent);
                    } else {
//                        Intent intent = new Intent(ChooseAssessmentActivity.this, CRLActivity.class);
                        Intent intent = new Intent(ChooseAssessmentActivity.this, ScienceAssessmentActivity.class);
                        intent.putExtra("nodeId", nodeId);
                        startActivity(intent);
                    }
                    eceLoginDialog.dismiss();
                } /*else {
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

                getLoggedInCrl(userName, password);
                String loggedCrlId = loggedCrl.getCRLId();
                Intent intent = new Intent(ChooseAssessmentActivity.this, SupervisedAssessmentActivity.class);
                intent.putExtra("loggedCrlId", loggedCrlId);
                intent.putExtra("nodeId", nodeId);
                startActivity(intent);
                eceLoginDialog.dismiss();
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
                }
            });
            alertDialog.show();
        } else
            this.loggedCrl = loggedCrl;

    }
}