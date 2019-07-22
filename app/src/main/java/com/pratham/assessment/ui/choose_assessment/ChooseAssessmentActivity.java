package com.pratham.assessment.ui.choose_assessment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.GridSpacingItemDecoration;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.domain.AssessmentPatternDetails;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.fragments.LanguageFragment;
import com.pratham.assessment.ui.choose_assessment.fragments.TopicFragment;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.certificate.AssessmentCertificateActivity;
import com.pratham.assessment.ui.login_menu.MenuFragment;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.json.JSONArray;

import java.lang.reflect.Type;
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
    int mediaDownloadCnt = 0;
    int topicCnt = 0;
    ProgressDialog progressDialog,mediaProgressDialog;
    List<DownloadMedia> downloadMediaList;
    List<String> topicsList;


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
        frameLayout.setVisibility(View.VISIBLE);

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
                    Intent intent = new Intent(ChooseAssessmentActivity.this, ScienceAssessmentActivity.class);
                    intent.putExtra("subId", sub.getSubjectid());
                    intent.putExtra("crlId", "");
                    startActivity(intent);
                 /*   Assessment_Utility.showFragment(ChooseAssessmentActivity.this, new TopicFragment(), R.id.nav_frame_layout,
                            null, TopicFragment.class.getSimpleName());
*/
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
        Assessment_Constants.SELECTED_LANGUAGE = languages.getLanguageid();
        drawerLayout.closeDrawer(GravityCompat.START);
        Toast.makeText(this, "Language " + languages.getLanguagename(), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void topicClicked(int pos, AssessmentTest test) {
        Toast.makeText(this, "" + test.getExamname(), Toast.LENGTH_SHORT).show();
        Assessment_Constants.SELECTED_EXAM_ID = test.getExamid();

       /* List<AssessmentTest> tests = AppDatabase.getDatabaseInstance(this).getTestDao().getTopicByExamId(Assessment_Constants.SELECTED_EXAM_ID);
        if (tests.size() <= 0) {
            downloadPaperPattern();
        }*/
       /* Intent intent = new Intent(ChooseAssessmentActivity.this, ScienceAssessmentActivity.class);
        startActivity(intent);*/
    }

    private void downloadPaperPattern() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Downloading paper pattern...");
//        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentPaperPatternAPI + Assessment_Constants.SELECTED_EXAM_ID)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        AssessmentPaperPattern assessmentPaperPattern = gson.fromJson(response, AssessmentPaperPattern.class);
                        if (assessmentPaperPattern != null)
                            AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getAssessmentPaperPatternDao().insertPaperPattern(assessmentPaperPattern);

                        List<AssessmentPatternDetails> assessmentPatternDetails = assessmentPaperPattern.getLstpatterndetail();
                        for (int i = 0; i < assessmentPatternDetails.size(); i++) {
                            assessmentPatternDetails.get(i).setExamId(assessmentPaperPattern.getExamid());
                        }
                        if (!assessmentPatternDetails.isEmpty())
                            insertPatternDetailsToDB(assessmentPatternDetails);

                        topicsList = AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this)
                                .getAssessmentPatternDetailsDao().getTopicsByExamId(Assessment_Constants.SELECTED_EXAM_ID);
                        if (topicsList.size() > 0) {
                            if (topicCnt < topicsList.size())
                                downloadQuestions(topicsList.get(topicCnt));

                        }
//                        downloadQuestions();



                      /*  if (paperPatternCnt < examIDList.size()) {
                            downloadPaperPattern(examIDList.get(paperPatternCnt), langId, subId);
                        } else {
                            progressDialog.dismiss();
                            for (int i = 0; i < examIDList.size(); i++) {
                                List<String> topicsList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this)
                                        .getAssessmentPatternDetailsDao().getTopicsByExamId(examIDList.get(i));
                                for (int j = 0; j < topicsList.size(); j++) {
                                    if (!topicIdList.contains(topicsList.get(j)))
                                        topicIdList.add(topicsList.get(j));
                                }
                            }
                            if (downloadFailedExamList.size() == 0)
                                if (topicIdList.size() > 0) {
                                    queDownloadIndex = 0;
                                    downloadQuestions(topicIdList.get(queDownloadIndex), langId, subId);
                                } else if (!downloadTopicDialog.isShowing())
                                    downloadTopicDialog.show();

//                            selectTopicDialog.show();

                            if (downloadFailedExamList.size() > 0)
                                showDownloadFailedDialog(langId, subId);
                        }*/
                    }

                    @Override
                    public void onError(ANError anError) {
                       /* downloadFailedExamList.add(AppDatabase.getDatabaseInstance
                                (ScienceAssessmentActivity.this).getTestDao().getExamNameById(examIDList.get(paperPatternCnt)));
                        paperPatternCnt++;
                        if (paperPatternCnt < examIDList.size()) {

                            downloadPaperPattern(examIDList.get(paperPatternCnt), langId, subId);
                        } else {*/
                        progressDialog.dismiss();
//                            selectTopicDialog.show();


                          /*  if (downloadFailedExamList.size() > 0)
                                showDownloadFailedDialog(langId, subId);*/
                    }
//                        progressDialog.dismiss();
//                        Toast.makeText(ScienceAssessmentActivity.this, "Error downloading paper pattern..", Toast.LENGTH_SHORT).show();
//                    }
                });


    }

    private void downloadQuestions(String topicId) {
        String questionUrl = APIs.AssessmentQuestionAPI + "languageid=" + Assessment_Constants.SELECTED_LANGUAGE + "&subjectid=" + Assessment_Constants.SELECTED_SUBJECT_ID + "&topicid=" + topicId;
        progressDialog.show();
        progressDialog.setMessage("Downloading questions...");
        AndroidNetworking.get(questionUrl)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        progressDialog.dismiss();
                        if (response.length() > 0) {
                            insertQuestionsToDB(response);
                            topicCnt++;
                            if (topicCnt < topicsList.size())
                                downloadQuestions(topicsList.get(topicCnt));

//                            queDownloadIndex++;
                       /*     if (queDownloadIndex < topicIdList.size()) {
                                if (downloadFailedExamList.size() == 0)
                                    downloadQuestions(topicIdList.get(queDownloadIndex), selectedLang, selectedSub);
                            } else {
                                progressDialog.dismiss();
                                if (downloadFailedExamList.size() == 0)
                                    showSelectTopicDialog();
                            }*/
                        } else if (response.length() == 0) {
                            topicCnt++;
                            if (topicCnt < topicsList.size())
                                downloadQuestions(topicsList.get(topicCnt));
                            else {
                                progressDialog.dismiss();
                              /*  if (downloadFailedExamList.size() == 0)
                                    showSelectTopicDialog();*/
                            }
                        } else {
                            progressDialog.dismiss();
                           /* if (downloadFailedExamList.size() == 0)
                                showSelectTopicDialog();*/
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ChooseAssessmentActivity.this, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
                        AppDatabase.getDatabaseInstance(ChooseAssessmentActivity.this).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                        finish();
                    }
                });
    }
    private void generatePaperPattern(String examId, String subId, String langId) {
     /*   assessmentPaperPatterns = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(examId);
        assessmentPatternDetails = AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().getAssessmentPatternDetailsByExamId(examId);
        // topicIdList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPatternDetailsDao().getDistinctTopicIds();

//        for (int i = 0; i < topicIdList.size(); i++) {
        if (assessmentPatternDetails.size() > 0)
            for (int j = 0; j < assessmentPatternDetails.size(); j++) {
                int noOfQues = Integer.parseInt(assessmentPatternDetails.get(j).getNoofquestion());
                List<ScienceQuestion> scienceQuestions = AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().getQuestionListByPattern1(langId, subId, assessmentPatternDetails.get(j).getTopicid(), assessmentPatternDetails.get(j).getQtid(), assessmentPatternDetails.get(j).getQlevel(), noOfQues);
                for (int i = 0; i < scienceQuestions.size(); i++) {
                    scienceQuestions.get(i).setOutofmarks(assessmentPatternDetails.get(j).getMarksperquestion());
                    scienceQuestions.get(i).setExamid(examId);
                }
                if (scienceQuestions.size() > 0)
                    scienceQuestionList.addAll(scienceQuestions);
            }
//        }*/
    }

    private void insertPatternDetailsToDB(List<AssessmentPatternDetails> paperPatterns) {
        for (int i = 0; i < paperPatterns.size(); i++) {
            AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().deletePatternDetailsByExamId(paperPatterns.get(i).getExamId());
        }
        AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().insertAllPatternDetails(paperPatterns);

    }

    private void insertQuestionsToDB(JSONArray response) {
        try {
            downloadMediaList = new ArrayList<>();
            Gson gson = new Gson();
            String jsonOutput = response.toString();
            Type listType = new TypeToken<List<ScienceQuestion>>() {
            }.getType();
            List<ScienceQuestion> scienceQuestionList = gson.fromJson(jsonOutput, listType);
            Log.d("hhh", scienceQuestionList.toString());

            if (scienceQuestionList.size() > 0) {
                AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().insertAllQuestions(scienceQuestionList);
                for (int i = 0; i < scienceQuestionList.size(); i++) {
                    if (scienceQuestionList.get(i).getLstquestionchoice().size() > 0)
                        AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().insertAllQuestionChoices(scienceQuestionList.get(i).getLstquestionchoice());
                    if (!scienceQuestionList.get(i).getPhotourl().equalsIgnoreCase("")) {
                        DownloadMedia downloadMedia = new DownloadMedia();
                        downloadMedia.setPhotoUrl(/*Assessment_Constants.loadOnlineImagePath + */scienceQuestionList.get(i).getPhotourl());
                        downloadMedia.setqId(scienceQuestionList.get(i).getQid());
                        downloadMedia.setQtId(scienceQuestionList.get(i).getQtid());
                        //todo add new session
//                        downloadMedia.setPaperId(assessmentSession);
                        downloadMediaList.add(downloadMedia);
                    }

                }
//                progressDialog.dismiss();
                if (downloadMediaList.size() > 0) {
                    mediaProgressDialog.setTitle("Downloading media please wait..");
                    mediaProgressDialog.setMessage("Progress : ");

                    mediaProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mediaProgressDialog.setProgress(0);
                    mediaProgressDialog.setMax(100);
                    mediaProgressDialog.setCancelable(false);
                    if (downloadMediaList.get(mediaDownloadCnt).getQtId().contains("8"))
                        downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                }
            }
            BackupDatabase.backup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void downloadMedia(String qid, String photoUrl) {
        String dirPath = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded";
//       String url="http://pef1.prathamskills.org/CourseContent/Image/Question/9f602206-4732-442c-a880-4c6848a0a2eb1280.mp4";
//        mediaProgressDialog.show();
        String fileName = getFileName(qid, photoUrl);
        AndroidNetworking.download(photoUrl, dirPath, fileName)
//                .setTag("downloadTest")
//                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                        int progress = (int) (bytesDownloaded / totalBytes);
//                        progressDialog.setProgress(progress);
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
//                        progressDialog.dismiss();
                        downloadMediaList.get(mediaDownloadCnt).setDownloadSuccessful(true);
                        mediaDownloadCnt++;
                        if (mediaDownloadCnt < downloadMediaList.size())
                            downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
//                        progressDialog.dismiss();
                        downloadMediaList.get(mediaDownloadCnt).setDownloadSuccessful(true);
                        mediaDownloadCnt++;
                        if (mediaDownloadCnt < downloadMediaList.size())
                            downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                        Toast.makeText(ChooseAssessmentActivity.this, "Error downloading Media", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private String getFileName(String qid, String photoUrl) {
        String[] splittedPath = photoUrl.split("/");
        String fileName = qid + "_" + splittedPath[splittedPath.length - 1];
        return fileName;
    }

    @Override
    protected void onResume() {
        super.onResume();
        rlSubject.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        tv_choose_assessment.setText("Choose subject");
    }
}