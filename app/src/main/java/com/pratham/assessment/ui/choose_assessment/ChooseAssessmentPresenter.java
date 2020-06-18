package com.pratham.assessment.ui.choose_assessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.AssessmentTest;
import com.pratham.assessment.domain.AssessmentTestModal;
import com.pratham.assessment.domain.NIOSExam;
import com.pratham.assessment.domain.NIOSExamTopics;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;

import org.androidannotations.annotations.EBean;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.BaseActivity.appDatabase;

@EBean
public class ChooseAssessmentPresenter implements ChooseAssessmentContract.ChooseAssessmentPresenter {

    Context context;
    ChooseAssessmentContract.ChooseAssessmentView assessView;
    ArrayList<AssessmentSubjects> contentTableList = new ArrayList<>();
    ArrayList<NIOSExam> NIOSSubjectList = new ArrayList<>();
    ArrayList<String> nodeIds;
    ArrayList<AssessmentSubjects> downloadedContentTableList;
    /*List<AssessmentTestModal> assessmentTestModals;
    List<AssessmentTest> assessmentTests = new ArrayList<>();*/

    public ChooseAssessmentPresenter(Context context) {
        this.context = context;
        this.assessView = (ChooseAssessmentContract.ChooseAssessmentView) context;
        nodeIds = new ArrayList<>();
    }


    @Override
    public void copyListData() {
        getListData();

/*        new AsyncTask<Object, Void, Object>() {
            String currentSession;

            @Override
            protected Object doInBackground(Object[] objects) {
                *//*try {

//                    if(!COS_Constants.SD_CARD_Content) {

                        AssetManager assetManager = context.getAssets();
                        try {
                            InputStream in = assetManager.open("cos_database");
                            OutputStream out = new FileOutputStream(AssessmentApplication.pradigiPath + "/.LLA/cos_database");
                            byte[] buffer = new byte[1024];
                            int read = in.read(buffer);
                            while (read != -1) {
                                out.write(buffer, 0, read);
                                read = in.read(buffer);
                            }
                            File folder_file, db_file;
                            if (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(COS_Constants.KEY_MENU_COPIED, false)) {
                                try {
                                    if (!COS_Constants.SMART_PHONE)
                                        folder_file = new File(COS_Constants.ext_path);
                                    else
                                        folder_file = new File(AssessmentApplication.pradigiPath + "/.LLA/English/");
                                    if (folder_file.exists()) {
                                        Log.d("-CT-", "doInBackground COS_Constants.ext_path: " + COS_Constants.ext_path);
                                        db_file = new File(folder_file.getAbsolutePath() + "/" + AppDatabase.DB_NAME);
                                        if (db_file.exists()) {
                                            SQLiteDatabase db = SQLiteDatabase.openDatabase(db_file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
                                            if (db != null) {
                                                Cursor content_cursor;
                                                try {
                                                    content_cursor = db.rawQuery("SELECT * FROM ContentTable", null);
                                                    //populate contents
                                                    List<ContentTable> contents = new ArrayList<>();
                                                    if (content_cursor.moveToFirst()) {
                                                        while (!content_cursor.isAfterLast()) {
                                                            ContentTable detail = new ContentTable();
                                                            detail.setNodeId(content_cursor.getString(content_cursor.getColumnIndex("subId")));
                                                            detail.setNodeType(content_cursor.getString(content_cursor.getColumnIndex("nodeType")));
                                                            detail.setNodeTitle(content_cursor.getString(content_cursor.getColumnIndex("nodeTitle")));
                                                            detail.setNodeKeywords(content_cursor.getString(content_cursor.getColumnIndex("nodeKeywords")));
                                                            detail.setNodeAge(content_cursor.getString(content_cursor.getColumnIndex("nodeAge")));
                                                            detail.setNodeDesc(content_cursor.getString(content_cursor.getColumnIndex("nodeDesc")));
                                                            detail.setNodeServerImage(content_cursor.getString(content_cursor.getColumnIndex("nodeServerImage")));
                                                            detail.setNodeImage(content_cursor.getString(content_cursor.getColumnIndex("nodeImage")));
                                                            detail.setResourceId(content_cursor.getString(content_cursor.getColumnIndex("resourceId")));
                                                            detail.setResourceType(content_cursor.getString(content_cursor.getColumnIndex("resourceType")));
                                                            detail.setResourcePath(content_cursor.getString(content_cursor.getColumnIndex("resourcePath")));
                                                            detail.setLevel("" + content_cursor.getInt(content_cursor.getColumnIndex("level")));
                                                            detail.setContentLanguage(content_cursor.getString(content_cursor.getColumnIndex("contentLanguage")));
                                                            detail.setParentId(content_cursor.getString(content_cursor.getColumnIndex("parentId")));
                                                            detail.setContentType(content_cursor.getString(content_cursor.getColumnIndex("contentType")));
                                                            if (!COS_Constants.SMART_PHONE)
                                                                detail.setOnSDCard(true);
                                                            else
                                                                detail.setOnSDCard(false);
                                                            contents.add(detail);
                                                            content_cursor.moveToNext();
                                                        }
                                                    }
                                                    appDatabase.getContentTableDao().addContentList(contents);
                                                    content_cursor.close();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }
 //                   }
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }*//*
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                getListData();
            }
        }.execute();*/
    }

  /*  @Override
    public void startAssessSession() {

        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Session startSesion = new Session();
                    String assessmentSession = FastSave.getInstance().getString("assessmentSession", "");

//                    startSesion.setSessionID("" + Assessment_Constants.assessmentSession);
                    startSesion.setSessionID("" + assessmentSession);
                    String timerTime = AssessmentApplication.getCurrentDateTime(false, AssessmentApplication.getCurrentDateTime());
                    startSesion.setFromDate(timerTime);
                    startSesion.setToDate("NA");
                    startSesion.setSentFlag(0);
                    AppDatabase.getDatabaseInstance(context).getSessionDao().insert(startSesion);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }*/

    private void getListData() {
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getLanguageData();
            if (FastSave.getInstance().getBoolean("enrollmentNoLogin", false))
                getNIOSSubjects();
            else
                getSubjectData();
        } else {
            /*if (FastSave.getInstance().getBoolean("enrollmentNoLogin", false))
                downloadedContentTableList=
              else*/
            if (FastSave.getInstance().getBoolean("enrollmentNoLogin", false))
                getOfflineNIOSSubjects();
            else
                downloadedContentTableList = (ArrayList<AssessmentSubjects>) AppDatabase.getDatabaseInstance(context).getSubjectDao().getAllSubjectsByLangId(Assessment_Constants.SELECTED_LANGUAGE);
            assessView.clearContentList();
     /*   }
        if (downloadedContentTableList.size() <= 0) {
        }else {*/
            BackupDatabase.backup(context);
            contentTableList.clear();
            contentTableList.addAll(downloadedContentTableList);
            assessView.addContentToViewList(contentTableList);
            assessView.notifyAdapter();
        }

       /* new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    try {
                        *//*for (int j = 0; j < downloadedContentTableList.size(); j++) {
                            ContentTable contentTable = new ContentTable();
                            contentTable.setNodeId("" + downloadedContentTableList.get(j).getNodeId());
                            contentTable.setNodeType("" + downloadedContentTableList.get(j).getNodeType());
                            contentTable.setNodeTitle("" + downloadedContentTableList.get(j).getNodeTitle());
                            contentTable.setNodeKeywords("" + downloadedContentTableList.get(j).getNodeKeywords());
                            contentTable.setNodeAge("" + downloadedContentTableList.get(j).getNodeAge());
                            contentTable.setNodeDesc("" + downloadedContentTableList.get(j).getNodeDesc());
                            contentTable.setNodeServerImage("" + downloadedContentTableList.get(j).getNodeServerImage());
                            contentTable.setNodeImage("" + downloadedContentTableList.get(j).getNodeImage());
                            contentTable.setResourceId("" + downloadedContentTableList.get(j).getResourceId());
                            contentTable.setResourceType("" + downloadedContentTableList.get(j).getNodeType());
                            contentTable.setResourcePath("" + downloadedContentTableList.get(j).getResourcePath());
                            contentTable.setParentId("" + downloadedContentTableList.get(j).getParentId());
                            contentTable.setLevel("" + downloadedContentTableList.get(j).getLevel());
                            contentTable.setContentType(downloadedContentTableList.get(j).getContentType());
                            contentTable.setIsDownloaded("true");
                            contentTable.setOnSDCard(true);

                        }*//*


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

            }
        }.execute();*/
    }

    private void getOfflineNIOSSubjects() {
        String currentStudentID = FastSave.getInstance().getString("currentStudentID", "");
        downloadedContentTableList = new ArrayList<>();
        List<NIOSExam> AllExams = AppDatabase.getDatabaseInstance(context).getNiosExamDao().getAllSubjectsByStudId(currentStudentID);
        if (AllExams != null && AllExams.size() > 0)
            for (int i = 0; i < AllExams.size(); i++) {
                List<NIOSExamTopics> NIOSTopics = AppDatabase.getDatabaseInstance(context).getNiosExamTopicDao().getTopicIdByExamId(AllExams.get(i).getExamid());
                if (NIOSTopics != null && NIOSTopics.size() > 0)
                    for (int j = 0; j < NIOSTopics.size(); j++) {
                        AssessmentSubjects subjects = new AssessmentSubjects();
                        subjects.setLanguageid(NIOSTopics.get(j).getLanguageid());
                        subjects.setSubjectid(NIOSTopics.get(j).getSubjectid());
                        subjects.setSubjectname(NIOSTopics.get(j).getSubjectname());
                        if (!containsId(downloadedContentTableList, subjects.getSubjectid()))
                            downloadedContentTableList.add(subjects);
                    }
            }
    }

    private void getNIOSSubjects() {
        NIOSSubjectList.clear();
        contentTableList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading subjects");
        AndroidNetworking.get(APIs.AssessmentEnrollmentNoExamAPI + FastSave.getInstance().getString("currentStudentID", ""))
                .build()
                .getAsString(new StringRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<NIOSExam>>() {
                        }.getType();
                        NIOSSubjectList = gson.fromJson(response, listType);
//                            assessmentTests.clear();
//                        contentTableList = new ArrayList<>();
                        JSONArray jsonArray = null;
                        try {

                           /* jsonArray = new JSONArray(response);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                Log.d("onResponse", "onResponse: " + jsonArray.length());
                                JSONObject outer = jsonArray.getJSONObject(j);
//                                    for (int i = 0; i < outer.length(); i++) {
                                Log.d("onResponse", "onResponse outer: " + outer.length());
                                AssessmentTestModal testModal = new AssessmentTestModal();
                                AssessmentSubjects subjects = new AssessmentSubjects();
                                JSONArray lststudentexamtopic = outer.getJSONArray("lststudentexamtopic");
                                JSONObject jsonObject = new JSONObject(lststudentexamtopic.getJSONObject(0).toString());
                                subjects.setSubjectid(jsonObject.getString("subjectid"));
                                subjects.setSubjectname(jsonObject.getString("subjectname"));
                                subjects.setLanguageid(jsonObject.getString("languageid"));

                                if (!containsName(contentTableList, subjects.getSubjectid()))
                                    contentTableList.add(subjects);
//                                    }
                            }*/


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
                            if (NIOSSubjectList.size() > 0) {
                                AppDatabase.getDatabaseInstance(context).getNiosExamDao().deleteByStudId(Assessment_Constants.SELECTED_LANGUAGE);
                                AppDatabase.getDatabaseInstance(context).getNiosExamDao().insertAllExams(NIOSSubjectList);
                                List<NIOSExamTopics> NIOSTopics;
                                for (int i = 0; i < NIOSSubjectList.size(); i++) {
                                    NIOSTopics = NIOSSubjectList.get(i).getLststudentexamtopic();
                                    if (NIOSTopics.size() > 0) {
                                        for (int j = 0; j < NIOSTopics.size(); j++) {
                                            AssessmentSubjects subjects = new AssessmentSubjects();
                                            subjects.setLanguageid(NIOSTopics.get(j).getLanguageid());
                                            subjects.setSubjectid(NIOSTopics.get(j).getSubjectid());
                                            subjects.setSubjectname(NIOSTopics.get(j).getSubjectname());
                                            if (!containsId(contentTableList, subjects.getSubjectid()))
                                                contentTableList.add(subjects);
                                            AppDatabase.getDatabaseInstance(context).getNiosExamTopicDao().insertAllTopics(NIOSTopics);
                                        }
                                    }
                                }

                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                BackupDatabase.backup(context);
//                            contentTableList.addAll(downloadedContentTableList);

                                assessView.addContentToViewList(contentTableList);
                                assessView.notifyAdapter();
                                //getTopicData();
                            } else
                                Toast.makeText(context, "No Exams..", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading..Check internet connection", Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(context).getAssessmentPaperPatternDao().deletePaperPatterns();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });


    }


    public boolean containsId(final ArrayList<AssessmentSubjects> list, final String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSubjectid().equalsIgnoreCase(id))
                return true;
        }
        return false;
    }

    private void getSubjectData() {
        contentTableList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading subjects");
        AndroidNetworking.get(APIs.AssessmentSubjectAPI + Assessment_Constants.SELECTED_LANGUAGE)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                AssessmentSubjects assessmentSubjects = new AssessmentSubjects();
                                assessmentSubjects.setSubjectid(response.getJSONObject(i).getString("subjectid"));
                                assessmentSubjects.setSubjectname(response.getJSONObject(i).getString("subjectname"));
                                assessmentSubjects.setLanguageid(Assessment_Constants.SELECTED_LANGUAGE);
                                contentTableList.add(assessmentSubjects);
                            }
                            if (contentTableList.size() > 0) {
                                AppDatabase.getDatabaseInstance(context).getSubjectDao().deleteSubjectsByLangId(Assessment_Constants.SELECTED_LANGUAGE);
                                AppDatabase.getDatabaseInstance(context).getSubjectDao().insertAllSubjects(contentTableList);
                                progressDialog.dismiss();
                                BackupDatabase.backup(context);
//                            contentTableList.addAll(downloadedContentTableList);
                                assessView.addContentToViewList(contentTableList);
                                assessView.notifyAdapter();
                                //getTopicData();
                            } else
                                Toast.makeText(context, "No subjects..", Toast.LENGTH_SHORT).show();
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

    /*private void getListData() {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    downloadedContentTableList = AppDatabase.getDatabaseInstance(context).getContentTableDao().getContentData(nodeIds.get(nodeIds.size() - 1));
                    BackupDatabase.backup(context);

                    assessView.clearContentList();

                    try {
                        for (int j = 0; j < downloadedContentTableList.size(); j++) {
                            ContentTable contentTable = new ContentTable();
                            contentTable.setNodeId("" + downloadedContentTableList.get(j).getNodeId());
                            contentTable.setNodeType("" + downloadedContentTableList.get(j).getNodeType());
                            contentTable.setNodeTitle("" + downloadedContentTableList.get(j).getNodeTitle());
                            contentTable.setNodeKeywords("" + downloadedContentTableList.get(j).getNodeKeywords());
                            contentTable.setNodeAge("" + downloadedContentTableList.get(j).getNodeAge());
                            contentTable.setNodeDesc("" + downloadedContentTableList.get(j).getNodeDesc());
                            contentTable.setNodeServerImage("" + downloadedContentTableList.get(j).getNodeServerImage());
                            contentTable.setNodeImage("" + downloadedContentTableList.get(j).getNodeImage());
                            contentTable.setResourceId("" + downloadedContentTableList.get(j).getResourceId());
                            contentTable.setResourceType("" + downloadedContentTableList.get(j).getNodeType());
                            contentTable.setResourcePath("" + downloadedContentTableList.get(j).getResourcePath());
                            contentTable.setParentId("" + downloadedContentTableList.get(j).getParentId());
                            contentTable.setLevel("" + downloadedContentTableList.get(j).getLevel());
                            contentTable.setContentType(downloadedContentTableList.get(j).getContentType());
                            contentTable.setIsDownloaded("true");
                            contentTable.setOnSDCard(true);

                            assessView.addContentToViewList(contentTable);
                        }
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
                assessView.notifyAdapter();
            }
        }.execute();
    }*/

    @Override
    public void clearNodeIds() {
        nodeIds.clear();
    }

    @Override
    public void endSession() {
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
                        BackupDatabase.backup(context);
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

    @Override
    public void startActivity(String activityName) {
    }


    private void getLanguageData() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                            List<AssessmentLanguages> assessmentLanguagesList = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                AssessmentLanguages assessmentLanguages = new AssessmentLanguages();
                                assessmentLanguages.setLanguageid(response.getJSONObject(i).getString("languageid"));
                                assessmentLanguages.setLanguagename(response.getJSONObject(i).getString("languagename"));
                                assessmentLanguagesList.add(assessmentLanguages);
                            }
                            if (assessmentLanguagesList.size() > 0)
                                AppDatabase.getDatabaseInstance(context).getLanguageDao().insertAllLanguages(assessmentLanguagesList);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, "Error in loading..Check internet connection.", Toast.LENGTH_SHORT).show();
//                        AppDatabase.getDatabaseInstance(getActivity()).getAssessmentPaperPatternDao().deletePaperPatterns();
  /*                      ((ChooseAssessmentActivity) getActivity()).frameLayout.setVisibility(View.GONE);
                        ((ChooseAssessmentActivity) getActivity()).rlSubject.setVisibility(View.VISIBLE);
                        ((ChooseAssessmentActivity) getActivity()).toggle_btn.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager().popBackStackImmediate();*/

                        progressDialog.dismiss();
//                        selectTopicDialog.show();
                    }
                });

    }

}


/*    public void getAPIContent(final String requestType, String url) {
        try {
            AndroidNetworking.get(url + "" + nodeIds.get(nodeIds.size() - 1))
                    .addHeaders("Content-Type", "application/json")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            recievedContent(requestType, response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            recievedError(requestType);
                            Log.d("Error:", anError.getErrorDetail());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recievedContent(String header, String response) {
        try {
            if (header.equalsIgnoreCase(COS_Constants.INTERNET_DOWNLOAD)) {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(0).toString());
                //               ContentTable contentData = gson.fromJson(response, ContentTable.class);
                Type listType = new TypeToken<ArrayList<ContentTable>>() {
                }.getType();
                List<ContentTable> serverContentList = gson.fromJson(response, listType);
                //JSONArray returnStoryNavigate = jsonObject.getJSONArray("storyList");
                String sName, nId, sId, sThumbnail, sDesc;
                boolean downloadedFlg = false, contentFound = false;

                for (int i = 0; i < serverContentList.size(); i++) {
                    ContentTable contentTableTemp = new ContentTable();
                    contentFound = false;
                    for (int j = 0; j < downloadedContentTableList.size(); j++) {
                        if (serverContentList.get(i).getNodeId().equalsIgnoreCase(downloadedContentTableList.get(j).getNodeId())) {
                            contentFound = true;
                            downloadedFlg = true;
                            break;
                        }
                    }
                    if (!contentFound) {
                        contentTableTemp = new ContentTable();
                        contentTableTemp.setNodeId("" + serverContentList.get(i).getNodeId());
                        contentTableTemp.setNodeType("" + serverContentList.get(i).getNodeType());
                        contentTableTemp.setNodeTitle("" + serverContentList.get(i).getNodeTitle());
                        contentTableTemp.setNodeKeywords("" + serverContentList.get(i).getNodeKeywords());
                        contentTableTemp.setNodeAge("" + serverContentList.get(i).getNodeAge());
                        contentTableTemp.setNodeDesc("" + serverContentList.get(i).getNodeDesc());
                        contentTableTemp.setNodeServerImage("" + serverContentList.get(i).getNodeServerImage());
                        contentTableTemp.setNodeImage("" + serverContentList.get(i).getNodeImage());
                        contentTableTemp.setResourceId("" + serverContentList.get(i).getResourceId());
                        contentTableTemp.setResourceType("" + serverContentList.get(i).getResourceType());
                        contentTableTemp.setResourcePath("" + serverContentList.get(i).getResourcePath());
                        contentTableTemp.setParentId("" + serverContentList.get(i).getParentId());
                        contentTableTemp.setLevel("" + serverContentList.get(i).getLevel());
                        contentTableTemp.setContentType("" + serverContentList.get(i).getContentType());
                        contentTableTemp.setIsDownloaded("false");
                        contentTableTemp.setOnSDCard(false);
                        contentViewList.add(contentTableTemp);
                    }
                }
                Collections.sort(contentViewList);
                Collections.sort(contentViewList, new Comparator<ContentTable>() {
                    @Override
                    public int compare(ContentTable o1, ContentTable o2) {
                        return o1.getNodeId().compareTo(o2.getNodeId());
                    }
                });
                Log.d("sorted", contentViewList.toString());
                levelAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recievedError(String header) {
        levelAdapter.notifyDataSetChanged();
        Log.d("API_Error", "recievedError: "+header);
    }*/
