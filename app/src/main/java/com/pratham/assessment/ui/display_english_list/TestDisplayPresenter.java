package com.pratham.assessment.ui.display_english_list;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.async.API_Content;
import com.pratham.assessment.custom.ZipDownloader;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.ContentTable;
import com.pratham.assessment.domain.Modal_DownloadAssessment;
import com.pratham.assessment.domain.Modal_DownloadContent;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.interfaces.API_Content_Result;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.BaseActivity.appDatabase;


public class TestDisplayPresenter implements TestDiaplayContract.TestDisplayPresenter, API_Content_Result {

    Context context;
    TestDiaplayContract.TestDisplayView testView;
    String downloadNodeId, fileName;
    Gson gson;
    ZipDownloader zipDownloader;
    Modal_DownloadContent download_content;
    ArrayList<ContentTable> pos;
    ContentTable contentDetail;
    List<ContentTable> downloadedContentTableList, ListForContentTable1, ListForContentTable2;
    ArrayList<String> nodeIds;
    API_Content api_content;

    public TestDisplayPresenter(Context context, TestDiaplayContract.TestDisplayView testView) {
        this.context = context;
        this.testView = testView;
        nodeIds = new ArrayList<>();
        gson = new Gson();
        zipDownloader = new ZipDownloader(context);
        pos = new ArrayList<>();
        zipDownloader = new ZipDownloader(context);
        ListForContentTable1 = new ArrayList<ContentTable>();
        ListForContentTable2 = new ArrayList<ContentTable>();
        api_content = new API_Content(context, TestDisplayPresenter.this);
    }

    @Override
    public void addNodeIdToList(String nodeId) {
        nodeIds.add(nodeId);
    }

    @Override
    public boolean removeLastNodeId() {
        if (nodeIds.size() > 1) {
            nodeIds.remove(nodeIds.size() - 1);
            return true;
        } else
            return false;
    }

    @Override
    public void getListData() {

        new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    downloadedContentTableList = appDatabase.getContentTableDao().getContentData("" + nodeIds.get(nodeIds.size() - 1));
                    BackupDatabase.backup(context);

                    testView.clearContentList();
                    ListForContentTable1.clear();
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
                            ListForContentTable1.add(contentTable);
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

                if (Assessment_Utility.isDataConnectionAvailable(context))
                    api_content.getAPIContent(Assessment_Constants.INTERNET_DOWNLOAD, Assessment_Constants.INTERNET_DOWNLOAD_API, nodeIds.get(nodeIds.size() - 1));
                else {
                    if (downloadedContentTableList.size() == 0 && !Assessment_Utility.isDataConnectionAvailable(context)) {
                        testView.showNoDataDownloadedDialog();
                    } else {
                        testView.addContentToViewList(ListForContentTable1);
                        testView.notifyAdapter();
                    }
                }
            }
        }.execute();
    }

    @Override
    public void downloadResource(String downloadId) {
        downloadNodeId = downloadId;
        api_content.getAPIContent(Assessment_Constants.INTERNET_DOWNLOAD_RESOURCE, Assessment_Constants.INTERNET_DOWNLOAD_RESOURCE_API, downloadNodeId);
//        getAPIContent(Assessment_Constants.INTERNET_DOWNLOAD_RESOURCE, Assessment_Constants.INTERNET_DOWNLOAD_RESOURCE_API);
    }

    public void receivedContent(String header, final String response) {
        try {
            if (header.equalsIgnoreCase(Assessment_Constants.INTERNET_DOWNLOAD)) {
                new AsyncTask<Object, Void, Object>() {

                    @Override
                    protected Object doInBackground(Object[] objects) {

                        boolean contentFound = false;

                        try {
                            ListForContentTable2.clear();

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = new JSONObject(jsonArray.getJSONObject(0).toString());
                            Type listType = new TypeToken<ArrayList<ContentTable>>() {
                            }.getType();
                            List<ContentTable> serverContentList = gson.fromJson(response, listType);

                            for (int i = 0; i < serverContentList.size(); i++) {
                                ContentTable contentTableTemp = new ContentTable();
                                contentFound = false;
                                for (int j = 0; j < downloadedContentTableList.size(); j++) {
                                    if (serverContentList.get(i).getNodeId().equalsIgnoreCase(downloadedContentTableList.get(j).getNodeId())) {
                                        contentFound = true;
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
                                    ListForContentTable2.add(contentTableTemp);
                                }

                            }
                            testView.addContentToViewList(ListForContentTable1);
                            testView.addContentToViewList(ListForContentTable2);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        testView.notifyAdapter();
                    }
                }.execute();
            } else if (header.equalsIgnoreCase(Assessment_Constants.INTERNET_DOWNLOAD_RESOURCE)) {
                new AsyncTask<Object, Void, Object>() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            download_content = gson.fromJson(jsonObject.toString(), Modal_DownloadContent.class);
                            contentDetail = download_content.getNodelist().get(download_content.getNodelist().size() - 1);

                            for (int i = 0; i < download_content.getNodelist().size(); i++) {
                                ContentTable contentTableTemp = download_content.getNodelist().get(i);
                                pos.add(contentTableTemp);
                            }

                            fileName = download_content.getDownloadurl()
                                    .substring(download_content.getDownloadurl().lastIndexOf('/') + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        zipDownloader.initialize(context, download_content.getDownloadurl(),
                                download_content.getFoldername(), fileName, contentDetail, pos);
                    }
                }.execute();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receivedError(String header) {
        testView.addContentToViewList(ListForContentTable1);
        testView.notifyAdapter();
    }

    @Override
    public void endTestSession() {
        try {
            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        String toDateTemp = appDatabase.getSessionDao().getToDate(Assessment_Constants.assessmentSession);

                        if (toDateTemp.equalsIgnoreCase("na")) {
                            appDatabase.getSessionDao().UpdateToDate(Assessment_Constants.assessmentSession, AssessmentApplication.getCurrentDateTime());
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

    public void fetchDownloadedJsonData(String jsonName, int downloadPos) {
        JSONArray returnModalGames = null;
        try {
            Log.d("DW_COMPLETE", "In FetchJson ");
            InputStream is = new FileInputStream((AssessmentApplication.assessPath + Assessment_Constants.GAME_PATH + jsonName));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            JSONObject jsonObject = new JSONObject(jsonStr);
            //           returnModalGames = jsonObj.getJSONArray("NodeList");

            Modal_DownloadAssessment download_content = gson.fromJson(jsonObject.toString(), Modal_DownloadAssessment.class);

            pos = new ArrayList<>();
            for (int i = 0; i < download_content.getNodelist().size(); i++) {
                ContentTable contentTableTemp = download_content.getNodelist().get(i);
                pos.add(contentTableTemp);
            }
            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {
                        Log.d("DW_COMPLETE", "In ASYNC");
                        BaseActivity.appDatabase.getContentTableDao().addContentList(pos);
                    } catch (Exception e) {
                        Log.d("DW_COMPLETE", "In ASYNC EXCEPTION");
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

            Log.d("DW_COMPLETE", "In Complete FetchJson");

            testView.showDownloaded(downloadPos);

            BackupDatabase.backup(context);
            Log.d("DW_COMPLETE", "In Complete After Backup");

        } catch (Exception e) {
            Log.d("DW_COMPLETE", "In  FetchJson Exception ");
            e.printStackTrace();
        }
    }


    @Override
    public void starContentEntry(final String contentID, final String Label) {
        new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    String deviceId = appDatabase.getStatusDao().getValue("DeviceId");
                    String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");

                    Score score = new Score();
                    score.setSessionID(Assessment_Constants.currentSession);
                    score.setResourceID("" + contentID);
                    score.setQuestionId(0);
                    score.setScoredMarks(0);
                    score.setTotalMarks(0);
                    score.setStudentID(Assessment_Constants.currentStudentID);
                    score.setStartDateTime(AssessmentApplication.getCurrentDateTime(false, AppStartDateTime));
                    score.setDeviceID(deviceId.equals(null) ? "0000" : deviceId);
                    score.setEndDateTime(AssessmentApplication.getCurrentDateTime());
                    score.setLevel(0);
                    score.setLabel(Label);
                    score.setSentFlag(0);
                    appDatabase.getScoreDao().insert(score);
                    BackupDatabase.backup(context);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();
    }

}


//    public void getAPIContent(final String requestType, String url) {
//        try {
//            String url_id;
//            url_id = url + "" + nodeIds.get(nodeIds.size() - 1);
//            if (requestType.equalsIgnoreCase(Assessment_Constants.INTERNET_DOWNLOAD_RESOURCE))
//                url_id = url + "" + downloadNodeId;
//            AndroidNetworking.get(url_id)
//                    .addHeaders("Content-Type", "application/json")
//                    .build()
//                    .getAsString(new StringRequestListener() {
//                        @Override
//                        public void onResponse(String response) {
//                            recievedContent(requestType, response);
//                        }
//
//                        @Override
//                        public void onError(ANError anError) {
//                            recievedError(requestType);
//                            try {
//                                Log.d("Error:", anError.getErrorDetail());
//                                Log.d("Error::", anError.getResponse().toString());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
