package com.pratham.assessment.ui.choose_assessment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.AssessmentLanguages;
import com.pratham.assessment.domain.AssessmentSubjects;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.utilities.APIs;
import com.pratham.assessment.utilities.Assessment_Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.BaseActivity.appDatabase;


public class ChooseAssessmentPresenter implements ChooseAssessmentContract.ChooseAssessmentPresenter {

    Context context;
    ChooseAssessmentContract.ChooseAssessmentView assessView;
    List<AssessmentSubjects> contentTableList = new ArrayList<>(), downloadedContentTableList;
    ArrayList<String> nodeIds;


    public ChooseAssessmentPresenter(Context context, ChooseAssessmentContract.ChooseAssessmentView assessView) {
        this.context = context;
        this.assessView = assessView;
        nodeIds = new ArrayList<>();
        nodeIds.add("1299");
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

    @Override
    public void startAssessSession() {

        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Session startSesion = new Session();
                    startSesion.setSessionID("" + Assessment_Constants.assessmentSession);
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
    }

    private void getListData() {
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            getLanguageData();
            getSubjectData();
        } else {
            downloadedContentTableList = AppDatabase.getDatabaseInstance(context).getSubjectDao().getAllSubjectsByLangId(Assessment_Constants.SELECTED_LANGUAGE);
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

                        String toDateTemp = appDatabase.getSessionDao().getToDate(Assessment_Constants.currentSession);
                        if (toDateTemp.equalsIgnoreCase("na")) {
                            appDatabase.getSessionDao().UpdateToDate(Assessment_Constants.currentSession, AssessmentApplication.getCurrentDateTime());
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
