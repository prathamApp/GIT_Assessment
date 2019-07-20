package com.pratham.assessment.ui.choose_assessment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.certificate.AssessmentCertificateActivity;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;

import org.json.JSONArray;
import org.json.JSONException;

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
  /*  @BindView(R.id.userName)
    TextView name;*/

    private RecyclerView recyclerView;
    List<AssessmentSubjects> contentTableList;
    ChooseAssessmentAdapter chooseAssessAdapter;
    ECELoginDialog eceLoginDialog;
    Crl loggedCrl;
    List<AssessmentLanguages> assessmentLanguagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_assessment);
        ButterKnife.bind(this);
        String studentName = AppDatabase.getDatabaseInstance(this).getStudentDao().getFullName(Assessment_Constants.currentStudentID);
        View view = navigation.getHeaderView(0);
        TextView name = view.findViewById(R.id.userName);
        name.setText(studentName);



        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_Subject:
                        break;
                    case R.id.menu_certificate:
                        startActivity(new Intent(ChooseAssessmentActivity.this, AssessmentCertificateActivity.class));
                        break;
                    case R.id.menu_supervision_type:/* eceLoginDialog.show();*/
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });

        presenter = new ChooseAssessmentPresenter(ChooseAssessmentActivity.this, this);
        contentTableList = new ArrayList<>();
        assessmentLanguagesList = new ArrayList<>();
        assessmentLanguagesList = AppDatabase.getDatabaseInstance(this).getLanguageDao().getAllLangs();
        if (assessmentLanguagesList.size() <= 0) {
            getLanguageData();
        } else setLanguageSpinner();


        recyclerView = findViewById(R.id.choose_assessment_recycler);
        chooseAssessAdapter = new ChooseAssessmentAdapter(this, contentTableList, this);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10, this), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chooseAssessAdapter);

        presenter.copyListData();
    }

    @OnClick(R.id.menu_icon)
    public void openMenu() {
        if (drawerLayout.isDrawerOpen(Gravity.START))
            drawerLayout.closeDrawer(Gravity.START);
        else
            drawerLayout.openDrawer(Gravity.START);
    }

    private void getLanguageData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentLanguageAPI)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            progressDialog.dismiss();

                            for (int i = 0; i < response.length(); i++) {
                                AssessmentLanguages assessmentLanguages = new AssessmentLanguages();
                                assessmentLanguages.setLanguageid(response.getJSONObject(i).getString("languageid"));
                                assessmentLanguages.setLanguagename(response.getJSONObject(i).getString("languagename"));
                                assessmentLanguagesList.add(assessmentLanguages);
                            }
                            AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getLanguageDao().insertAllLanguages(assessmentLanguagesList);
                            setLanguageSpinner();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ChooseAssessmentActivity.this, "Error in loading..Check internet connection.", Toast.LENGTH_SHORT).show();
                        finish();
                        AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getAssessmentPaperPatternDao().deletePaperPatterns();

                        progressDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }

    private void setLanguageSpinner() {
        String[] lang = new String[assessmentLanguagesList.size() + 1];
        lang[0] = "Select Language";

        for (int i = 0; i < assessmentLanguagesList.size(); i++) {
            lang[i + 1] = assessmentLanguagesList.get(i).getLanguagename();
        }
        ArrayAdapter langAdapter = new ArrayAdapter(ChooseAssessmentActivity.this, android.R.layout.simple_spinner_dropdown_item, lang);
        spinner_choose_lang.setAdapter(langAdapter);


        spinner_choose_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_choose_lang.getSelectedItem().toString().equalsIgnoreCase("Select Language")) {
                    Toast.makeText(ChooseAssessmentActivity.this, "Please select language", Toast.LENGTH_SHORT).show();
//                                        makeSubjectsNonClickable(false);
                } else {
                    Assessment_Constants.SELECTED_LANGUAGE = spinner_choose_lang.getSelectedItem().toString();
//                                        makeSubjectsNonClickable(true);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        if (subId.equalsIgnoreCase("0")) {
            eceLoginDialog.btn_unsupervised.setVisibility(View.GONE);
        } else eceLoginDialog.btn_unsupervised.setVisibility(View.VISIBLE);

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

                if (subId.equalsIgnoreCase("0")) {
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