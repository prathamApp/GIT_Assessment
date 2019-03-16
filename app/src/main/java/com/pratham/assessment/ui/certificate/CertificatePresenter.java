package com.pratham.assessment.ui.certificate;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.domain.CertificateModelClass;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.ui.content_player.WebViewActivity;
import com.pratham.assessment.utilities.Assessment_Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.pratham.assessment.BaseActivity.appDatabase;


public class CertificatePresenter implements CertificateContract.CertificatePresenter {

    CertificateContract.CertificateView certificateView;
    Context context;
    public List<ContentTable> certiGameList;
    ArrayList<String> codesText;
    String testStudentId;
    JSONArray resultCodeList = null, quesCodeList = null;

    public CertificatePresenter(Context context, CertificateContract.CertificateView certificateView) {
        this.context = context;
        this.certificateView = certificateView;
        codesText = new ArrayList<>();
    }

    @Override
    public void getStudentName() {
        new AsyncTask<Object, Void, Object>() {
            String studName = "";

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Student student;
//                    if (Assessment_Constants.GROUP_LOGIN) {
//                        String sId = assessmentProfile.getStartDateTimea().split("_")[0];
//                        student = appDatabase.getStudentDao().getStudent(sId);
//                    }else
                        student = appDatabase.getStudentDao().getStudent(Assessment_Constants.currentStudentID);
                    studName = "" + student.getFullName();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                certificateView.setStudentName(studName);
            }
        }.execute();
    }

    public void proceed(final JSONArray certiData, final String nodeId) {
        try {
            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        certiGameList = appDatabase.getContentTableDao().getContentData(nodeId);
                        WebViewActivity.gameLevel = certiGameList.get(0).getNodeAge();
                        BackupDatabase.backup(context);
                        codesText = new ArrayList<>();
                        codesText.clear();

                        try {
                            for (int j = 0; j < certiGameList.size(); j++) {

                                codesText.add(certiGameList.get(j).getNodeDesc());

                                CertificateModelClass contentTable = new CertificateModelClass();

                                contentTable.setCodeCount(Collections.frequency(codesText, certiGameList.get(j).getNodeDesc()));

                                contentTable.setNodeId("" + certiGameList.get(j).getNodeId());
                                contentTable.setCertiCode("" + certiGameList.get(j).getNodeDesc());
                                contentTable.setNodeAge("" + certiGameList.get(j).getNodeAge());
                                contentTable.setResourceId("" + certiGameList.get(j).getResourceId());
                                contentTable.setResourcePath("" + certiGameList.get(j).getResourcePath());
                                contentTable.setAsessmentGiven(false);
                                contentTable.setStudentId(Assessment_Constants.currentStudentID);
                                contentTable.setScoredMarks(0);
                                contentTable.setTotalMarks(0);
                                contentTable.setCertificateRating(0.0f);
                                contentTable.setStudentPercentage("");

                                for (int i = 0; i < certiData.length(); i++) {
                                    String lang = certiData.getJSONObject(i).getString("lang");
                                    String questionList = certiData.getJSONObject(i).getJSONObject("questionList").getString("" + certiGameList.get(j).getNodeDesc());
                                    String answerList = certiData.getJSONObject(i).getJSONObject("answerList").getString("" + certiGameList.get(j).getNodeDesc());
                                    if (lang.equalsIgnoreCase("english")) {
                                        contentTable.setEnglishQues("" + questionList);
                                        contentTable.setEnglishAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("hindi")) {
                                        contentTable.setHindiQues("" + questionList);
                                        contentTable.setHindiAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("marathi")) {
                                        contentTable.setMarathiQues("" + questionList);
                                        contentTable.setMarathiAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Gujarati")) {
                                        contentTable.setGujaratiQues("" + questionList);
                                        contentTable.setGujaratiAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Kannada")) {
                                        contentTable.setKannadaQues("" + questionList);
                                        contentTable.setKannadaAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Bengali")) {
                                        contentTable.setBengaliQues("" + questionList);
                                        contentTable.setBengaliAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Assamese")) {
                                        contentTable.setAssameseQues("" + questionList);
                                        contentTable.setAssameseAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Telugu")) {
                                        contentTable.setTeluguQues("" + questionList);
                                        contentTable.setTeluguAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Tamil")) {
                                        contentTable.setTamilQues("" + questionList);
                                        contentTable.setTamilAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Odia")) {
                                        contentTable.setOdiaQues("" + questionList);
                                        contentTable.setOdiaAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Urdu")) {
                                        contentTable.setUrduQues("" + questionList);
                                        contentTable.setUrduAnsw("" + answerList);
                                    } else if (lang.equalsIgnoreCase("Punjabi")) {
                                        contentTable.setPunjabiQues("" + questionList);
                                        contentTable.setPunjabiAnsw("" + answerList);
                                    }

                                }
                                certificateView.addContentToViewList(contentTable);
                            }

                            certificateView.doubleQuestionCheck();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    certificateView.initializeTheIndex();
                    certificateView.notifyAdapter();
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fillAdapter(Assessment assessmentProfile, JSONArray certiData) {
        try {

            JSONObject jsonObject;
/*            if (Assessment_Constants.GROUP_LOGIN) {
                testStudentId = assessmentProfile.getResourceIDa().split("$")[0];
                jsonObject = new JSONObject(assessmentProfile.getResourceIDa().split("$")[1]);
            }else*/
            jsonObject = new JSONObject(assessmentProfile.getResourceIDa());

            Iterator<String> iter = jsonObject.keys();
            codesText = new ArrayList<>();
            codesText.clear();

            while (iter.hasNext()) {

                String key = iter.next();

                try {

                    codesText.add(key.split("_")[1]);

                    CertificateModelClass contentTable = new CertificateModelClass();

                    contentTable.setCodeCount(Collections.frequency(codesText, key.split("_")[1]));

                    contentTable.setNodeId("" + assessmentProfile.getResourceIDa());
                    contentTable.setCertiCode("" + key.split("_")[1]);
                    contentTable.setNodeAge("");
                    contentTable.setResourceId("");
                    contentTable.setResourcePath("");
                    contentTable.setAsessmentGiven(true);
                    contentTable.setStudentId(Assessment_Constants.currentStudentID);
                    contentTable.setScoredMarks(0);
                    contentTable.setTotalMarks(0);
                    contentTable.setCertificateRating(getStarRating(Float.parseFloat(jsonObject.getString(key))));
                    contentTable.setStudentPercentage("" + jsonObject.getString(key));

                    for (int i = 0; i < certiData.length(); i++) {
                        String lang = certiData.getJSONObject(i).getString("lang");
                        String questionList = certiData.getJSONObject(i).getJSONObject("questionList").getString("" + key.split("_")[1]);
                        String answerList = certiData.getJSONObject(i).getJSONObject("answerList").getString("" + key.split("_")[1]);

                        if (lang.equalsIgnoreCase("english")) {
                            contentTable.setEnglishQues("" + questionList);
                            contentTable.setEnglishAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("hindi")) {
                            contentTable.setHindiQues("" + questionList);
                            contentTable.setHindiAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("marathi")) {
                            contentTable.setMarathiQues("" + questionList);
                            contentTable.setMarathiAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Gujarati")) {
                            contentTable.setGujaratiQues("" + questionList);
                            contentTable.setGujaratiAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Kannada")) {
                            contentTable.setKannadaQues("" + questionList);
                            contentTable.setKannadaAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Bengali")) {
                            contentTable.setBengaliQues("" + questionList);
                            contentTable.setBengaliAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Assamese")) {
                            contentTable.setAssameseQues("" + questionList);
                            contentTable.setAssameseAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Telugu")) {
                            contentTable.setTeluguQues("" + questionList);
                            contentTable.setTeluguAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Tamil")) {
                            contentTable.setTamilQues("" + questionList);
                            contentTable.setTamilAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Odia")) {
                            contentTable.setOdiaQues("" + questionList);
                            contentTable.setOdiaAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Urdu")) {
                            contentTable.setUrduQues("" + questionList);
                            contentTable.setUrduAnsw("" + answerList);
                        } else if (lang.equalsIgnoreCase("Punjabi")) {
                            contentTable.setPunjabiQues("" + questionList);
                            contentTable.setPunjabiAnsw("" + answerList);
                        }
                    }

                    certificateView.addContentToViewList(contentTable);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            certificateView.doubleQuestionCheck();

        } catch (Exception e) {
            e.printStackTrace();
        }
        certificateView.initializeTheIndex();
        certificateView.notifyAdapter();
    }

    @Override
    public float getStarRating(float perc) {
        float ratings = 5;

        if (perc < 21)
            ratings = (float) 1;
        else if (perc >= 21 && perc < 41)
            ratings = (float) 2;
        else if (perc >= 41 && perc < 61)
            ratings = (float) 3;
        else if (perc >= 61 && perc < 81)
            ratings = (float) 4;
        else if (perc >= 81)
            ratings = (float) 5;

        return ratings;
    }

    @Override
    public void recordTestData(final JSONObject jsonObjectAssessment, final String certiTitle) {
        new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Assessment assessment = new Assessment();
                    assessment.setResourceIDa(jsonObjectAssessment.toString());
                    /*gameWebViewList.get(WebViewActivity.gameCounter).getResourceId()*/
//                            WebViewActivity.webResId);
                    assessment.setSessionIDa(Assessment_Constants.assessmentSession);
                    assessment.setSessionIDm(Assessment_Constants.currentSession);
                    assessment.setQuestionIda(0);
                    assessment.setScoredMarksa(0);
                    assessment.setTotalMarksa(0);
                    assessment.setStudentIDa(Assessment_Constants.currentStudentID);
/*                    if (Assessment_Constants.GROUP_LOGIN)
                        assessment.setStartDateTimea(Assessment_Constants.currentAssessmentStudentID + "_" + certiTitle);
                    else*/
                        assessment.setStartDateTimea("" + certiTitle);
                    assessment.setEndDateTime(AssessmentApplication.getCurrentDateTime());
//                    if (Assessment_Constants.supervisedAssessment)
                        assessment.setDeviceIDa("" + Assessment_Constants.currentSupervisorID);
/*                    else
                        assessment.setDeviceIDa("na");*/
                    assessment.setLevela(Integer.parseInt(WebViewActivity.gameLevel));
                    assessment.setLabel("" + Assessment_Constants.CERTIFICATE_LBL);
                    assessment.setSentFlag(0);
                    appDatabase.getAssessmentDao().insert(assessment);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

    @Override
    public JSONArray fetchAssessmentList() {
        JSONArray returnCodeList = null;
        try {
            InputStream is = context.getAssets().open("CertificateData.json");
//            InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/Game/CertificateData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            JSONObject jsonObj = new JSONObject(jsonStr);
            returnCodeList = jsonObj.getJSONArray("CodeList");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return returnCodeList;
    }

/*    @Override
    public void getSupervisorData(String certiMode) {
        new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    SupervisorData supervisorData;
                    if (!certiMode.equalsIgnoreCase("display")) {
                        supervisorData = appDatabase.getSupervisorDataDao().getSupervisorById(Assessment_Constants.currentsupervisorID);
                    } else
                        supervisorData = appDatabase.getSupervisorDataDao().getSupervisorById("" + assessmentProfile.getDeviceIDa());

                    certificateView.setSupervisorData("" + supervisorData.getSupervisorName(), "" +
                            Environment.getExternalStorageDirectory().toString() + "/.EngGameInternal/supervisorImages/" +
                            supervisorData.getSupervisorPhoto());

                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }.execute();
    }*/

    public void fetchAssessments() {
        try {
//            InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/Game/CertificateData.json");
            InputStream is = context.getAssets().open("CertificateData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            JSONObject jsonObj = new JSONObject(jsonStr);
            quesCodeList = jsonObj.getJSONArray("quesCodeList");
            resultCodeList = jsonObj.getJSONArray("resultCodeList");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //createLists(quesCodeList, resultCodeList);
    }

}