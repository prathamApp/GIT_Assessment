package com.pratham.assessment.ui.choose_assessment.science;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.NonSwipeableViewPager;
import com.pratham.assessment.custom.circular_progress_view.CircleView;
import com.pratham.assessment.custom.dots_indicator.WormDotsIndicator;
import com.pratham.assessment.custom.swipeButton.ProSwipeButton;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.domain.AssessmentPatternDetails;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.bottomFragment.BottomQuestionFragment;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.ViewpagerAdapter;
import com.pratham.assessment.constants.APIs;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.robinhood.ticker.TickerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.pratham.assessment.constants.Assessment_Constants.EXAMID;
import static com.pratham.assessment.constants.Assessment_Constants.MULTIPLE_CHOICE;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;

@EActivity(R.layout.activity_science_assessment)
public class DownloadQuestionsActivity extends AppCompatActivity implements AssessmentAnswerListener {

    public static ViewpagerAdapter viewpagerAdapter;
    public static int ExamTime = 0;
    static boolean isActivityRunning = false;
    @ViewById(R.id.timer_progress_bar)
    public ProgressBar timer_progress_bar;
    /*  @BindView(R.id.ll_count_down)
      public RelativeLayout ll_count_down;*/
    @ViewById(R.id.rl_exam_info)
    public RelativeLayout rl_exam_info;
    @ViewById(R.id.rl_que)
    public RelativeLayout rl_que;
    List<String> examIDList = new ArrayList<>();
    List<String> topicIdList = new ArrayList<>();
    List<DownloadMedia> downloadMediaList;
    Intent serviceIntent;
    Fragment currentFragment;
    ProgressDialog progressDialog, mediaProgressDialog;
    List<ScienceQuestion> scienceQuestionList = new ArrayList<>();
    AssessmentPaperPattern assessmentPaperPatterns;
    List<AssessmentPatternDetails> assessmentPatternDetails;
    /*
        @BindView(R.id.circle_progress_bar)
        public ProgressBar circle_progress_bar;*/
    List<String> downloadFailedExamList = new ArrayList<>();
    int mediaDownloadCnt = 0;
    int queDownloadIndex = 0;
    int paperPatternCnt = 0;
    int ansCnt = 0, queCnt = 0;
    boolean showSubmit = false;
    List attemptedQIds = new ArrayList();
    boolean timesUp = false;
    @ViewById(R.id.fragment_view_pager)
    NonSwipeableViewPager fragment_view_pager;
    @ViewById(R.id.dots_indicator)
    WormDotsIndicator dots_indicator;
    @ViewById(R.id.tv_timer)
    TextView tv_timer;
    @ViewById(R.id.btn_save_Assessment)
    ImageView btn_save_Assessment;
    @ViewById(R.id.btn_submit)
    Button btn_submit;
    @ViewById(R.id.swipe_btn)
    ProSwipeButton swipe_btn;
    @ViewById(R.id.circle_view)
    CircleView circle_view;
    @ViewById(R.id.texture_view)
    TextureView texture_view;
    @ViewById(R.id.iv_prev)
    ImageView iv_prev;
    @ViewById(R.id.txt_prev)
    TextView txt_prev;
    @ViewById(R.id.txt_next)
    TextView txt_next;
    @ViewById(R.id.tv_exam_name)
    TextView tv_exam_name;
    @ViewById(R.id.tv_marks)
    TextView tv_marks;
    @ViewById(R.id.tv_time)
    TextView tv_time;
    @ViewById(R.id.tv_total_que)
    TextView tv_total_que;
    @ViewById(R.id.frame_video_monitoring)
    FrameLayout frame_video_monitoring;
    @ViewById(R.id.frame_supervisor)
    FrameLayout frame_supervisor;

    @ViewById(R.id.tickerView)
    TickerView tickerView;
    @ViewById(R.id.txt_question_cnt)
    TextView txt_question_cnt;
    boolean isEndTimeSet = false;

    int totalMarks = 0, outOfMarks = 0;
    String examStartTime, examEndTime;
    String answer = "", ansId = "";
    String questionType = "";
    CountDownTimer mCountDownTimer;
    String supervisorId, subjectId;
    String assessmentSession;
    private int correctAnsCnt = 0, wrongAnsCnt = 0, skippedCnt = 0;

    BottomQuestionFragment bottomQuestionFragment;
    Context context;

    @AfterViews
    public void init() {

        assessmentSession = "" + UUID.randomUUID().toString();
//        Assessment_Constants.assessmentSession=assessmentSession;
        downloadMediaList = new ArrayList<>();
        context = this;

        rl_que.setBackgroundColor(Assessment_Utility.selectedColor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supervisorId = getIntent().getStringExtra("crlId");
//        subjectId = getIntent().getStringExtra("subId");
//        subjectId = Assessment_Constants.SELECTED_SUBJECT_ID;
        subjectId = FastSave.getInstance().getString("SELECTED_SUBJECT_ID", "1");
        progressDialog = new ProgressDialog(this);
        mediaProgressDialog = new ProgressDialog(this);
//        showSelectTopicDialog();
       /* if (Assessment_Constants.VIDEOMONITORING) {
            frame_video_monitoring.setVisibility(View.VISIBLE);
//            btn_save_Assessment.setVisibility(View.GONE);
//            txt_next.setVisibility(View.GONE);
            serviceIntent = new Intent(getApplicationContext(), VideoMonitoringService.class);
        }*/
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            downloadPaperPattern();
        } else {
            AssessmentPaperPattern assessmentPaperPatterns = AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(Assessment_Constants.SELECTED_EXAM_ID);
            if (assessmentPaperPatterns != null) {
                generatePaperPattern();
//                showQuestions();
            } else {
                finish();
                Toast.makeText(context, getString(R.string.connect_to_internet_to_download_paper_format), Toast.LENGTH_SHORT).show();
            }
        }

     /*   AssessmentPaperPattern assessmentPaperPatterns = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(Assessment_Constants.SELECTED_EXAM_ID);
        if (assessmentPaperPatterns != null) {
            generatePaperPattern();
            showQuestions();
        } else {
            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                downloadPaperPattern();
            } else {
                finish();
                Toast.makeText(this, "Check internet connection to download paper", Toast.LENGTH_SHORT).show();
            }
        }*/

        //getTopicData();
        //scienceModalClassList = fetchJson("science.json");

        // setQuestions();

        Assessment_Constants.isShowcaseDisplayed = false;
        bottomQuestionFragment = new BottomQuestionFragment();


        Resources res = getResources();
// Change locale settings in the app.

        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("en");
        res.updateConfiguration(conf, dm);
    }


    private void downloadPaperPattern(/*final String examId, final String langId,
                                      final String subId*/) {
        String examId = FastSave.getInstance().getString(EXAMID, "");
        progressDialog.show();
        progressDialog.setMessage("Downloading paper pattern...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//        AndroidNetworking.get(APIs.AssessmentPaperPatternAPI + Assessment_Constants.SELECTED_EXAM_ID)
        AndroidNetworking.get(APIs.AssessmentPaperPatternAPI + examId)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        AssessmentPaperPattern assessmentPaperPattern = gson.fromJson(response, AssessmentPaperPattern.class);
                        if (assessmentPaperPattern != null)
                            AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().insertPaperPattern(assessmentPaperPattern);

                        List<AssessmentPatternDetails> assessmentPatternDetails = assessmentPaperPattern.getLstpatterndetail();
                        for (int i = 0; i < assessmentPatternDetails.size(); i++) {
                            assessmentPatternDetails.get(i).setExamId(assessmentPaperPattern.getExamid());

                        }

                        if (!assessmentPatternDetails.isEmpty())
                            insertPatternDetailsToDB(assessmentPatternDetails);

                       /* paperPatternCnt++;
                        if (paperPatternCnt < examIDList.size()) {
                            downloadPaperPattern();
                        } else {*/
                        progressDialog.dismiss();
//                            for (int i = 0; i < examIDList.size(); i++) {
                        List<String> topicsList = AppDatabase.getDatabaseInstance(context)
                                .getAssessmentPatternDetailsDao().getTopicsByExamId(examId);
                        for (int j = 0; j < topicsList.size(); j++) {
                            if (!topicIdList.contains(topicsList.get(j)))
                                topicIdList.add(topicsList.get(j));
                        }
//                            }
                        if (downloadFailedExamList.size() == 0)
                            if (topicIdList.size() > 0) {
                                queDownloadIndex = 0;
                                downloadQuestions(topicIdList.get(queDownloadIndex));
                            } else finish();/*if (!downloadTopicDialog.isShowing())
                                    downloadTopicDialog.show();*/

//                            selectTopicDialog.show();

                        if (downloadFailedExamList.size() > 0)
                            showDownloadFailedDialog(Assessment_Constants.SELECTED_LANGUAGE);
//                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        downloadFailedExamList.add(AppDatabase.getDatabaseInstance
                                (context).getTestDao().getExamNameById(examId));
                        paperPatternCnt++;
                      /*  if (paperPatternCnt < examIDList.size()) {

                            downloadPaperPattern(examIDList.get(paperPatternCnt), langId, subId);
                        } else {*/
                        progressDialog.dismiss();
//                            selectTopicDialog.show();


                        if (downloadFailedExamList.size() > 0)
                            showDownloadFailedDialog(Assessment_Constants.SELECTED_LANGUAGE);
                        /* }*/
//                        progressDialog.dismiss();
//                        Toast.makeText(ScienceAssessmentActivity.context, "Error downloading paper pattern..", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void downloadQuestions(final String topicId) {
        String questionUrl = APIs.AssessmentQuestionAPI + "languageid=" + Assessment_Constants.SELECTED_LANGUAGE + "&subjectid=" + subjectId + "&topicid=" + topicId;
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
                            queDownloadIndex++;
                            if (queDownloadIndex < topicIdList.size()) {
                                if (downloadFailedExamList.size() == 0)
                                    downloadQuestions(topicIdList.get(queDownloadIndex));
                            } else {
                                progressDialog.dismiss();
                                if (downloadFailedExamList.size() == 0) {
//                                    showSelectTopicDialog();
                                    generatePaperPattern();
//                                    showQuestions();
                                }

                            }
                        } else if (response.length() == 0) {
                            queDownloadIndex++;
                            if (queDownloadIndex < topicIdList.size())
                                downloadQuestions(topicIdList.get(queDownloadIndex));
                            else {
                                progressDialog.dismiss();
                                if (downloadFailedExamList.size() == 0) {
//                                    showSelectTopicDialog();
                                    generatePaperPattern();
//                                    showQuestions();
                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            if (downloadFailedExamList.size() == 0) {
//                                showSelectTopicDialog();
                                generatePaperPattern();
//                                showQuestions();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
                        AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                        finish();
                    }
                });
    }

    private void insertQuestionsToDB(JSONArray response) {
        try {
            Gson gson = new Gson();
            String jsonOutput = response.toString();
            Type listType = new TypeToken<List<ScienceQuestion>>() {
            }.getType();
            List<ScienceQuestion> scienceQuestionList = gson.fromJson(jsonOutput, listType);
            Log.d("hhh", scienceQuestionList.toString());

            if (scienceQuestionList.size() > 0) {
                AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().deleteByLangIdSubIdTopicId(scienceQuestionList.get(0).getTopicid(), Assessment_Constants.SELECTED_LANGUAGE, Assessment_Constants.SELECTED_SUBJECT_ID);
                for (int i = 0; i < scienceQuestionList.size(); i++) {
                    if (scienceQuestionList.get(i).getLstquestionchoice().size() > 0) {
                        List<ScienceQuestionChoice> choiceList = scienceQuestionList.get(i).getLstquestionchoice();
                        if (choiceList.size() > 0) {
                            AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().deleteQuestionChoicesByQID(scienceQuestionList.get(i).getQid());
                            AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().insertAllQuestionChoices(choiceList);
                            for (int j = 0; j < choiceList.size(); j++) {
                                if (!choiceList.get(j).getChoiceurl().equalsIgnoreCase("")) {
                                    DownloadMedia downloadMedia = new DownloadMedia();
                                    downloadMedia.setPhotoUrl(/*Assessment_Constants.loadOnlineImagePath + */choiceList.get(j).getChoiceurl());
                                    downloadMedia.setqId(scienceQuestionList.get(i).getQid());
                                    downloadMedia.setQtId(scienceQuestionList.get(i).getQtid());
                                    downloadMedia.setPaperId(assessmentSession);
                                    downloadMedia.setMediaType("optionImage");
                                    downloadMediaList.add(downloadMedia);
                                }
                                if (!choiceList.get(j).getMatchingurl().equalsIgnoreCase("")) {
                                    DownloadMedia downloadMedia = new DownloadMedia();
                                    downloadMedia.setPhotoUrl(/*Assessment_Constants.loadOnlineImagePath + */choiceList.get(j).getMatchingurl());
                                    downloadMedia.setqId(scienceQuestionList.get(i).getQid());
                                    downloadMedia.setQtId(scienceQuestionList.get(i).getQtid());
                                    downloadMedia.setPaperId(assessmentSession);
                                    downloadMedia.setMediaType("optionImage");
                                    downloadMediaList.add(downloadMedia);
                                }
                                if (scienceQuestionList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_CHOICE)) {
                                    if (choiceList.get(j).getCorrect().equalsIgnoreCase("true")) {
                                        if (choiceList.get(j).getChoiceurl().equalsIgnoreCase(""))
                                            scienceQuestionList.get(i).setAnswer(choiceList.get(j).getChoicename());
                                    }
                                }
                            }
                        }
                    }
                    AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().insertAllQuestions(scienceQuestionList);

                    if (!scienceQuestionList.get(i).getPhotourl().equalsIgnoreCase("")) {
                        DownloadMedia downloadMedia = new DownloadMedia();
                        downloadMedia.setPhotoUrl(/*Assessment_Constants.loadOnlineImagePath + */scienceQuestionList.get(i).getPhotourl());
                        downloadMedia.setqId(scienceQuestionList.get(i).getQid());
                        downloadMedia.setQtId(scienceQuestionList.get(i).getQtid());
                        downloadMedia.setMediaType("questionImage");
                        downloadMedia.setPaperId(assessmentSession);
                        downloadMediaList.add(downloadMedia);


                    }


                }
//                progressDialog.dismiss();
//                if (queDownloadIndex >= topicIdList.size()) {
//
//                    if (downloadMediaList.size() > 0) {
//
//                        mediaProgressDialog.setTitle("Downloading media please wait..");
////                    mediaProgressDialog.setMessage("Progress : ");
//
//                        mediaProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        mediaProgressDialog.setProgress(0);
//                        mediaProgressDialog.setMax(downloadMediaList.size());
//                        mediaProgressDialog.setCancelable(false);
//                        if (mediaProgressDialog != null && isActivityRunning)
//                            mediaProgressDialog.show();
////                    if (downloadMediaList.get(mediaDownloadCnt).getQtId().contains("8") || downloadMediaList.get(mediaDownloadCnt).getQtId().contains("9"))
//                        File direct = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH);
//                        if (!direct.exists())
//                            direct.mkdir();
//                        if (direct.exists())
//                            downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
//                        else {
//                            Toast.makeText(this, "Media download failed..", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//
//                    }
//                }
            }
            BackupDatabase.backup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadMedia(String qid, String photoUrl) {

        try {
//        String dirPath = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Downloaded";
            String dirPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH;

            String fileName = getFileName(qid, photoUrl);
            AndroidNetworking.download(photoUrl, dirPath, fileName)
                    //                .setTag("downloadTest")
                    //                .setPriority(Priority.MEDIUM)
                    .build()
                    .setDownloadProgressListener(new DownloadProgressListener() {
                        @Override
                        public void onProgress(long bytesDownloaded, long totalBytes) {
                            // do anything with progress
                            //                        int progress = (int) (bytesDownloaded / totalBytes);
                            mediaProgressDialog.setProgress(mediaDownloadCnt);
                        }
                    })
                    .startDownload(new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            // do anything after completion
                            //                        progressDialog.dismiss();
                            //                        downloadMediaList.get(mediaDownloadCnt).setDownloadSuccessful(true);
                            //                        mediaProgressDialog.setMessage("Progress : " + mediaDownloadCnt + "/" + downloadMediaList.size());

                            mediaDownloadCnt++;

                            if (mediaDownloadCnt < downloadMediaList.size())
                                downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                            else {
                                if (mediaProgressDialog != null && isActivityRunning) {
                                    mediaProgressDialog.dismiss();
                                }
                                createPaperPatten();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            //                        progressDialog.dismiss();
                            //                        downloadMediaList.get(mediaDownloadCnt).setDownloadSuccessful(true);
                            Log.d("media error:::", downloadMediaList.get(mediaDownloadCnt).getPhotoUrl() + " " + downloadMediaList.get(mediaDownloadCnt).getqId());
                            //                        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {

                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setContentView(R.layout.exit_dialog);
                            dialog.setCanceledOnTouchOutside(false);
                            TextView title = dialog.findViewById(R.id.dia_title);
                            Button skip_btn = dialog.findViewById(R.id.dia_btn_restart);
                            Button restart_btn = dialog.findViewById(R.id.dia_btn_exit);
                            Button cancel_btn = dialog.findViewById(R.id.dia_btn_cancel);
                            cancel_btn.setVisibility(View.VISIBLE);
                            title.setText("Media download failed..!");
                            restart_btn.setText("Skip All");
                            skip_btn.setText("Skip this");
                            cancel_btn.setText("Cancel");
                            dialog.show();

                            skip_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mediaDownloadCnt++;
                                    if (mediaDownloadCnt < downloadMediaList.size()) {
                                        downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                                    } else
                                        mediaProgressDialog.dismiss();
                                    dialog.dismiss();
                                    //                                createPaperPatten();
                                }
                            });

                            restart_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mediaDownloadCnt = downloadMediaList.size();
                                    /*if (mediaDownloadCnt < downloadMediaList.size()) {
                                        downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                                    } else*/
                                    mediaProgressDialog.dismiss();
                                    dialog.dismiss();
                                    createPaperPatten();

                                }
                            });

                            cancel_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mediaProgressDialog.dismiss();
                                    if (isActivityRunning) {
                                        if (scienceQuestionList.size() > 0)
                                            AppDatabase.getDatabaseInstance(context).getScienceQuestionDao().deleteQuestionByExamId(scienceQuestionList.get(0).getExamid());
                                        finish();
                                    }
                                }
                            });

                            /*} else {
                                Toast.makeText(context, "Connect to internet", Toast.LENGTH_SHORT).show();
                                finish();
                            }*/



                            /* mediaDownloadCnt++;
                            if (mediaDownloadCnt < downloadMediaList.size()) {
                                downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                            } else*/
                           /* mediaProgressDialog.dismiss();
                            AppDatabase.getDatabaseInstance(context).getScienceQuestionDao().deleteQuestionByExamId(scienceQuestionList.get(0).getExamid());
                            Toast.makeText(context, "Error downloading Media..Try again", Toast.LENGTH_SHORT).show();
                            finish();*/
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showDownloadFailedDialog(final String langId) {
        String failedExams = "";
        for (int i = 0; i < downloadFailedExamList.size(); i++) {
            failedExams = failedExams + "\n" + downloadFailedExamList.get(i);
        }
        new AlertDialog.Builder(this)
                .setTitle("Download Failed")
                .setCancelable(false)
                .setMessage("Download failed for following exams : " + failedExams)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        for (int i = 0; i < examIDList.size(); i++) {
                            List<String> topicsList = AppDatabase.getDatabaseInstance(context)
                                    .getAssessmentPatternDetailsDao().getTopicsByExamId(examIDList.get(i));
                            for (int j = 0; j < topicsList.size(); j++) {
                                if (topicIdList.contains(topicsList.get(j)))
                                    topicIdList.add(topicsList.get(j));
                            }
                        }
                        downloadFailedExamList.clear();
                        if (topicIdList.size() > 0)
                            downloadQuestions(topicIdList.get(queDownloadIndex));
                        else finish();

                            /* if (!downloadTopicDialog.isShowing())
                            downloadTopicDialog.show();*/
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void generatePaperPattern() {
        try {
            if (downloadMediaList.size() > 0)
                downloadMediaFiles();
            else {
                createPaperPatten();

                //        setExamInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPaperPatten() {

        try {
            String examId = FastSave.getInstance().getString(EXAMID, "");
            Assessment_Constants.SELECTED_EXAM_ID = examId;
            assessmentPaperPatterns = AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(Assessment_Constants.SELECTED_EXAM_ID);
            assessmentPatternDetails = AppDatabase.getDatabaseInstance(context).getAssessmentPatternDetailsDao().getAssessmentPatternDetailsByExamId(Assessment_Constants.SELECTED_EXAM_ID);
            // topicIdList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.context).getAssessmentPatternDetailsDao().getDistinctTopicIds();
            boolean isCameraQuestion = false;

            if (assessmentPatternDetails.size() > 0)
                for (int j = 0; j < assessmentPatternDetails.size(); j++) {
                    int noOfQues = Integer.parseInt(assessmentPatternDetails.get(j).getNoofquestion());


                    List<ScienceQuestion> scienceQuestions;
                    //                if (!assessmentPaperPatterns.getSubjectid().equalsIgnoreCase("30") || !assessmentPaperPatterns.getSubjectname().equalsIgnoreCase("aser")) {
//                    if (assessmentPaperPatterns.getIsRandom()) {
                        scienceQuestions = AppDatabase.getDatabaseInstance(context).
                                getScienceQuestionDao().getQuestionListByPattern(Assessment_Constants.SELECTED_LANGUAGE,
                                subjectId, assessmentPatternDetails.get(j).getTopicid(),
                                assessmentPatternDetails.get(j).getQtid(), assessmentPatternDetails.get(j).getQlevel(),
                                 noOfQues,"");
                    /*} else {
                        scienceQuestions = AppDatabase.getDatabaseInstance(context).
                                getScienceQuestionDao().getQuestionListByPatternRandomly(Assessment_Constants.SELECTED_LANGUAGE,
                                subjectId, assessmentPatternDetails.get(j).getTopicid(),
                                assessmentPatternDetails.get(j).getQtid(), assessmentPatternDetails.get(j).getQlevel(), noOfQues);

                    }*/

                    for (int i = 0; i < scienceQuestions.size(); i++) {
                        scienceQuestions.get(i).setOutofmarks(assessmentPatternDetails.get(j).getMarksperquestion());
                        scienceQuestions.get(i).setExamid(assessmentPatternDetails.get(j).getExamId());
                    }
                    if (scienceQuestions.size() > 0)
                        scienceQuestionList.addAll(scienceQuestions);

                  /*  String qtid = assessmentPatternDetails.get(j).getQtid();
                    if (qtid.equalsIgnoreCase("8") || qtid.equalsIgnoreCase("12"))
                        isCameraQuestion = true;*/

                }
        /*if (isCameraQuestion) {
            if (!AssessmentApplication.isTablet) {
                VideoMonitoringService.releaseMediaRecorder();
                Assessment_Constants.VIDEOMONITORING = false;
                texture_view.setVisibility(View.GONE);
                tv_timer.setTextColor(Color.BLACK);
                frame_video_monitoring.setVisibility(View.GONE);
                btn_save_Assessment.setVisibility(View.VISIBLE);
//            Toast.makeText(context, "video monitoring not prepared", Toast.LENGTH_LONG).show();
            }
        }*/


//        if (!assessmentPaperPatterns.getSubjectid().equalsIgnoreCase("30") || !assessmentPaperPatterns.getSubjectname().equalsIgnoreCase("aser"))
            if (assessmentPaperPatterns.getIsRandom()) Collections.shuffle(scienceQuestionList);
            for (int i = 0; i < scienceQuestionList.size(); i++) {
                scienceQuestionList.get(i).setPaperid(assessmentSession);
                String qid = scienceQuestionList.get(i).getQid();
                ArrayList<ScienceQuestionChoice> scienceQuestionChoiceList = (ArrayList<ScienceQuestionChoice>) AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(qid);
                scienceQuestionList.get(i).setLstquestionchoice(scienceQuestionChoiceList);
            }

            if (scienceQuestionList.size() <= 0) {
                finish();
                if (!AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                    Toast.makeText(context, "Connect to internet to download questions.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "No questions.", Toast.LENGTH_SHORT).show();
            }

           /* if (assessmentPaperPatterns.getExammode() != null) {
                if (assessmentPaperPatterns.getExammode().equalsIgnoreCase(Assessment_Constants.SUPERVISED)) {
                    frame_supervisor.setVisibility(View.VISIBLE);
                    rl_exam_info.setVisibility(View.GONE);
                    Assessment_Constants.supervisedAssessment = true;
                    Assessment_Constants.ASSESSMENT_TYPE = Assessment_Constants.SUPERVISED;
                    FastSave.getInstance().saveBoolean(Assessment_Constants.SUPERVISED, true);
                    if (mediaDownloadCnt >= downloadMediaList.size()) {
                        if (mediaProgressDialog != null && isActivityRunning)
                            mediaProgressDialog.dismiss();
                        Assessment_Utility.showFragment(this, new SupervisedAssessmentFragment_(), R.id.frame_supervisor, null, SupervisedAssessmentFragment.class.getSimpleName());
                    }
                }
            } else {*/
//                setExamInfo();
            Toast.makeText(context, "Questions downloaded successfully", Toast.LENGTH_SHORT).show();
            Log.d("QQQQ", "" + scienceQuestionList.size());
               /* Assessment_Constants.supervisedAssessment = false;
                Assessment_Constants.ASSESSMENT_TYPE = Assessment_Constants.PRACTICE;
                FastSave.getInstance().saveBoolean(Assessment_Constants.SUPERVISED, false);
            }*/
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void downloadMediaFiles() {
//        if (queDownloadIndex >= topicIdList.size()) {

        if (downloadMediaList.size() > 0) {

            mediaProgressDialog.setTitle(getString(R.string.downloading_media_please_wait));
//                    mediaProgressDialog.setMessage("Progress : ");

            mediaProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mediaProgressDialog.setProgress(0);
            mediaProgressDialog.setMax(downloadMediaList.size());
            mediaProgressDialog.setCancelable(false);
            if (mediaProgressDialog != null && isActivityRunning)
                mediaProgressDialog.show();
//                    if (downloadMediaList.get(mediaDownloadCnt).getQtId().contains("8") || downloadMediaList.get(mediaDownloadCnt).getQtId().contains("9"))
            File direct = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH);
            if (!direct.exists())
                direct.mkdir();
            if (direct.exists())
                downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
            else {
                Toast.makeText(context, "Media download failed..", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
//        }
    }

    private void insertPatternDetailsToDB(List<AssessmentPatternDetails> paperPatterns) {
        for (int i = 0; i < paperPatterns.size(); i++) {
            AppDatabase.getDatabaseInstance(context).getAssessmentPatternDetailsDao().deletePatternDetailsByExamId(paperPatterns.get(i).getExamId());
        }
        AppDatabase.getDatabaseInstance(context).getAssessmentPatternDetailsDao().insertAllPatternDetails(paperPatterns);

    }

    @Override
    public void onStart() {
        super.onStart();
        isActivityRunning = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActivityRunning = false;

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaProgressDialog != null && isActivityRunning && mediaProgressDialog.isShowing())
            if (mediaDownloadCnt >= downloadMediaList.size()) {
                mediaProgressDialog.dismiss();
            }
    }

    @Override
    public void setAnswerInActivity(String ansId, String answer, String qid, List<ScienceQuestionChoice> list) {

    }

    @Override
    public void removeSupervisorFragment() {
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count > 0) {
//                setExamInfo();
                frame_supervisor.setVisibility(View.GONE);
                rl_exam_info.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void setParagraph(String para, boolean isParaQuestion) {

    }*/
}
