package com.pratham.assessment.ui.choose_assessment.science;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.discrete_view.DSVOrientation;
import com.pratham.assessment.discrete_view.DiscreteScrollView;
import com.pratham.assessment.discrete_view.ScaleTransformer;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentToipcsModal;
import com.pratham.assessment.domain.ScienceModalClass;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.utilities.APIs;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScienceAssessmentActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener, TopicSelectListener {
    List<ScienceModalClass> scienceModalClassList;

    List<AssessmentToipcsModal> topics = new ArrayList<>();
    ArrayList<String> topicIDList = new ArrayList<>();
    List<AssessmentLanguages> languages = new ArrayList<>();
    List<AssessmentSubjects> subjects = new ArrayList<>();
    ProgressDialog progressDialog;

    int queDownloadIndex = 0;

    @BindView(R.id.question_discrete_view)
    DiscreteScrollView discreteScrollView;
    @BindView(R.id.timer)
    Chronometer chronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science_assessment);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog = new ProgressDialog(this);
        showSelectTopicDialog();
        //getTopicData();
        //scienceModalClassList = fetchJson("science.json");

        // setQuestions();

    }

    private void showSelectTopicDialog() {
        topics = AppDatabase.getDatabaseInstance(this).getAssessmentTopicDao().getAllAssessmentToipcs();
        subjects = AppDatabase.getDatabaseInstance(this).getSubjectDao().getAllSubjects();
        languages = AppDatabase.getDatabaseInstance(this).getLanguageDao().getAllLangs();
        SelectTopicDialog selectTopicDialog = new SelectTopicDialog(this, this, topics, languages, subjects);


        if (topics.size() > 0) {
            selectTopicDialog.show();
            selectTopicDialog.updateTopics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    topics.clear();
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
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }

    private void getTopicData(String selectedSub) {

//        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);

        topics.clear();

        AndroidNetworking.get(APIs.AssessmentSubjectWiseTopicAPI + selectedSub)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response != null) {
                                for (int i = 0; i < response.length(); i++) {
                                    AssessmentToipcsModal assessmentToipcsModal = new AssessmentToipcsModal();
                                    assessmentToipcsModal.setTopicid(response.getJSONObject(i).getString("topicid"));
                                    assessmentToipcsModal.setTopicname(response.getJSONObject(i).getString("topicname"));
                                    assessmentToipcsModal.setSubjectid(response.getJSONObject(i).getString("subjectid"));
                                    topics.add(assessmentToipcsModal);

                                }
                                progressDialog.dismiss();
                                showDownloadTopicDialog();
                            } else {
                                Toast.makeText(ScienceAssessmentActivity.this, "No data", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });

    }

    private void showDownloadTopicDialog() {
        Dialog topicDialog = new DownloadTopicsDialog(this, this, topics, languages, subjects);
        topicDialog.show();
    }

    private List<ScienceModalClass> fetchJson(String jasonName) {
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

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    @Override
    public void getSelectedItems(ArrayList<String> topicIDList, String selectedLang, String selectedSub, List<AssessmentToipcsModal> topics) {
        this.topics = topics;
        for (int i = 0; i < topicIDList.size(); i++) {
            insertTopicsToDB(topicIDList.get(i));
        }
        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
        String langId = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangNameById(selectedLang);

        downloadQuestions(topicIDList.get(queDownloadIndex), langId, subId);

    }

    @Override
    public void getSelectedTopic(String topic, String selectedSub, String selectedLang, SelectTopicDialog selectTopicDialog) {
        String topicId = AppDatabase.getDatabaseInstance(this).getAssessmentTopicDao().getTopicIdByTopicName(topic);
        String subId = AppDatabase.getDatabaseInstance(this).getSubjectDao().getIdByName(selectedSub);
        String langId = AppDatabase.getDatabaseInstance(this).getLanguageDao().getLangNameById(selectedLang);
        showQuestions(topicId, subId, langId, selectTopicDialog);

    }


    private void insertTopicsToDB(String topicId) {
        for (int i = 0; i < topics.size(); i++) {
            if (topics.get(i).getTopicid().equalsIgnoreCase(topicId))
                AppDatabase.getDatabaseInstance(this).getAssessmentTopicDao().insertTopic(topics.get(i));
        }
        BackupDatabase.backup(this);

    }

    private void downloadQuestions(final String topicId, final String selectedLang, final String selectedSub) {
        String questionUrl = APIs.AssessmentQuestionAPI + "languageid=" + selectedLang + "&subjectid=" + selectedSub + "&topicid=" + topicId;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Downloading...");
        progressDialog.show();
        AndroidNetworking.get(questionUrl)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.dismiss();
                        if (response.length() > 0) {
                            insertQuestionsToDB(response);
                            queDownloadIndex++;
                            if (queDownloadIndex < topicIDList.size())
                                downloadQuestions(topicIDList.get(queDownloadIndex), selectedLang, selectedSub);
                            else {
                                progressDialog.dismiss();
                                showSelectTopicDialog();
                            }
                        } else {
                            Toast.makeText(ScienceAssessmentActivity.this, "Nothing to download...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ScienceAssessmentActivity.this, "Error in loading", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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

    private void showQuestions(String topicId, String subId, String langId, SelectTopicDialog selectTopicDialog) {
        List<ScienceQuestion> scienceQuestionList = AppDatabase.getDatabaseInstance(this).getScienceQuestionDao().getQuestionListByLangIdSubIdTopicId(topicId, langId, subId);
        if (scienceQuestionList.isEmpty()) {
            Toast.makeText(this, "No questions", Toast.LENGTH_SHORT).show();
        } else {
            selectTopicDialog.dismiss();
            ScienceAdapter scienceAdapter = new ScienceAdapter(this, scienceQuestionList);
            discreteScrollView.setOrientation(DSVOrientation.HORIZONTAL);
            discreteScrollView.addOnItemChangedListener(this);
            discreteScrollView.setItemTransitionTimeMillis(200);
            discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.5f)
                    .build());
            discreteScrollView.setAdapter(scienceAdapter);
            scienceAdapter.notifyDataSetChanged();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        chronometer.stop();
    }
}
