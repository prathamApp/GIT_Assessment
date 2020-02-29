package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.AssessmentSubjectsExpandable;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
@EBean
public class SubjectPresenter implements SubjectContract.SubjectPresenter {
    Context context;
    SubjectContract.SubjectView subjectView;
    List<AssessmentPaperForPush> paperList;
    ProgressDialog progressDialog;
    int subjectCnt = 0;
    List<String> langIds;
    List<AssessmentLanguages> assessmentLanguagesList;


    public SubjectPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getSubjectsFromDB(String selectedLang) {
        String langId = AppDatabase.getDatabaseInstance(context).getLanguageDao().getLangIdByName(selectedLang.toUpperCase());
        List<AssessmentSubjects> subjects = new ArrayList<>();
        List<AssessmentSubjects> AllSubjects;
        List<AssessmentSubjectsExpandable> assessmentSubjectsExpandables = new ArrayList<>();

//        subjects = AppDatabase.getDatabaseInstance(context).getSubjectDao().getAllSubjects();
        AllSubjects = AppDatabase.getDatabaseInstance(context).getSubjectDao().getAllSubjectsByLangId(langId);
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
//        List<String> attemptedSubjectIds = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPapersByUniqueSubId(langId, Assessment_Constants.currentStudentID);
        List<String> attemptedSubjectIds = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPapersByUniqueSubId(langId, currentStudentID);
        for (int j = 0; j < AllSubjects.size(); j++) {
            for (int i = 0; i < attemptedSubjectIds.size(); i++) {
                if (attemptedSubjectIds.get(i).equalsIgnoreCase(AllSubjects.get(j).getSubjectid()))
                    subjects.add(AllSubjects.get(j));
            }
        }

        for (AssessmentSubjects assessmentSubjects : subjects) {
//            List<AssessmentPaperForPush> assessmentPaperForPush = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPaperBySubIdAndLangId(assessmentSubjects.getSubjectid(), Assessment_Constants.currentStudentID, langId);
            List<AssessmentPaperForPush> assessmentPaperForPush = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPaperBySubIdAndLangId(assessmentSubjects.getSubjectid(), currentStudentID, langId);
            Collections.sort(assessmentPaperForPush, new Comparator<AssessmentPaperForPush>() {
                @Override
                public int compare(AssessmentPaperForPush o1, AssessmentPaperForPush o2) {
                    Date date1 = Assessment_Utility.stringToDate(o1.getPaperEndTime());
                    Date date2 = Assessment_Utility.stringToDate(o2.getPaperEndTime());
                    if (date1 != null && date2 != null)
                        return date1.compareTo(date2);
                    else return 0;
                }
            });
            if (assessmentPaperForPush.size() > 0) {
                Collections.reverse(assessmentPaperForPush);
                assessmentSubjectsExpandables.add(new AssessmentSubjectsExpandable(assessmentSubjects.getSubjectid(), assessmentSubjects.getSubjectname(), assessmentPaperForPush));
            } else {
                assessmentSubjectsExpandables.add(new AssessmentSubjectsExpandable("", "", assessmentPaperForPush));

            }
        }
        subjectView.setSubjects(assessmentSubjectsExpandables);
    }

    @Override
    public void pullCertificates() {
//        String url = APIs.pullCertificateByStudIdAPI + Assessment_Constants.currentStudentID;
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");

        String url = APIs.pullCertificateByStudIdAPI + currentStudentID;
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading Certificates..");
        AndroidNetworking.get(url)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            paperList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                AssessmentPaperForPush paper = new AssessmentPaperForPush();
                                paper.setPaperStartTime(response.getJSONObject(i).getString("paperstarttime"));
                                paper.setPaperEndTime(response.getJSONObject(i).getString("paperendtime"));
                                paper.setLanguageId(response.getJSONObject(i).getString("languageid"));
                                paper.setExamId(response.getJSONObject(i).getString("examid"));
                                paper.setSubjectId(response.getJSONObject(i).getString("subjectid"));
                                paper.setPaperId(response.getJSONObject(i).getString("paperId"));
                                paper.setOutOfMarks(response.getJSONObject(i).getString("TotalMarks"));
                                paper.setTotalMarks(response.getJSONObject(i).getString("ScoredMarks"));
                                paper.setCorrectCnt(response.getJSONObject(i).getInt("correctCount"));
                                paper.setWrongCnt(response.getJSONObject(i).getInt("wrongCount"));
                                paper.setStudentId(response.getJSONObject(i).getString("studentid"));
                                paper.setExamName(response.getJSONObject(i).getString("examname"));
                                paper.setExamTime(response.getJSONObject(i).getString("examduration"));
                                paper.setQuestion1Rating(response.getJSONObject(i).getString("question1Rating"));
                                paper.setQuestion2Rating(response.getJSONObject(i).getString("question2Rating"));
                                paper.setQuestion3Rating(response.getJSONObject(i).getString("question3Rating"));
                                paperList.add(paper);

                            }
                            if (paperList.size() > 0) {
                                List<AssessmentLanguages> languages = AppDatabase.getDatabaseInstance(context).getLanguageDao().getAllLangs();
                                if (languages.size() <= 0) getLanguageData();
                                else savePaperToDB();

//                                AppDatabase.getDatabaseInstance(context).getSubjectDao().deleteSubjectsByLangId(Assessment_Constants.SELECTED_LANGUAGE);
//                                AppDatabase.getDatabaseInstance(context).getSubjectDao().insertAllSubjects(contentTableList);
//                                BackupDatabase.backup(context);
//                            contentTableList.addAll(downloadedContentTableList);
//                                assessView.addContentToViewList(paperList);
//                                assessView.notifyAdapter();
                                //getTopicData();
                            } else {
                                Toast.makeText(context, "Nothing to pull..", Toast.LENGTH_SHORT).show();
                                subjectView.setSubjectToSpinner();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void setView(SubjectContract.SubjectView subjectView) {
        this.subjectView = subjectView;

    }

    private void savePaperToDB() {
        langIds = new ArrayList<>();
        for (int i = 0; i < paperList.size(); i++) {
            AssessmentPaperForPush paper = paperList.get(i);
//            AssessmentSubjects subjects = new AssessmentSubjects();
//            subjects.setLanguageid(paper.getLanguageId());
//            subjects.setSubjectid(paper.getSubjectId());
//            subjects.setSubjectname();
            if (!langIds.contains(paper.getLanguageId()))
                langIds.add(paper.getLanguageId());


        }

        addSubjectsToDB();

    }

    private void addSubjectsToDB() {
        if (subjectCnt < langIds.size()) {
            getSubjectData(langIds.get(subjectCnt));
        } else {
            for (int i = 0; i < paperList.size(); i++) {
                AssessmentPaperForPush paper = paperList.get(i);
                AssessmentPaperForPush existingPaper = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPapersByPaperId(paperList.get(i).getPaperId());
                if (existingPaper != null) {
                    AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().
                            updatePaper(paper.getLanguageId(), paper.getSubjectId(), paper.getExamId(),
                                    paper.getExamName(), paper.getPaperId(), paper.getTotalMarks(),
                                    paper.getOutOfMarks(), paper.getPaperStartTime(),
                                    paper.getPaperEndTime(), paper.getExamTime());
                } else {
                    paper.setSentFlag(1);
                    Student student = AppDatabase.getDatabaseInstance(context).getStudentDao().getStudent(paper.getStudentId());
                    if (student != null) {
                        paper.setFullName(student.getFullName());
                        paper.setGender(student.getGender());
                        paper.setAge(student.getAge());
                    }
                    AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().insertPaperForPush(paper);

                }
            }

            BackupDatabase.backup(context);
            progressDialog.dismiss();
            subjectView.setSubjectToSpinner();
        }

    }

    private void getSubjectData(final String languageId) {
        final List<AssessmentSubjects> subjects = new ArrayList<>();
//        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading subjects");
        AndroidNetworking.get(APIs.AssessmentSubjectAPI + languageId)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                AssessmentSubjects assessmentSubjects = new AssessmentSubjects();
                               /* List<AssessmentPaperForPush> paper1 = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPaperBySubId(assessmentSubjects.getSubjectid());
                                if (paper1.size() > 0) {*/
                                assessmentSubjects.setSubjectid(response.getJSONObject(i).getString("subjectid"));
                                assessmentSubjects.setSubjectname(response.getJSONObject(i).getString("subjectname"));
                                assessmentSubjects.setLanguageid(languageId);
                                subjects.add(assessmentSubjects);
//                                }
                            }
                            if (subjects.size() > 0) {
                                AppDatabase.getDatabaseInstance(context).getSubjectDao().deleteSubjectsByLangId(languageId);
                                AppDatabase.getDatabaseInstance(context).getSubjectDao().insertAllSubjects(subjects);
                                progressDialog.dismiss();
                                BackupDatabase.backup(context);
                            } else
                                Toast.makeText(context, "No subjects..", Toast.LENGTH_SHORT).show();
                            subjectCnt++;
                            addSubjectsToDB();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                    }
                });

    }


    private void getLanguageData() {
        assessmentLanguagesList = new ArrayList<>();
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
                            AppDatabase.getDatabaseInstance(context).getLanguageDao().insertAllLanguages(assessmentLanguagesList);
//                            setLanguageRecyclerView();
                            savePaperToDB();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading..Check internet connection.", Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperPatternDao().deletePaperPatterns()

                        progressDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }
}
