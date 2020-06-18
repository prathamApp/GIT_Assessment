package com.pratham.assessment.ui.choose_assessment.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.AssessmentTestModal;
import com.pratham.assessment.domain.NIOSExam;
import com.pratham.assessment.domain.NIOSExamTopics;
import com.pratham.assessment.ui.choose_assessment.ChooseAssessmentActivity;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

@EFragment(R.layout.fragment_topic)
public class TopicFragment extends Fragment {
    List<AssessmentTestModal> assessmentTestModals;
    List<AssessmentTest> assessmentTests = new ArrayList<>();
    @ViewById(R.id.rv_topics)
    RecyclerView rv_topics;
    String subjectId;
    ProgressDialog progressDialog;


    public TopicFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        subjectId = FastSave.getInstance().getString("SELECTED_SUBJECT_ID", "1");
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            if (FastSave.getInstance().getBoolean("enrollmentNoLogin", false))
                getNIOSExams();
            else
                getExamData();
        } else {
            if (FastSave.getInstance().getBoolean("enrollmentNoLogin", false))
                getNIOSExams();
            else
                getOfflineTests();
        }
    }

    private void getNIOSExams() {
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");

        List<NIOSExam> AllExams = AppDatabase.getDatabaseInstance(getActivity()).getNiosExamDao().getAllSubjectsByStudId(currentStudentID);

        if (AllExams != null && AllExams.size() > 0)
            for (int i = 0; i < AllExams.size(); i++) {
                List<NIOSExamTopics> allTopics = AppDatabase.getDatabaseInstance(getActivity()).getNiosExamTopicDao().getTopicIdByExamId(AllExams.get(i).getExamid());
                if (allTopics != null && allTopics.size() > 0)
                    for (int j = 0; j < allTopics.size(); j++) {
                        AssessmentTest test = new AssessmentTest();
                        test.setLanguageId(allTopics.get(j).getLanguageid());
                        test.setSubjectname(allTopics.get(j).getSubjectname());
                        test.setSubjectid(allTopics.get(j).getSubjectid());
                        test.setExamname(allTopics.get(j).getExamname());
                        test.setExamid(allTopics.get(j).getExamid());
                        assessmentTests.add(test);
                    }
            }
        setTopicsToRecyclerView();

    }

    private void getNIOSTest() {
        try {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Exams");
            progressDialog.setCancelable(false);
            progressDialog.show();
            AndroidNetworking.get(APIs.AssessmentEnrollmentNoExamAPI + FastSave.getInstance().getString("currentStudentID", ""))
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                         /*   Gson gson = new Gson();
                            Type listType = new TypeToken<List<AssessmentTestModal>>() {
                            }.getType();
                            assessmentTestModals = gson.fromJson(response, listType);
                            assessmentTests.clear();*/
                            assessmentTestModals = new ArrayList<>();
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    Log.d("onResponse", "onResponse: " + jsonArray.length());
                                    JSONObject outer = jsonArray.getJSONObject(j);
//                                    for (int i = 0; i < outer.length(); i++) {
                                    Log.d("onResponse", "onResponse outer: " + outer.length());
                                    AssessmentTestModal testModal = new AssessmentTestModal();
                                    AssessmentTest test = new AssessmentTest();
                                    JSONArray lststudentexamtopic = outer.getJSONArray("lststudentexamtopic");
                                    JSONObject jsonObject = new JSONObject(lststudentexamtopic.getJSONObject(0).toString());
                                    testModal.setSubjectid(jsonObject.getString("subjectid"));
                                    testModal.setSubjectname(jsonObject.getString("subjectname"));
                                    assessmentTestModals.add(testModal);
                                    test.setExamid(jsonObject.getString("examid"));
                                    test.setSubjectid(jsonObject.getString("subjectid"));
                                    test.setExamname(jsonObject.getString("examname"));
                                    test.setSubjectname(jsonObject.getString("subjectname"));
                                    test.setLanguageId(jsonObject.getString("languageid"));
                                    assessmentTests.add(test);
//                                    }
                                }


                             /*   AppDatabase.getDatabaseInstance(getActivity()).getTestDao().deleteTestsByLangIdAndSubId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
                                AppDatabase.getDatabaseInstance(getActivity()).getTestDao().insertAllTest(assessmentTests);
*/



                           /* for (int i = 0; i < assessmentTestModals.size(); i++) {
                                assessmentTests.addAll(assessmentTestModals.get(i).getLstsubjectexam());
                                for (int j = 0; j < assessmentTests.size(); j++) {
                                    assessmentTests.get(j).setSubjectid(assessmentTestModals.get(i).getSubjectid());
                                    assessmentTests.get(j).setSubjectname(assessmentTestModals.get(i).getSubjectname());
                                    assessmentTests.get(j).setLanguageId(Assessment_Constants.SELECTED_LANGUAGE);
                                }
                            }*/
                                if (assessmentTests.size() > 0) {
                                    AppDatabase.getDatabaseInstance(getActivity()).getTestDao().deleteTestsByLangIdAndSubId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
                                    AppDatabase.getDatabaseInstance(getActivity()).getTestDao().insertAllTest(assessmentTests);
                                    if (progressDialog != null && progressDialog.isShowing() && isVisible()) {
                                        progressDialog.dismiss();
                                    }

                                    setTopicsToRecyclerView();
                               /* flowLayout.removeAllViews();
                                setTopicsToCheckBox(assessmentTests);*/
                                } else {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                              /*  ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                                ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                                getActivity().getSupportFragmentManager().popBackStack();
                                Toast.makeText(getActivity(), "No Exams..", Toast.LENGTH_SHORT).show();
    */
                                    //                            getOfflineTests();

                                    //                           btnOk.setEnabled(false);

                                    AppDatabase.getDatabaseInstance(getActivity()).getTestDao().deleteTestsByLangIdAndSubId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
                                    Toast.makeText(getActivity(), "No exams", Toast.LENGTH_SHORT).show();

                                }
                            } catch (
                                    JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            if (progressDialog != null && progressDialog.isShowing() && isVisible()) {
                                progressDialog.dismiss();
                            }
                            ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                            ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                            getActivity().getSupportFragmentManager().popBackStack();

                            Toast.makeText(getActivity(), "" + anError, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subjectId = FastSave.getInstance().getString("SELECTED_SUBJECT_ID", "1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }*/

    private void getExamData() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Exams");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AndroidNetworking.get(APIs.AssessmentExamAPI + subjectId + "&languageid=" + Assessment_Constants.SELECTED_LANGUAGE)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<AssessmentTestModal>>() {
                        }.getType();
                        assessmentTestModals = gson.fromJson(response, listType);
                        assessmentTests.clear();
                        for (int i = 0; i < assessmentTestModals.size(); i++) {
                            assessmentTests.addAll(assessmentTestModals.get(i).getLstsubjectexam());
                            for (int j = 0; j < assessmentTests.size(); j++) {
                                assessmentTests.get(j).setSubjectid(assessmentTestModals.get(i).getSubjectid());
                                assessmentTests.get(j).setSubjectname(assessmentTestModals.get(i).getSubjectname());
                                assessmentTests.get(j).setLanguageId(Assessment_Constants.SELECTED_LANGUAGE);
                            }
                        }
                        if (assessmentTests.size() > 0) {
                            AppDatabase.getDatabaseInstance(getActivity()).getTestDao().deleteTestsByLangIdAndSubId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
                            AppDatabase.getDatabaseInstance(getActivity()).getTestDao().insertAllTest(assessmentTests);
                            if (progressDialog != null && progressDialog.isShowing() && isVisible()) {
                                progressDialog.dismiss();
                            }

                            setTopicsToRecyclerView();
                           /* flowLayout.removeAllViews();
                            setTopicsToCheckBox(assessmentTests);*/
                        } else {
                            if (progressDialog != null && progressDialog.isShowing() && isVisible()) {
                                progressDialog.dismiss();
                            }
                          /*  ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                            ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                            getActivity().getSupportFragmentManager().popBackStack();
                            Toast.makeText(getActivity(), "No Exams..", Toast.LENGTH_SHORT).show();
*/
//                            getOfflineTests();

//                           btnOk.setEnabled(false);

                            AppDatabase.getDatabaseInstance(getActivity()).getTestDao().deleteTestsByLangIdAndSubId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
                            Toast.makeText(getActivity(), "No exams", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (progressDialog != null && progressDialog.isShowing() && isVisible()) {
                            progressDialog.dismiss();
                        }
                        ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                        ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager().popBackStack();

                        Toast.makeText(getActivity(), "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setTopicsToRecyclerView() {
        TopicAdapter topicAdapter = new TopicAdapter(getActivity(), assessmentTests);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_topics.setLayoutManager(linearLayoutManager);
        rv_topics.setAdapter(topicAdapter);
        topicAdapter.notifyDataSetChanged();
    }

  /*  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getExamData();
        } else {
            getOfflineTests();
        }

    }*/

    private void getOfflineTests() {
        assessmentTests = AppDatabase.getDatabaseInstance(getContext()).getTestDao().getTopicBySubIdAndLangId(subjectId, Assessment_Constants.SELECTED_LANGUAGE);
        if (assessmentTests.size() > 0)
            setTopicsToRecyclerView();
        else {
           /* if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                getExamData();
            } else*/
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
            ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
            getActivity().getSupportFragmentManager().popBackStack();
            Toast.makeText(getActivity(), "Connect to internet to download exams", Toast.LENGTH_SHORT).show();
        }
    }
}
