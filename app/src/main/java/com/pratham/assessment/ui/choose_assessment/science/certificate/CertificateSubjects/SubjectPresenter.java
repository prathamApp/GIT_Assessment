package com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.R;
import com.pratham.assessment.constants.APIs;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.AssessmentPaperPattern;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.ui.choose_assessment.science.certificate.CertificateSubjects.ExpandableRecyclerView.AssessmentSubjectsExpandable;
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
//        List<String> attemptedSubjectIds = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPapersByUniqueSubId(langId, currentStudentID);
        List<String> attemptedSubjectIds = AppDatabase.getDatabaseInstance(context).getCertificateKeywordRatingDao().getDistinctSubjectsByStudentIdLangId(currentStudentID, langId);
        for (int j = 0; j < AllSubjects.size(); j++) {
            for (int i = 0; i < attemptedSubjectIds.size(); i++) {
                if (attemptedSubjectIds.get(i).equalsIgnoreCase(AllSubjects.get(j).getSubjectid()))
                    subjects.add(AllSubjects.get(j));
            }
        }


        for (AssessmentSubjects assessmentSubjects : subjects) {
            List<AssessmentPaperPattern> paperPatterns = AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().getAllAssessmentPaperPatternsBySubjectId(assessmentSubjects.getSubjectid());
//            List<AssessmentPaperPattern> paperWithCertQuestions = checkQuestions(paperPatterns);
            List<String> examIds = new ArrayList<>();
            for (int i = 0; i < paperPatterns.size(); i++) {
                if (!examIds.contains(paperPatterns.get(i).getExamid())) {
//                    if (paperPatterns.get(i).getExammode() == null || paperPatterns.get(i).getExammode().equalsIgnoreCase(Assessment_Constants.PRACTICE))
                        examIds.add(paperPatterns.get(i).getExamid());
                }
            }


//            List<AssessmentPaperForPush> assessmentPaperForPush = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAssessmentPaperBySubIdAndLangId(assessmentSubjects.getSubjectid(), Assessment_Constants.currentStudentID, langId);
            List<AssessmentPaperForPush> assessmentPaperForPush = new ArrayList<>();
            if (!examIds.isEmpty()) {
                for (int i = 0; i < examIds.size(); i++) {
                    assessmentPaperForPush.addAll(AppDatabase.getDatabaseInstance(context)
                            .getAssessmentPaperForPushDao()
                            .getAssessmentPaperBySubIdAndLangIdExamId(assessmentSubjects.getSubjectid(), currentStudentID, langId, examIds.get(i)));
                }

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
        }
        subjectView.setSubjects(assessmentSubjectsExpandables);

    }

    private List<AssessmentPaperPattern> checkQuestions
            (List<AssessmentPaperPattern> paperPatterns) {
        List<AssessmentPaperPattern> newList = new ArrayList<>();
        for (int i = 0; i < paperPatterns.size(); i++) {
            if (!paperPatterns.get(i).getCertificateQuestion1().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion2().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion3().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion4().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion5().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion6().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion7().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion8().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion9().equalsIgnoreCase("") &&
                    !paperPatterns.get(i).getCertificateQuestion10().equalsIgnoreCase("")) {
                newList.add(paperPatterns.get(i));
            }
        }

        return newList;
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
        progressDialog.setMessage(context.getString(R.string.loading_certificates));
        AndroidNetworking.get(url)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            paperList = new ArrayList<>();
                           /* Gson gson = new Gson();
                            Type listType = new TypeToken<List<AssessmentPaperForPush>>() {
                            }.getType();
                            paperList = gson.fromJson(response.toString(), listType);
                          */
                            for (int i = 0; i < response.length(); i++) {
                                AssessmentPaperForPush paper = new AssessmentPaperForPush();
                                paper.setLanguageId(response.getJSONObject(i).getString("languageid"));
                                paper.setSubjectId(response.getJSONObject(i).getString("subjectid"));
                                paper.setExamId(response.getJSONObject(i).getString("examid"));
                                paper.setPaperId(response.getJSONObject(i).getString("paperId"));
                                paper.setPaperStartTime(response.getJSONObject(i).getString("paperstarttime"));
                                paper.setPaperEndTime(response.getJSONObject(i).getString("paperendtime"));
                                paper.setOutOfMarks(response.getJSONObject(i).getString("TotalMarks"));
                                paper.setTotalMarks(response.getJSONObject(i).getString("ScoredMarks"));
                                paper.setStudentId(response.getJSONObject(i).getString("studentid"));
                                paper.setCorrectCnt(response.getJSONObject(i).getInt("correctCount"));
                                paper.setWrongCnt(response.getJSONObject(i).getInt("wrongCount"));
                                paper.setExamName(response.getJSONObject(i).getString("examname"));
                                paper.setExamTime(response.getJSONObject(i).getString("examduration"));
                                paper.setQuestion1Rating(response.getJSONObject(i).getString("question1Rating"));
                                paper.setQuestion2Rating(response.getJSONObject(i).getString("question2Rating"));
                                paper.setQuestion3Rating(response.getJSONObject(i).getString("question3Rating"));
                                paper.setQuestion4Rating(response.getJSONObject(i).getString("question4Rating"));
                                paper.setQuestion5Rating(response.getJSONObject(i).getString("question5Rating"));
                                paper.setQuestion6Rating(response.getJSONObject(i).getString("question6Rating"));
                                paper.setQuestion7Rating(response.getJSONObject(i).getString("question7Rating"));
                                paper.setQuestion8Rating(response.getJSONObject(i).getString("question8Rating"));
                                paper.setQuestion9Rating(response.getJSONObject(i).getString("question9Rating"));
                                paper.setQuestion10Rating(response.getJSONObject(i).getString("question10Rating"));
//                                paper.setIsniosstudent(response.getJSONObject(i).getString("isniosstudent"));
//                                paper.setPaperEndTime(response.getJSONObject(i).getString("exammode"));
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
                                Toast.makeText(context, R.string.nothing_to_pull, Toast.LENGTH_SHORT).show();
                                subjectView.setSubjectToSpinner();
                                progressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, R.string.error_in_loading_check_internet_connection, Toast.LENGTH_SHORT).show();
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
            String id = langIds.get(subjectCnt);
            if (id != null && !id.equalsIgnoreCase(""))
                getSubjectData(langIds.get(subjectCnt));
            else {
                Toast.makeText(context, R.string.nothing_to_pull, Toast.LENGTH_SHORT).show();
                BackupDatabase.backup(context);
                progressDialog.dismiss();

            }
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
                        paper.setIsniosstudent(student.getIsniosstudent());
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
        progressDialog.setMessage(context.getString(R.string.loading_subjects));
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
                                Toast.makeText(context, R.string.no_subjects, Toast.LENGTH_SHORT).show();
                            subjectCnt++;
                            addSubjectsToDB();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, R.string.error_in_loading_check_internet_connection, Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().deletePaperPatterns();
                        progressDialog.dismiss();
                    }
                });

    }


    private void getLanguageData() {
        assessmentLanguagesList = new ArrayList<>();
        progressDialog.setMessage(context.getString(R.string.loading));
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
                        Toast.makeText(context, R.string.error_in_loading_check_internet_connection, Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperPatternDao().deletePaperPatterns()

                        progressDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }
}
