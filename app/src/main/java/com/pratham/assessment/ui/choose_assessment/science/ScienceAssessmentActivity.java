package com.pratham.assessment.ui.choose_assessment.science;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ScienceAdapter;
import com.pratham.assessment.ui.choose_assessment.science.bottomFragment.BottomQuestionFragment;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.AssessmentTimeUpDialog;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.DownloadTopicsDialog;
import com.pratham.assessment.ui.choose_assessment.science.custom_dialogs.SelectTopicDialog;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTrackerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.TopicSelectListener;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScienceAssessmentActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener, TopicSelectListener, AssessmentAnswerListener, QuestionTrackerListener {

    List<AssessmentPaperPattern> exams = new ArrayList<>();
    List<String> examIDList = new ArrayList<>();
    List<String> topicIdList = new ArrayList<>();
    List<AssessmentLanguages> languages = new ArrayList<>();
    List<AssessmentSubjects> subjects = new ArrayList<>();
    ProgressDialog progressDialog;
    List<ScienceQuestion> scienceQuestionList = new ArrayList<>();
    AssessmentPaperPattern assessmentPaperPatterns;
    List<AssessmentPatternDetails> assessmentPatternDetails;
    List<String> downloadFailedExamList = new ArrayList<>();

    int queDownloadIndex = 0;
    int paperPatternCnt = 0;
    int ansCnt = 0, queCnt = 1;
    List attemptedQIds = new ArrayList();
    List<ScienceQuestionChoice> userAnsList = new ArrayList();
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

    @BindView(R.id.current_cnt)
    TextView currentCount;

    @BindView(R.id.iv_prev)
    ImageView iv_prev;
    @BindView(R.id.iv_next)
    ImageView iv_next;

    int i = 0;
    int totalMarks = 0;
    int outOfMarks = 0;

    String answer = "", ansId = "";
    String questionType = "";
    SelectTopicDialog selectTopicDialog;
    Dialog downloadTopicDialog;
    CountDownTimer mCountDownTimer;
    String supervisorId;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science_assessment);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supervisorId = getIntent().getStringExtra("crlId");
        progressDialog = new ProgressDialog(this);
        showSelectTopicDialog();
        //getTopicData();
        //scienceModalClassList = fetchJson("science.json");

        // setQuestions();

        Assessment_Constants.isShowcaseDisplayed = false;

    }

    private void showSelectTopicDialog() {
        exams = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getAllAssessmentPaperPatterns();
        subjects = AppDatabase.getDatabaseInstance(this).getSubjectDao().getAllSubjects();
        languages = AppDatabase.getDatabaseInstance(this).getLanguageDao().getAllLangs();
        selectTopicDialog = new SelectTopicDialog(this, this, exams, languages, subjects);


        if (exams.size() > 0) {
            selectTopicDialog.show();
            selectTopicDialog.updateTopics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTopicDialog.dismiss();
                    exams.clear();
                    getLanguageData();
                }
            });
        } else {

            getLanguageData();
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

                            getSubjectData();
//                            showDownloadTopicDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading..Check internet connection.", Toast.LENGTH_SHORT).show();
                        finish();
                        progressDialog.dismiss();
                        downloadTopicDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }

    private void getSubjectData() {
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
                        progressDialog.dismiss();
                    }
                });

    }


    private void showDownloadTopicDialog() {
        downloadTopicDialog = new DownloadTopicsDialog(this, this, exams, languages, subjects);
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
    public void getSelectedItems(List<String> examIdList, String selectedLang, String selectedSub, List<AssessmentToipcsModal> topics) {
        /*this.exams = exams;
        for (int i = 0; i < examIDList.size(); i++) {
            insertTopicsToDB(examIDList.get(i));
        }*/
        this.examIDList = examIdList;

        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
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
        downloadPaperPattern(examIdList.get(paperPatternCnt), langId, subId);
       /* }

        topicIdList = AppDatabase.getDatabaseInstance(this).getAssessmentPatternDetailsDao().getDistinctTopicIds();
        downloadQuestions(topicIdList.get(queDownloadIndex), langId, subId);
*/
    }

    @Override
    public void getSelectedTopic(String exam, String selectedSub, String selectedLang, SelectTopicDialog selectTopicDialog) {
//        String topicId = AppDatabase.getDatabaseInstance(this).getAssessmentTopicDao().getTopicIdByTopicName(topic);
        String examId = AppDatabase.getDatabaseInstance(this).getAssessmentPaperPatternDao().getExamIdByExamName(exam);
        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
        String langId = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangNameById(selectedLang);

        generatePaperPattern(examId, subId, langId);

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
                        progressDialog.dismiss();
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
                            topicIdList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPatternDetailsDao().getDistinctTopicIds();
                            if (downloadFailedExamList.size() == 0)
                                if (topicIdList.size() > 0)
                                    downloadQuestions(topicIdList.get(queDownloadIndex), langId, subId);
                                else if (!downloadTopicDialog.isShowing())
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
                        topicIdList = AppDatabase.getDatabaseInstance(ScienceAssessmentActivity.this).getAssessmentPatternDetailsDao().getDistinctTopicIds();
//                            progressDialog.dismiss();
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
                }
            }
            BackupDatabase.backup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showQuestions() {
        Collections.shuffle(scienceQuestionList);
        for (int i = 0; i < scienceQuestionList.size(); i++) {
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
            scienceQuestionList.get(0).setStartTime(Assessment_Utility.GetCurrentDateTime());

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

                }

            });
            scienceAdapter.notifyDataSetChanged();
        }
    }

    private void setProgressBarAndTimer() {
        progressBarTimer.setProgress(100);
        ExamTime = Integer.parseInt(assessmentPaperPatterns.getExamduration());
        if (ExamTime == 0)
            ExamTime = 30;
        final int timer = ExamTime * 60000;

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

 /*   @OnClick(R.id.btn_next)
    public void onNextClick() {
        int cnt=0;

        for (int i = 0; i < scienceQuestionList.size(); i++) {
            if (scienceQuestionList.get(i).getIsAttempted()) {
                cnt++;
            } else {
//                Toast.makeText(this, "Please complete all questions...", Toast.LENGTH_SHORT).show();
                discreteScrollView.scrollToPosition(i);
                break;
            }
        }

        Toast.makeText(this, "qqqq   " + answer + " " + ansId, Toast.LENGTH_SHORT).show();

    }*/


    @OnClick(R.id.iv_prev)
    public void prevClick() {
        Animation animation;

        if (queCnt > 1) {

            scienceQuestionList.get(queCnt - 1).setEndTime(Assessment_Utility.GetCurrentDateTime());
            queCnt--;
            discreteScrollView.smoothScrollToPosition(queCnt - 1);
            scienceQuestionList.get(queCnt).setStartTime(Assessment_Utility.GetCurrentDateTime());

        }


       /* if (queCnt == 1) {
            animation = AnimationUtils.loadAnimation(this, R.anim.move_to_right);
            iv_prev.startAnimation(animation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_prev.setVisibility(View.GONE);
                    iv_prev.setEnabled(false);
                }
            }, 800);
        }*/
        currentCount.setText(queCnt + "/" + scienceQuestionList.size());


    }


    @OnClick(R.id.iv_next)
    public void nextClick() {
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
           /* animation = AnimationUtils.loadAnimation(this, R.anim.move_to_left);
            iv_next.startAnimation(animation);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_next.setVisibility(View.GONE);
                    iv_next.setEnabled(false);
                }
            }, 800);*/
        }
     /*   if (queCnt == 2) {
            animation = AnimationUtils.loadAnimation(this, R.anim.move_to_left);
            iv_prev.startAnimation(animation);
        }*/
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
/*
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to save assessment?");
        builder.setMessage("Attempted : " + ansCnt + "  Total Questions : " + scienceQuestionList.size());
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveAssessmentToDB();
                finish();
            }
        });
        builder.setNegativeButton("Review", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();

    }*/


  /*  private void saveAssessmentToDB() {
        List<Assessment> assessmentList = new ArrayList<>();

        for (int i = 0; i < scienceQuestionList.size(); i++) {
            Assessment assessment = new Assessment();
            assessment.setResourceIDa("");
            assessment.setSessionIDa(Assessment_Constants.assessmentSession);
            assessment.setSessionIDm(Assessment_Constants.currentSession);
            assessment.setQuestionIda(0);
            *//*if (eceModelList.get(i).getIsSelected() == 1)
                assessment.setScoredMarksa(10);
            else if (eceModelList.get(i).getIsSelected() == 2)
                assessment.setScoredMarksa(5);
          *//*
            assessment.setTotalMarksa(0);
            assessment.setStudentIDa(Assessment_Constants.currentStudentID);
            assessment.setStartDateTimea("");
            assessment.setEndDateTime(AssessmentApplication.getCurrentDateTime());
            assessment.setDeviceIDa("");
            assessment.setLevela(0);
            assessment.setLabel("");
            assessment.setSentFlag(0);
            assessmentList.add(assessment);
        }
        AppDatabase.getDatabaseInstance(this).getAssessmentDao().insertAllAssessments(assessmentList);
        BackupDatabase.backup(this);
    }*/

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
        calculateMarks();
        AssessmentPaperForPush paper = new AssessmentPaperForPush();
        paper.setExamId(scienceQuestionList.get(0).getExamid());
        paper.setPaperId(scienceQuestionList.get(0).getPaperid());
        paper.setOutOfMarks("" + outOfMarks);
        paper.setTotalMarks("" + totalMarks);

        ArrayList<Score> scores = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            Score score = new Score();
            score.setQuestionId(Integer.parseInt(scienceQuestionList.get(i).getQid()));
            score.setLevel(getLevel(scienceQuestionList.get(i).getQlevel()));
            score.setIsAttempted(scienceQuestionList.get(i).getIsAttempted());
            score.setIsCorrect(scienceQuestionList.get(i).getIsCorrect());
            score.setTotalMarks(Integer.parseInt(scienceQuestionList.get(i).getOutofmarks()));
            score.setStartDateTime(scienceQuestionList.get(i).getStartTime());
            if (supervisorId.equalsIgnoreCase(""))
                score.setLabel("practice");
            else score.setLabel("supervised assessment");
            score.setEndDateTime(scienceQuestionList.get(i).getEndTime());
            score.setStudentID(Assessment_Constants.currentStudentID);
            score.setDeviceID(AppDatabase.getDatabaseInstance(this).getStatusDao().getValue("DeviceId"));
            score.setSessionID(Assessment_Constants.assessmentSession);
            score.setScoredMarks(Integer.parseInt(scienceQuestionList.get(i).getMarksPerQuestion()));
            if (!scienceQuestionList.get(i).getUserAnswer().equalsIgnoreCase(""))
                score.setUserAnswer(scienceQuestionList.get(i).getUserAnswerId());
            else if (scienceQuestionList.get(i).getUserAnswer().equalsIgnoreCase("") && !scienceQuestionList.get(i).getUserAnswerId().equalsIgnoreCase(""))
                score.setUserAnswer(scienceQuestionList.get(i).getUserAnswerId());
            scores.add(score);
        }
        paper.setScoreList(scores);
        AppDatabase.getDatabaseInstance(this).getScoreDao().insertAllScores(scores);
        AppDatabase.getDatabaseInstance(this).getAssessmentPaperForPushDao().insertPaperForPush(paper);
        Toast.makeText(this, "Assessment saved successfully", Toast.LENGTH_SHORT).show();
        BackupDatabase.backup(this);
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
    }

    /*@Override
    public void onSaveAssessmentClick() {
        List<Assessment> assessments = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {

            Assessment assessment = new Assessment();
            assessment.setQuestionIda(Integer.parseInt(scienceQuestionList.get(i).getQid()));
//            assessment.setLevela(Integer.parseInt(scienceQuestionList.get(i).getQlevel()));
            assessment.setIsAttempted(scienceQuestionList.get(i).getIsAttempted());
            assessment.setIsCorrect(scienceQuestionList.get(i).getIsCorrect());
            assessment.setExamId(scienceQuestionList.get(i).getExamid());
            assessment.setPaperId(scienceQuestionList.get(i).getPaperid());
            assessment.setTotalMarksa(Integer.parseInt(scienceQuestionList.get(i).getOutofmarks()));
            assessment.setStartDateTimea(scienceQuestionList.get(i).getStartTime());
            assessment.setLabel("assessment");
            assessment.setEndDateTime(scienceQuestionList.get(i).getEndTime());
            assessment.setStudentIDa(Assessment_Constants.currentStudentID);
            assessment.setScoredMarksa(Integer.parseInt(scienceQuestionList.get(i).getMarksPerQuestion()));
            assessments.add(assessment);

        }
        AppDatabase.getDatabaseInstance(this).getAssessmentDao().insertAllAssessments(assessments);
        Toast.makeText(this, "Assessment saved successfully", Toast.LENGTH_SHORT).show();
    }*/



    /*    @Override
    public void onSaveAssessmentClick() {
//        List<ScienceAssessmentAnswer> scienceAssessmentAnswers = new ArrayList<>();
        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {
//            ScienceAssessmentAnswer scienceAssessmentAnswer = new ScienceAssessmentAnswer();

            Score score = new Score();
            score.setQuestionId(Integer.parseInt(scienceQuestionList.get(i).getQid()));
            score.setLevel(Integer.parseInt(scienceQuestionList.get(i).getQlevel()));
//            score.setIsAttempted(scienceQuestionList.get(i).getIsAttempted());
//            score.setIsCorrect(scienceQuestionList.get(i).getIsCorrect());
//            score.setExamid(scienceQuestionList.get(i).getExamid());
//            score.setPaperid(scienceQuestionList.get(i).getPaperid());
            score.setTotalMarks(Integer.parseInt(scienceQuestionList.get(i).getOutofmarks()));
            score.setStartDateTime(scienceQuestionList.get(i).getStartTime());
            score.setEndDateTime(scienceQuestionList.get(i).getEndTime());
            score.setStudentID(Assessment_Constants.currentStudentID);
            score.setScoredMarks(Integer.parseInt(scienceQuestionList.get(i).getMarksPerQuestion()));
            scores.add(score);

            *//* scienceAssessmentAnswer.setQid(scienceQuestionList.get(i).getQid());
            scienceAssessmentAnswer.setLanguageid(scienceQuestionList.get(i).getLanguageid());
            scienceAssessmentAnswer.setSubjectid(scienceQuestionList.get(i).getSubjectid());
            scienceAssessmentAnswer.setTopicid(scienceQuestionList.get(i).getTopicid());
            scienceAssessmentAnswer.setLessonid(scienceQuestionList.get(i).getLessonid());
            scienceAssessmentAnswer.setQtid(scienceQuestionList.get(i).getQtid());
            scienceAssessmentAnswer.setQlevel(scienceQuestionList.get(i).getQlevel());
            scienceAssessmentAnswer.setIsAttempted(scienceQuestionList.get(i).getIsAttempted());
            scienceAssessmentAnswer.setIsCorrect(scienceQuestionList.get(i).getIsCorrect());
            scienceAssessmentAnswer.setOutofmarks(scienceQuestionList.get(i).getOutofmarks());
            scienceAssessmentAnswer.setExamid(scienceQuestionList.get(i).getExamid());
            scienceAssessmentAnswer.setPaperid(scienceQuestionList.get(i).getPaperid());
//           scienceAssessmentAnswer.setQuestionStartTime(scienceQuestionList.get(i).ge());
//           scienceAssessmentAnswer.setTimeTakenToAnswer(scienceQuestionList.get(i).ge());
            scienceAssessmentAnswer.setStudentID(Assessment_Constants.currentStudentID);
            scienceAssessmentAnswer.setScoredMarks(scienceQuestionList.get(i).getMarksPerQuestion());
            scienceAssessmentAnswers.add(scienceAssessmentAnswer);*//*
        }
        AppDatabase.getDatabaseInstance(this).getScoreDao().addScoreList(scores);
//        AppDatabase.getDatabaseInstance(this).getScienceAssessmentAnswerDao().insertAllAnswers(scienceAssessmentAnswers);
    }*/
}
