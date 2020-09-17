package com.pratham.assessment.ui.choose_assessment.science;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.async.PushDataToServer;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.NonSwipeableViewPager;
import com.pratham.assessment.custom.circular_progress_view.AnimationStyle;
import com.pratham.assessment.custom.circular_progress_view.CircleView;
import com.pratham.assessment.custom.circular_progress_view.CircleViewAnimation;
import com.pratham.assessment.custom.dots_indicator.WormDotsIndicator;
import com.pratham.assessment.custom.swipeButton.ProSwipeButton;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.discrete_view.DiscreteScrollView;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.domain.AssessmentPatternDetails;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.ResultModalClass;
import com.pratham.assessment.domain.ResultOuterModalClass;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.TempScienceQuestion;
import com.pratham.assessment.interfaces.DataPushListener;
import com.pratham.assessment.services.image_capture.APictureCapturingService;
import com.pratham.assessment.services.image_capture.PictureCapturingListener;
import com.pratham.assessment.services.image_capture.PictureCapturingServiceImpl;
import com.pratham.assessment.ui.choose_assessment.SupervisedAssessmentFragment;
import com.pratham.assessment.ui.choose_assessment.SupervisedAssessmentFragment_;
import com.pratham.assessment.ui.choose_assessment.result.ResultActivity_;
import com.pratham.assessment.ui.choose_assessment.science.bottomFragment.BottomQuestionFragment;
import com.pratham.assessment.services.camera.VideoMonitoringService;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.AssessmentTimeUpDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTrackerListener;
import com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.ViewpagerAdapter;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.atm.custom.LockNavigation.PinActivity;
import com.robinhood.ticker.TickerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.pratham.assessment.utilities.Assessment_Constants.ARRANGE_SEQUENCE;
import static com.pratham.assessment.utilities.Assessment_Constants.AUDIO;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_AUDIO;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING;
import static com.pratham.assessment.utilities.Assessment_Constants.EXAMID;
import static com.pratham.assessment.utilities.Assessment_Constants.FILL_IN_THE_BLANK;
import static com.pratham.assessment.utilities.Assessment_Constants.FILL_IN_THE_BLANK_WITH_OPTION;
import static com.pratham.assessment.utilities.Assessment_Constants.IMAGE_ANSWER;
import static com.pratham.assessment.utilities.Assessment_Constants.KEYWORDS_QUESTION;
import static com.pratham.assessment.utilities.Assessment_Constants.LANGUAGE;
import static com.pratham.assessment.utilities.Assessment_Constants.MATCHING_PAIR;
import static com.pratham.assessment.utilities.Assessment_Constants.MULTIPLE_CHOICE;
import static com.pratham.assessment.utilities.Assessment_Constants.MULTIPLE_SELECT;
import static com.pratham.assessment.utilities.Assessment_Constants.PARAGRAPH_BASED_QUESTION;
import static com.pratham.assessment.utilities.Assessment_Constants.TEXT_PARAGRAPH;
import static com.pratham.assessment.utilities.Assessment_Constants.TRUE_FALSE;
import static com.pratham.assessment.utilities.Assessment_Constants.VIDEO;
import static com.pratham.assessment.utilities.Assessment_Constants.VIDEOMONITORING;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/
//@Fullscreen
@EActivity(R.layout.activity_science_assessment)
public class ScienceAssessmentActivity extends BaseActivity implements PictureCapturingListener, DiscreteScrollView.OnItemChangedListener, AssessmentAnswerListener, QuestionTrackerListener, DataPushListener {


    public static ViewpagerAdapter viewpagerAdapter;
    public static int ExamTime = 0;
    static boolean isActivityRunning = false;
    /*    @ViewById(R.id.timer_progress_bar)
        public ProgressBar timer_progress_bar;*/
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
    boolean showSubmit = false, isAssessmentCompleted = false;
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
    @ViewById(R.id.tv_img_capture_warning)
    TextView tv_img_capture_warning;

    int imgCaptureCnt = 0;
    String imgFileName = "";
    int captureInterval = 0;

    public static boolean dialogOpen = false;
    /*  @ViewById(R.id.iv_question_mark)
      ImageView iv_question_mark;
  */
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

    PinActivity pinActivity;

    @Bean(PushDataToServer.class)
    PushDataToServer pushDataToServer;
    String selectedExamId, selectedLang, currentStudentID;

    List<TempScienceQuestion> attemptedList;


    private static final String[] requiredPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 1;
    private APictureCapturingService pictureService;

    @AfterViews
    public void init() {
        selectedExamId = FastSave.getInstance().getString(EXAMID, "");
        selectedLang = FastSave.getInstance().getString(LANGUAGE, "1");
        subjectId = FastSave.getInstance().getString("SELECTED_SUBJECT_ID", "1");
        currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
        pinActivity = new PinActivity();

        assessmentSession = UUID.randomUUID().toString();
//        Assessment_Constants.assessmentSession=assessmentSession;
        downloadMediaList = new ArrayList<>();
        attemptedList = new ArrayList<>();
//        iv_question_mark.setVisibility(View.GONE);
        rl_que.setBackgroundColor(Assessment_Utility.selectedColor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supervisorId = getIntent().getStringExtra("crlId");
        checkPermissions();
//        subjectId = getIntent().getStringExtra("subId");
//        subjectId = Assessment_Constants.SELECTED_SUBJECT_ID;
        progressDialog = new ProgressDialog(this);
        mediaProgressDialog = new ProgressDialog(this);

//        showSelectTopicDialog();
        if (Assessment_Constants.VIDEOMONITORING) {
            pictureService = PictureCapturingServiceImpl.getInstance(this);

//            frame_video_monitoring.setVisibility(View.VISIBLE);

//            serviceIntent = new Intent(getApplicationContext(), VideoMonitoringService.class);
        }


        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            downloadPaperPattern();
        } else {
            AssessmentPaperPattern assessmentPaperPatterns = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(selectedExamId);
            if (assessmentPaperPatterns != null) {
                generatePaperPattern();
//                showQuestions();
            } else {
                finish();
                Toast.makeText(this, "Connect to internet to download paper format.", Toast.LENGTH_SHORT).show();
            }
        }

     /*   AssessmentPaperPattern assessmentPaperPatterns = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(selectedExamId);
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

    private boolean checkPaperAlreadyAttempted() {
//        String currentSession = FastSave.getInstance().getString("currentSession", "");

        attemptedList = AppDatabase.getDatabaseInstance(this).getTempScienceQuestionDao().getAlreadyAttemptedPaper(currentStudentID, subjectId, selectedExamId, selectedLang);
        if (attemptedList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void createNewQuestionList(List<TempScienceQuestion> attemptedList) {
//        Collections.sort(attemptedList, (o1, o2) -> o1.getQid().compareTo(o2.getQid()));
        for (int i = 0; i < attemptedList.size(); i++) {
            String qid = attemptedList.get(i).getQid();
            Iterator iterator = scienceQuestionList.iterator();
            while (iterator.hasNext()) {
                ScienceQuestion scienceQuestion = (ScienceQuestion) iterator.next();
                if (scienceQuestion.getQid().equalsIgnoreCase(qid))
                    iterator.remove();

            }
      /*  }
        for (int i = 0; i < attemptedList.size(); i++) {*/
            ScienceQuestion scienceQuestion = new ScienceQuestion();
            scienceQuestion.setAnsdesc(attemptedList.get(i).getAnsdesc());
            scienceQuestion.setIsAttempted(attemptedList.get(i).isAttempted());
            scienceQuestion.setUpdatedby(attemptedList.get(i).getUpdatedby());
            scienceQuestion.setQlevel(attemptedList.get(i).getQlevel().trim());
            scienceQuestion.setAddedby(attemptedList.get(i).getAddedby());
            scienceQuestion.setLanguageid(attemptedList.get(i).getLanguageid());
            scienceQuestion.setActive(attemptedList.get(i).getActive());
            scienceQuestion.setLessonid(attemptedList.get(i).getLessonid());
//            scienceQuestion.setLstquestionchoice(attemptedList.get(i).getLstquestionchoice());
            scienceQuestion.setLstquestionchoice((ArrayList<ScienceQuestionChoice>) AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().getQuestionChoicesByQID(attemptedList.get(i).getQid()));
//            scienceQuestion.setMatchingNameList(attemptedList.get(i).getMatchingNameList());
            if (scienceQuestion.getIsAttempted()) {
                if (attemptedList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_SELECT) ||
                        attemptedList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_CHOICE) ||
                        attemptedList.get(i).getQtid().equalsIgnoreCase(MATCHING_PAIR) ||
                        attemptedList.get(i).getQtid().equalsIgnoreCase(ARRANGE_SEQUENCE) ||
                        attemptedList.get(i).getQtid().equalsIgnoreCase(IMAGE_ANSWER)) {
                    List<ScienceQuestionChoice> choices = new ArrayList<>();

                   /* switch (attemptedList.get(i).getQtid()) {
                        case MATCHING_PAIR:
                        case MULTIPLE_SELECT:
                        case ARRANGE_SEQUENCE:
                        case IMAGE_ANSWER:*/
                    String[] ans = attemptedList.get(i).getUserAnswer().split(",");
                    for (int k = 0; k < ans.length; k++) {
                        for (int m = 0; m < scienceQuestion.getLstquestionchoice().size(); m++) {
                            if (ans[k].equalsIgnoreCase(scienceQuestion.getLstquestionchoice().get(m).getQcid())) {
                                if (attemptedList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_SELECT)) {
                                    scienceQuestion.getLstquestionchoice().get(m).setMyIscorrect("true");
                                    choices.add(scienceQuestion.getLstquestionchoice().get(m));
                                } else
                                    choices.add(scienceQuestion.getLstquestionchoice().get(m));
                            }
                        }
//                            }
                        scienceQuestion.setMatchingNameList(choices);
//                            break;
                    }
                }
            }
            scienceQuestion.setQtid(attemptedList.get(i).getQtid());
            scienceQuestion.setQid(attemptedList.get(i).getQid());
            scienceQuestion.setSubjectid(attemptedList.get(i).getSubjectid());
            scienceQuestion.setAddedtime(attemptedList.get(i).getAddedtime());
            scienceQuestion.setUpdatedtime(attemptedList.get(i).getUpdatedtime());
            scienceQuestion.setPhotourl(attemptedList.get(i).getPhotourl());
            scienceQuestion.setExamtime(assessmentPaperPatterns.getExamduration().trim());
            scienceQuestion.setTopicid(attemptedList.get(i).getTopicid());
            scienceQuestion.setAnswer(attemptedList.get(i).getAnswer());
            scienceQuestion.setOutofmarks(attemptedList.get(i).getOutofmarks());
            scienceQuestion.setQname(attemptedList.get(i).getQname());
            scienceQuestion.setHint(attemptedList.get(i).getHint());
            scienceQuestion.setHint(attemptedList.get(i).getHint());
            scienceQuestion.setExamid(selectedExamId);
            scienceQuestion.setPdid(attemptedList.get(i).getPdid());
            scienceQuestion.setStartTime(attemptedList.get(i).getStartTime());
            scienceQuestion.setEndTime(attemptedList.get(i).getEndTime());
            scienceQuestion.setMarksPerQuestion(attemptedList.get(i).getMarksPerQuestion());
            scienceQuestion.setPaperid(attemptedList.get(i).getPaperId());
            scienceQuestion.setUserAnswerId(attemptedList.get(i).getUserAnswerId());
            scienceQuestion.setUserAnswer(attemptedList.get(i).getUserAnswer());
            scienceQuestion.setIsCorrect(attemptedList.get(i).isCorrect());
            scienceQuestion.setIsParaQuestion(attemptedList.get(i).isParaQuestion());
            scienceQuestion.setRefParaID(attemptedList.get(i).getRefParaID());
//            scienceQuestionList.add(scienceQuestion);
            scienceQuestionList.add(scienceQuestion);
        }
        if (scienceQuestionList.size() > 0)
            showSupervisedPracticeExam();
        else
            Toast.makeText(ScienceAssessmentActivity.this, "No questions.", Toast.LENGTH_SHORT).show();

    }


   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science_assessment);
        ButterKnife.bind(this);
        assessmentSession = "" + UUID.randomUUID().toString();
//        Assessment_Constants.assessmentSession=assessmentSession;


        rl_que.setBackgroundColor(Assessment_Utility.selectedColor);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supervisorId = getIntent().getStringExtra("crlId");
//        subjectId = getIntent().getStringExtra("subId");
//        subjectId = Assessment_Constants.SELECTED_SUBJECT_ID;
        subjectId = FastSave.getInstance().getString("SELECTED_SUBJECT_ID", "1");
        progressDialog = new ProgressDialog(this);
        mediaProgressDialog = new ProgressDialog(this);
//        showSelectTopicDialog();
        if (Assessment_Constants.VIDEOMONITORING) {
            frame_video_monitoring.setVisibility(View.VISIBLE);
            btn_save_Assessment.setVisibility(View.GONE);
            txt_next.setVisibility(View.GONE);
            serviceIntent = new Intent(getApplicationContext(), VideoMonitoringService.class);
        }
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            downloadPaperPattern();
        } else {
            AssessmentPaperPattern assessmentPaperPatterns = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(selectedExamId);
            if (assessmentPaperPatterns != null) {
                generatePaperPattern();
//                showQuestions();
            } else {
                finish();
                Toast.makeText(this, "Connect to internet to download paper format.", Toast.LENGTH_SHORT).show();
            }
        }

     *//*   AssessmentPaperPattern assessmentPaperPatterns = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(selectedExamId);
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
        }*//*

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
    }*/


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

   /* @Override
    public void getSelectedItems(List<String> examIdList, String selectedLang, List<AssessmentToipcsModal> topics) {
        *//*this.exams = exams;
        for (int i = 0; i < examIDList.size(); i++) {
            insertTopicsToDB(examIDList.get(i));
        }*//*
        this.examIDList = examIdList;

//        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
        String langId = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangIdByName(selectedLang);
        //  topicIdList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPatternDetailsDao().getDistinctTopicIds();

//        AssessmentPaperPattern pattern=new AssessmentPaperPattern();
//        pattern=AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId()
*//*        List<String> examsToDownload = new ArrayList<>();
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

        } else {*//*
        progressDialog.show();
        paperPatternCnt = 0;

//        downloadPaperPattern(examIdList.get(paperPatternCnt), langId, subjectId);
       *//* }


        topicIdList = AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().getDistinctTopicIds();
        downloadQuestions(topicIdList.get(queDownloadIndex), langId, subId);
*//*
    }
*/
   /* @Override
    public void getSelectedTopic(String exam, String selectedLang, SelectTopicDialog selectTopicDialog) {
//        String topicId = AppDatabase.getDatabaseInstance(this).getAssessmentTopicDao().getTopicIdByTopicName(topic);
        String examId = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getExamIdByExamName(exam);
//        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
//        langId = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangIdByName(selectedLang);

//        generatePaperPattern(examId, subjectId, selectedLang);

        showQuestions();

    }
*/

    private void downloadQuestions(final String topicId) {
        String questionUrl = APIs.AssessmentQuestionAPI + "languageid=" + selectedLang + "&subjectid=" + subjectId + "&topicid=" + topicId;
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
                                if (progressDialog != null && isActivityRunning && progressDialog.isShowing())
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
                                if (progressDialog != null && isActivityRunning && progressDialog.isShowing())
                                    progressDialog.dismiss();
                                if (downloadFailedExamList.size() == 0) {
//                                    showSelectTopicDialog();
                                    generatePaperPattern();
//                                    showQuestions();
                                }
                            }
                        } else {
                            if (progressDialog != null && isActivityRunning && progressDialog.isShowing())
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
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
                        AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPaperPatternDao().deletePaperPatterns();
                        if (progressDialog != null && isActivityRunning && progressDialog.isShowing())
                            progressDialog.dismiss();
                        finish();
                    }
                });
    }

    private void downloadPaperPattern(/*final String examId, final String langId,
                                      final String subId*/) {
        progressDialog.show();
        progressDialog.setMessage("Downloading paper pattern...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentPaperPatternAPI + selectedExamId)
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

                       /* paperPatternCnt++;
                        if (paperPatternCnt < examIDList.size()) {
                            downloadPaperPattern();
                        } else {*/
                        if (progressDialog != null && isActivityRunning && progressDialog.isShowing())
                            progressDialog.dismiss();
//                            for (int i = 0; i < examIDList.size(); i++) {
                        List<String> topicsList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this)
                                .getAssessmentPatternDetailsDao().getTopicsByExamId(selectedExamId);
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
                            showDownloadFailedDialog(selectedLang);
//                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        downloadFailedExamList.add(AppDatabase.getDatabaseInstance
                                (ScienceAssessmentActivity.this).getTestDao().getExamNameById(selectedExamId));
                        paperPatternCnt++;
                      /*  if (paperPatternCnt < examIDList.size()) {

                            downloadPaperPattern(examIDList.get(paperPatternCnt), langId, subId);
                        } else {*/
                        if (progressDialog != null && isActivityRunning && progressDialog.isShowing())
                            progressDialog.dismiss();
//                            selectTopicDialog.show();


                        if (downloadFailedExamList.size() > 0)
                            showDownloadFailedDialog(selectedLang);
                        /* }*/
//                        progressDialog.dismiss();
//                        Toast.makeText(ScienceAssessmentActivity.this, "Error downloading paper pattern..", Toast.LENGTH_SHORT).show();
                    }
                });


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
                            List<String> topicsList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this)
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

    private void insertPatternDetailsToDB(List<AssessmentPatternDetails> paperPatterns) {
        for (int i = 0; i < paperPatterns.size(); i++) {
            AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().deletePatternDetailsByExamId(paperPatterns.get(i).getExamId());
        }
        AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().insertAllPatternDetails(paperPatterns);

    }

    /*private void insertQuestionsToDB(JSONArray response) {
        try {
            downloadMediaList = new ArrayList<>();
            Gson gson = new Gson();
            String jsonOutput = response.toString();
            Type listType = new TypeToken<List<ScienceQuestion>>() {
            }.getType();
            List<ScienceQuestion> scienceQuestionList = gson.fromJson(jsonOutput, listType);
            Log.d("hhh", scienceQuestionList.toString());

            if (scienceQuestionList.size() > 0) {
                AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().deleteByLangIdSubIdTopicId(scienceQuestionList.get(0).getTopicid(), selectedLang, Assessment_Constants.SELECTED_SUBJECT_ID);
                AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().insertAllQuestions(scienceQuestionList);
                for (int i = 0; i < scienceQuestionList.size(); i++) {
                    if (scienceQuestionList.get(i).getLstquestionchoice().size() > 0) {
                        List<ScienceQuestionChoice> choiceList = scienceQuestionList.get(i).getLstquestionchoice();
                        if (choiceList.size() > 0) {
                            AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().deleteQuestionChoicesByQID(scienceQuestionList.get(i).getQid());
                            AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().insertAllQuestionChoices(choiceList);
                            for (int j = 0; j < choiceList.size(); j++) {
                                if (!choiceList.get(j).getChoiceurl().equalsIgnoreCase("")) {
                                    DownloadMedia downloadMedia = new DownloadMedia();
                                    downloadMedia.setPhotoUrl(*//*Assessment_Constants.loadOnlineImagePath + *//*choiceList.get(j).getChoiceurl());
                                    downloadMedia.setqId(scienceQuestionList.get(i).getQid());
                                    downloadMedia.setQtId(scienceQuestionList.get(i).getQtid());
                                    downloadMedia.setPaperId(assessmentSession);
                                    downloadMedia.setMediaType("optionImage");
                                    downloadMediaList.add(downloadMedia);
                                }
                            }
                        }
                    }
                    if (!scienceQuestionList.get(i).getPhotourl().equalsIgnoreCase("")) {
                        DownloadMedia downloadMedia = new DownloadMedia();
                        downloadMedia.setPhotoUrl(*//*Assessment_Constants.loadOnlineImagePath + *//*scienceQuestionList.get(i).getPhotourl());
                        downloadMedia.setqId(scienceQuestionList.get(i).getQid());
                        downloadMedia.setQtId(scienceQuestionList.get(i).getQtid());
                        downloadMedia.setMediaType("questionImage");
                        downloadMedia.setPaperId(assessmentSession);
                        downloadMediaList.add(downloadMedia);


                    }


                }
//                progressDialog.dismiss();
                if (downloadMediaList.size() > 0) {

                    mediaProgressDialog.setTitle("Downloading media please wait..");
//                    mediaProgressDialog.setMessage("Progress : ");

                    mediaProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mediaProgressDialog.setProgress(0);
                    mediaProgressDialog.setMax(downloadMediaList.size());
                    mediaProgressDialog.setCancelable(false);
                    mediaProgressDialog.show();
//                    if (downloadMediaList.get(mediaDownloadCnt).getQtId().contains("8") || downloadMediaList.get(mediaDownloadCnt).getQtId().contains("9"))
                    File direct = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH);
                    if (!direct.exists())
                        direct.mkdir();
                    if (direct.exists())
                        downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                    else {
                        Toast.makeText(this, "Media download failed..", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            }
            BackupDatabase.backup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void insertQuestionsToDB(JSONArray response) {
        try {
            Gson gson = new Gson();
            String jsonOutput = response.toString();
            Type listType = new TypeToken<List<ScienceQuestion>>() {
            }.getType();
            List<ScienceQuestion> scienceQuestionList = gson.fromJson(jsonOutput, listType);
            Log.d("hhh", scienceQuestionList.toString());

            if (scienceQuestionList.size() > 0) {
                AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().deleteByLangIdSubIdTopicId(scienceQuestionList.get(0).getTopicid(), selectedLang, Assessment_Constants.SELECTED_SUBJECT_ID);
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
                                if (scienceQuestionList.get(i).getQtid().equalsIgnoreCase(TRUE_FALSE)) {
                                    if (choiceList.get(j).getCorrect().equalsIgnoreCase("true")) {
                                        scienceQuestionList.get(i).setAnswer(choiceList.get(j).getChoicename().toLowerCase());
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

                            final Dialog dialog = new Dialog(ScienceAssessmentActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setContentView(R.layout.exit_dialog);
                            dialog.setCanceledOnTouchOutside(false);
                            TextView title = dialog.findViewById(R.id.dia_title);
                            Button skip_btn = dialog.findViewById(R.id.dia_btn_restart);
                            Button restart_btn = dialog.findViewById(R.id.dia_btn_exit);
                            Button cancel_btn = dialog.findViewById(R.id.dia_btn_cancel);
                            cancel_btn.setVisibility(View.VISIBLE);
                            title.setText(R.string.Media_download_failed);
                            restart_btn.setText(R.string.skip_all);
                            skip_btn.setText(R.string.skip_this);
                            cancel_btn.setText(R.string.cancel);
                            dialog.show();

                            skip_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mediaDownloadCnt++;
                                    if (mediaDownloadCnt < downloadMediaList.size()) {
                                        downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                                    } else if (mediaProgressDialog != null && isActivityRunning)
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
                                    if (mediaProgressDialog != null && isActivityRunning)
                                        mediaProgressDialog.dismiss();
                                    dialog.dismiss();
                                    createPaperPatten();

                                }
                            });

                            cancel_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mediaProgressDialog != null && isActivityRunning)
                                        mediaProgressDialog.dismiss();
                                    if (isActivityRunning) {
                                        if (scienceQuestionList.size() > 0)
                                            AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getScienceQuestionDao().deleteQuestionByExamId(selectedExamId);
                                        finish();
                                    }
                                }
                            });

                            /*} else {
                                Toast.makeText(ScienceAssessmentActivity.this, "Connect to internet", Toast.LENGTH_SHORT).show();
                                finish();
                            }*/



                            /* mediaDownloadCnt++;
                            if (mediaDownloadCnt < downloadMediaList.size()) {
                                downloadMedia(downloadMediaList.get(mediaDownloadCnt).getqId(), downloadMediaList.get(mediaDownloadCnt).getPhotoUrl());
                            } else*/
                           /* mediaProgressDialog.dismiss();
                            AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getScienceQuestionDao().deleteQuestionByExamId(scienceQuestionList.get(0).getExamid());
                            Toast.makeText(ScienceAssessmentActivity.this, "Error downloading Media..Try again", Toast.LENGTH_SHORT).show();
                            finish();*/
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            finish();
            e.printStackTrace();
        }
    }

    private void createPaperPatten() {

        try {
            assessmentPaperPatterns = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getAssessmentPaperPatternsByExamId(selectedExamId);
            assessmentPatternDetails = AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().getAssessmentPatternDetailsByExamId(selectedExamId);

            // topicIdList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPatternDetailsDao().getDistinctTopicIds();
            boolean isCameraQuestion = false;
            ScienceQuestion para = new ScienceQuestion();

            if (assessmentPatternDetails.size() > 0)
                for (int j = 0; j < assessmentPatternDetails.size(); j++) {
                    int noOfQues = Integer.parseInt(assessmentPatternDetails.get(j).getNoofquestion());


                    List<ScienceQuestion> scienceQuestions;
                    //                if (!assessmentPaperPatterns.getSubjectid().equalsIgnoreCase("30") || !assessmentPaperPatterns.getSubjectname().equalsIgnoreCase("aser")) {
                    para = AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().getParagraphs(selectedLang,
                            subjectId, assessmentPatternDetails.get(j).getTopicid(), assessmentPatternDetails.get(j).getParalevel());

                    if (assessmentPaperPatterns.getIsRandom()) {

                        scienceQuestions = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                                getScienceQuestionDao().getQuestionListByPattern(selectedLang,
                                subjectId, assessmentPatternDetails.get(j).getTopicid(),
                                assessmentPatternDetails.get(j).getQtid(), assessmentPatternDetails.get(j).getQlevel(), noOfQues);
                    } else {
                        scienceQuestions = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                                getScienceQuestionDao().getQuestionListByPatternRandomly(selectedLang,
                                subjectId, assessmentPatternDetails.get(j).getTopicid(),
                                assessmentPatternDetails.get(j).getQtid(), assessmentPatternDetails.get(j).getQlevel(), noOfQues);

                    }

                    for (int i = 0; i < scienceQuestions.size(); i++) {
                        scienceQuestions.get(i).setOutofmarks(assessmentPatternDetails.get(j).getMarksperquestion());
                        scienceQuestions.get(i).setExamid(assessmentPatternDetails.get(j).getExamId());
                    }
                    if (scienceQuestions.size() > 0) {
                        scienceQuestionList.addAll(scienceQuestions);
                       /* if (assessmentPaperPatterns.getIsRandom())
                        Collections.shuffle(scienceQuestionList);*/
                    }

                    List<ScienceQuestion> paraQuestionList;
                    if (para != null) {
                        if (para.getQtid().equalsIgnoreCase(PARAGRAPH_BASED_QUESTION)) {

                            paraQuestionList = AppDatabase.getDatabaseInstance(this)
                                    .getScienceQuestionDao().getParaQuestionByQidAndLevel(para.getQid(), assessmentPatternDetails.get(j).getQlevel());

                            if (paraQuestionList.size() > 0) {
                                for (int k = 0; k < assessmentPatternDetails.size(); k++) {
                                    if (assessmentPatternDetails.get(k).getQtid()
                                            .equalsIgnoreCase(para.getQtid()) &&
                                            assessmentPatternDetails.get(k).getTopicid()
                                                    .equalsIgnoreCase(para.getTopicid()) &&
                                            assessmentPatternDetails.get(k).getParalevel()
                                                    .equalsIgnoreCase(para.getQlevel())) {
                                        for (int p = 0; p < paraQuestionList.size(); p++) {
                                            if (paraQuestionList.get(p).getQlevel().equalsIgnoreCase(assessmentPatternDetails.get(k).getQlevel()))
                                                paraQuestionList.get(p).setOutofmarks(assessmentPatternDetails.get(k).getMarksperquestion() + "");
                                        }
                                    }
                                }
                                scienceQuestionList.addAll(paraQuestionList);
                            }
                        }
                    }
//                        }
                }


//        if (!assessmentPaperPatterns.getSubjectid().equalsIgnoreCase("30") || !assessmentPaperPatterns.getSubjectname().equalsIgnoreCase("aser"))
            for (int i = 0; i < scienceQuestionList.size(); i++) {
                scienceQuestionList.get(i).setPaperid(assessmentSession);
                String qid = scienceQuestionList.get(i).getQid();
                ArrayList<ScienceQuestionChoice> scienceQuestionChoiceList = (ArrayList<ScienceQuestionChoice>) AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getScienceQuestionChoicesDao().getQuestionChoicesByQID(qid);
                scienceQuestionList.get(i).setLstquestionchoice(scienceQuestionChoiceList);
            }

            if (scienceQuestionList.size() <= 0) {
                finish();
                if (!AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork())
                    Toast.makeText(ScienceAssessmentActivity.this, "Connect to internet to download questions.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ScienceAssessmentActivity.this, "No questions.", Toast.LENGTH_SHORT).show();
            }
            if (scienceQuestionList.size() > 0) {
                boolean examAttempted = checkPaperAlreadyAttempted();
                if (examAttempted)
                    createNewQuestionList(attemptedList);
                else {
                    showSupervisedPracticeExam();

                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void showSupervisedPracticeExam() {
        saveTempPaper(scienceQuestionList);

//            JSONArray jsArray = new JSONArray(scienceQuestionList);
        String objectString = new Gson().toJson(scienceQuestionList);
        Log.d("showQuestions", "showQuestions: " + objectString);

        if (assessmentPaperPatterns.getExammode() != null) {
            if (assessmentPaperPatterns.getExammode().equalsIgnoreCase(Assessment_Constants.SUPERVISED)) {
                pinActivity = new PinActivity();
//                pinActivity.init(this, getApplication());
                frame_supervisor.setVisibility(View.VISIBLE);
                rl_exam_info.setVisibility(View.GONE);
                Assessment_Constants.supervisedAssessment = true;
                VIDEOMONITORING = true;
                Assessment_Constants.ASSESSMENT_TYPE = Assessment_Constants.SUPERVISED;
                FastSave.getInstance().saveBoolean(Assessment_Constants.SUPERVISED, true);
                if (mediaDownloadCnt >= downloadMediaList.size()) {
                    if (mediaProgressDialog != null && isActivityRunning)
                        mediaProgressDialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("paperId", assessmentSession);
                    Assessment_Utility.showFragment(this, new SupervisedAssessmentFragment_(), R.id.frame_supervisor, bundle, SupervisedAssessmentFragment.class.getSimpleName());
                }
            } else {
                setExamInfo();
                Assessment_Constants.supervisedAssessment = false;
                VIDEOMONITORING = false;
                Assessment_Constants.ASSESSMENT_TYPE = Assessment_Constants.PRACTICE;
                FastSave.getInstance().saveBoolean(Assessment_Constants.SUPERVISED, false);
            }
        } else {
            VIDEOMONITORING = false;
            setExamInfo();
            Assessment_Constants.supervisedAssessment = false;
            Assessment_Constants.ASSESSMENT_TYPE = Assessment_Constants.PRACTICE;
            FastSave.getInstance().saveBoolean(Assessment_Constants.SUPERVISED, false);
        }
    }

    private void saveTempPaper(List<ScienceQuestion> tempScienceQuestionList) {
        try {
            List<TempScienceQuestion> tempList = new ArrayList<>();
            calculateMarks();
            for (int i = 0; i < tempScienceQuestionList.size(); i++) {
                TempScienceQuestion tempScienceQuestion = new TempScienceQuestion();
                tempScienceQuestion.setAnsdesc(tempScienceQuestionList.get(i).getAnsdesc());
                tempScienceQuestion.setUpdatedby(tempScienceQuestionList.get(i).getUpdatedby());
                tempScienceQuestion.setQlevel(tempScienceQuestionList.get(i).getQlevel());
                tempScienceQuestion.setAddedby(tempScienceQuestionList.get(i).getAddedby());
                tempScienceQuestion.setLanguageid(tempScienceQuestionList.get(i).getLanguageid());
                tempScienceQuestion.setActive(tempScienceQuestionList.get(i).getActive());
                tempScienceQuestion.setLessonid(tempScienceQuestionList.get(i).getLessonid());
                tempScienceQuestion.setQtid(tempScienceQuestionList.get(i).getQtid());
                tempScienceQuestion.setQid(tempScienceQuestionList.get(i).getQid());
                tempScienceQuestion.setSubjectid(tempScienceQuestionList.get(i).getSubjectid());
                tempScienceQuestion.setAddedtime(tempScienceQuestionList.get(i).getAddedtime());
                tempScienceQuestion.setUpdatedtime(tempScienceQuestionList.get(i).getUpdatedtime());
                tempScienceQuestion.setPhotourl(tempScienceQuestionList.get(i).getPhotourl());
                tempScienceQuestion.setExamtime(assessmentPaperPatterns.getExamduration());
                tempScienceQuestion.setTopicid(tempScienceQuestionList.get(i).getTopicid());
                tempScienceQuestion.setAnswer(tempScienceQuestionList.get(i).getAnswer());
                tempScienceQuestion.setOutofmarks(tempScienceQuestionList.get(i).getOutofmarks());
                tempScienceQuestion.setQname(tempScienceQuestionList.get(i).getQname());
                tempScienceQuestion.setHint(tempScienceQuestionList.get(i).getHint());
                tempScienceQuestion.setExamid(selectedExamId);
                tempScienceQuestion.setPdid(tempScienceQuestionList.get(i).getPdid());
                tempScienceQuestion.setStartTime(tempScienceQuestionList.get(i).getStartTime());
                tempScienceQuestion.setLstquestionchoice(tempScienceQuestionList.get(i).getLstquestionchoice());
                //            tempScienceQuestion.setMatchingNameList(tempScienceQuestionList.get(i).getMatchingNameList());
                tempScienceQuestion.setEndTime(tempScienceQuestionList.get(i).getEndTime());
                tempScienceQuestion.setMarksPerQuestion(tempScienceQuestionList.get(i).getMarksPerQuestion());
                //            tempScienceQuestion.setUserAnswerId(tempScienceQuestionList.get(i).getUserAnswerId());
                //            tempScienceQuestion.setUserAnswer(tempScienceQuestionList.get(i).getUserAnswer());
                //            tempScienceQuestion.setAttempted(tempScienceQuestionList.get(i).getIsAttempted());
                //            tempScienceQuestion.setCorrect(tempScienceQuestionList.get(i).getIsCorrect());
                tempScienceQuestion.setRefParaID(tempScienceQuestionList.get(i).getRefParaID());
                tempScienceQuestion.setPaperId(tempScienceQuestionList.get(i).getPaperid());
                tempScienceQuestion.setPaperStartDateTime(examStartTime);
                tempScienceQuestion.setPaperEndDateTime(examEndTime);
                tempScienceQuestion.setStudentID(currentStudentID);
                tempList.add(tempScienceQuestion);
               /* tempScienceQuestion.setParaQuestion(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setSessionID(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setStudentID(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setDeviceID(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setScoredMarks(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setTotalMarks(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setStartDateTime(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setEndDateTime(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setLevel(tempScienceQuestionList.get(i).getQlevel());
                tempScienceQuestion.setLabel(tempScienceQuestionList.get(i).get);
                tempScienceQuestion.setSentFlag(tempScienceQuestionList.get(i).get);*/

            }
            AppDatabase.getDatabaseInstance(this).getTempScienceQuestionDao().insertAllQuestions(tempList);

            BackupDatabase.backup(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadMediaFiles() {
//        if (queDownloadIndex >= topicIdList.size()) {

        if (downloadMediaList.size() > 0) {

            mediaProgressDialog.setTitle("Downloading media please wait..");
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
                Toast.makeText(this, "Media download failed..", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
//        }
    }

    private void setExamInfo() {
        if (assessmentPaperPatterns.getExammode() != null) {
            if (assessmentPaperPatterns.getExammode().equalsIgnoreCase(Assessment_Constants.SUPERVISED)) {
                tv_img_capture_warning.setVisibility(View.VISIBLE);
            } else tv_img_capture_warning.setVisibility(View.GONE);
        } else tv_img_capture_warning.setVisibility(View.GONE);

        tv_exam_name.setText(getString(R.string.exam) + " " + assessmentPaperPatterns.getExamname());
        tv_time.setText(getString(R.string.time) + " " + assessmentPaperPatterns.getExamduration() + " mins.");
        tv_marks.setText(getString(R.string.marks) + " " + assessmentPaperPatterns.getOutofmarks());
        tv_total_que.setText(getString(R.string.total_questions) + " " + scienceQuestionList.size());

        swipe_btn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (scienceQuestionList.isEmpty()) {
                            Toast.makeText(ScienceAssessmentActivity.this, "No questions", Toast.LENGTH_SHORT).show();
                            swipe_btn.showResultIcon(false);
                        } else {
                            swipe_btn.showResultIcon(true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showQuestions();
                                }
                            }, 2500);
                        }


                        // task success! show TICK icon in ProSwipeButton
                        // false if task failed
                    }
                }, 2000);
            }
        });


    }

    private void showToast(final String text) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show()
        );
    }

    private void showQuestions() {

//        ll_count_down.setVisibility(View.GONE);
        rl_exam_info.setVisibility(View.GONE);
        rl_que.setVisibility(View.VISIBLE);
//        showStartAssessment();
        setProgressBarAndTimer();

//todo video monitoring start
        if (Assessment_Constants.VIDEOMONITORING) {

            //******* Video monitoring *******

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    startCameraService();
                   /* if (!Assessment_Utility.isCameraBusy() && isActivityRunning) {
                        showToast("Starting capture!");

                        imgFileName = assessmentSession + "_" + Assessment_Utility.getCurrentDateTime() + "_" + DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING + ".jpg";
                        imgFileName = imgFileName.replaceAll("[\\\\/:*?\"<>|]", "_");
                        String videoPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_VIDEO_MONITORING_PATH + "/" + imgFileName;
//        VideoMonitoringService.releaseMediaRecorder();
                        String finalFileName = imgFileName;
                        pictureService.startCapturing(ScienceAssessmentActivity.this, imgFileName);
//                        imgCaptureCnt++;
//                        captureInterval = captureInterval - captureTime;
                    }*/
                }
            }, 1000);
        }
        // ****** circle 5 sec time ****
     /*       final long period = 50;
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //this repeats every 100 ms
                    if (i < 100) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                             *//*   selectTopicDialog.timer.setText(String.valueOf(i)+"%");
                                selectTopicDialog.timer.setText("" + time);
                                selectTopicDialog*//*
                                circle_progress_bar.setProgress(i);
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
//                                selectTopicDialog.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
//                                        selectTopicDialog.dismiss();
                                        ll_count_down.setVisibility(View.GONE);
                                        rl_que.setVisibility(View.VISIBLE);
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
*/

//            selectTopicDialog.dismiss();

        try {
            viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), this, scienceQuestionList);
//        fragment_view_pager.setOffscreenPageLimit(scienceQuestionList.size());
            fragment_view_pager.setSaveFromParentEnabled(true);
            fragment_view_pager.setAdapter(viewpagerAdapter);
            dots_indicator.setViewPager(fragment_view_pager);
            currentFragment = viewpagerAdapter.getItem(0);
            examStartTime = Assessment_Utility.getCurrentDateTime();
            scienceQuestionList.get(0).setStartTime(Assessment_Utility.getCurrentDateTime());

            iv_prev.setVisibility(View.GONE);
            txt_prev.setVisibility(View.GONE);
            txt_question_cnt.setText(getString(R.string.question) + " " + "1" + "/" + scienceQuestionList.size());

            fragment_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                // This method will be invoked when a new page becomes selected.
                @Override
                public void onPageSelected(int position) {
                      /*  Toast.makeText(ScienceAssessmentActivity.this,
                                "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                      */
                    if (position > 0) {
                        scienceQuestionList.get(position - 1).setEndTime(Assessment_Utility.getCurrentDateTime());
                        scienceQuestionList.get(position).setStartTime(Assessment_Utility.getCurrentDateTime());
                    }
                    if (!showSubmit)
                        btn_submit.setVisibility(View.GONE);
                    else
                        btn_submit.setVisibility(View.VISIBLE);


                    Assessment_Utility.HideInputKeypad(ScienceAssessmentActivity.this);
                    if (position == 0) {
                        iv_prev.setVisibility(View.GONE);
                        txt_prev.setVisibility(View.GONE);
                    } else {
                        iv_prev.setVisibility(View.VISIBLE);
                        txt_prev.setVisibility(View.VISIBLE);
                    }

                    //                if (!Assessment_Constants.VIDEOMONITORING) {
                    if (position == scienceQuestionList.size() - 1) {
                        btn_save_Assessment.setVisibility(View.GONE);
                        showDoneAnimation();
                    } else {
                        //                    if (!Assessment_Constants.VIDEOMONITORING) {
                        //                        btn_save_Assessment.setBackground(getResources().getDrawable(R.drawable.ripple_round));
                        btn_save_Assessment.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_arrow));
                        btn_save_Assessment.setVisibility(View.VISIBLE);
                        txt_next.setVisibility(View.VISIBLE);
                        //                    } else {
                        //                        btn_save_Assessment.setVisibility(View.GONE);
                        //                        txt_next.setVisibility(View.GONE);
                        //                    }
                        //                    }
                    }
                    queCnt = position;
                    txt_question_cnt.setText(getString(R.string.question) + " " + (position + 1) + "/" + scienceQuestionList.size());

                    if (currentFragment != null) {
                        currentFragment.onPause();
                    }
                    currentFragment = viewpagerAdapter.getItem(position);

                }

                // This method will be invoked when the current page is scrolled
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Code goes here
                }

                // Called when the scroll state changes:
                // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
                @Override
                public void onPageScrollStateChanged(int state) {
                    // Code goes here
                }
            });


    /*    fragment_view_pager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                *//*if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else*//*
                if (position <= 1) { // [-1,1]
                    view.setAlpha(1);
                    // Counteract the default slide transition
                    view.setTranslationX(view.getWidth() * -position);

                    //set Y position to swipe in from top s
                    float yPosition = position * view.getHeight();
                    view.setTranslationY(yPosition);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }

            }
        });*/


            fragment_view_pager.setCurrentItem(0);

//            ScienceAdapter scienceAdapter = new ScienceAdapter(this, scienceQuestionList);
       /* discreteScrollView.setOrientation(DSVOrientation.HORIZONTAL);
        discreteScrollView.addOnItemChangedListener(this);
        discreteScrollView.setItemTransitionTimeMillis(200);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.5f)
                .build());
//            discreteScrollView.setAdapter(scienceAdapter);

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
//                queCnt = adapterPosition + 1;
                currentCount.setText(queCnt + "/" + scienceQuestionList.size());
                currentViewHolder = viewHolder;

                Assessment_Utility.HideInputKeypad(ScienceAssessmentActivity.this);
//                    step_view.go(adapterPosition, true);

            }

        });*/
//            scienceAdapter.notifyDataSetChanged();
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDoneAnimation() {
        Animation animation;
        /*animation = AnimationUtils.loadAnimation(this, R.anim.pop_out);
        btn_save_Assessment.startAnimation(animation);*/
        btn_save_Assessment.setVisibility(View.GONE);
        txt_next.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                btn_save_Assessment.setBackgroundResource(android.R.color.transparent);
//                if (scienceQuestionList.size() == (queCnt + 1)) {
                btn_save_Assessment.setVisibility(View.GONE);
//                    btn_save_Assessment.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle_36dp));
//                }
                /* else {
                    btn_save_Assessment.setBackground(getResources().getDrawable(R.drawable.ripple_round));
                }*/
                Animation animation = AnimationUtils.loadAnimation(ScienceAssessmentActivity.this, R.anim.zoom_in);
//                if (!Assessment_Constants.VIDEOMONITORING) {
//                    btn_save_Assessment.setVisibility(View.VISIBLE);
//                    btn_save_Assessment.startAnimation(animation);
                if (scienceQuestionList.size() == (queCnt + 1)) {
                    btn_submit.setVisibility(View.VISIBLE);
                    btn_submit.startAnimation(animation);
                    txt_next.setVisibility(View.GONE);
                    dots_indicator.setVisibility(View.GONE);
                }
            /*    } else {
                    if (scienceQuestionList.size() == (queCnt + 1)) {
                        btn_submit.setVisibility(View.VISIBLE);
                        dots_indicator.setVisibility(View.GONE);

                    }
                }*/
                showSubmit = true;
            }
        }, 500);
    }

    @Click(R.id.btn_submit)
    public void onSubmitClick() {
       /* if (mCountDownTimer != null)
            mCountDownTimer.cancel();*/
        try {
            scienceQuestionList.get(queCnt).setEndTime(Assessment_Utility.getCurrentDateTime());
//        BottomQuestionFragment bottomQuestionFragment = new BottomQuestionFragment();
            if (!bottomQuestionFragment.isVisible()) {
                bottomQuestionFragment.show(getSupportFragmentManager(), BottomQuestionFragment.class.getSimpleName());
                Bundle args = new Bundle();
                args.putSerializable("questionList", (Serializable) scienceQuestionList);
                bottomQuestionFragment.setArguments(args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setProgressBarAndTimer() {
//        progressBarTimer.setProgress(100);
        try {
            ExamTime = Integer.parseInt(assessmentPaperPatterns.getExamduration().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
//        ExamTime = 1;
        if (ExamTime == 0)
            ExamTime = 30;
        final int timer = ExamTime * 60000;

        int captureTime = ExamTime / 3;

       /* Date now = new Date();
        long timeRangeMs = ExamTime *60*60;// examtime hours in ms
        ArrayList<String> timeIntervals = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            long random = getRandomTime(timeRangeMs);
            Time randDate = new Time(random);
            int min = randDate.getMinutes();
            String[] minSecArr = randDate.toString().split(":");
            String minSec = minSecArr[0] + ":" + minSecArr[1];
            Log.d("mmm", "setProgressBarAndTimer: " + minSec);
            while (timeIntervals.contains(minSec)) {
                random = getRandomTime(timeRangeMs);
                randDate = new Time(random);
                minSecArr = randDate.toString().split(":");
                minSec = minSecArr[0] + ":" + minSecArr[1];
            }
            timeIntervals.add(minSec);
        }*/
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startCameraService();
            }
        }, 800);
*/



     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/

         /*   }
        }, 1000);*/


        setCircleViewAnimation();


        final long period = 1000;
        final int n = (int) (ExamTime * 60000 / period);
        tickerView.setCharacterLists("" + ExamTime);
        final Timer examTimer = new Timer();
        circle_view.setMaximumValue(n);
/*        examTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //this repeats every 100 ms
                if (tick < n) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circle_view.setProgressValue(tick);
                        }
                    });
                    tick++;
                } else {
                    //closing the timer
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timesUp = true;
                            if (isActivityRunning) {
                                try {
                                    AssessmentTimeUpDialog timeUpDialog = new AssessmentTimeUpDialog(ScienceAssessmentActivity.this);
                                    timeUpDialog.show();
                                    if (mCountDownTimer != null)
                                        mCountDownTimer.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    examTimer.cancel();
                    examTimer.purge();
                }
            }
        }, 0, period);*/
        List<String> randomTimeStamp = new ArrayList<>();
        if (VIDEOMONITORING) {
            randomTimeStamp = getRandomTimeStamp(TimeUnit.MINUTES.toMillis(ExamTime));
            Log.d("onTick", "randomTimeStamp: " + randomTimeStamp.toString());
        }
        List<String> finalRandomTimeStamp = randomTimeStamp;
        mCountDownTimer = new CountDownTimer(timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    if (VIDEOMONITORING) {
                        Log.d("onTick", "randomTimeStamp: " + finalRandomTimeStamp.toString());

                        String imgCaptureTime = String.format("%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                        if (finalRandomTimeStamp.contains(imgCaptureTime)) {

                            Log.d("tk", "onTick: " + imgCaptureTime);

                           /* if (imgCaptureCnt == 0) {

                                captureInterval = ExamTime - captureTime;
                            }*/
//                            if (imgCaptureCnt < 3) {
//                                if (captureInterval == min) {
                            if (!Assessment_Utility.isCameraBusy() && (isActivityRunning || dialogOpen)) {
                                showToast("Starting capture!");

                                imgFileName = assessmentSession + "_" + Assessment_Utility.getCurrentDateTime() + "_" + DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING + ".jpg";
                                imgFileName = imgFileName.replaceAll("[\\\\/:*?\"<>|]", "_");
//        VideoMonitoringService.releaseMediaRecorder();
                                if (pictureService != null)
                                    pictureService.startCapturing(ScienceAssessmentActivity.this, imgFileName);
                                else {
                                    pictureService = PictureCapturingServiceImpl.getInstance(ScienceAssessmentActivity.this);
                                    pictureService.startCapturing(ScienceAssessmentActivity.this, imgFileName);

                                }
                                Log.d("tk", "onTick: " + imgCaptureTime + "captured");

                            } else
                                Log.d("tk", "onTick: " + imgCaptureTime + "camera busy.. Not captured");

//                                }
//                            }

                        }
                    }
                    tickerView.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                    ));


                    tv_timer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                    ));
                } catch (
                        Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                timesUp = true;
                isEndTimeSet = true;
                scienceQuestionList.get(queCnt).setEndTime(Assessment_Utility.getCurrentDateTime());
                examEndTime = Assessment_Utility.getCurrentDateTime();
                if (isActivityRunning) {
                    try {
                        AssessmentTimeUpDialog timeUpDialog = new AssessmentTimeUpDialog(ScienceAssessmentActivity.this);
                        timeUpDialog.show();
                        int count = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                                getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdPaperIdStudId(selectedExamId, selectedLang, subjectId, assessmentSession, currentStudentID);
                        int count1 = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                                getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdStudId(selectedExamId, selectedLang, subjectId, currentStudentID);
                        Log.d("delete count", "doInBackground: " + count + count1);

//                        progressBarTimer.setProgress(0);
//                        isActivityRunning=false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                onSaveAssessmentClick();
//                Toast.makeText(ScienceAssessmentActivity.this, "Time up...", Toast.LENGTH_SHORT).show();
                }
            }
        }

        ;
        mCountDownTimer.start();
    }


    private List<String> getRandomTimeStamp(long milliseconds) {
        List list = new ArrayList<Float>();
        while (list.size() < 12) {
            int x = (int) (Math.random() * milliseconds);
            if (!list.contains(x)) {
                String time = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(x),
                        TimeUnit.MILLISECONDS.toSeconds(x) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(x)));
                list.add(time);
            }
        }
        return list;
    }


    private void setCircleViewAnimation() {
        CircleViewAnimation circleViewAnimation = new CircleViewAnimation()
                .setCircleView(circle_view)
                .setAnimationStyle(AnimationStyle.CONTINUOUS)
                .setDuration(circle_view.getProgressValue())
                .setCustomAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Animation Starts

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Animation Ends
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                }).setTimerOperationOnFinish(new Runnable() {
                    @Override
                    public void run() {
                        // Run when the duration reaches 0. Regardless of the AnimationLifecycle or main thread.
                        // Runs and triggers on background.
                    }
                })

                .setCustomInterpolator(new LinearInterpolator());

        circle_view.setAnimation(circleViewAnimation);
        circle_view.setProgressStep(10);
        circleViewAnimation.start();
    }

    @Override
    public void onBackPressed() {
        {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.exit_dialog);
            dialog.setCanceledOnTouchOutside(false);
            TextView title = dialog.findViewById(R.id.dia_title);
            Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
            Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);
            Button cancel_btn = dialog.findViewById(R.id.dia_btn_cancel);
            cancel_btn.setVisibility(View.GONE);

            title.setText(R.string.do_you_want_to_leave_assessment);
            restart_btn.setText(R.string.no);
            exit_btn.setText(R.string.yes);
            dialog.show();

            exit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
//                    finish();
//                chronometer.stop();
                    if (mCountDownTimer != null)
                        mCountDownTimer.cancel();
                    saveAttemptedQuestionsInDB();
                    endTestSession();

                }
            });

            restart_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

    }


    @Click(R.id.iv_prev)
    public void prevClick() {

        Assessment_Utility.HideInputKeypad(this);
        Animation animation;

        if (queCnt > 0) {

            scienceQuestionList.get(queCnt - 1).setEndTime(Assessment_Utility.getCurrentDateTime());
            queCnt--;
            fragment_view_pager.setCurrentItem(queCnt);
//            discreteScrollView.smoothScrollToPosition(queCnt - 1);
            scienceQuestionList.get(queCnt).setStartTime(Assessment_Utility.getCurrentDateTime());
        }


//        currentCount.setText(queCnt + "/" + scienceQuestionList.size());
//        step_view.go(queCnt, true);


    }


    //    @OnClick(R.id.iv_next)
    public void nextClick() {

        Assessment_Utility.HideInputKeypad(this);
        Animation animation;

        if (queCnt < scienceQuestionList.size()) {

            scienceQuestionList.get(queCnt).setEndTime(Assessment_Utility.getCurrentDateTime());
//            discreteScrollView.smoothScrollToPosition(queCnt);
            queCnt++;
            fragment_view_pager.setCurrentItem(queCnt);
            scienceQuestionList.get(queCnt).setStartTime(Assessment_Utility.getCurrentDateTime());

        } /*else if (queCnt == scienceQuestionList.size()) {
            queCnt = scienceQuestionList.size();


        }*/
//        currentCount.setText(queCnt + "/" + scienceQuestionList.size());
//        step_view.go(queCnt, true);


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
            this.answer = answer;
        } else if (list != null) {
            String ans = "";
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
                        if (scienceQuestionList.get(i).getQtid().equalsIgnoreCase(MATCHING_PAIR) || scienceQuestionList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_SELECT) || scienceQuestionList.get(i).getQtid().equalsIgnoreCase(ARRANGE_SEQUENCE) || scienceQuestionList.get(i).getQtid().equalsIgnoreCase(IMAGE_ANSWER)) {
                            for (int m = 0; m < scienceQuestionList.get(i).getMatchingNameList().size(); m++) {
                                if (scienceQuestionList.get(i).getQtid().equalsIgnoreCase(MULTIPLE_SELECT)) {
                                    if (scienceQuestionList.get(i).getMatchingNameList().get(m).getMyIscorrect().equalsIgnoreCase("true"))
                                        ans += scienceQuestionList.get(i).getMatchingNameList().get(m).getQcid() + ",";

                                } else
                                    ans += scienceQuestionList.get(i).getMatchingNameList().get(m).getQcid() + ",";
                            }
                            scienceQuestionList.get(i).setUserAnswer(ans);
                        } else {
                            if (scienceQuestionList.get(i).getQtid().equalsIgnoreCase(TRUE_FALSE)) {
                                scienceQuestionList.get(i).setUserAnswer(scienceQuestionList.get(i).getMatchingNameList().get(0).getChoicename().toLowerCase());
                                scienceQuestionList.get(i).setUserAnswerId(scienceQuestionList.get(i).getMatchingNameList().get(0).getQcid());
                            } else {
                                scienceQuestionList.get(i).setUserAnswer(scienceQuestionList.get(i).getMatchingNameList().get(0).getChoicename());
                                scienceQuestionList.get(i).setUserAnswerId(scienceQuestionList.get(i).getMatchingNameList().get(0).getQcid());
//                        scienceQuestionList.get(i).setMarksPerQuestion(marksPerQuestion);
                            }
                        }
                    } else {
                        scienceQuestionList.get(i).setUserAnswer("");
                        scienceQuestionList.get(i).setUserAnswerId(ansId);
                    }
                    break;
                }
            }
            this.answer = ans;

        }
        if (ansId.equalsIgnoreCase(""))
            this.ansId = "-1";
        if (scienceQuestionList.size() > 0 && queCnt < scienceQuestionList.size())
            checkAssessment(queCnt);
    }

    @Override
    public void removeSupervisorFragment() {

        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count > 0) {
                setExamInfo();
                frame_supervisor.setVisibility(View.GONE);
                rl_exam_info.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void setParagraph(String para, boolean isParaQuestion) {
        paragraph = para;
        if (isParaQuestion) {
            iv_question_mark.setVisibility(View.VISIBLE);
        } else iv_question_mark.setVisibility(View.GONE);

    }*/

   /* @Click(R.id.iv_question_mark)
    public void showPara() {
        showZoomDialog(this, "", "", paragraph);
    }*/
    /*  @Override
      public void setImageCaptureResult(final ScienceQuestion scienceQuestion) {
          final ChooseImageDialog chooseImageDialog = new ChooseImageDialog(this);
          imageFileName = scienceQuestion.getQid() + "_" + scienceQuestion.getPaperid() + ".jpg";

  //            String path = Environment.getExternalStorageDirectory().toString() + "/.Assessment/Content/Answers/" + fileName;
          imageFilePath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH;

          chooseImageDialog.btn_take_photo.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  chooseImageDialog.cancel();
                  if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                      String[] permissionArray = new String[]{PermissionUtils.Manifest_CAMERA};

                      if (!isPermissionsGranted(ScienceAssessmentActivity.this, permissionArray)) {
                          Toast.makeText(ScienceAssessmentActivity.this, "Give Camera permissions through settings and restart the app.", Toast.LENGTH_SHORT).show();
                      } else {
  //                        imageName = Assessment_Utility.getFileName(scienceQuestion.getQid())
                          scienceQuestion.setUserAnswer(imageFileName);
  //                        selectedImage = selectedImageTemp;
                          Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                          startActivityForResult(takePicture, CAPTURE_IMAGE);
                      }
                  } else {
  //                    imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
                      scienceQuestion.setUserAnswer(imageFileName);
  //                    selectedImage = selectedImageTemp;
                      Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                      startActivityForResult(takePicture, CAPTURE_IMAGE);
                  }
              }
          });

          chooseImageDialog.btn_choose_from_gallery.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  chooseImageDialog.cancel();
                  if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                      String[] permissionArray = new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE};

                      if (!isPermissionsGranted(ScienceAssessmentActivity.this, permissionArray)) {
                          Toast.makeText(ScienceAssessmentActivity.this, "Give Storage permissions through settings and restart the app.", Toast.LENGTH_SHORT).show();
                      } else {
  //                        imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
                          scienceQuestion.setUserAnswer(imageFileName);
  //                        selectedImage = selectedImageTemp;
                          Intent intent = new Intent();
                          intent.setType("image/*");
                          intent.setAction(Intent.ACTION_GET_CONTENT);
                          startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY);
                      }
                  } else {
  //                    imageName = entryID + "_" + dde_questions.getQuestionId() + ".jpg";
                      scienceQuestion.setUserAnswer(imageFileName);
  //                    selectedImage = selectedImageTemp;
                      Intent intent = new Intent();
                      intent.setType("image/*");
                      intent.setAction(Intent.ACTION_GET_CONTENT);
                      startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY);
                  }
              }
          });
          chooseImageDialog.show();
      }
  */
    /* @Override
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
         filePath = AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH + "/" + videoName;

         startActivityForResult(intent, videoCapture);
     }
 */
/*    @Override
    public void setAudio(String path, boolean isAudioRecording) {
        if (isAudioRecording) {
            AudioUtil.startRecording(path);
        } else {
            AudioUtil.stopRecording();
        }
    }*/

    private void checkAssessment(int queCnt) {
        ScienceQuestion scienceQuestion = scienceQuestionList.get(queCnt);
        questionType = scienceQuestion.getQtid();
        switch (questionType) {
            case FILL_IN_THE_BLANK_WITH_OPTION:
            case MULTIPLE_CHOICE:
            case TRUE_FALSE:
                if (scienceQuestion.getIsAttempted()) {
                    if (scienceQuestion.getMatchingNameList() != null) {
                        if (scienceQuestion.getMatchingNameList().get(0).getCorrect().equalsIgnoreCase("true") || scienceQuestion.getUserAnswerId().equalsIgnoreCase(ansId)) {
                            scienceQuestionList.get(queCnt).setIsCorrect(true);
                            scienceQuestionList.get(queCnt).
                                    setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());

                        } else {
                            scienceQuestionList.get(queCnt).setIsCorrect(false);
                            scienceQuestionList.get(queCnt).
                                    setMarksPerQuestion("0");
                        }
                    }
                } else {
                    scienceQuestionList.get(queCnt).setIsCorrect(false);
                    scienceQuestionList.get(queCnt).
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
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());

                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion("0");
                    }
                }
                break;
            case FILL_IN_THE_BLANK:
                if (scienceQuestion.getIsAttempted()) {
                    if ((scienceQuestion.getAnswer().equalsIgnoreCase(answer.trim()))) {
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());
                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion("0");
                    }
                }
                break;
           /* case TRUE_FALSE:
                if (scienceQuestion.getIsAttempted())
                    if (scienceQuestion.getAnswer().equalsIgnoreCase(answer.trim())) {
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());
                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion("0");
                    }
                break;*/
            case ARRANGE_SEQUENCE:
                if (scienceQuestion.getIsAttempted()) {
                    int matchPairCnt = 0;
                    List<ScienceQuestionChoice> queOptions = AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
                    for (int i = 0; i < queOptions.size(); i++) {
                        if (queOptions.get(i).getQcid().equalsIgnoreCase(scienceQuestion.getMatchingNameList().get(i).getQcid())) {
                            matchPairCnt++;
                        }
                    }
                    if (matchPairCnt == queOptions.size()) {
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());

                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion("0");
                    }
                }
                break;
            case MATCHING_PAIR:
                if (scienceQuestion.getIsAttempted()) {
                    int matchPairCnt = 0;
                    List<ScienceQuestionChoice> queOptions = AppDatabase.getDatabaseInstance(this).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
                    float marksPerQ = 0;
                    if (queOptions.size() > 0) {
                        marksPerQ = Float.parseFloat(scienceQuestionList.get(queCnt).getOutofmarks()) / queOptions.size();
                    }
                    for (int i = 0; i < queOptions.size(); i++) {
                        if (queOptions.get(i).getQcid().equalsIgnoreCase(scienceQuestion.getMatchingNameList().get(i).getQcid())) {
                            matchPairCnt++;
                        }
                    }
                    if (matchPairCnt > 0) {
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                    }
                    float marksObtained = marksPerQ * matchPairCnt;
                    int marks = Math.round(marksObtained);
                    scienceQuestionList.get(queCnt).
                            setMarksPerQuestion(marks + "");
                }
                break;
            case VIDEO:
                //todo decide marks,isCorrect for video,audio
                scienceQuestionList.get(queCnt).setUserAnswer(answer);
                if (scienceQuestionList.get(queCnt).getUserAnswer() != null && !scienceQuestionList.get(queCnt).getUserAnswer().equalsIgnoreCase("")) {
                    scienceQuestionList.get(queCnt).setIsAttempted(true);
                    scienceQuestionList.get(queCnt).setIsCorrect(true);
                    scienceQuestionList.get(queCnt).
                            setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());
                    DownloadMedia downloadMedia = new DownloadMedia();
                    downloadMedia.setPhotoUrl(answer);
                    downloadMedia.setqId(scienceQuestionList.get(queCnt).getQid());
                    downloadMedia.setQtId(scienceQuestionList.get(queCnt).getQtid());
                    downloadMedia.setMediaType(DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO);
                    downloadMedia.setPaperId(assessmentSession);
                    AppDatabase.getDatabaseInstance(this).getDownloadMediaDao().deleteByPaperIdAndQid(assessmentSession, scienceQuestionList.get(queCnt).getQid());
                    AppDatabase.getDatabaseInstance(this).getDownloadMediaDao().insert(downloadMedia);
                } else {
                    scienceQuestionList.get(queCnt).setIsAttempted(false);
                    scienceQuestionList.get(queCnt).setIsCorrect(false);
                    scienceQuestionList.get(queCnt).
                            setMarksPerQuestion("0");
                }
                break;
            case AUDIO:
//                scienceQuestionList.get(queCnt).setUserAnswer(filePath);
                if (scienceQuestionList.get(queCnt).getUserAnswer() != null && !scienceQuestionList.get(queCnt).getUserAnswer().equalsIgnoreCase("")) {
                    scienceQuestionList.get(queCnt).setIsAttempted(true);
                    scienceQuestionList.get(queCnt).setIsCorrect(true);
                    scienceQuestionList.get(queCnt).
                            setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());
                    DownloadMedia downloadMediaAudio = new DownloadMedia();
                    downloadMediaAudio.setPhotoUrl(answer);
                    downloadMediaAudio.setqId(scienceQuestionList.get(queCnt).getQid());
                    downloadMediaAudio.setQtId(scienceQuestionList.get(queCnt).getQtid());
                    downloadMediaAudio.setMediaType(DOWNLOAD_MEDIA_TYPE_ANSWER_AUDIO);
                    downloadMediaAudio.setPaperId(assessmentSession);
                    AppDatabase.getDatabaseInstance(this).getDownloadMediaDao().deleteByPaperIdAndQid(assessmentSession, scienceQuestionList.get(queCnt).getQid());
                    AppDatabase.getDatabaseInstance(this).getDownloadMediaDao().insert(downloadMediaAudio);
                } else {
                    scienceQuestionList.get(queCnt).setIsAttempted(false);
                    scienceQuestionList.get(queCnt).setIsCorrect(false);
                    scienceQuestionList.get(queCnt).
                            setMarksPerQuestion("0");
                }
                break;
            case TEXT_PARAGRAPH:
                if (scienceQuestion.getIsAttempted()) {
                    if (!answer.equalsIgnoreCase("")) {
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt).getUserAnswerId());
                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion("0");
                    }
                }
                break;
            case KEYWORDS_QUESTION:
                if (scienceQuestion.getIsAttempted()) {
                    if (!answer.equalsIgnoreCase("")) {
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());
                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion("0");
                    }
                }
                break;
            case IMAGE_ANSWER:
                if (scienceQuestion.getIsAttempted()) {
                    if (!answer.equalsIgnoreCase("")) {
                        scienceQuestionList.get(queCnt).setIsCorrect(true);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion(scienceQuestionList.get(queCnt).getOutofmarks());
                    } else {
                        scienceQuestionList.get(queCnt).setIsCorrect(false);
                        scienceQuestionList.get(queCnt).
                                setMarksPerQuestion("0");
                    }
                    if (scienceQuestionList.get(queCnt).getMatchingNameList() != null
                            && scienceQuestionList.get(queCnt).getMatchingNameList().size() > 0) {
                        int count = AppDatabase.getDatabaseInstance(this).getDownloadMediaDao().deleteByPaperIdAndQid(assessmentSession, scienceQuestionList.get(queCnt).getQid());
                        Log.d("delete count", "media: " + count);

                        for (int i = 0; i < scienceQuestionList.get(queCnt).getMatchingNameList().size(); i++) {
                            DownloadMedia imageMedia = new DownloadMedia();
                            imageMedia.setPhotoUrl(scienceQuestionList.get(queCnt).getMatchingNameList().get(i).getQcid());
                            imageMedia.setqId(scienceQuestionList.get(queCnt).getQid());
                            imageMedia.setQtId(scienceQuestionList.get(queCnt).getQtid());
                            imageMedia.setMediaType(DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE);
                            imageMedia.setPaperId(assessmentSession);
                            AppDatabase.getDatabaseInstance(this).getDownloadMediaDao().insert(imageMedia);
                        }
                    }

                }
                break;

        }

        updateTempList(queCnt);
    }

    private void updateTempList(int queCnt) {
        try {
//        List<TempScienceQuestion> tempList = new ArrayList<>();
            String currentSession = FastSave.getInstance().getString("currentSession", "");
            String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
            calculateMarks();
//        for (int i = 0; i < scienceQuestionList.size(); i++) {
            TempScienceQuestion tempScienceQuestion = new TempScienceQuestion();
            tempScienceQuestion.setAnsdesc(scienceQuestionList.get(queCnt).getAnsdesc());
            tempScienceQuestion.setUpdatedby(scienceQuestionList.get(queCnt).getUpdatedby());
            tempScienceQuestion.setQlevel(scienceQuestionList.get(queCnt).getQlevel());
            tempScienceQuestion.setAddedby(scienceQuestionList.get(queCnt).getAddedby());
            tempScienceQuestion.setLanguageid(scienceQuestionList.get(queCnt).getLanguageid());
            tempScienceQuestion.setActive(scienceQuestionList.get(queCnt).getActive());
            tempScienceQuestion.setLessonid(scienceQuestionList.get(queCnt).getLessonid());
            tempScienceQuestion.setQtid(scienceQuestionList.get(queCnt).getQtid());
            tempScienceQuestion.setQid(scienceQuestionList.get(queCnt).getQid());
            tempScienceQuestion.setSubjectid(scienceQuestionList.get(queCnt).getSubjectid());
            tempScienceQuestion.setAddedtime(scienceQuestionList.get(queCnt).getAddedtime());
            tempScienceQuestion.setUpdatedtime(scienceQuestionList.get(queCnt).getUpdatedtime());
            tempScienceQuestion.setPhotourl(scienceQuestionList.get(queCnt).getPhotourl());
            tempScienceQuestion.setExamtime(assessmentPaperPatterns.getExamduration());
            tempScienceQuestion.setTopicid(scienceQuestionList.get(queCnt).getTopicid());
            tempScienceQuestion.setAnswer(scienceQuestionList.get(queCnt).getAnswer());
            tempScienceQuestion.setOutofmarks(scienceQuestionList.get(queCnt).getOutofmarks());
            tempScienceQuestion.setQname(scienceQuestionList.get(queCnt).getQname());
            tempScienceQuestion.setHint(scienceQuestionList.get(queCnt).getHint());
            tempScienceQuestion.setExamid(selectedExamId);
            tempScienceQuestion.setPdid(scienceQuestionList.get(queCnt).getPdid());
            tempScienceQuestion.setStartTime(scienceQuestionList.get(queCnt).getStartTime());
            tempScienceQuestion.setLstquestionchoice(scienceQuestionList.get(queCnt).getLstquestionchoice());
            tempScienceQuestion.setMatchingNameList(scienceQuestionList.get(queCnt).getMatchingNameList());
            tempScienceQuestion.setEndTime(scienceQuestionList.get(queCnt).getEndTime());
            tempScienceQuestion.setMarksPerQuestion(scienceQuestionList.get(queCnt).getMarksPerQuestion());
            tempScienceQuestion.setUserAnswerId(scienceQuestionList.get(queCnt).getUserAnswerId());
            tempScienceQuestion.setUserAnswer(scienceQuestionList.get(queCnt).getUserAnswer());
            tempScienceQuestion.setAttempted(scienceQuestionList.get(queCnt).getIsAttempted());
            tempScienceQuestion.setCorrect(scienceQuestionList.get(queCnt).getIsCorrect());
            tempScienceQuestion.setRefParaID(scienceQuestionList.get(queCnt).getRefParaID());
            tempScienceQuestion.setPaperId(scienceQuestionList.get(queCnt).getPaperid());

            tempScienceQuestion.setParaQuestion(scienceQuestionList.get(queCnt).isParaQuestion());
            tempScienceQuestion.setSessionID(currentSession);
            tempScienceQuestion.setStudentID(currentStudentID);
            tempScienceQuestion.setDeviceID(AppDatabase.getDatabaseInstance(this).getStatusDao().getValue("DeviceId"));
            tempScienceQuestion.setScoredMarks(Integer.parseInt(scienceQuestionList.get(queCnt).getMarksPerQuestion()));
            tempScienceQuestion.setPaperTotalMarks(totalMarks);
            tempScienceQuestion.setPaperStartDateTime(examStartTime);
            tempScienceQuestion.setPaperEndDateTime(examEndTime);
            if (scienceQuestionList.get(queCnt).getQlevel().matches("[0-9]+"))
                tempScienceQuestion.setLevel(Integer.parseInt(scienceQuestionList.get(queCnt).getQlevel()));
            else tempScienceQuestion.setLevel(0);
            if (FastSave.getInstance().getBoolean(Assessment_Constants.SUPERVISED, false))
                //            if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("practice"))
                tempScienceQuestion.setLabel("supervised assessment" + "_temp");
            else tempScienceQuestion.setLabel(Assessment_Constants.PRACTICE + "_temp");
            tempScienceQuestion.setSentFlag(0);

//            tempList.add(tempScienceQuestion);
//        }
//            AppDatabase.getDatabaseInstance(this).getTempScienceQuestionDao().deleteQuestionByPaperIdQid(currentSession, scienceQuestionList.get(queCnt).getQid());
            int count = AppDatabase.getDatabaseInstance(this).getTempScienceQuestionDao().
                    updateQuestionByPaperIdQid(tempScienceQuestion.getPaperId(), tempScienceQuestion.getQid(),
                            tempScienceQuestion.getOutofmarks(), tempScienceQuestion.getStartTime(),
                            tempScienceQuestion.getEndTime(), tempScienceQuestion.getMarksPerQuestion(),
                            tempScienceQuestion.getUserAnswer(), tempScienceQuestion.getUserAnswerId(),
                            tempScienceQuestion.isAttempted(), tempScienceQuestion.isCorrect(),
                            tempScienceQuestion.isParaQuestion(), tempScienceQuestion.getRefParaID(),
                            tempScienceQuestion.getSessionID(), tempScienceQuestion.getDeviceID(),
                            tempScienceQuestion.getMarksPerQuestion(), tempScienceQuestion.getOutofmarks(),
                            tempScienceQuestion.getPaperStartDateTime(), tempScienceQuestion.getPaperStartDateTime(),
                            tempScienceQuestion.getLabel());
//            AppDatabase.getDatabaseInstance(this).getTempScienceQuestionDao().insert(tempScienceQuestion);
            Log.d("updateTempList", "updateTempList: " + count);
//            BackupDatabase.backup(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.btn_save_Assessment)
    public void saveAssessment() {
        //todo uncomment while adding practice mode
/*        if (Assessment_Constants.isPracticeModeOn) {
            if (!scienceQuestionList.get(queCnt).getIsAttempted()) {
                if (queCnt < scienceQuestionList.size() - 1)
                    nextClick();
                else {
//            mCountDownTimer.cancel();
                    scienceQuestionList.get(queCnt).setEndTime(Assessment_Utility.GetCurrentDateTime());
                    BottomQuestionFragment bottomQuestionFragment = new BottomQuestionFragment();
                    bottomQuestionFragment.show(getSupportFragmentManager(), BottomQuestionFragment.class.getSimpleName());
                    Bundle args = new Bundle();
                    args.putSerializable("questionList", (Serializable) scienceQuestionList);
                    bottomQuestionFragment.setArguments(args);
                }
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (queCnt < scienceQuestionList.size() - 1)
                            nextClick();
                        else {
//            mCountDownTimer.cancel();
                            scienceQuestionList.get(queCnt).setEndTime(Assessment_Utility.GetCurrentDateTime());
                            BottomQuestionFragment bottomQuestionFragment = new BottomQuestionFragment();
                            bottomQuestionFragment.show(getSupportFragmentManager(), BottomQuestionFragment.class.getSimpleName());
                            Bundle args = new Bundle();
                            args.putSerializable("questionList", (Serializable) scienceQuestionList);
                            bottomQuestionFragment.setArguments(args);
                        }
                    }
                }, 3000);
                 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(currentFragment);
                ft.attach(currentFragment);
                ft.commit();
            }


        } else {*/
        if (queCnt < scienceQuestionList.size() - 1)
            nextClick();
        else {
//            mCountDownTimer.cancel();
            try {
                scienceQuestionList.get(queCnt).setEndTime(Assessment_Utility.getCurrentDateTime());
//            BottomQuestionFragment bottomQuestionFragment = new BottomQuestionFragment();
                if (!bottomQuestionFragment.isVisible()) {
                    bottomQuestionFragment.show(getSupportFragmentManager(), BottomQuestionFragment.class.getSimpleName());
                    Bundle args = new Bundle();
                    args.putSerializable("questionList", (Serializable) scienceQuestionList);
                    bottomQuestionFragment.setArguments(args);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        }
        // }
    }


    @Override
    public void onQuestionClick(int pos) {
//        questionTrackDialog.dismiss();
        queCnt = pos;
//        currentCount.setText(queCnt + "/" + scienceQuestionList.size());
//        discreteScrollView.smoothScrollToPosition(pos - 1);
        fragment_view_pager.setCurrentItem(pos);
    }

    @Override
    public void onSaveAssessmentClick() {
        new AsyncTask<Object, Void, Object>() {
//            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
             /*   progressDialog = new ProgressDialog(ScienceAssessmentActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Pushing data..");
                progressDialog.show();*/
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    int count = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                            getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdPaperIdStudId(selectedExamId, selectedLang, subjectId, assessmentSession, currentStudentID);
                    int count1 = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                            getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdStudId(selectedExamId, selectedLang, subjectId, currentStudentID);
                    Log.d("delete count", "doInBackground: " + count + count1);
                    insertInDB(scienceQuestionList, " Exam completed");
                    BackupDatabase.backup(ScienceAssessmentActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                isAssessmentCompleted = true;
                Toast.makeText(ScienceAssessmentActivity.this, "Assessment saved successfully", Toast.LENGTH_SHORT).show();

                if (!AssessmentApplication.isTablet)
                    pushDataForNIOS();
                else {
                    onResponseGet();
                }
            }
        }.execute();
//        AssessmentPaperForPush paper = createPaperToSave(scienceQuestionList);
      /*  stopService(new Intent(this, BkgdVideoRecordingService.class));
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (!timesUp) {
            isEndTimeSet = true;
            examEndTime = Assessment_Utility.GetCurrentDateTime();
        }
        calculateMarks();
        skippedCnt = correctAnsCnt = wrongAnsCnt = 0;
        calculateCorrectWrongCount();
        AssessmentPaperForPush paper = new AssessmentPaperForPush();
        paper.setPaperStartTime(examStartTime);
        paper.setPaperEndTime(examEndTime);
        paper.setLanguageId(selectedLang);
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
        String currentSession = FastSave.getInstance().getString("currentSession", "");
//        paper.setSessionID(Assessment_Constants.currentSession);
        paper.setSessionID(currentSession);
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        paper.setStudentId("" + Assessment_Constants.currentStudentID);
        paper.setStudentId("" + currentStudentID);
        paper.setExamName(assessmentPaperPatterns.getExamname());

        ArrayList<Score> scores = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            Score score = new Score();
            score.setQuestionId(Integer.parseInt(scienceQuestionList.get(i).getQid()));
//            score.setLevel(getLevel(scienceQuestionList.get(i).getQlevel()));
            score.setLevel(Integer.parseInt(scienceQuestionList.get(i).getQlevel()));
            score.setIsAttempted(scienceQuestionList.get(i).getIsAttempted());
            score.setIsCorrect(scienceQuestionList.get(i).getIsCorrect());
            score.setTotalMarks(Integer.parseInt(scienceQuestionList.get(i).getOutofmarks()));
            score.setExamId(scienceQuestionList.get(i).getExamid());
            score.setStartDateTime(scienceQuestionList.get(i).getStartTime());
            if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("practice"))
                score.setLabel("practice");
            else score.setLabel("supervised assessment");
            score.setEndDateTime(scienceQuestionList.get(i).getEndTime());
//            score.setStudentID(Assessment_Constants.currentStudentID);
            score.setStudentID(currentStudentID);
            score.setDeviceID(AppDatabase.getDatabaseInstance(this).getStatusDao().getValue("DeviceId"));
//            score.setSessionID(Assessment_Constants.assessmentSession);
            score.setPaperId(assessmentSession);
//            score.setSessionID(Assessment_Constants.currentSession);
            score.setSessionID(currentSession);
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
*/
      /*  if (!AssessmentApplication.isTablet)
            pushDataForNIOS();

        Toast.makeText(this, "Assessment saved successfully", Toast.LENGTH_SHORT).show();
        BackupDatabase.backup(this);*/

    }


    public void pushDataForNIOS() {
        pushDataToServer.setValue(this, false);
        pushDataToServer.doInBackground();
    }

    private AssessmentPaperForPush createPaperToSave
            (List<ScienceQuestion> scienceQuestionList) {
        AssessmentPaperForPush paper = new AssessmentPaperForPush();
        paper.setPaperStartTime(examStartTime);
        paper.setPaperEndTime(examEndTime);
        paper.setLanguageId(selectedLang);
        paper.setExamId(selectedExamId);
        paper.setSubjectId(subjectId);
//        paper.setPaperId(scienceQuestionList.get(0).getPaperid());
        paper.setOutOfMarks("" + outOfMarks);
        paper.setPaperId(assessmentSession);
        paper.setTotalMarks("" + totalMarks);
        paper.setExamTime("" + ExamTime);
        paper.setCorrectCnt(correctAnsCnt);
        paper.setWrongCnt(wrongAnsCnt);
        paper.setSkipCnt(skippedCnt);
        String currentSession = FastSave.getInstance().getString("currentSession", "");
//        paper.setSessionID(Assessment_Constants.currentSession);
        paper.setSessionID(currentSession);
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        paper.setStudentId("" + Assessment_Constants.currentStudentID);
        paper.setStudentId("" + currentStudentID);
        paper.setQuestion1Rating(calculateTotalLevelQuestions("1") + "");
        paper.setQuestion2Rating(calculateTotalLevelQuestions("2") + "");
        paper.setQuestion3Rating(calculateTotalLevelQuestions("3") + "");
        paper.setQuestion4Rating(calculateTotalLevelQuestions("4") + "");
        paper.setQuestion5Rating(calculateTotalLevelQuestions("5") + "");
        paper.setQuestion6Rating(calculateTotalLevelQuestions("6") + "");
        paper.setQuestion7Rating(calculateTotalLevelQuestions("7") + "");
        paper.setQuestion8Rating(calculateTotalLevelQuestions("8") + "");
        paper.setQuestion9Rating(calculateTotalLevelQuestions("9") + "");
        paper.setQuestion10Rating(calculateTotalLevelQuestions("10") + "");
        Student student = AppDatabase.getDatabaseInstance(this).getStudentDao().getStudent(currentStudentID);
        if (student != null) {
            paper.setFullName(student.getFullName());
            paper.setAge(student.getAge());
            paper.setGender(student.getGender());
            paper.setIsniosstudent(student.getIsniosstudent());

        }
        paper.setExamName(assessmentPaperPatterns.getExamname());
        return paper;
    }

    private float calculateTotalLevelQuestions(String level) {
        int totalLevelQuestions = 0, correctQuestions = 0;
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            if (scienceQuestionList.get(i).getQlevel().equalsIgnoreCase(level)) {
                totalLevelQuestions++;
                if (scienceQuestionList.get(i).getIsCorrect())
                    correctQuestions++;
            }
        }
        return CalculateRating(totalLevelQuestions, correctQuestions);
    }

  /*  public float getRating(String level, String paperId) {
        int q1Total = AppDatabase.getDatabaseInstance(context).getScoreDao().getLevelCnt(level, paperId);
        int q1CorrectCnt = AppDatabase.getDatabaseInstance(context).getScoreDao().getLevelCorrectCnt(level, paperId, true);
        float q1Rating = CalculateRating(q1Total, q1CorrectCnt);
        return q1Rating;
    }*/

    private float CalculateRating(int q1Total, int q1CorrectCnt) {
        float ratings = 0;
        try {
            float perc = (float) q1CorrectCnt / (float) q1Total;
            if (perc < 0.2)
                ratings = (float) 1;
            else if (perc >= 0.2 && perc < 0.4)
                ratings = (float) 2;
            else if (perc >= 0.4 && perc < 0.6)
                ratings = (float) 3;
            else if (perc >= 0.6 && perc < 0.8)
                ratings = (float) 4;
            else if (perc >= 0.8)
                ratings = (float) 5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratings;
    }

    private void saveAttemptedQuestionsInDB() {
        List<ScienceQuestion> attemptedQuestion = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            if (scienceQuestionList.get(i).getIsAttempted())
                attemptedQuestion.add(scienceQuestionList.get(i));
        }
        int count = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdPaperIdStudId(selectedExamId, selectedLang, subjectId, assessmentSession, currentStudentID);

        int count1 = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdStudId(selectedExamId, selectedLang, subjectId, currentStudentID);
        Log.d("delete count", "doInBackground: " + count + count1);
        if (attemptedQuestion.size() > 0) {
            insertInDB(attemptedQuestion, " Exam incomplete");
            if (!AssessmentApplication.isTablet) {
                pushDataForNIOS();
            } else finish();
        } else finish();

    }


    private void insertInDB(List<ScienceQuestion> scienceQuestionList, String examStatus) {
//        stopService(new Intent(this, BkgdVideoRecordingService.class));
        try {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            if (!timesUp) {
                isEndTimeSet = true;
                examEndTime = Assessment_Utility.getCurrentDateTime();
            }
            calculateMarks();
            calculateCorrectWrongCount();
            AssessmentPaperForPush paper = createPaperToSave(scienceQuestionList);
            String currentSession = FastSave.getInstance().getString("currentSession", "");
            String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");

        /*AssessmentPaperForPush paper = new AssessmentPaperForPush();
        paper.setPaperStartTime(examStartTime);
        paper.setPaperEndTime(examEndTime);
        paper.setLanguageId(selectedLang);
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
        String currentSession = FastSave.getInstance().getString("currentSession", "");
//        paper.setSessionID(Assessment_Constants.currentSession);
        paper.setSessionID(currentSession);
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        paper.setStudentId("" + Assessment_Constants.currentStudentID);
        paper.setStudentId("" + currentStudentID);
        paper.setExamName(assessmentPaperPatterns.getExamname());
*/
            ArrayList<Score> scores = new ArrayList<>();
            for (int i = 0; i < scienceQuestionList.size(); i++) {
                Score score = new Score();
                score.setQuestionId(Integer.parseInt(scienceQuestionList.get(i).getQid()));
                //            score.setLevel(getLevel(scienceQuestionList.get(i).getQlevel()));
                if (scienceQuestionList.get(i).getQlevel().matches("[0-9]+"))
                    score.setLevel(Integer.parseInt(scienceQuestionList.get(i).getQlevel()));
                else score.setLevel(Integer.parseInt(scienceQuestionList.get(i).getQlevel()));
                score.setIsAttempted(scienceQuestionList.get(i).getIsAttempted());
                score.setIsCorrect(scienceQuestionList.get(i).getIsCorrect());
                score.setTotalMarks(Integer.parseInt(scienceQuestionList.get(i).getOutofmarks()));
                score.setExamId(selectedExamId);
                score.setStartDateTime(scienceQuestionList.get(i).getStartTime());
                if (FastSave.getInstance().getBoolean(Assessment_Constants.SUPERVISED, false))
                    //            if (Assessment_Constants.ASSESSMENT_TYPE.equalsIgnoreCase("practice"))
                    score.setLabel("supervised assessment" + examStatus);
                else score.setLabel(Assessment_Constants.PRACTICE + examStatus);
                score.setEndDateTime(scienceQuestionList.get(i).getEndTime());
                //            score.setStudentID(Assessment_Constants.currentStudentID);
                score.setStudentID(currentStudentID);
                score.setDeviceID(AppDatabase.getDatabaseInstance(this).getStatusDao().getValue("DeviceId"));
                //            score.setSessionID(Assessment_Constants.assessmentSession);
                score.setPaperId(assessmentSession);
                //            score.setSessionID(Assessment_Constants.currentSession);
                score.setSessionID(currentSession);
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


            BackupDatabase.backup(this);


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }


    private void calculateCorrectWrongCount() {
        skippedCnt = correctAnsCnt = wrongAnsCnt = 0;

        for (int i = 0; i < scienceQuestionList.size(); i++) {
            if (scienceQuestionList.get(i).getIsCorrect()) correctAnsCnt++;
            else if (!scienceQuestionList.get(i).getIsCorrect() && scienceQuestionList.get(i).getIsAttempted())
                wrongAnsCnt++;
            if (!scienceQuestionList.get(i).getIsAttempted()) skippedCnt++;
        }
    }

    private void generateResultData(AssessmentPaperForPush paper) {
        ArrayList<ResultModalClass> resultList = new ArrayList<>();
        ResultOuterModalClass outerModalClass = new ResultOuterModalClass();

        outerModalClass.setOutOfMarks("" + outOfMarks);
        outerModalClass.setMarksObtained("" + totalMarks);
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        outerModalClass.setStudentId(Assessment_Constants.currentStudentID);
        outerModalClass.setStudentId(currentStudentID);
        outerModalClass.setExamStartTime(examStartTime);
        outerModalClass.setExamId(selectedExamId);
        outerModalClass.setSubjectId(paper.getSubjectId());
        outerModalClass.setExamEndTime(examEndTime);
        outerModalClass.setPaperId(assessmentSession);
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            ResultModalClass result = new ResultModalClass();
            result.setQuestion(scienceQuestionList.get(i).getQname());
            result.setqId(scienceQuestionList.get(i).getQid());
            result.setUserAnswer(scienceQuestionList.get(i).getUserAnswer());
            result.setUserAnswerId(scienceQuestionList.get(i).getUserAnswerId());
            result.setCorrectAnswer(scienceQuestionList.get(i).getAnswer());
            result.setCorrect(scienceQuestionList.get(i).getIsCorrect());
            result.setAttempted(scienceQuestionList.get(i).getIsAttempted());
            result.setQuestionImg(scienceQuestionList.get(i).getPhotourl());
            resultList.add(result);
        }
        outerModalClass.setResultList(resultList);
        if (resultList.size() > 0) {
            endTestSession();
            Intent intent = new Intent(this, ResultActivity_.class);
            intent.putExtra("result", outerModalClass);
           /* intent.putExtra("outOfMarks", "" + outOfMarks);
            intent.putExtra("marksObtained", "" + totalMarks);
            intent.putExtra("studentId", Assessment_Constants.currentStudentID);
            intent.putExtra("examStartTime", examStartTime);
            intent.putExtra("examId", paper.getExamId());
            intent.putExtra("subjectId", paper.getSubjectId());
            intent.putExtra("examEndTime", examEndTime);
            intent.putExtra("paperId", assessmentSession);*/
            startActivity(intent);
            finish();
        }
    }

    private void calculateMarks() {

        try {
            totalMarks = outOfMarks = 0;
            for (int i = 0; i < scienceQuestionList.size(); i++) {
                totalMarks += Integer.parseInt(scienceQuestionList.get(i).getMarksPerQuestion());
//                outOfMarks += Integer.parseInt(scienceQuestionList.get(i).getOutofmarks());
            }
            outOfMarks = Integer.parseInt(assessmentPaperPatterns.getOutofmarks().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
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
        /*if (speech != null)
            speech.stopListening();*/
//        speech = null;
       /* if (Assessment_Constants.VIDEOMONITORING) {
            VideoMonitoringService.releaseMediaRecorder();
        }*/
//        stopService(serviceIntent);
       /* if (Assessment_Constants.supervisedAssessment)
            if (pinActivity != null) {
                pinActivity.unLockHomeButton();
            }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
       /* if (speech != null)
            speech.stopListening();*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timesUp) {
            if (!isEndTimeSet)
                examEndTime = Assessment_Utility.getCurrentDateTime();
            if (isActivityRunning) {
                AssessmentTimeUpDialog timeUpDialog = new AssessmentTimeUpDialog(this);
                timeUpDialog.show();
                if (mCountDownTimer != null)
                    mCountDownTimer.cancel();

                int count = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                        getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdPaperIdStudId(selectedExamId, selectedLang, subjectId, assessmentSession, currentStudentID);
                int count1 = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).
                        getTempScienceQuestionDao().deleteByLangIdSubIdTopicIdStudId(selectedExamId, selectedLang, subjectId, currentStudentID);
                Log.d("delete count", "doInBackground: " + count + count1);

            }
        } /*else if (Assessment_Constants.VIDEOMONITORING) {

            //******* Video monitoring *******

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (serviceIntent != null)
                        startCameraService();
                }
            }, 1000);
        }*/
//todo        resetSpeechRecognizer(); remove speech=null from onStop
        if (mediaProgressDialog != null && isActivityRunning && mediaProgressDialog.isShowing())
            if (mediaDownloadCnt >= downloadMediaList.size()) {
                if (mediaProgressDialog != null && isActivityRunning) mediaProgressDialog.dismiss();
            }

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == -1 && requestCode == VIDEO_CAPTURE) {
                showCapturedVideo();
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
          *//*  File direct = new File(Environment.getExternalStorageDirectory().toString() + "/.Assessment");

            if (!direct.exists()) direct.mkdir();
           *//*
//                File direct = new File(Environment.getExternalStorageDirectory().toString() + Assessment_Constants.STORE_ANSWER_MEDIA_PATH);
                File direct = new File(AssessmentApplication.assessPath + Assessment_Constants.STORE_ANSWER_MEDIA_PATH);

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
*/


    public void endTestSession() {
        try {
            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        String currentSession = FastSave.getInstance().getString("currentSession", "");

//                        String toDateTemp = appDatabase.getSessionDao().getToDate(Assessment_Constants.currentSession);
                        String toDateTemp = appDatabase.getSessionDao().getToDate(currentSession);

                        if (toDateTemp.equalsIgnoreCase("na")) {
//                            appDatabase.getSessionDao().UpdateToDate(Assessment_Constants.currentSession, AssessmentApplication.getCurrentDateTime());
                            appDatabase.getSessionDao().UpdateToDate(currentSession, AssessmentApplication.getCurrentDateTime());
                        }
                        BackupDatabase.backup(ScienceAssessmentActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void startCameraService() {
        if (serviceIntent != null)
            startService(serviceIntent);
        String fileName = assessmentSession + "_" + Assessment_Utility.getCurrentDateTime() + ".mp4";
        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
        String videoPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_VIDEO_MONITORING_PATH + "/" + fileName;
        VideoMonitoringService.releaseMediaRecorder();
        String finalFileName = fileName;
        new Handler().postDelayed(() -> {
            if (VideoMonitoringService.prepareVideoRecorder(texture_view, finalFileName)) {
                VideoMonitoringService.startCapture();
                DownloadMedia video = new DownloadMedia();
                video.setMediaType(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING);
                video.setPhotoUrl(videoPath);
                video.setPaperId(assessmentSession);
                AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getDownloadMediaDao().insert(video);

            } else {
                Assessment_Constants.VIDEOMONITORING = false;
                texture_view.setVisibility(View.GONE);
                tv_timer.setTextColor(Color.BLACK);
                frame_video_monitoring.setVisibility(View.GONE);
                btn_save_Assessment.setVisibility(View.VISIBLE);
                Toast.makeText(ScienceAssessmentActivity.this, "video monitoring not prepared", Toast.LENGTH_LONG).show();
            }

        }, 300);
    }

    @Override
    public void onCaptureDone(String pictureUrl, byte[] pictureData) {
        if (pictureData != null && pictureUrl != null) {
            runOnUiThread(() -> {
                final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                final int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                final Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                /*if (pictureUrl.contains("0_pic.jpg")) {
                    uploadBackPhoto.setImageBitmap(scaled);
                } else if (pictureUrl.contains("1_pic.jpg")) {
                    uploadFrontPhoto.setImageBitmap(scaled);
                }*/
            });
//            showToast("Picture saved to " + pictureUrl);
        }
    }

    @Override
    public void onDoneCapturingAllPhotos(TreeMap<String, byte[]> picturesTaken) {
        if (picturesTaken != null && !picturesTaken.isEmpty()) {
//            showToast("Done capturing photo!");
            DownloadMedia video = new DownloadMedia();
            video.setMediaType(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING);
            String videoPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_VIDEO_MONITORING_PATH + "/" + imgFileName;
            video.setPhotoUrl(videoPath);
            video.setPaperId(assessmentSession);
            AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getDownloadMediaDao().insert(video);

            return;
        }
        showToast("No camera detected!");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    checkPermissions();
                }
            }
        }
    }

    /**
     * checking  permissions at Runtime.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }


    @Override
    public void onResponseGet() {
        if (isAssessmentCompleted) {
            AssessmentPaperForPush paper = createPaperToSave(scienceQuestionList);
            generateResultData(paper);
        } else {
            finish();
        }
    }
}
