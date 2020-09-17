package com.pratham.assessment.async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.custom.custom_dialogs.PushDataDialog;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.database.BackupDatabase;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.domain.AssessmentPaperForPush;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.DownloadMedia;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.domain.Modal_RaspFacility;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.domain.SupervisorData;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.login.MainActivity;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;
import static com.pratham.assessment.AssessmentApplication.isTablet;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_AUDIO;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_SUPERVISOR;
import static com.pratham.assessment.utilities.Assessment_Constants.DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING;
import static com.pratham.assessment.utilities.Assessment_Constants.PUSH_DATA_FROM_DRAWER;
import static com.pratham.assessment.utilities.Assessment_Utility.getFileExtension;

/******* This async task is used for data push******/

@EBean
public class PushDataToServer {

    Context context;
    boolean autoPush;
    JSONArray scoreData;
    JSONArray assessmentScoreData;
    JSONArray attendanceData;
    JSONArray studentData;
    JSONArray crlData;
    JSONArray sessionData;
    JSONArray learntWords;
    JSONArray supervisorData;
    JSONArray groupsData;
    JSONArray assessmentData;
    JSONArray assessmentScienceData;
    JSONArray logsData;
    Boolean isConnectedToRasp = false;

    String programID = "";

    boolean dataPushed = false;
    boolean supervisorImagesPushed = false;
    boolean answerMediaPushed = false;
    boolean videoMonImagesPushed = false;
    List<DownloadMedia> downloadMediaList = new ArrayList<>();
    List<DownloadMedia> supervisorMediaList = new ArrayList<>();
    List<DownloadMedia> videoRecordingList = new ArrayList<>();
    private int pushCnt = 0, mediaCnt = 0, videoMonCnt = 0, supervisorCnt = 0, answerMediaCnt = 0;
    private int totalVideoMonCnt = 0, totalSupervisorCnt = 0, totalAnswerMediaCnt = 0;
    //    ProgressDialog progressDialog;
    JSONObject requestJsonObjectScience;
    AlertDialog.Builder alertDialog;
    LottieAnimationView push_lottie;
    TextView txt_push_dialog_msg;
    TextView txt_push_cnt;
    RelativeLayout rl_btn;
    Button ok_btn;

    public PushDataToServer(Context context) {
        this.context = context;
    }

    public void setValue(Context context, boolean autoPush) {

        this.context = context;
        this.autoPush = autoPush;
        scoreData = new JSONArray();
        attendanceData = new JSONArray();
        crlData = new JSONArray();
        sessionData = new JSONArray();
        learntWords = new JSONArray();
        supervisorData = new JSONArray();
        groupsData = new JSONArray();
        logsData = new JSONArray();
        studentData = new JSONArray();
        assessmentData = new JSONArray();
        assessmentScienceData = new JSONArray();
//        progressDialog = new ProgressDialog(context);
    }

    @UiThread
    protected void onPreExecute() {
     /*   progressDialog.setCancelable(false);
        progressDialog.setMessage("Pushing data..");*/
        if (isTablet || !autoPush)
//            progressDialog.show();
        {
            PushDataDialog pushDialog = new PushDataDialog(context);
//            pushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pushDialog.setContentView(R.layout.app_push_data_dialog);
            Objects.requireNonNull(pushDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pushDialog.setCancelable(false);
            pushDialog.setCanceledOnTouchOutside(false);
            pushDialog.show();

            push_lottie = pushDialog.findViewById(R.id.push_lottie);
            txt_push_dialog_msg = pushDialog.findViewById(R.id.txt_push_dialog_msg);
            txt_push_cnt = pushDialog.findViewById(R.id.txt_push_cnt);
            rl_btn = pushDialog.findViewById(R.id.rl_btn);
            ok_btn = pushDialog.findViewById(R.id.ok_btn);
            ok_btn.setOnClickListener(view -> {

                pushDialog.dismiss();
                if (context instanceof MainActivity)
                    ((MainActivity) context).onResponseGet();
                if (context instanceof ScienceAssessmentActivity)
                    ((ScienceAssessmentActivity) context).onResponseGet();
            });
        }
    }

    @Background
    public void doInBackground() {
        onPreExecute();
        List<Score> scoreList = AppDatabase.getDatabaseInstance(context).getScoreDao().getAllPushScores("ece_assessment");
        scoreData = fillScoreData(scoreList);
        List<AssessmentPaperForPush> assessmentScoreList = AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().getAllAssessmentPapersForPush();
        assessmentScoreData = fillAssessmentScoreData(assessmentScoreList);
        List<Attendance> attendanceList = AppDatabase.getDatabaseInstance(context).getAttendanceDao().getAllPushAttendanceEntries();
        attendanceData = fillAttendanceData(attendanceList);
        List<Student> studentList = AppDatabase.getDatabaseInstance(context).getStudentDao().getAllNewStudents();
        studentData = fillStudentData(studentList);
        List<Crl> crlList = AppDatabase.getDatabaseInstance(context).getCrlDao().getAllCrls();
        crlData = fillCrlData(crlList);
        List<Session> sessionList = AppDatabase.getDatabaseInstance(context).getSessionDao().getAllNewSessions();
        sessionData = fillSessionData(sessionList);
/*        List<LearntWords> learntWordsList = AppDatabase.getDatabaseInstance(context).getLearntWordDao().getAllData();
        learntWords = fillLearntWordsData(learntWordsList);
    */
        List<SupervisorData> supervisorDataList = AppDatabase.getDatabaseInstance(context).getSupervisorDataDao().getAllSupervisorData();
        supervisorData = fillSupervisorData(supervisorDataList);
        List<Modal_Log> logsList = AppDatabase.getDatabaseInstance(context).getLogsDao().getPushAllLogs();
        logsData = fillLogsData(logsList);
        List<Assessment> assessmentList = AppDatabase.getDatabaseInstance(context).getAssessmentDao().getAllECEAssessment();
        assessmentData = fillAssessmentData(assessmentList);
        List<Assessment> scienceAssessmentList = AppDatabase.getDatabaseInstance(context).getAssessmentDao().getAllScienceAssessment();
        assessmentScienceData = fillAssessmentData(scienceAssessmentList);

        List<Groups> groupsList = AppDatabase.getDatabaseInstance(context).getGroupsDao().getAllGroups();
        groupsData = fillGroupsData(groupsList);

        JSONObject rootJson = new JSONObject();

        try {
            Gson gson = new Gson();
            //iterate through all new sessions
            JSONObject metadataJson = new JSONObject();
/*            JSONArray sessionArray = new JSONArray();
            List<Session> newSessions = AppDatabase.getDatabaseInstance(context).getSessionDao().getAllNewSessions();

            for (Session session : newSessions) {
                //fetch all logs
                JSONArray logArray = new JSONArray();
                List<Modal_Log> allLogs = AppDatabase.getDatabaseInstance(context).getLogsDao().getAllLogs(session.getSessionID());
                for (Modal_Log log : allLogs)
                    logArray.put(new JSONObject(gson.toJson(log)));
                //fetch attendance
                JSONArray attendanceArray = new JSONArray();
                List<Attendance> newAttendance = AppDatabase.getDatabaseInstance(context).getAttendanceDao().getNewAttendances(session.getSessionID());
                for (Attendance att : newAttendance) {
                    attendanceArray.put(new JSONObject(gson.toJson(att)));
                }
                //fetch Scores & convert to Json Array
                JSONArray scoreArray = new JSONArray();
                List<Score> newScores = AppDatabase.getDatabaseInstance(context).getScoreDao().getAllNewScores(session.getSessionID());
                for (Score score : newScores) {
                    scoreArray.put(new JSONObject(gson.toJson(score)));
                }
                // fetch Session Data
                JSONObject sessionJson = new JSONObject();
                sessionJson.put("SessionID", session.getSessionID());
                sessionJson.put("fromDate", session.getFromDate());
                sessionJson.put("toDate", session.getToDate());
                sessionArray.put(sessionJson);

                JSONArray studentArray = new JSONArray();
                if (!COSApplication.isTablet) {
                    List<Student> newStudents = AppDatabase.getDatabaseInstance(context).getStudentDao().getAllNewStudents();
                    for (Student std : newStudents)
                        studentArray.put(new JSONObject(gson.toJson(std)));
                }
                if (!COSApplication.isTablet)
                    rootJson.put(COS_Constants.STUDENTS, studentArray);
                rootJson.put(COS_Constants.SESSION, sessionArray);
                rootJson.put(COS_Constants.ATTENDANCE, attendanceArray);
                rootJson.put(COS_Constants.SCORE, scoreArray);
                rootJson.put(COS_Constants.LOGS, logArray);
                rootJson.put(COS_Constants.ASSESSMENT, assessmentData);
                rootJson.put(COS_Constants.SUPERVISOR, supervisorData);
                rootJson.put(COS_Constants.LEARNTWORDS, learntWords);

            }*/
           /* List<com.pratham.cityofstories.domain.Status> metadata = AppDatabase.getDatabaseInstance(context).getStatusDao().getAllStatuses();
            for (com.pratham.cityofstories.domain.Status status : metadata) {
                metadataJson.put(status.getStatusKey(), status.getValue());
                if (status.getStatusKey().equalsIgnoreCase("programId"))
                    programID = status.getValue();
            }
            metadataJson.put(COS_Constants.SCORE_COUNT, (metadata.size() > 0) ? metadata.size() : 0);
            rootJson.put(COS_Constants.METADATA, metadataJson);
*/
//        JSONObject requestJsonObject = generateRequestString(scoreData, attendanceData, sessionData, learntWords, supervisorData, logsData, assessmentData, studentData);
            requestJsonObjectScience = generateRequestString(scoreData, assessmentScoreData, attendanceData, sessionData, learntWords, supervisorData, logsData, assessmentScienceData, studentData);


            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("username", "pratham");
                        object.put("password", "pratham");
/*                    new PD_ApiRequest(context, ContentPresenterImpl.this)
                            .getacilityIdfromRaspberry(COS_Constants.FACILITY_ID, COS_Constants.RASP_IP + "/api/session/", object);*/
                        AndroidNetworking.post(Assessment_Constants.RASP_IP + "/api/session/")
                                .addHeaders("Content-Type", "application/json")
                                .addJSONObjectBody(object)
                                .build()
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        isConnectedToRasp = true;
                                        Gson gson = new Gson();
                                        Modal_RaspFacility facility = gson.fromJson(response, Modal_RaspFacility.class);
                                        FastSave.getInstance().saveString(Assessment_Constants.FACILITY_ID, facility.getFacilityId());
                                        pushDataToRaspberry("" + Assessment_Constants.URL.DATASTORE_RASPBERY_URL.toString(),
                                                "" + requestJsonObjectScience, programID, Assessment_Constants.USAGEDATA);

                                    }

                                    @Override
                                    public void onError(ANError anError) {
//                            apiResult.notifyError(requestType/*, null*/);
                                        isConnectedToRasp = false;

                                        Log.d("Error::", anError.getErrorDetail());
                                        Log.d("Error::", anError.getMessage());
                                        Log.d("Error::", anError.getResponse().toString());
                                    }
                                });
                    } catch (Exception e) {
                        isConnectedToRasp = false;
                        e.printStackTrace();
                    }
                }
            } else isConnectedToRasp = false;
            programID = AppDatabase.appDatabase.getStatusDao().getValue("programId");


        } catch (Exception e) {
            e.printStackTrace();
        }

        //        if (checkEmptyness(requestString))

        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            if (!AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {

//            pushDataToServer(context, requestJsonObject, AssessmentApplication.uploadDataUrl);

//                if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                pushDataScienceToServer(context, requestJsonObjectScience, AssessmentApplication.uploadScienceUrl);
            /*    createMediaFileToPush();
                CreateFilesForVideoMonitoring();*/

             /*   } else {

                }*/
            }/* else {//todo raspberry push
                pushDataToRaspberry("" + Assessment_Constants.URL.DATASTORE_RASPBERY_URL.toString(),
                        "" + requestJsonObjectScience, programID, Assessment_Constants.USAGEDATA);
            }*/
        } else {
            onPostExecute();
         /*   if (progressDialog != null)
                progressDialog.dismiss();*/
        }

    }

    private void pushSupervisorImages() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                supervisorMediaList.addAll(AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByTypeForPush(DOWNLOAD_MEDIA_TYPE_SUPERVISOR));
                if (supervisorMediaList.size() > 0) {
                    totalSupervisorCnt = supervisorMediaList.size();
                    pushMediaToServer(AssessmentApplication.uploadScienceFilesUrl, DOWNLOAD_MEDIA_TYPE_SUPERVISOR, supervisorMediaList);
                } else supervisorImagesPushed = true;
                return null;
            }
        }.execute();
    }


    //    @Background
    private void createMediaFileToPush() {
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected Void doInBackground(Void... voids) {
                downloadMediaList.addAll(AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByTypeForPush(DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE));
                downloadMediaList.addAll(AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByTypeForPush(DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO));
                downloadMediaList.addAll(AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByTypeForPush(DOWNLOAD_MEDIA_TYPE_ANSWER_AUDIO));
             /*   for (int i = 0; i < temp.size(); i++) {
                    if (!downloadMediaList.contains(temp.get(i)))
                        downloadMediaList.add(temp.get(i));
                }
                List<DownloadMedia> temp1 = AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByTypeForPush(DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO);
                for (int i = 0; i < temp1.size(); i++) {
                    if (!downloadMediaList.contains(temp1.get(i)))
                        downloadMediaList.add(temp1.get(i));
                }*/

                if (downloadMediaList.size() > 0) {
                    totalAnswerMediaCnt = downloadMediaList.size();
                    pushMediaToServer(AssessmentApplication.uploadScienceFilesUrl, DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA, downloadMediaList);
                } else answerMediaPushed = true;
                return null;
            }
        }.execute();
//        String filePath = downloadMediaList.get(mediaCnt).getPhotoUrl();
//        String type = downloadMediaList.get(mediaCnt).getMediaType();
//        if (filePath != null)
//            if (!filePath.equalsIgnoreCase("")) {
       /* if (downloadMediaList.size() > 0)
            pushMediaToServer(AssessmentApplication.uploadScienceFilesUrl, DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE);
*/
            /*} else {
                mediaCnt++;
                if (mediaCnt < downloadMediaList.size())
                    createMediaFileToPush();
            }*/
    }

    //    @Background
    private void CreateFilesForVideoMonitoring() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                videoRecordingList.addAll(AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByTypeForPush(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING));
                if (videoRecordingList.size() > 0) {
           /* String filePath = videoRecordingList.get(videoRecCnt).getPhotoUrl();
            if (!filePath.equalsIgnoreCase("")) {
                try {
                    File file = new File(filePath);
                    if (file.exists())*/
                    totalVideoMonCnt = videoRecordingList.size();
                    pushMediaToServer(AssessmentApplication.uploadScienceFilesUrl, DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING, videoRecordingList);
               /* } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
                } else {
                    videoMonImagesPushed = true;
                    PushDataToServer.this.onPostExecute();
                }
                return null;
            }
        }.execute();
    }

    private void pushMediaToServer(String url, String type, List<DownloadMedia> pushList) {
//        if (!type.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING)) {
        try {

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
            final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
            final MediaType MEDIA_TYPE_MP4 = MediaType.parse("video/mp4");
            final MediaType MEDIA_TYPE_3GP = MediaType.parse("video/3gp");
            final MediaType MEDIA_TYPE_MP3 = MediaType.parse("audio/mp3");

            MultipartBody.Builder builderNew = new MultipartBody.Builder().setType(MultipartBody.FORM);

//            if (!type.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING)) {
            String fileName;
            for (int i = 0; i < pushList.size(); i++) {
                String file[] = pushList.get(i).getPhotoUrl().split("/");
                fileName = file[file.length - 1];
//                        fileName = downloadMediaList.get(i).getqId() + "_" + downloadMediaList.get(i).getPaperId() + "_" + type;
                String extension = getFileExtension(pushList.get(i).getPhotoUrl());
//                    String fileWithExt = fileName + "." + extension;
                File f = new File(pushList.get(i).getPhotoUrl());
                if (f.exists()) {
                    MediaType mediaType = MEDIA_TYPE_PNG;
                    if (extension.equalsIgnoreCase("png"))
                        mediaType = MEDIA_TYPE_PNG;
                    else if (extension.equalsIgnoreCase("jpg"))
                        mediaType = MEDIA_TYPE_JPG;
                    else if (extension.equalsIgnoreCase("3gp"))
                        mediaType = MEDIA_TYPE_3GP;
                    else if (extension.equalsIgnoreCase("mp4"))
                        mediaType = MEDIA_TYPE_MP4;
                    else if (extension.equalsIgnoreCase("mp3"))
                        mediaType = MEDIA_TYPE_MP3;
                    builderNew.addFormDataPart(fileName, fileName, RequestBody.create(mediaType, f));
                }
            }
           /* } else {
                for (int i = 0; i < videoRecordingList.size(); i++) {
//                    String fileName = DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING + "_" + videoRecordingList.get(i).getPaperId();
                    String fileName = DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING + "_" + videoRecordingList.get(i).getPaperId() + "_" + Assessment_Utility.getCurrentDateTime() + "_" + i;
                    fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
                    String extension = getFileExtension(videoRecordingList.get(i).getPhotoUrl());
                    String fileWithExt = fileName + "." + extension;
                    File f = new File(videoRecordingList.get(i).getPhotoUrl());
                    if (f.exists()) {
                        builderNew.addFormDataPart(fileName, fileWithExt, RequestBody.create(MEDIA_TYPE_JPG, f));
                    }
                }
            }*/
            MultipartBody requestBody = builderNew.build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(5, TimeUnit.MINUTES);
            builder.readTimeout(5, TimeUnit.MINUTES);
            builder.writeTimeout(5, TimeUnit.MINUTES);
            OkHttpClient client = builder.build();
            Response response = client.newCall(request).execute();
            Log.d("response", type + response.body().string());
            if (response.isSuccessful()) {
                setMediaPushFlag(type);
            } else {
                if (type.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING))
                    onPostExecute();
//                Toast.makeText(context, "Media push failed..", Toast.LENGTH_SHORT).show();
            }
//            return new JSONObject(response.body().string());

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
            onPostExecute();

        } catch (Exception e) {
            onPostExecute();
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
 /*       } else {
            try {
                final MediaType MEDIA_TYPE_MP4 = MediaType.parse("video/mp4");

                MultipartBody.Builder builderNew = new MultipartBody.Builder().setType(MultipartBody.FORM);
                for (int i = 0; i < videoRecordingList.size(); i++) {
                    String fileName = DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING + "_" + videoRecordingList.get(i).getPaperId();
                    String extension = getExtension(videoRecordingList.get(i).getPhotoUrl());
                    String fileWithExt = fileName + "." + extension;
                    File f = new File(videoRecordingList.get(i).getPhotoUrl());
                    if (f.exists()) {
                        builderNew.addFormDataPart(fileName, fileWithExt, RequestBody.create(MEDIA_TYPE_MP4, f));
                    }
                }
                MultipartBody requestBody = builderNew.build();
                final Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(5, TimeUnit.MINUTES);
                builder.readTimeout(5, TimeUnit.MINUTES);
                builder.writeTimeout(5, TimeUnit.MINUTES);
                OkHttpClient client = builder.build();
                Response response = client.newCall(request).execute();
                Log.d("response", "uploadVideo:" + response.body().string());
                if (response.isSuccessful())
                    setMediaPushFlag(type);
                else Toast.makeText(context, "Media push failed..", Toast.LENGTH_SHORT).show();

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
            }


        }*/


















       /* AndroidNetworking.upload(url)
                .addMultipartFile(fileName, file)
//                .addMultipartParameter("key", "value")
//                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        Log.d("onProgress", bytesUploaded + "/" + totalBytes);

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        if (videoType.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO) || videoType.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE)) {
                            mediaCnt++;
                            if (mediaCnt < downloadMediaList.size())
                                createMediaFileToPush();
                            else {
                                if (AssessmentApplication.isTablet)
                                    Toast.makeText(context, "Media pushed successfully", Toast.LENGTH_SHORT).show();
                                setMediaPushFlag();
                            }
                        } else {
                            videoRecCnt++;
                            if (videoRecCnt < videoRecordingList.size())
                                createMediaFileToPush();
                            else {
                                setMediaPushFlag();
                                if (AssessmentApplication.isTablet)
                                    Toast.makeText(context, "Media pushed successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        if (videoType.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO) || videoType.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE)) {
                            mediaCnt++;
                            if (mediaCnt < downloadMediaList.size())
                                createMediaFileToPush();
                            else progressDialog.dismiss();

                        } else {
                            if (AssessmentApplication.isTablet) {
                                Toast.makeText(context, "video monitoring Media push failed", Toast.LENGTH_SHORT).show();
                            }
                            videoRecCnt++;
                            if (videoRecCnt < videoRecordingList.size())
                                createMediaFileToPush();
                            else progressDialog.dismiss();
                        }
                    }
                });*/
    }


    private boolean checkEmptyness(String requestString) {
        try {
            JSONObject jsonObject = new JSONObject(requestString);
            JSONObject jsonObjectSession = jsonObject.getJSONObject("session");

            return jsonObjectSession.getJSONArray("scoreData").length() > 0 ||
                    jsonObjectSession.getJSONArray("attendanceData").length() > 0 ||
                    jsonObjectSession.getJSONArray("sessionsData").length() > 0 ||
                    jsonObjectSession.getJSONArray("learntWordsData").length() > 0 ||
                    jsonObjectSession.getJSONArray("logsData").length() > 0 ||
                    jsonObjectSession.getJSONArray("assessmentData").length() > 0 ||
                    jsonObjectSession.getJSONArray("supervisor").length() > 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private JSONObject generateRequestString(JSONArray eceScoreData, JSONArray assessmentScoreData, JSONArray attendanceData, JSONArray sessionData, JSONArray learntWordsData, JSONArray supervisorData, JSONArray logsData, JSONArray assessmentData, JSONArray studentData) {
        String requestString = "";
        JSONObject rootJson = new JSONObject();

        try {
            JSONObject sessionObj = new JSONObject();
            JSONObject metaDataObj = new JSONObject();
            metaDataObj.put("ScoreCount", assessmentScoreData.length());

            metaDataObj.put("CRLID", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("CRLID"));
            metaDataObj.put("group1", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("group1"));
            metaDataObj.put("group2", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("group2"));
            metaDataObj.put("group3", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("group3"));
            metaDataObj.put("group4", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("group4"));
            metaDataObj.put("group5", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("group5"));
            metaDataObj.put("DeviceId", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("DeviceId"));
            metaDataObj.put("DeviceName", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("DeviceName"));
            metaDataObj.put("ActivatedDate", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("ActivatedDate"));
            metaDataObj.put("village", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("village"));
            metaDataObj.put("ActivatedForGroups", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("ActivatedForGroups"));
            metaDataObj.put("SerialID", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("SerialID"));
            metaDataObj.put("gpsFixDuration", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("gpsFixDuration"));
            metaDataObj.put("prathamCode", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("prathamCode"));
            metaDataObj.put("programId", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("programId"));
            metaDataObj.put("WifiMAC", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("wifiMAC"));
            metaDataObj.put("apkType", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("apkType"));
            metaDataObj.put("appName", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("appName"));
            metaDataObj.put("apkVersion", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("apkVersion"));
            metaDataObj.put("GPSDateTime", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("GPSDateTime"));
            metaDataObj.put("Latitude", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("Latitude"));
            metaDataObj.put("Longitude", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("Longitude"));

            //new Entries
            metaDataObj.put("OsVersionName", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("OsVersionName"));
            metaDataObj.put("OsVersionNum", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("OsVersionNum"));
            metaDataObj.put("AvailableStorage", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AvailableStorage"));
            metaDataObj.put("ScreenResolution", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("ScreenResolution"));
            metaDataObj.put("Manufacturer", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("Manufacturer"));
            metaDataObj.put("Model", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("Model"));
            metaDataObj.put("ApiLevel", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("ApiLevel"));
            metaDataObj.put("InternalStorageSize", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("InternalStorageSize"));


            sessionObj.put("scoreData", assessmentScoreData);
            sessionObj.put("eceScoreData", eceScoreData);
/*            if (!COS_Constants.SD_CARD_Content)
                sessionObj.put("studentData", studentData);*/
            sessionObj.put("attendanceData", attendanceData);
            sessionObj.put("sessionsData", sessionData);
            sessionObj.put("learntWordsData", learntWordsData);
            sessionObj.put("logsData", logsData);
            sessionObj.put("assessmentData", assessmentData);
            sessionObj.put("supervisor", supervisorData);
            if (!isTablet)
                sessionObj.put("studentData", studentData);

           /* requestString = "{ \"session\": " + sessionObj +
                    ", \"metadata\": " + metaDataObj +
                    "}";
*/
            rootJson.put("session", sessionObj);
            rootJson.put("metadata", metaDataObj);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return rootJson;
    }

    private JSONArray fillSessionData(List<Session> sessionList) {
        JSONArray newSessionsData = new JSONArray();
        JSONObject _sessionObj;
        try {
            for (int i = 0; i < sessionList.size(); i++) {
                _sessionObj = new JSONObject();
                _sessionObj.put("SessionID", sessionList.get(i).getSessionID());
                _sessionObj.put("fromDate", sessionList.get(i).getFromDate());
                _sessionObj.put("toDate", sessionList.get(i).getToDate());
                newSessionsData.put(_sessionObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newSessionsData;
    }
/*
    private JSONArray fillLearntWordsData(List<LearntWords> learntWordsList) {
        JSONArray newLearntWords = new JSONArray();
        JSONObject _learntWordsObj;
        try {
            for (int i = 0; i < learntWordsList.size(); i++) {
                _learntWordsObj = new JSONObject();
                _learntWordsObj.put("studentId", learntWordsList.get(i).getStudentId());
                _learntWordsObj.put("synId", learntWordsList.get(i).getSynId());
                _learntWordsObj.put("wordUUId", learntWordsList.get(i).getWordUUId());
                _learntWordsObj.put("word", learntWordsList.get(i).getWord());
                newLearntWords.put(_learntWordsObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newLearntWords;
    }
*/

    private JSONArray fillCrlData(List<Crl> crlsList) {

        JSONArray crlsData = new JSONArray();
        JSONObject _crlObj;
        try {
            for (int i = 0; i < crlsList.size(); i++) {
                _crlObj = new JSONObject();
                _crlObj.put("CRLId", crlsList.get(i).getCRLId());
                _crlObj.put("FirstName", crlsList.get(i).getFirstName());
                _crlObj.put("LastName", crlsList.get(i).getLastName());
                _crlObj.put("UserName", crlsList.get(i).getUserName());
                _crlObj.put("UserName", crlsList.get(i).getUserName());
                _crlObj.put("Password", crlsList.get(i).getPassword());
                _crlObj.put("ProgramId", crlsList.get(i).getProgramId());
                _crlObj.put("Mobile", crlsList.get(i).getMobile());
                _crlObj.put("State", crlsList.get(i).getState());
                _crlObj.put("Email", crlsList.get(i).getEmail());
                _crlObj.put("CreatedBy", crlsList.get(i).getCreatedBy());
                _crlObj.put("newCrl", !crlsList.get(i).isNewCrl());
                crlsData.put(_crlObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return crlsData;
    }

    private JSONArray fillStudentData(List<Student> studentList) {
        JSONArray studentData = new JSONArray();
        JSONObject _studentObj;
        try {
            for (int i = 0; i < studentList.size(); i++) {
                _studentObj = new JSONObject();
                _studentObj.put("StudentID", studentList.get(i).getStudentID());
                _studentObj.put("StudentUID", studentList.get(i).getStudentUID());
                _studentObj.put("FirstName", studentList.get(i).getFirstName());
                _studentObj.put("MiddleName", studentList.get(i).getMiddleName());
                _studentObj.put("LastName", studentList.get(i).getLastName());
                _studentObj.put("FullName", studentList.get(i).getFullName());
                _studentObj.put("Gender", studentList.get(i).getGender());
                _studentObj.put("regDate", studentList.get(i).getRegDate());
                _studentObj.put("Age", studentList.get(i).getAge());
                _studentObj.put("villageName", studentList.get(i).getVillageName());
                _studentObj.put("newFlag", studentList.get(i).getNewFlag());
                _studentObj.put("isniosstudent", studentList.get(i).getIsniosstudent());
                _studentObj.put("DeviceId", Assessment_Utility.getDeviceId(context));
                studentData.put(_studentObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return studentData;
    }

    private JSONArray fillAttendanceData(List<Attendance> attendanceList) {
        JSONArray attendanceData = new JSONArray();
        JSONObject _obj;
        try {
            for (int i = 0; i < attendanceList.size(); i++) {
                _obj = new JSONObject();
                Attendance _attendance = attendanceList.get(i);
                _obj.put("attendanceID", _attendance.getAttendanceID());
                _obj.put("SessionID", _attendance.getSessionID());
                _obj.put("StudentID", _attendance.getStudentID());
                attendanceData.put(_obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return attendanceData;
    }

    private JSONArray fillScoreData(List<Score> scoreList) {
        JSONArray scoreData = new JSONArray();
        JSONObject _obj;
        try {
            for (int i = 0; i < scoreList.size(); i++) {
                _obj = new JSONObject();
                Score _score = scoreList.get(i);
//                _obj.put("ScoreId", _score.getScoreId());
                _obj.put("SessionID", _score.getSessionID());
                _obj.put("StudentID", _score.getStudentID());
                _obj.put("DeviceID", Assessment_Utility.getDeviceId(context));
                _obj.put("ResourceID", _score.getResourceID());
                _obj.put("QuestionId", _score.getQuestionId());
                _obj.put("ScoredMarks", _score.getScoredMarks());
                _obj.put("TotalMarks", _score.getTotalMarks());
                _obj.put("StartDateTime", _score.getStartDateTime());
                _obj.put("EndDateTime", _score.getEndDateTime());
                _obj.put("Level", _score.getLevel());
                _obj.put("Label", _score.getLabel());
//                _obj.put("isAttempted", _score.getIsAttempted());
//                _obj.put("isCorrect", _score.getIsCorrect());
                _obj.put("userAnswer", _score.getUserAnswer());
//                _obj.put("examId", _score.getExamId());
                scoreData.put(_obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return scoreData;
    }

    private JSONArray fillAssessmentScoreData(List<AssessmentPaperForPush> paperList) {
        JSONArray paperData = new JSONArray();
        JSONObject _obj_paper = null;
        JSONArray scoreData = new JSONArray();
        pushCnt = paperList.size();
        JSONObject _obj_score;
        try {
            for (int p = 0; p < paperList.size(); p++) {
                _obj_paper = new JSONObject();
                AssessmentPaperForPush _paper = paperList.get(p);
                List<Score> scoreList = AppDatabase.getDatabaseInstance(context).getScoreDao().getAllNewScores(paperList.get(p).getPaperId(), paperList.get(p).getSessionID());
                if (scoreList.size() > 0) {
                    _obj_paper.put("languageId", _paper.getLanguageId());
                    _obj_paper.put("subjectId", _paper.getSubjectId());
                    _obj_paper.put("examId", _paper.getExamId());
                    _obj_paper.put("paperId", _paper.getPaperId());
                    _obj_paper.put("paperStartTime", _paper.getPaperStartTime());
                    _obj_paper.put("paperEndTime", _paper.getPaperEndTime());
                    _obj_paper.put("totalMarks", _paper.getOutOfMarks());
                    _obj_paper.put("scoredMarks", _paper.getTotalMarks());
                    _obj_paper.put("studentId", _paper.getStudentId());
                    _obj_paper.put("SessionID", _paper.getSessionID());
                    _obj_paper.put("question1Rating", _paper.getQuestion1Rating());
                    _obj_paper.put("question2Rating", _paper.getQuestion2Rating());
                    _obj_paper.put("question3Rating", _paper.getQuestion3Rating());
                    _obj_paper.put("question4Rating", _paper.getQuestion4Rating());
                    _obj_paper.put("question5Rating", _paper.getQuestion5Rating());
                    _obj_paper.put("question6Rating", _paper.getQuestion6Rating());
                    _obj_paper.put("question7Rating", _paper.getQuestion7Rating());
                    _obj_paper.put("question8Rating", _paper.getQuestion8Rating());
                    _obj_paper.put("question9Rating", _paper.getQuestion9Rating());
                    _obj_paper.put("question10Rating", _paper.getQuestion10Rating());
                    _obj_paper.put("isniosstudent", _paper.getIsniosstudent());
//                    DownloadMedia video = AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByTypeAndPaperId(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING, _paper.getPaperId());

                    /*  DownloadMedia video = new DownloadMedia();
                    video.setPaperId(_paper.getPaperId());*/
//                    video.setPhotoUrl(Environment.getExternalStorageDirectory() + "/.Assessment/Content/videoMonitoring/" + _paper.getPaperId() + ".mp4");
//                    video.setPhotoUrl(AssessmentApplication.assessPath + Assessment_Constants.STORE_VIDEO_MONITORING_PATH + _paper.getPaperId() + ".mp4");
               /*     if (video != null)
                        videoRecordingList.add(video);*/
                    scoreData = new JSONArray();
                    for (int i = 0; i < scoreList.size(); i++) {
                        _obj_score = new JSONObject();
                        Score _score = scoreList.get(i);
//                _obj.put("ScoreId", _score.getScoreId());
                        _obj_score.put("SessionID", _score.getSessionID());
                        _obj_score.put("StudentID", _score.getStudentID());
                        _obj_score.put("DeviceID", Assessment_Utility.getDeviceId(context));
                        _obj_score.put("ResourceID", _score.getResourceID());
                        _obj_score.put("QuestionId", _score.getQuestionId());
                        _obj_score.put("ScoredMarks", _score.getScoredMarks());
                        _obj_score.put("TotalMarks", _score.getTotalMarks());
                        _obj_score.put("StartDateTime", _score.getStartDateTime());
                        _obj_score.put("EndDateTime", _score.getEndDateTime());
                        _obj_score.put("questionLevel", _score.getLevel());
                        _obj_score.put("questionLabel", _score.getLabel());
                        _obj_score.put("isAttempted", _score.getIsAttempted());
                        _obj_score.put("isCorrect", _score.getIsCorrect());
                        _obj_score.put("userAnswer", _score.getUserAnswer());
                        _obj_score.put("paperId", _score.getPaperId());
//                        downloadMediaList.addAll(AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().getMediaByQidAndPaperId(_score.getQuestionId() + "", _score.getPaperId()));
//                    _obj_score.put("examId", _score.getExamId());
                        scoreData.put(_obj_score);
                    }
                }
                _obj_paper.put("assessmentScoreData", scoreData);
                paperData.put(_obj_paper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return paperData;
    }

    private JSONArray fillSupervisorData(List<SupervisorData> supervisorDataList) {
        JSONArray supervisorData = new JSONArray();
        JSONObject _supervisorDataObj;
        try {
            for (int i = 0; i < supervisorDataList.size(); i++) {
                _supervisorDataObj = new JSONObject();
                SupervisorData supervisorDataTemp = supervisorDataList.get(i);
                _supervisorDataObj.put("sId", supervisorDataTemp.getsId());
                _supervisorDataObj.put("assessmentSessionId", supervisorDataTemp.getAssessmentSessionId());
                _supervisorDataObj.put("supervisorId", supervisorDataTemp.getSupervisorId());
                _supervisorDataObj.put("supervisorName", supervisorDataTemp.getSupervisorName());
                _supervisorDataObj.put("supervisorPhoto", supervisorDataTemp.getSupervisorPhoto());
                supervisorData.put(_supervisorDataObj);
//                DownloadMedia downloadMedia = new DownloadMedia();
//                downloadMedia.setPaperId(supervisorDataTemp.getSupervisorId());
//                downloadMedia.setQtId(supervisorDataTemp.getAssessmentSessionId());
//                downloadMedia.setqId(supervisorDataTemp.getSupervisorName());
//                String fileName = AssessmentApplication.assessPath + Assessment_Constants.STORE_SUPERVISOR_IMAGE_PATH + "/" + supervisorDataTemp.getSupervisorPhoto();
//                downloadMedia.setPhotoUrl(fileName);
//                downloadMedia.setMediaType("supervisorData");
//                supervisorMediaList.add(downloadMedia);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return supervisorData;
    }

    private JSONArray fillLogsData(List<Modal_Log> logsList) {
        JSONArray logsData = new JSONArray();
        JSONObject _logsObj;
        try {
            for (int i = 0; i < logsList.size(); i++) {
                _logsObj = new JSONObject();
                Modal_Log modal_log = logsList.get(i);
                _logsObj.put("logId", modal_log.getLogId());
                _logsObj.put("deviceId", modal_log.getDeviceId());
                _logsObj.put("currentDateTime", modal_log.getCurrentDateTime());
                _logsObj.put("errorType", modal_log.getErrorType());
                _logsObj.put("exceptionMessage", modal_log.getExceptionMessage());
                _logsObj.put("exceptionStackTrace", modal_log.getExceptionStackTrace());
                _logsObj.put("groupId", modal_log.getGroupId());
                _logsObj.put("LogDetail", modal_log.getLogDetail());
                _logsObj.put("methodName", modal_log.getMethodName());

                logsData.put(_logsObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return logsData;
    }

    private JSONArray fillAssessmentData(List<Assessment> assessmentList) {
        JSONArray assessmentData = new JSONArray();
        JSONObject _assessmentobj;
        try {
            for (int i = 0; i < assessmentList.size(); i++) {
                _assessmentobj = new JSONObject();
                Assessment _Assessment = assessmentList.get(i);
                _assessmentobj.put("DeviceIDa", Assessment_Utility.getDeviceId(context));
                _assessmentobj.put("EndDateTimea", _Assessment.getEndDateTime());
                _assessmentobj.put("Labela", _Assessment.getLabel());
                _assessmentobj.put("Levela", _Assessment.getLevela());
                _assessmentobj.put("QuestionIda", _Assessment.getQuestionIda());
                _assessmentobj.put("ResourceIDa", _Assessment.getResourceIDa());
                _assessmentobj.put("ScoredMarksa", _Assessment.getScoredMarksa());
                _assessmentobj.put("ScoreIda", _Assessment.getScoreIda());
                _assessmentobj.put("SessionIDa", _Assessment.getSessionIDa());
                _assessmentobj.put("SessionIDm", _Assessment.getSessionIDm());
                _assessmentobj.put("StartDateTimea", _Assessment.getStartDateTimea());
                _assessmentobj.put("StudentIDa", _Assessment.getStudentIDa());
                _assessmentobj.put("TotalMarksa", _Assessment.getTotalMarksa());


                assessmentData.put(_assessmentobj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return assessmentData;
    }

    private JSONArray fillGroupsData(List<Groups> groupsList) {
        JSONArray groupsData = new JSONArray();
        JSONObject _groupsObj;
        try {
            for (int i = 0; i < groupsList.size(); i++) {
                _groupsObj = new JSONObject();
                Groups group = groupsList.get(i);
                _groupsObj.put("GroupId", group.getGroupId());
                _groupsObj.put("DeviceId", group.getDeviceId());
                _groupsObj.put("GroupCode", group.getGroupCode());
                _groupsObj.put("GroupName", group.getGroupName());
                _groupsObj.put("ProgramId", group.getProgramId());
                _groupsObj.put("SchoolName", group.getSchoolName());
                _groupsObj.put("VillageId", group.getVillageId());
                _groupsObj.put("VIllageName", group.getVIllageName());

                groupsData.put(_groupsObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return groupsData;
    }

   /* private void pushDataToServer(final Context context, JSONObject requestJsonObject, String url) {
        try {
//            JSONObject jsonArrayData = new JSONObject(data);

            AndroidNetworking.post(url)
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(requestJsonObject)
                    .build()
                    .getAsString(new StringRequestListener() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("PUSH_STATUS", "Data pushed successfully");
                            dataPushed = true;
                            if (!autoPush) {
                               *//* new AlertDialog.Builder(context)
                                        .setMessage("Data pushed successfully")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                ((MainActivity) context).onResponseGet();
                                            }
                                        }).create().show();*//*
                            }
//                            setPushFlag();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d("PUSH_STATUS", "Data push failed");
                            dataPushed = false;
                           *//* if (!autoPush) {
                                new AlertDialog.Builder(context)
                                        .setMessage("Data push failed")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                ((MainActivity) context).onResponseGet();
                                            }
                                        }).create().show();
                            }*//*
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    private void pushDataScienceToServer(final Context context, JSONObject requestJsonObject, String url) {
        try {
//            JSONObject jsonArrayData = new JSONObject(data);
            AndroidNetworking.post(url)
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(requestJsonObject)
                    .build()
                    .getAsString(new StringRequestListener() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("PUSH_STATUS", "Data pushed successfully");
                            Drawable icon = context.getResources().getDrawable(R.drawable.ic_check);
                            pushSupervisorImages();
                            createMediaFileToPush();
                            CreateFilesForVideoMonitoring();
                            //todo push on main thread
                            dataPushed = true;

                            if (!autoPush) {
//                                String msg = "Data pushed successfully. " + pushCnt + " paper(s) pushed.";
                              /*  if (!dataPushed) {
                                    icon = context.getResources().getDrawable(R.drawable.ic_warning);
                                    msg = "Science data pushed successfully. ECE data push failed";
                                }*/
                                if (isTablet) {
                                   /* alertDialog = new AlertDialog.Builder(context)
                                            .setMessage(msg)
                                            .setCancelable(false)
                                            .setIcon(icon)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    ((MainActivity) context).onResponseGet();
                                                }
                                            });
                                    alertDialog.create().show();*/
                                }
//                                onPostExecute();
                            }

                            setPushFlag();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d("PUSH_STATUS", "Science Data push failed");
                            dataPushed = false;
                            if (!autoPush) {
                                String msg = "Data push failed";
                                if (dataPushed) {
                                    msg = "Ece data pushed successfully.Science data push failed.";
                                }
                                if (isTablet) {
                                   /* alertDialog = new AlertDialog.Builder(context)
                                            .setMessage(msg)
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    ((MainActivity) context).onResponseGet();
                                                }
                                            });
                                    alertDialog.create().show();*/
                                }
                                onPostExecute();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getAuthHeader() {
        String encoded = Base64.encodeToString(("pratham" + ":" + "pratham").getBytes(), Base64.NO_WRAP);
        return "Basic " + encoded;
    }

    public void pushDataToRaspberry(/*final String requestType, */String url, String data,
                                                                  String filter_name, String table_name) {
        AndroidNetworking.post(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", getAuthHeader())
                .addBodyParameter("filter_name", filter_name)
                .addBodyParameter("table_name", table_name)
                .addBodyParameter("facility", FastSave.getInstance().getString(Assessment_Constants.FACILITY_ID, ""))
                .addBodyParameter("data", data)
                .setExecutor(Executors.newSingleThreadExecutor())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        dataPushed = true;

                        if (!autoPush) {
                        }
                        onPostExecute();
                        setPushFlag();
                        BackupDatabase.backup(AssessmentApplication.getInstance());
                    }

                    @Override
                    public void onError(ANError anError) {
                        dataPushed = false;
                        if (!autoPush) {
                           /* alertDialog = new AlertDialog.Builder(context)
                                    .setMessage("Data push failed")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            ((MainActivity) context).onResponseGet();

                                        }
                                    });
                            alertDialog.create().show();*/
                        }
                        onPostExecute();
                        Log.d("Error::", anError.getErrorDetail());
                        Log.d("Error::", anError.getMessage());
                        Log.d("Error::", anError.getResponse().toString());
                    }
                });
    }

    /*@UiThread
    private void showDialog() {
        alertDialog = new AlertDialog.Builder(context)
                .setMessage("Data pushed Successfully")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((MainActivity) context).onResponseGet();

                    }
                });
        alertDialog.create().show();
    }*/


    private void setMediaPushFlag(String type) {
        if (type.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_SUPERVISOR)) {
            int cnt = AppDatabase.getDatabaseInstance(context).getSupervisorDataDao().setSentFlag();
            int sCnt = AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().setSentFlag(DOWNLOAD_MEDIA_TYPE_SUPERVISOR);
            supervisorImagesPushed = true;
            supervisorCnt += sCnt;
        } else {
            if (type.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING)) {
                int vmCnt = AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().setSentFlag(DOWNLOAD_MEDIA_TYPE_VIDEO_MONITORING);
                videoMonImagesPushed = true;
                videoMonCnt += vmCnt;
                onPostExecute();
            } else if (type.equalsIgnoreCase(DOWNLOAD_MEDIA_TYPE_ANSWER_MEDIA)) {
                answerMediaPushed = true;
                int audioCnt = AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().setSentFlag(DOWNLOAD_MEDIA_TYPE_ANSWER_AUDIO);
                int imgCnt = AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().setSentFlag(DOWNLOAD_MEDIA_TYPE_ANSWER_IMAGE);
                int videoCnt = AppDatabase.getDatabaseInstance(context).getDownloadMediaDao().setSentFlag(DOWNLOAD_MEDIA_TYPE_ANSWER_VIDEO);
                answerMediaCnt += audioCnt + imgCnt + videoCnt;
            }
        }
        BackupDatabase.backup(context);
    }


    private void setPushFlag() {
        AppDatabase.getDatabaseInstance(context).getLogsDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getSessionDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getAttendanceDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getScoreDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getAssessmentDao().setSentFlag();
        if (!isTablet)
            AppDatabase.getDatabaseInstance(context).getStudentDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getAssessmentPaperForPushDao().setSentFlag();
//        AppDatabase.getDatabaseInstance(context).getLearntWordDao().setSentFlag();
        BackupDatabase.backup(context);

    }

    @UiThread
    protected void onPostExecute() {
        // super.onPostExecute(o);
        try {
            ok_btn.setVisibility(View.VISIBLE);
            if (!AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                if (isTablet || !autoPush)
                    txt_push_dialog_msg.setText(R.string.no_internet_connection);
//                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            } else {
                if (!dataPushed) {
                    push_lottie.setAnimation("error_cross.json");
                    push_lottie.playAnimation();
                    txt_push_dialog_msg.setText(R.string.data_push_failed);
                } else if (isTablet || !autoPush) {
                    push_lottie.setAnimation("success.json");
                    push_lottie.playAnimation();
                    String msg1 = "", msg2 = "";
                    msg1 = context.getString(R.string.papers_pushed) + pushCnt;
//                    if (answerMediaPushed && supervisorImagesPushed && videoMonImagesPushed) {
                    mediaCnt = supervisorCnt + answerMediaCnt + videoMonCnt;
                    int totalMediaCnt = totalSupervisorCnt + totalAnswerMediaCnt + totalVideoMonCnt;
                    msg2 = context.getString(R.string.media_pushed) + mediaCnt + "/" + totalMediaCnt;
                    txt_push_dialog_msg.setText(msg1);
                    txt_push_cnt.setVisibility(View.VISIBLE);
                    txt_push_cnt.setText(msg2);

//                    } else {
//                        msg = "Data push failed.";
//                    }
/*                    alertDialog = new AlertDialog.Builder(context)
                            .setMessage(msg)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (context instanceof MainActivity)
                                        ((MainActivity) context).onResponseGet();
                                    if (context instanceof ScienceAssessmentActivity)
                                        ((ScienceAssessmentActivity) context).onResponseGet();

                                }
                            });
                    alertDialog.create().show();*/
                }
            }
           /* if (progressDialog.isShowing())
                progressDialog.dismiss();*/
            PUSH_DATA_FROM_DRAWER = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
