package com.pratham.assessment.ui.choose_assessment.science;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.discrete_view.DSVOrientation;
import com.pratham.assessment.discrete_view.DiscreteScrollView;
import com.pratham.assessment.discrete_view.ScaleTransformer;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.domain.AssessmentPatternDetails;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentToipcsModal;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.ResultModalClass;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.services.BkgdVideoRecordingService;
import com.pratham.assessment.ui.choose_assessment.result.ResultActivity;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.bottomFragment.BottomQuestionFragment;
import com.pratham.assessment.ui.choose_assessment.science.camera.VideoMonitoringService;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.AssessmentTimeUpDialog;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.DownloadTopicsDialog;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.SelectTopicDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTrackerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.TopicSelectListener;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.VideoViewHolder;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.utilities.AudioUtil;
import com.pratham.assessment.utilities.PermissionUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pratham.assessment.ui.choose_assessment.science.viewHolders.FillInTheBlanksWithoutOptionsViewHolder.speech;

public class ScienceAssessmentActivity extends BaseActivity implements DiscreteScrollView.OnItemChangedListener, TopicSelectListener, AssessmentAnswerListener, QuestionTrackerListener {

    List<AssessmentPaperPattern> exams = new ArrayList<>();
    List<String> examIDList = new ArrayList<>();
    List<String> topicIdList = new ArrayList<>();
    List<AssessmentLanguages> languages = new ArrayList<>();
    List<AssessmentSubjects> subjects = new ArrayList<>();
    List<DownloadMedia> downloadMediaList;
    Intent serviceIntent;

    ProgressDialog progressDialog, mediaProgressDialog;
    List<ScienceQuestion> scienceQuestionList = new ArrayList<>();
    AssessmentPaperPattern assessmentPaperPatterns;
    List<AssessmentPatternDetails> assessmentPatternDetails;
    List<String> downloadFailedExamList = new ArrayList<>();

    int mediaDownloadCnt = 0;
    int queDownloadIndex = 0;
    int paperPatternCnt = 0;
    int ansCnt = 0, queCnt = 1;
    List attemptedQIds = new ArrayList();
    @BindView(R.id.question_discrete_view)
    DiscreteScrollView discreteScrollView;
    @BindView(R.id.timer)
    Chronometer chronometer;
    @BindView(R.id.tv_timer)
    TextView tv_timer;
    @BindView(R.id.btn_save_Assessment)
    Button save;

    @BindView(R.id.progressbar_timer)
    ProgressBar progressBarTimer;
    @BindView(R.id.texture_view)
    TextureView texture_view;

    @BindView(R.id.current_cnt)
    TextView currentCount;

    @BindView(R.id.iv_prev)
    ImageView iv_prev;
    @BindView(R.id.iv_next)
    ImageView iv_next;

    int i = 0;
    int totalMarks = 0, outOfMarks = 0;
    String examStartTime, examEndTime;
    String answer = "", ansId = "";
    String questionType = "";
    SelectTopicDialog selectTopicDialog;
    Dialog downloadTopicDialog;
    CountDownTimer mCountDownTimer;
    String supervisorId, subjectId;
    static boolean isActivityRunning = false;

    public static final String MULTIPLE_CHOICE = "1";
    public static final String MULTIPLE_SELECT = "2";
    public static final String TRUE_FALSE = "3";
    public static final String MATCHING_PAIR = "4";
    public static final String FILL_IN_THE_BLANK_WITH_OPTION = "5";
    public static final String FILL_IN_THE_BLANK = "6";
    public static final String ARRANGE_SEQUENCE = "7";
    public static final String VIDEO = "8";
    public static final String AUDIO = "9";
    public static int ExamTime = 0;
    private int correctAnsCnt = 0, wrongAnsCnt = 0, skippedCnt = 0;
    String langId;
    String assessmentSession;
    String videoName = "", filePath;
    RecyclerView.ViewHolder currentViewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science_assessment);
        ButterKnife.bind(this);
        assessmentSession = "" + UUID.randomUUID().toString();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supervisorId = getIntent().getStringExtra("crlId");
        subjectId = getIntent().getStringExtra("subId");
        progressDialog = new ProgressDialog(this);
        mediaProgressDialog = new ProgressDialog(this);
        showSelectTopicDialog();
        //getTopicData();
        //scienceModalClassList = fetchJson("science.json");

        // setQuestions();

        Assessment_Constants.isShowcaseDisplayed = false;


        Resources res = getResources();
// Change locale settings in the app.

        DisplayMetrics dm = res.getDisplayMetrics();

        android.content.res.Configuration conf = res.getConfiguration();

        conf.locale = new Locale("en");
        res.updateConfiguration(conf, dm);
//        serviceIntent = startService(new Intent(ScienceAssessmentActivity.this, BkgdVideoRecordingService.class));
        serviceIntent = new Intent(getApplicationContext(), VideoMonitoringService.class);
    }

    private void showSelectTopicDialog() {
        exams = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getAllAssessmentPaperPatterns();
        subjects = AppDatabase.getDatabaseInstance(this).getSubjectDao().getAllSubjects();
//        languages = AppDatabase.getDatabaseInstance(this).getLanguageDao().getAllLangs();
        selectTopicDialog = new SelectTopicDialog(this, this, exams, languages, subjectId);


        if (exams.size() > 0) {
            selectTopicDialog.show();
            selectTopicDialog.updateTopics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTopicDialog.dismiss();
                    exams.clear();
//                    getLanguageData();
                    showDownloadTopicDialog();

                }
            });
        } else {

//            getLanguageData();
            showDownloadTopicDialog();

        }
    }

    private void getLanguageData() {
        progressDialog.setMessage("Loading language");
        progressDialog.setCancelable(false);
        progressDialog.show();
        languages.clear();
        AndroidNetworking.get(APIs.AssessmentLanguageAPI)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                AssessmentLanguages assessmentLanguages = new AssessmentLanguages();
                                assessmentLanguages.setLanguageid(response.getJSONObject(i).getString("languageid"));
                                assessmentLanguages.setLanguagename(response.getJSONObject(i).getString("languagename"));
                                languages.add(assessmentLanguages);
                            }
                            AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getLanguageDao().insertAllLanguages(languages);

//                            getSubjectData();
                            showDownloadTopicDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading..Check internet connection.", Toast.LENGTH_SHORT).show();
                        finish();
                        AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().deletePaperPatterns();

                        progressDialog.dismiss();
                        downloadTopicDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }

 /*   private void getSubjectData() {
        subjects.clear();
        progressDialog.setMessage("Loading subjects");
        AndroidNetworking.get(APIs.AssessmentSubjectAPI)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                AssessmentSubjects assessmentSubjects = new AssessmentSubjects();
                                assessmentSubjects.setSubjectid(response.getJSONObject(i).getString("subjectid"));
                                assessmentSubjects.setSubjectname(response.getJSONObject(i).getString("subjectname"));
                                subjects.add(assessmentSubjects);
                            }
                            AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getSubjectDao().insertAllSubjects(subjects);
                            progressDialog.dismiss();
                            showDownloadTopicDialog();
                            //getTopicData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
                        AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                        finish();
                    }
                });

    }
*/

    private void showDownloadTopicDialog() {
        downloadTopicDialog = new DownloadTopicsDialog(this, this, exams, languages, subjectId);
        downloadTopicDialog.show();
    }

    /*  private List<ScienceModalClass> fetchJson(String jasonName) {
          List<ScienceModalClass> scienceModalClasses = new ArrayList<>();
          try {
              //InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/RC/" + jasonName);
              InputStream is = this.getAssets().open("" + jasonName);
              int size = is.available();
              byte[] buffer = new byte[size];
              is.read(buffer);
              is.close();
              String jsonStr = new String(buffer);
              Gson gson = new Gson();
              Type listType = new TypeToken<ArrayList<ScienceModalClass>>() {
              }.getType();
              scienceModalClasses = gson.fromJson(jsonStr, listType);

              // jsonArr = new JSONArray(jsonStr);
              //returnStoryNavigate = jsonObj.getJSONArray();
          } catch (Exception e) {
              e.printStackTrace();
          }
          return scienceModalClasses;
      }
  */
    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    @Override
    public void getSelectedItems(List<String> examIdList, String selectedLang, List<AssessmentToipcsModal> topics) {
        /*this.exams = exams;
        for (int i = 0; i < examIDList.size(); i++) {
            insertTopicsToDB(examIDList.get(i));
        }*/
        this.examIDList = examIdList;

//        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
        String langId = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangNameById(selectedLang);
        //  topicIdList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPatternDetailsDao().getDistinctTopicIds();

//        AssessmentPaperPattern pattern=new AssessmentPaperPattern();
//        pattern=AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId()
/*        List<String> examsToDownload = new ArrayList<>();
        List<AssessmentPatternDetails> patternDetails;
        if (!topicIdList.isEmpty()) {
            for (int i = 0; i < examIdList.size(); i++) {
                    String examId = examIdList.get(i);
                    patternDetails = AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao()
                            .getAssessmentPatternDetailsByExamId(examId);
                    if (patternDetails.size()==0) {
                        examsToDownload.add(examId);
                    }else {

                    }
                }

            if (examsToDownload.size()>0) {
                this.examIDList = examsToDownload;
                downloadPaperPattern(examIDList.get(paperPatternCnt), langId, subId);
            }

        } else {*/
        progressDialog.show();
        paperPatternCnt = 0;
        downloadPaperPattern(examIdList.get(paperPatternCnt), langId, subjectId);
       /* }

        topicIdList = AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().getDistinctTopicIds();
        downloadQuestions(topicIdList.get(queDownloadIndex), langId, subId);
*/
    }

    @Override
    public void getSelectedTopic(String exam, String selectedLang, SelectTopicDialog selectTopicDialog) {
//        String topicId = AppDatabase.getDatabaseInstance(this).getAssessmentTopicDao().getTopicIdByTopicName(topic);
        String examId = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getExamIdByExamName(exam);
//        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
        langId = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangNameById(selectedLang);

        generatePaperPattern(examId, subjectId, langId);

        showQuestions();

    }

    private void generatePaperPattern(String examId, String subId, String langId) {
        assessmentPaperPatterns = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(examId);
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
//        }
    }

    private void downloadQuestions(final String topicId, final String selectedLang,
                                   final String selectedSub) {
        String questionUrl = APIs.AssessmentQuestionAPI + "languageid=" + selectedLang + "&subjectid=" + selectedSub + "&topicid=" + topicId;
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
                                    downloadQuestions(topicIdList.get(queDownloadIndex), selectedLang, selectedSub);
                            } else {
                                progressDialog.dismiss();
                                if (downloadFailedExamList.size() == 0)
                                    showSelectTopicDialog();
                            }
                        } else if (response.length() == 0) {
                            queDownloadIndex++;
                            if (queDownloadIndex < topicIdList.size())
                                downloadQuestions(topicIdList.get(queDownloadIndex), selectedLang, selectedSub);
                            else {
                                progressDialog.dismiss();
                                if (downloadFailedExamList.size() == 0)
                                    showSelectTopicDialog();
                            }
                        } else {
                            progressDialog.dismiss();
                            if (downloadFailedExamList.size() == 0)
                                showSelectTopicDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
                        AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                        finish();
                    }
                });
    }

    private void downloadPaperPattern(final String examId, final String langId,
                                      final String subId) {
        progressDialog.setMessage("Downloading paper pattern...");
//        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentPaperPatternAPI + examId)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        AssessmentPaperPattern assessmentPaperPattern = gson.fromJson(response, AssessmentPaperPattern.class);
                        if (assessmentPaperPattern != null)
                            AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().insertPaperPattern(assessmentPaperPattern);

                        List<AssessmentPatternDetails> assessmentPatternDetails = assessmentPaperPattern.getLstpatterndetail();
                        for (int i = 0; i < assessmentPatternDetails.size(); i++) {
                            assessmentPatternDetails.get(i).setExamId(assessmentPaperPattern.getExamid());
                        }
                        if (!assessmentPatternDetails.isEmpty())
                            insertPatternDetailsToDB(assessmentPatternDetails);

                        paperPatternCnt++;
                        if (paperPatternCnt < examIDList.size()) {
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
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        downloadFailedExamList.add(AppDatabase.getDatabaseInstance
                                (ScienceAssessmentActivity.this).getTestDao().getExamNameById(examIDList.get(paperPatternCnt)));
                        paperPatternCnt++;
                        if (paperPatternCnt < examIDList.size()) {

                            downloadPaperPattern(examIDList.get(paperPatternCnt), langId, subId);
                        } else {
                            progressDialog.dismiss();
//                            selectTopicDialog.show();


                            if (downloadFailedExamList.size() > 0)
                                showDownloadFailedDialog(langId, subId);
                        }
//                        progressDialog.dismiss();
//                        Toast.makeText(ScienceAssessmentActivity.this, "Error downloading paper pattern..", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void showDownloadFailedDialog(final String langId, final String subId) {
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
                            List<String> topicsList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this)
                                    .getAssessmentPatternDetailsDao().getTopicsByExamId(examIDList.get(i));
                            for (int j = 0; j < topicsList.size(); j++) {
                                if (topicIdList.contains(topicsList.get(j)))
                                    topicIdList.add(topicsList.get(j));
                            }
                        }
                        downloadFailedExamList.clear();
                        if (topicIdList.size() > 0)
                            downloadQuestions(topicIdList.get(queDownloadIndex), langId, subId);
                        else if (!downloadTopicDialog.isShowing())
                            downloadTopicDialog.show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
                        downloadMedia.setPaperId(assessmentSession);
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
                        Toast.makeText(ScienceAssessmentActivity.this, "Error downloading Media", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFileName(String qid, String photoUrl) {
        String[] splittedPath = photoUrl.split("/");
        String fileName = qid + "_" + splittedPath[splittedPath.length - 1];
        return fileName;
    }

    private void showQuestions() {
        Collections.shuffle(scienceQuestionList);
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            scienceQuestionList.get(i).setPaperid(assessmentSession);
            String qid = scienceQuestionList.get(i).getQid();
            ArrayList<ScienceQuestionChoice> scienceQuestionChoiceList = (ArrayList<ScienceQuestionChoice>) AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().getQuestionChoicesByQID(qid);
            scienceQuestionList.get(i).setLstquestionchoice(scienceQuestionChoiceList);
        }
        if (scienceQuestionList.isEmpty()) {
            Toast.makeText(this, "No questions", Toast.LENGTH_SHORT).show();
        } else {
            selectTopicDialog.ll_count_down.setVisibility(View.VISIBLE);
            selectTopicDialog.ll_select_topic.setVisibility(View.GONE);
//            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.circle_progress_bar);
          /*  CountDownTimer countDownTimer = new CountDownTimer(4000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int time = (int) (millisUntilFinished / 1000);
                    //selectTopicDialog.timer.setText("" + time);
                    selectTopicDialog.circle_progress_bar.setProgress(time);
                    Log.d("jjjjjjjj", time + "");
                }

                @Override
                public void onFinish() {
                    selectTopicDialog.circle_progress_bar.setProgress(0);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            selectTopicDialog.dismiss();
                            setProgressBarAndTimer();
                        }
                    }, 800);

                }
            };
            countDownTimer.start();*/

            startCameraService();

            final long period = 50;
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //this repeats every 100 ms
                    if (i < 100) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             /*   selectTopicDialog.timer.setText(String.valueOf(i)+"%");
                                selectTopicDialog.timer.setText("" + time);
                                selectTopicDialog*/
                                selectTopicDialog.circle_progress_bar.setProgress(i);
                                Log.d("progress", "" + i);
                            }
                        });
//                        progressBar.setProgress(i);
                        i++;
                    } else {
                        //closing the timer
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                selectTopicDialog.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        selectTopicDialog.dismiss();
                                        setProgressBarAndTimer();
                                    }
                                }, 800);
                            }
                        });
                        timer.cancel();
                        timer.purge();
                    }
                }
            }, 0, period);


//            selectTopicDialog.dismiss();
            ScienceAdapter scienceAdapter = new ScienceAdapter(this, scienceQuestionList);
            discreteScrollView.setOrientation(DSVOrientation.HORIZONTAL);
            discreteScrollView.addOnItemChangedListener(this);
            discreteScrollView.setItemTransitionTimeMillis(200);
            discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.5f)
                    .build());
            discreteScrollView.setAdapter(scienceAdapter);

            discreteScrollView.addScrollListener(new DiscreteScrollView.ScrollListener<RecyclerView.ViewHolder>() {
                @Override
                public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
                    scienceQuestionList.get(currentPosition).setEndTime(Assessment_Utility.GetCurrentDateTime());
                    scienceQuestionList.get(newPosition).setStartTime(Assessment_Utility.GetCurrentDateTime());
                    Log.d("bbbbbbb", currentPosition + "");
                }
            });
            discreteScrollView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
                @Override
                public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                    queCnt = adapterPosition + 1;
                    currentCount.setText(queCnt + "/" + scienceQuestionList.size());
                    currentViewHolder = viewHolder;
                    Assessment_Utility.HideInputKeypad(ScienceAssessmentActivity.this);

                }

            });
            scienceAdapter.notifyDataSetChanged();
        }
    }

    private void setProgressBarAndTimer() {
        progressBarTimer.setProgress(100);
        ExamTime = Integer.parseInt(assessmentPaperPatterns.getExamduration());
//        ExamTime=2;
        if (ExamTime == 0)
            ExamTime = 30;
        final int timer = ExamTime * 60000;
     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/

         /*   }
        }, 1000);*/
        mCountDownTimer = new CountDownTimer(timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                tv_timer.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                ));

                int timeRemaining = (int) ((millisUntilFinished * 100) / timer);
                Log.v("Log_tag", "Tick of Progress" + millisUntilFinished + ";;; " + timeRemaining);
                if (timeRemaining < 5)
                    progressBarTimer.setProgressTintList(ColorStateList.valueOf(Color.RED));
                else if (timeRemaining < 15)
                    progressBarTimer.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorProgress15)));
                else if (timeRemaining < 25)
                    progressBarTimer.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorProgress25)));
                else if (timeRemaining < 50)
                    progressBarTimer.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorProgress50)));

                progressBarTimer.setProgress(timeRemaining);
                examStartTime = Assessment_Utility.GetCurrentDateTime();
                scienceQuestionList.get(0).setStartTime(Assessment_Utility.GetCurrentDateTime());

            }

            @Override
            public void onFinish() {
                if (isActivityRunning) {
                    try {
                        AssessmentTimeUpDialog timeUpDialog = new AssessmentTimeUpDialog(ScienceAssessmentActivity.this);
                        timeUpDialog.show();
                        progressBarTimer.setProgress(0);
//                        isActivityRunning=false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                onSaveAssessmentClick();
//                Toast.makeText(ScienceAssessmentActivity.this, "Time up...", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView title = dialog.findViewById(R.id.dia_title);
        Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);

        title.setText("Do you want to leave assessment?");
        restart_btn.setText("No");
        exit_btn.setText("Yes");
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScienceAssessmentActivity.super.onBackPressed();
                chronometer.stop();
                mCountDownTimer.cancel();


            }
        });

        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


    @OnClick(R.id.iv_prev)
    public void prevClick() {

        Assessment_Utility.HideInputKeypad(this);
        Animation animation;

        if (queCnt > 1) {

            scienceQuestionList.get(queCnt - 1).setEndTime(Assessment_Utility.GetCurrentDateTime());
            queCnt--;
            discreteScrollView.smoothScrollToPosition(queCnt - 1);
            scienceQuestionList.get(queCnt).setStartTime(Assessment_Utility.GetCurrentDateTime());

        }



        currentCount.setText(queCnt + "/" + scienceQuestionList.size());


    }


    @OnClick(R.id.iv_next)

    public void nextClick() {

        Assessment_Utility.HideInputKeypad(this);

        Animation animation;

        if (queCnt < scienceQuestionList.size()) {
          /*  iv_next.setVisibility(View.VISIBLE);
            iv_prev.setVisibility(View.VISIBLE);*/
            scienceQuestionList.get(queCnt).setEndTime(Assessment_Utility.GetCurrentDateTime());
            discreteScrollView.smoothScrollToPosition(queCnt);
            queCnt++;
            scienceQuestionList.get(queCnt - 1).setStartTime(Assessment_Utility.GetCurrentDateTime());

        } else if (queCnt == scienceQuestionList.size()) {
            queCnt = scienceQuestionList.size();

        }
        currentCount.setText(queCnt + "/" + scienceQuestionList.size());


    }


    @Override
    public void setAnswerInActivity(String ansId, String answer, String
            qid, List<ScienceQuestionChoice> list) {
        if (!ansId.equalsIgnoreCase("") || !answer.equalsIgnoreCase("")) {
            if (!attemptedQIds.contains(qid)) {
                attemptedQIds.add(qid);
                ansCnt++;
            }
            for (int i = 0; i < scienceQuestionList.size(); i++) {
                if (scienceQuestionList.get(i).getQid().equalsIgnoreCase(qid)) {
                    scienceQuestionList.get(i).setIsAttempted(true);
                    if (answer != null) {
                        scienceQuestionList.get(i).setUserAnswer(answer);
                        scienceQuestionList.get(i).setUserAnswerId(ansId);
//                        scienceQuestionList.get(i).setMarksPerQuestion(marksPerQuestion);
                    } else {
                        scienceQuestionList.get(i).setUserAnswer("");
                        scienceQuestionList.get(i).setUserAnswerId("");
                        scienceQuestionList.get(i).setUserAnswerId("");
                    }
                    break;
                }
            }
        } else if (list != null) {
            for (int i = 0; i < scienceQuestionList.size(); i++) {
                if (scienceQuestionList.get(i).getQid().equalsIgnoreCase(qid)) {
                    scienceQuestionList.get(i).setMatchingNameList(list);
                }
            }
            if (!attemptedQIds.contains(qid)) {
                attemptedQIds.add(qid);
                ansCnt++;
            }
            for (int i = 0; i < scienceQuestionList.size(); i++) {
                if (scienceQuestionList.get(i).getQid().equalsIgnoreCase(qid)) {
                    scienceQuestionList.get(i).setIsAttempted(true);
                    if (scienceQuestionList.get(i).getMatchingNameList().size() > 0) {
                        if (scienceQuestionList.get(i).getQtid().equalsIgnoreCase(MATCHING_PAIR) || scienceQuestionList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_SELECT)) {
                            String ans = "";
                            for (int m = 0; m < scienceQuestionList.get(i).getMatchingNameList().size(); m++) {
                                if (scienceQuestionList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_SELECT)) {
                                    if (scienceQuestionList.get(i).getMatchingNameList().get(m).getMyIscorrect().equalsIgnoreCase("true"))
                                        ans += scienceQuestionList.get(i).getMatchingNameList().get(m).getQcid() + ",";

                                } else
                                    ans += scienceQuestionList.get(i).getMatchingNameList().get(m).getQcid() + ",";
                            }
                            scienceQuestionList.get(i).setUserAnswer(ans);
                        } else {
                            scienceQuestionList.get(i).setUserAnswer(scienceQuestionList.get(i).getMatchingNameList().get(0).getChoicename());
                            scienceQuestionList.get(i).setUserAnswerId(scienceQuestionList.get(i).getMatchingNameList().get(0).getQcid());
//                        scienceQuestionList.get(i).setMarksPerQuestion(marksPerQuestion);
                        }
                    } else {
                        scienceQuestionList.get(i).setUserAnswer("");
                        scienceQuestionList.get(i).setUserAnswerId(ansId);
                    }
                    break;
                }
            }
        }
        this.answer = answer;
        if (ansId.equalsIgnoreCase(""))
            this.ansId = "-1";
        checkAssessment(queCnt);
    }

    @Override
    public void setVideoResult(Intent intent, int videoCapture, ScienceQuestion scienceQuestion) {
        if (hasCamera()) {
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA};

                if (!isPermissionsGranted(this, permissionArray)) {
                    Toast.makeText(this, "Give Camera permissions through settings and restart the app.", Toast.LENGTH_LONG).show();
                } else {
                    videoName = scienceQuestion.getPaperid() + "_" + scienceQuestion.getQid() + ".mp4";
                    scienceQuestion.setUserAnswer(videoName);
//                    Intent takePicture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3 * 60);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                    startActivityForResult(intent, VIDEO_CAPTURE);
                }
            } else {
                videoName = scienceQuestion.getPaperid() + "_" + scienceQuestion.getQid() + ".mp4";
                scienceQuestion.setUserAnswer(videoName);

//                Intent takePicture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3 * 60);
//                startActivityForResult(intent, VIDEO_CAPTURE);
            }
        } else {
            Toast.makeText(this, "Camera not found", Toast.LENGTH_LONG).show();
        }
        filePath = Environment.getExternalStorageDirectory().toString() + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + videoName;

        startActivityForResult(intent, videoCapture);
    }

    @Override
    public void setAudio(String path, boolean isRecording) {
        if (isRecording) {
            AudioUtil.startRecording(path);
        } else {
            AudioUtil.stopRecording();
        }
    }

    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }


    private void checkAssessment(int queCnt) {
        ScienceQuestion scienceQuestion = scienceQuestionList.get(queCnt - 1);
        questionType = scienceQuestion.getQtid();
        switch (questionType) {
            case FILL_IN_THE_BLANK_WITH_OPTION:
            case MULTIPLE_CHOICE:

                if (scienceQuestion.getIsAttempted())
                    if (scienceQuestion.getMatchingNameList().get(0).getCorrect().equalsIgnoreCase("true")) {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(true);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt - 1).getOutofmarks());

                    } else {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(false);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion("0");
                    }
              /*  if (scienceQuestion.getIsAttempted())
                    if (scienceQuestion.getAnswer().equalsIgnoreCase(answer)) {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(true);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt - 1).getOutofmarks());
                    } else {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(false);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion("0");
                    }*/
                break;
            case MULTIPLE_SELECT:
                boolean flag = true;
                if (scienceQuestion.getIsAttempted()) {
                    for (ScienceQuestionChoice scienceQuestionChoice : scienceQuestion.getMatchingNameList()) {
                        if (!scienceQuestionChoice.getCorrect().trim().equals(scienceQuestionChoice.getMyIscorrect().trim())) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(true);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt - 1).getOutofmarks());

                    } else {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(false);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion("0");
                    }
                }
                break;
            case FILL_IN_THE_BLANK:
            case TRUE_FALSE:
                if (scienceQuestion.getIsAttempted())
                    if (scienceQuestion.getAnswer().equalsIgnoreCase(answer)) {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(true);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt - 1).getOutofmarks());
                    } else {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(false);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion("0");
                    }
                break;
            case MATCHING_PAIR:
                if (scienceQuestion.getIsAttempted()) {
                    int matchPairCnt = 0;
                    List<ScienceQuestionChoice> queOptions = AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
                    for (int i = 0; i < queOptions.size(); i++) {
                        if (queOptions.get(i).getQcid().equalsIgnoreCase(scienceQuestion.getMatchingNameList().get(i).getQcid())) {
                            matchPairCnt++;
                        }
                    }
                    if (matchPairCnt == queOptions.size()) {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(true);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt - 1).getOutofmarks());

                    } else {
                        scienceQuestionList.get(queCnt - 1).setIsCorrect(false);
                        scienceQuestionList.get(queCnt - 1).
                                setMarksPerQuestion("0");
                    }
                }
                break;
            // TODO: 03-05-2019 arrange seq,audio,video
            case ARRANGE_SEQUENCE:
                break;
            case VIDEO:
                //todo decide marks,isCorrect for video
                scienceQuestionList.get(queCnt - 1).setUserAnswer(filePath);
                scienceQuestionList.get(queCnt - 1).setIsAttempted(true);
                scienceQuestionList.get(queCnt - 1).setIsCorrect(true);
                scienceQuestionList.get(queCnt - 1).
                        setMarksPerQuestion(scienceQuestionList.get(queCnt - 1).getOutofmarks());
                DownloadMedia downloadMedia = new DownloadMedia();
                downloadMedia.setPhotoUrl(filePath);
                downloadMedia.setqId(scienceQuestionList.get(queCnt - 1).getQid());
                downloadMedia.setQtId(scienceQuestionList.get(queCnt - 1).getQtid());
                downloadMedia.setPaperId(assessmentSession);
                AppDatabase.getDatabaseInstance(this).getDownloadMediaDao().insert(downloadMedia);

                break;
            case AUDIO:
                break;
        }
    }

    @OnClick(R.id.btn_save_Assessment)
    public void saveAssessment() {
        //if (ansCnt == scienceQuestionList.size()) {
//        showConfirmationDialog();
        /*questionTrackDialog = new QuestionTrackDialog(this,scienceQuestionList);
        questionTrackDialog.show();*/
        mCountDownTimer.cancel();
        scienceQuestionList.get(queCnt - 1).setEndTime(Assessment_Utility.GetCurrentDateTime());

        BottomQuestionFragment bottomQuestionFragment = new BottomQuestionFragment();
        bottomQuestionFragment.show(getSupportFragmentManager(), BottomQuestionFragment.class.getSimpleName());
        Bundle args = new Bundle();
        args.putSerializable("questionList", (Serializable) scienceQuestionList);
        bottomQuestionFragment.setArguments(args);
        // }
    }

    @Override
    public void onQuestionClick(int pos) {
//        questionTrackDialog.dismiss();
        queCnt = pos;
        currentCount.setText(queCnt + "/" + scienceQuestionList.size());
        discreteScrollView.smoothScrollToPosition(pos - 1);
    }

    @Override
    public void onSaveAssessmentClick() {
        stopService(new Intent(this, BkgdVideoRecordingService.class));

        examEndTime = Assessment_Utility.GetCurrentDateTime();
        calculateMarks();
        calculateCorrectWrongCount();
        AssessmentPaperForPush paper = new AssessmentPaperForPush();
        paper.setPaperStartTime(examStartTime);
        paper.setPaperEndTime(examEndTime);
        paper.setLanguageId(langId);
        paper.setExamId(scienceQuestionList.get(0).getExamid());
        paper.setSubjectId(scienceQuestionList.get(0).getSubjectid());
//        paper.setPaperId(scienceQuestionList.get(0).getPaperid());
        paper.setOutOfMarks("" + outOfMarks);
        paper.setPaperId(assessmentSession);
        paper.setTotalMarks("" + totalMarks);
        paper.setExamTime("" + ExamTime);
        paper.setCorrectCnt(correctAnsCnt);
        paper.setWrongCnt(wrongAnsCnt);
        paper.setSkipCnt(skippedCnt);
        paper.setSessionID(Assessment_Constants.currentSession);
        paper.setStudentId("" + Assessment_Constants.currentStudentID);
        paper.setExamName(assessmentPaperPatterns.getExamname());

        ArrayList<Score> scores = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            Score score = new Score();
            score.setQuestionId(Integer.parseInt(scienceQuestionList.get(i).getQid()));
            score.setLevel(getLevel(scienceQuestionList.get(i).getQlevel()));
            score.setIsAttempted(scienceQuestionList.get(i).getIsAttempted());
            score.setIsCorrect(scienceQuestionList.get(i).getIsCorrect());
            score.setTotalMarks(Integer.parseInt(scienceQuestionList.get(i).getOutofmarks()));
            score.setExamId(scienceQuestionList.get(i).getExamid());
            score.setStartDateTime(scienceQuestionList.get(i).getStartTime());
            if (supervisorId.equalsIgnoreCase(""))
                score.setLabel("practice");
            else score.setLabel("supervised assessment");
            score.setEndDateTime(scienceQuestionList.get(i).getEndTime());
            score.setStudentID(Assessment_Constants.currentStudentID);
            score.setDeviceID(AppDatabase.getDatabaseInstance(this).getStatusDao().getValue("DeviceId"));
//            score.setSessionID(Assessment_Constants.assessmentSession);
            score.setPaperId(assessmentSession);
            score.setSessionID(Assessment_Constants.currentSession);
            score.setScoredMarks(Integer.parseInt(scienceQuestionList.get(i).getMarksPerQuestion()));
            if (!scienceQuestionList.get(i).getUserAnswer().equalsIgnoreCase(""))
                score.setUserAnswer(scienceQuestionList.get(i).getUserAnswer());
            else if (scienceQuestionList.get(i).getUserAnswer().equalsIgnoreCase("") && !scienceQuestionList.get(i).getUserAnswerId().equalsIgnoreCase(""))
                score.setUserAnswer(scienceQuestionList.get(i).getUserAnswerId());
            scores.add(score);
        }
        paper.setScoreList(scores);
        AppDatabase.getDatabaseInstance(this).getScoreDao().insertAllScores(scores);
        AppDatabase.getDatabaseInstance(this).getAssessmentPaperForPushDao().insertPaperForPush(paper);

        generateResultData(paper);


        Toast.makeText(this, "Assessment saved successfully", Toast.LENGTH_SHORT).show();
        BackupDatabase.backup(this);

    }

    private void calculateCorrectWrongCount() {
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            if (scienceQuestionList.get(i).getIsCorrect()) correctAnsCnt++;
            else if (!scienceQuestionList.get(i).getIsCorrect() && scienceQuestionList.get(i).getIsAttempted())
                wrongAnsCnt++;
            if (!scienceQuestionList.get(i).getIsAttempted()) skippedCnt++;
        }
    }

    private void generateResultData(AssessmentPaperForPush paper) {
        ArrayList<ResultModalClass> resultList = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            ResultModalClass result = new ResultModalClass();
            result.setQuestion(scienceQuestionList.get(i).getQname());
            result.setqId(scienceQuestionList.get(i).getQid());
            result.setUserAnswer(scienceQuestionList.get(i).getUserAnswer());
            result.setUserAnswerId(scienceQuestionList.get(i).getUserAnswerId());
            result.setCorrectAnswer(scienceQuestionList.get(i).getAnswer());
            result.setCorrect(scienceQuestionList.get(i).getIsCorrect());
            result.setAttempted(scienceQuestionList.get(i).getIsAttempted());
            resultList.add(result);
        }

        if (resultList.size() > 0) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("result", resultList);
            intent.putExtra("outOfMarks", "" + outOfMarks);
            intent.putExtra("marksObtained", "" + totalMarks);
            intent.putExtra("studentId", Assessment_Constants.currentStudentID);
            intent.putExtra("examStartTime", examStartTime);
            intent.putExtra("examId", paper.getExamId());
            intent.putExtra("subjectId", paper.getSubjectId());
            intent.putExtra("examEndTime", examEndTime);
            startActivity(intent);
        }
    }

    private void calculateMarks() {

        for (int i = 0; i < scienceQuestionList.size(); i++) {
            totalMarks += Integer.parseInt(scienceQuestionList.get(i).getMarksPerQuestion());
            outOfMarks += Integer.parseInt(scienceQuestionList.get(i).getOutofmarks());
        }
    }

    private int getLevel(String qlevel) {
        int level = 0;
        if (qlevel.equalsIgnoreCase("easy"))
            level = 0;
        else if (qlevel.equalsIgnoreCase("normal"))
            level = 1;
        else if (qlevel.equalsIgnoreCase("difficult"))
            level = 2;
        return level;
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
        if (speech != null)
            speech.stopListening();
        speech = null;

        VideoMonitoringService.releaseMediaRecorder();
        stopService(serviceIntent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null)
            speech.stopListening();

    }

    @Override
    protected void onResume() {
        super.onResume();
//todo
//        resetSpeechRecognizer(); remove speech=null from onStop

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == -1) {
                showCapturedVideo();
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
          /*  File direct = new File(Environment.getExternalStorageDirectory().toString() + "/.Assessment");

            if (!direct.exists()) direct.mkdir();
           */
                File direct = new File(Environment.getExternalStorageDirectory().toString() + Assessment_Constants.STORE_ANSWER_MEDIA_PATH);
                if (!direct.exists()) direct.mkdir();

                File fileName = new File(direct, videoName);
                if (fileName.exists())
                    fileName.delete();
                OutputStream out = new FileOutputStream(fileName);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCapturedVideo() {
        ((VideoViewHolder) currentViewHolder).rl_answer_video.setVisibility(View.VISIBLE);

        checkAssessment(queCnt);


    }

    public void startCameraService() {
        startService(serviceIntent);
        String fileName = assessmentSession + ".mp4";
        if (VideoMonitoringService.prepareVideoRecorder(texture_view, fileName)) {
            VideoMonitoringService.startCapture();
        } else {
            texture_view.setVisibility(View.GONE);
            tv_timer.setTextColor(Color.BLACK);
            Toast.makeText(this, "not prepared", Toast.LENGTH_LONG).show();
        }
    }

}
