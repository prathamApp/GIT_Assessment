package com.pratham.assessment.admin_pannel.PushOrAssign;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.Assessment_Constants;
import com.pratham.assessment.Assessment_Utility;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.domain.Attendance;
import com.pratham.assessment.domain.Crl;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.Modal_Log;
import com.pratham.assessment.domain.Modal_RaspFacility;
import com.pratham.assessment.domain.Score;
import com.pratham.assessment.domain.Session;
import com.pratham.assessment.domain.Student;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Anki on 12/10/2018.
 */

public class PushDataPresenterImpl implements PushDataContract.PushDataPresenter {
    Context context;
    PushDataContract.PushDataView view;
    JSONObject rootJson;

    public PushDataPresenterImpl(Context context) {
        this.context = context;
        this.view = (PushDataContract.PushDataView) context;
    }

    @Override
    public void createJsonForTransfer() {
        try {
            new AsyncTask<Object, Void, Object>() {

                JSONArray scoreData = new JSONArray(),
                        attendanceData = new JSONArray(),
                        studentData = new JSONArray(),
                        crlData = new JSONArray(),
                //statusData = new JSONArray(),
                sessionData = new JSONArray(),
                        learntWords = new JSONArray(),
                        supervisorData = new JSONArray(),
                        groupsData = new JSONArray(),
                        assessmentData = new JSONArray(),
                        logsData = new JSONArray();

                @Override
                protected Object doInBackground(Object... objects) {
                    List<Score> scoreList = AppDatabase.getDatabaseInstance(context).getScoreDao().getAllPushScores();
                    scoreData = fillScoreData(scoreList);
                    List<Attendance> attendanceList = AppDatabase.getDatabaseInstance(context).getAttendanceDao().getAllPushAttendanceEntries();
                    attendanceData = fillAttendanceData(attendanceList);
                    List<Student> studentList = AppDatabase.getDatabaseInstance(context).getStudentDao().getAllStudents();
                    studentData = fillStudentData(studentList);
                    List<Crl> crlList = AppDatabase.getDatabaseInstance(context).getCrlDao().getAllCrls();
                    crlData = fillCrlData(crlList);
                    /*List<Status> statusList = AppDatabase.getDatabaseInstance(context).getStatusDao().getAllStatuses();
                    statusData = fillStatusData(statusList);*/
                    List<Session> sessionList = AppDatabase.getDatabaseInstance(context).getSessionDao().getAllNewSessions();
                    sessionData = fillSessionData(sessionList);
                   /* List<LearntWords> learntWordsList = AppDatabase.getDatabaseInstance(context).getLearntWordDao().getAllData();
                    learntWords = fillLearntWordsData(learntWordsList);
                    List<SupervisorData> supervisorDataList = AppDatabase.getDatabaseInstance(context).getSupervisorDataDao().getAllSupervisorData();
                    supervisorData = fillSupervisorData(supervisorDataList);
                  */  List<Modal_Log> logsList = AppDatabase.getDatabaseInstance(context).getLogsDao().getPushAllLogs();
                    logsData = fillLogsData(logsList);
                    List<Assessment> assessmentList = AppDatabase.getDatabaseInstance(context).getAssessmentDao().getAllAssessment();
                    assessmentData = fillAssessmentData(assessmentList);
                    List<Groups> groupsList = AppDatabase.getDatabaseInstance(context).getGroupsDao().getAllGroups();
                    groupsData = fillGroupsData(groupsList);


                    try {
                        String programID = "";
                        rootJson = new JSONObject();
                        Gson gson = new Gson();
                        //iterate through all new sessions
                        JSONArray sessionArray = new JSONArray();
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
                       /* sessionJson.put(PD_Constant.SCORE, scoreArray);
                        sessionJson.put(PD_Constant.ATTENDANCE, attendanceArray);
                        sessionJson.put(PD_Constant.LOGS, logArray);*/


                        /*_sessionObj = new JSONObject();
                        _sessionObj.put("SessionID", sessionList.get(i).getSessionID());
                        _sessionObj.put("fromDate", sessionList.get(i).getFromDate());
                        _sessionObj.put("toDate", sessionList.get(i).getToDate());
                        newSessionsData.put(_sessionObj);*/
                            sessionArray.put(sessionJson);


                            JSONArray studentArray = new JSONArray();
                            if (!AssessmentApplication.isTablet) {
                                List<Student> newStudents = AppDatabase.getDatabaseInstance(context).getStudentDao().getAllNewStudents();
                                for (Student std : newStudents)
                                    studentArray.put(new JSONObject(gson.toJson(std)));
                            }

                            JSONObject metadataJson = new JSONObject();
                            List<com.pratham.assessment.domain.Status> metadata = AppDatabase.getDatabaseInstance(context).getStatusDao().getAllStatuses();
                            for (com.pratham.assessment.domain.Status status : metadata) {
                                metadataJson.put(status.getStatusKey(), status.getValue());
                                if (status.getStatusKey().equalsIgnoreCase("programId"))
                                    programID = status.getValue();
                            }
                            metadataJson.put(Assessment_Constants.SCORE_COUNT, (metadata.size() > 0) ? metadata.size() : 0);
                            if (!AssessmentApplication.isTablet)
                                rootJson.put(Assessment_Constants.STUDENTS, studentArray);
                            rootJson.put(Assessment_Constants.SESSION, sessionArray);
                            rootJson.put(Assessment_Constants.METADATA, metadataJson);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    // String requestString = generateRequestString(scoreData, attendanceData, studentData, crlData, sessionData, learntWords, supervisorData, logsData, assessmentData, groupsData);
                    //transferFileName = "COS_Usage:" + AssessmentApplication.getUniqueID().toString();

                    if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                        JSONObject object = new JSONObject();
                        try {
                            String requestString = generateRequestString(scoreData, attendanceData, sessionData, learntWords, supervisorData, logsData, assessmentData,studentData);

                            object.put("username", "pratham");
                            object.put("password", "pratham");
                            if (checkEmptyness(requestString))
                                getFacilityIdfromRaspberry(Assessment_Constants.FACILITY_ID, Assessment_Constants.RASP_IP + "/api/session/", object,requestString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (AssessmentApplication.wiseF.isDeviceConnectedToMobileNetwork() || AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
                        if (AssessmentApplication.isTablet) {
                            String requestString = generateRequestString(scoreData, attendanceData, sessionData, learntWords, supervisorData, logsData, assessmentData,studentData);

                            if (checkEmptyness(requestString))
                                pushDataToServer(requestString, AssessmentApplication.uploadDataUrl);
                        }
                    }
                    //WriteSettings(context, requestString, transferFileName);
                    /*if(!currentPush)
                        adminConsoleView.transferFile(transferFileName);*/
                    super.onPostExecute(o);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushDataToServer(String data, String url) {
        try {
            JSONObject jsonArrayData = new JSONObject(data);
            //final String entryId = data.get(0).getAsJsonObject().get("EntryId").getAsString();

            final ProgressDialog dialog = new ProgressDialog(context);
            Assessment_Utility.showDialogInApiCalling(dialog, context, "Uploading Data..");

            AndroidNetworking.post(url)
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(jsonArrayData)
                    .addBodyParameter("facility", Assessment_Constants.FACILITY_ID)//FastSave.getInstance().getString(Assessment_Constants.FACILITY_ID, ""))
                    .build()
                    .getAsString(new StringRequestListener() {

                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Data pushed successfully", Toast.LENGTH_SHORT).show();
                            Assessment_Utility.dismissDialog(dialog);
                            view.finishActivity();
                            setPushFlag();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(context, "Data push failed", Toast.LENGTH_SHORT).show();
                            Assessment_Utility.dismissDialog(dialog);
                            view.finishActivity();

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean checkEmptyness(String requestString) {
        try {
            JSONObject jsonObject = new JSONObject(requestString);
            JSONObject jsonObjectSession = jsonObject.getJSONObject("session");

            if (jsonObjectSession.getJSONArray("scoreData").length() > 0 ||
                    jsonObjectSession.getJSONArray("attendanceData").length() > 0 ||
                    jsonObjectSession.getJSONArray("sessionsData").length() > 0 ||
                    jsonObjectSession.getJSONArray("learntWordsData").length() > 0 ||
                    jsonObjectSession.getJSONArray("logsData").length() > 0 ||
                    jsonObjectSession.getJSONArray("assessmentData").length() > 0 ||
                    jsonObjectSession.getJSONArray("supervisor").length() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("error : ","JSON Parsing error");
            return false;
        }
    }

    private void setPushFlag() {
        AppDatabase.getDatabaseInstance(context).getLogsDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getSessionDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getAttendanceDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getScoreDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getAssessmentDao().setSentFlag();
       /* AppDatabase.getDatabaseInstance(context).getSupervisorDataDao().setSentFlag();
        AppDatabase.getDatabaseInstance(context).getLearntWordDao().setSentFlag();*/

    }

    public void getFacilityIdfromRaspberry(final String requestType, String url, JSONObject data, final String requestString) {
        AndroidNetworking.post(url)
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(data)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        saveFacility(requestType, response);
                        pushDataToRasp(requestString);
                    }

                    @Override
                    public void onError(ANError anError) {
//                            contentPresenter.notifyError(requestType/*, null*/);
                        Log.d("Error::", anError.getErrorDetail());
                        Log.d("Error::", anError.getMessage());
                        Log.d("Error::", anError.getResponse().toString());
                    }
                });
    }

    private void saveFacility(String header, String response) {

        if (header.equalsIgnoreCase(Assessment_Constants.FACILITY_ID)) {
            Gson gson = new Gson();

            Modal_RaspFacility facility = gson.fromJson(response, Modal_RaspFacility.class);
            Assessment_Constants.FACILITY_ID = facility.getFacilityId();
        }
    }

    private void pushDataToRasp(String requestString) {
        pushDataToRaspServer(Assessment_Constants.USAGEDATA, Assessment_Constants.URL.DATASTORE_RASPBERY_URL.toString(),
                requestString, "", Assessment_Constants.USAGEDATA);
    }


    public void pushDataToRaspServer(final String requestType, String url, String data,
                                     String filter_name, String table_name) {
        try {
//            String stringData = data.toString();
            // JSONObject jsonArrayData = new JSONObject(data);
            //final String entryId = data.get(0).getAsJsonObject().get("EntryId").getAsString();

            final ProgressDialog dialog = new ProgressDialog(context);
            Assessment_Utility.showDialogInApiCalling(dialog, context, "Uploading Data..");

            AndroidNetworking.post(url)
                    .addHeaders("Content-Type", "application/json")
                    .addHeaders("Authorization", getAuthHeader("pratham", "pratham"))
                    .addBodyParameter("filter_name", filter_name)
                    .addBodyParameter("table_name", table_name)
                    .addBodyParameter("facility", Assessment_Constants.FACILITY_ID)
                    .addBodyParameter("data", data)
//                    .addBodyParameter("data", "{\"session\":[{\"sessionid\":\"6d08ee79-e449-49d3-94df-0b4c30e0f360\",\"fromDate\":\"21-01-2019 16:27:07\",\"toDate\":\"NA\",\"scores\":[],\"attendances\":[{\"AttendanceID\":3,\"Date\":\"21-01-2019 16:27:07\",\"GroupID\":\"SmartPhone\",\"Present\":0,\"SessionID\":\"6d08ee79-e449-49d3-94df-0b4c30e0f360\",\"StudentID\":\"429747b2-7f30-40e9-88b2-c9f53bbe444a\",\"sentFlag\":0}],\"logs\":[]},{\"sessionid\":\"7b4f939a-5c9e-49b9-8574-001edad5e1d4\",\"fromDate\":\"21-01-2019 16:32:01\",\"toDate\":\"NA\",\"scores\":[],\"attendances\":[{\"AttendanceID\":4,\"Date\":\"21-01-2019 16:32:01\",\"GroupID\":\"SmartPhone\",\"Present\":0,\"SessionID\":\"7b4f939a-5c9e-49b9-8574-001edad5e1d4\",\"StudentID\":\"429747b2-7f30-40e9-88b2-c9f53bbe444a\",\"sentFlag\":0}],\"logs\":[]}],\"metadata\":{\"CRLID\":\"default\",\"group1\":\"\",\"group2\":\"\",\"group3\":\"\",\"group4\":\"\",\"group5\":\"\",\"DeviceId\":\"e93177c79c82dc69\",\"DeviceName\":\"Lenovo TB3-710I\",\"ActivatedDate\":\"\",\"village\":\"\",\"ActivatedForGroups\":\"\",\"SerialID\":\"FMM7USW899999999\",\"gpsFixDuration\":\"\",\"prathamCode\":\"\",\"programId\":\"\",\"wifiMAC\":\"64:db:43:01:23:71\",\"Latitude\":\"18.5575767\",\"Longitude\":\"73.7779433\",\"GPSDateTime\":\"21-01-2019 18:32:32\",\"apkType\":\"Pratham Digital with New UI, Kolibri, New POS, Raspberry Pie, Tablet Apk\",\"appName\":\"Pratham Digital\",\"apkVersion\":\"2.7\",\"ScoreCount\":22}}")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {

                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Data pushed successfully", Toast.LENGTH_SHORT).show();
                            Assessment_Utility.dismissDialog(dialog);
                            view.finishActivity();
                            setPushFlag();
                        }

                        @Override
                        public void onError(ANError anError) {
                            Assessment_Utility.dismissDialog(dialog);
                            Toast.makeText(context, "Data push failed", Toast.LENGTH_SHORT).show();
                            view.finishActivity();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*  private String generateRequestString(JSONArray scoreData, JSONArray attendanceData, JSONArray newStudentData, JSONArray newCrlData, JSONArray sessionData, JSONArray learntWordsData, JSONArray supervisorData, JSONArray logsData, JSONArray assessmentData, JSONArray groupsData) {
          String requestString = "";
          try {
              JSONObject sessionObj = new JSONObject();
              JSONObject metaDataObj = new JSONObject();
              metaDataObj.put("ScoreCount", scoreData.length());
              metaDataObj.put("AttendanceCount", attendanceData.length());
              metaDataObj.put("NewStudentsCount", newStudentData.length());
              metaDataObj.put("NewCrlsCount", newCrlData.length());
              metaDataObj.put("SessionsCount", sessionData.length());
              metaDataObj.put("learntWordsCount", learntWordsData.length());
              metaDataObj.put("supervisorDataCount", supervisorData.length());
              metaDataObj.put("logsCount", logsData.length());
              metaDataObj.put("assessmentCount", assessmentData.length());
              metaDataObj.put("groupsCount", groupsData.length());
              metaDataObj.put("TransId", AssessmentApplication.getUniqueID());
              metaDataObj.put("SerialID", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("SerialID"));
              metaDataObj.put("AndroidID", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AndroidID"));
              metaDataObj.put("ApkVersion", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("ApkVersion"));
              metaDataObj.put("AppName", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AppName"));
              metaDataObj.put("WifiMAC", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("WifiMAC"));
              metaDataObj.put("DBVersion", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("DBVersion"));
              metaDataObj.put("DeviceID", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("DeviceID"));
              metaDataObj.put("AppLang", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AppLang"));
              metaDataObj.put("AppStartDateTime", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AppStartDateTime"));
              metaDataObj.put("MobileNumber", "0");

              sessionObj.put("scoreData", scoreData);
              sessionObj.put("attendanceData", attendanceData);
              sessionObj.put("newStudentsData", newStudentData);
              sessionObj.put("newCrlsData", newCrlData);
              sessionObj.put("sessionsData", sessionData);
              sessionObj.put("learntWordsData", learntWordsData);
              sessionObj.put("logsData", logsData);
              sessionObj.put("assessmentData", assessmentData);
              sessionObj.put("groupsData", groupsData);

              requestString = "{ \"metadata\": " + metaDataObj +
                      ", \"supervisor\": " + supervisorData +
                      ", \"sessionsData\": " + sessionObj + "}";


          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }
          return requestString;
      }
  */
    private String generateRequestString(JSONArray scoreData, JSONArray attendanceData, JSONArray sessionData, JSONArray learntWordsData, JSONArray supervisorData, JSONArray logsData, JSONArray assessmentData, JSONArray studentData) {
        String requestString = "";
        try {
            JSONObject sessionObj = new JSONObject();
            JSONObject metaDataObj = new JSONObject();
            metaDataObj.put("ScoreCount", scoreData.length());
           // metaDataObj.put("AttendanceCount", attendanceData.length());
          //  metaDataObj.put("SessionsCount", sessionData.length());
           // metaDataObj.put("learntWordsCount", learntWordsData.length());
        //    metaDataObj.put("supervisorDataCount", supervisorData.length());
          //  metaDataObj.put("logsCount", logsData.length());
        //    metaDataObj.put("assessmentCount", assessmentData.length());
        //    metaDataObj.put("TransId", AssessmentApplication.getUniqueID());
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
            /*metaDataObj.put("AndroidID", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AndroidID"));
            metaDataObj.put("DBVersion", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("DBVersion"));
            metaDataObj.put("AppLang", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AppLang"));
            metaDataObj.put("AppStartDateTime", AppDatabase.getDatabaseInstance(context).getStatusDao().getValue("AppStartDateTime"));
            metaDataObj.put("MobileNumber", "0");*/

            sessionObj.put("scoreData", scoreData);
            sessionObj.put("studentData", studentData);
            sessionObj.put("attendanceData", attendanceData);
            sessionObj.put("sessionsData", sessionData);
            sessionObj.put("learntWordsData", learntWordsData);
            sessionObj.put("logsData", logsData);
            sessionObj.put("assessmentData", assessmentData);
            sessionObj.put("supervisor", supervisorData);

            requestString = "{ \"session\": "  + sessionObj +
                    ", \"metadata\": " + metaDataObj +
                    "}";
/*
 requestString =  "{ \"session\": " + sessionObj +
                    ", \"metadata\": " + metaDataObj +
                    ", \"supervisor\": " + supervisorData +
                    "}";
*/


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return requestString;
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

   /* private JSONArray fillLearntWordsData(List<LearntWords> learntWordsList) {
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
                _studentObj.put("Gender", studentList.get(i).getGender());
                _studentObj.put("regDate", studentList.get(i).getRegDate());
                _studentObj.put("Age", studentList.get(i).getAge());
                _studentObj.put("villageName", studentList.get(i).getVillageName());
                _studentObj.put("newFlag", studentList.get(i).getNewFlag());
                _studentObj.put("DeviceId", studentList.get(i).getDeviceId());
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
                _obj.put("ScoreId", _score.getScoreId());
                _obj.put("SessionID", _score.getSessionID());
                _obj.put("StudentID", _score.getStudentID());
                _obj.put("DeviceID", _score.getDeviceID());
                _obj.put("ResourceID", _score.getResourceID());
                _obj.put("QuestionId", _score.getQuestionId());
                _obj.put("ScoredMarks", _score.getScoredMarks());
                _obj.put("TotalMarks", _score.getTotalMarks());
                _obj.put("StartDateTime", _score.getStartDateTime());
                _obj.put("EndDateTime", _score.getEndDateTime());
                _obj.put("Level", _score.getLevel());
                _obj.put("Label", _score.getLabel());
                scoreData.put(_obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return scoreData;
    }

  /*  private JSONArray fillSupervisorData(List<SupervisorData> supervisorDataList) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return supervisorData;
    }
*/
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
                _assessmentobj.put("DeviceIDa", _Assessment.getDeviceIDa());
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

   /* @Override
    public void pushRaspData() {
        pushTabletJsons(true);
    }*/

    private String getAuthHeader(String ID, String pass) {
        String encoded = Base64.encodeToString((ID + ":" + pass).getBytes(), Base64.NO_WRAP);
        String returnThis = "Basic " + encoded;
        return returnThis;
    }
}
