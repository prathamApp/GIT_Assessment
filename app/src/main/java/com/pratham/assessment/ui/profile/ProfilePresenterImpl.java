package com.pratham.assessment.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.utilities.Assessment_Constants;
import java.util.List;

public class ProfilePresenterImpl implements ProfileContract.profilePresenter {
    Context context;
    ProfileContract.profileView profileView;
    int totalWords;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String MyPREFERENCES = "MyPrefs";
    String LeaderboardData = "LeaderboardData";
    Gson gson = new Gson();


    public ProfilePresenterImpl(Context context) {
        this.context = context;
        profileView = (ProfileContract.profileView) context;
        pref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /* @Override
     public void displayProfileNameAndImage() {
          }
 */
    @Override
    public String displayProfileName() {
        String profileName;
//        if (!Assessment_Constants.GROUP_LOGIN)
            profileName = AppDatabase.getDatabaseInstance(context).getStudentDao().getFullName(Assessment_Constants.currentStudentID);
//        else
//            profileName = AppDatabase.getDatabaseInstance(context).getGroupsDao().getGroupNameByGrpID(Assessment_Constants.currentStudentID);
        return profileName;
    }

    @Override
    public String displayProfileImage() {
        String sImage;
//        if (!Assessment_Constants.GROUP_LOGIN)
            sImage = AppDatabase.getDatabaseInstance(context).getStudentDao().getStudentAvatar(Assessment_Constants.currentStudentID);
/*        else
            sImage = "group_icon";*/
        return sImage;
    }

    @Override
    public List<Assessment> getCertificates(String currentStudentID, String certificateLbl) {
        return AppDatabase.getDatabaseInstance(context).getAssessmentDao().getCertificates(currentStudentID, certificateLbl);
    }

    @Override
    public String getStudentName(String sId) {
        return AppDatabase.getDatabaseInstance(context).getStudentDao().getFullName(sId);
    }

    @Override
    public boolean checkConnectivity() {
        boolean isConnectedToInternet;
        if (AssessmentApplication.wiseF.isDeviceConnectedToMobileNetwork()) {
            isConnectedToInternet = true;
        } else if (AssessmentApplication.wiseF.isDeviceConnectedToWifiNetwork()) {
            if (AssessmentApplication.wiseF.isDeviceConnectedToSSID(Assessment_Constants.PRATHAM_KOLIBRI_HOTSPOT)) {
                isConnectedToInternet = false;
            } else {
                isConnectedToInternet = true;
            }
        } else {
            isConnectedToInternet = false;
        }
        return isConnectedToInternet;
    }




/*

    @Override
    public void callTranslateApi(String textToBeTranslated, String languageCode) {
*/
/*
        if (checkConnectivity()) {
            String yandexKey = "trnsl.1.1.20180113T102250Z.941f2a23f7d88084.0e9f4357f3500975fac5d2cbb11c2a946f72faa1";
            String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + yandexKey
                    + "&text=" + textToBeTranslated + "&lang=" + languageCode;
            final String[] resultString = {""};

            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Loading... Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            AndroidNetworking.get(yandexUrl)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            String string = null;
                            try {
                                progressDialog.dismiss();
                                string = response.getString("text");
                                resultString[0] = string.substring(string.indexOf('[') + 2, string.indexOf(']') - 1);
                                profileView.setTranslatedWord(resultString[0]);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                        }
                    });
        } else Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
*//*


        //  return translatedWord;
    }

    @Override
    public List<LearntWords> getLearntWords(String currentStudentID) {
        return AppDatabase.getDatabaseInstance(context).getLearntWordDao().getLearntWords(currentStudentID);
    }

    @Override
    public List<LearntWords> getLearntSentences(String currentStudentID) {
        return AppDatabase.getDatabaseInstance(context).getLearntWordDao().getLearntSentences(currentStudentID);
    }

    private void sortListReverse(List<Leaderboard> list) {
        Collections.sort(list, new Comparator<Leaderboard>() {
            public int compare(Leaderboard ideaVal1, Leaderboard ideaVal2) {
                // avoiding NullPointerException in case name is null
                Integer idea1 = new Integer(ideaVal1.getScoredMarks());
                Integer idea2 = new Integer(ideaVal2.getScoredMarks());
                return idea2.compareTo(idea1);
            }
        });
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
                _obj.put("DeviceId", _score.getDeviceID());
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

    @Override
    public void displaySentenceProgress() {
        int totalEntries = AppDatabase.getDatabaseInstance(context).getEnglishWordDao().getTotalEntries();
        int totalSentence = AppDatabase.getDatabaseInstance(context).getEnglishWordDao().getSentenceCount();
        totalWords = totalEntries - totalSentence;

        int learntSentence = AppDatabase.getDatabaseInstance(context).getLearntWordDao().getLearntSentenceCount(Assessment_Constants.currentStudentID);
        float sentenceProgress;
        if (totalSentence == 0) {
            sentenceProgress = 0.0f;
        } else {
            sentenceProgress = ((float) learntSentence / (float) totalSentence) * 100;
        }

        profileView.displaySentenceProgress(sentenceProgress, learntSentence, totalEntries, totalSentence);
    }

    @Override
    public void displayWordProgress() {
        int learntWords = AppDatabase.getDatabaseInstance(context).getLearntWordDao().getLearntWordCount(Assessment_Constants.currentStudentID);
        float wordsProgress;
        if (totalWords == 0) {
            wordsProgress = 0.0f;
        } else {
            wordsProgress = ((float) learntWords / (float) totalWords) * 100;
        }
        profileView.displayWordProgress(wordsProgress, learntWords);
    }

    @Override
    public int getRCHighScore(String currentStudentID) {
        return AppDatabase.getDatabaseInstance(context).getScoreDao().getRCHighScore(currentStudentID);

    }

    @Override
    public void parseLeaderboardJson(String data) {
        JSONObject obj = null;
        List<Leaderboard> leaderboardList = new ArrayList<>();

        try {
            obj = new JSONObject(data);

            int profileRank = obj.getInt("Rank");
            // leaderboard data
            JSONArray leadArray = obj.getJSONArray("LeaderList");
            Type listType = new TypeToken<ArrayList<Leaderboard>>() {
            }.getType();
            leaderboardList = gson.fromJson(leadArray.toString(), listType);
            sortListReverse(leaderboardList);
            profileView.setLeaderBoardListToAdapter(leaderboardList);
            int HighScore = AppDatabase.getDatabaseInstance(context).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
            int Rank = profileRank;
            profileView.displayRCHighScore(Rank, HighScore);
        } catch (JSONException e) {
            e.printStackTrace();
            profileView.showException();
            int HighScore = AppDatabase.getDatabaseInstance(context).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
            int Rank = 0;
            profileView.displayRCHighScore(Rank, HighScore);
        }
    }

    @Override
    public String getDataFromPref(String leaderboardData, String value) {
        return pref.getString(leaderboardData, "Empty");

    }

    @Override
    public void pushData(String stdID, String profileName) {
        try {
            // Push Data
//            StdID = Assessment_Constants.currentStudentID;
            List<Score> scoreList = AppDatabase.getDatabaseInstance(context).getScoreDao().getScoreByStdID(stdID);

            JSONArray scoreData = new JSONArray();
            scoreData = fillScoreData(scoreList);

            JSONObject PushObj = new JSONObject();
            PushObj.put("scoreData", scoreData);
            PushObj.put("studentId", stdID);
            PushObj.put("studentName", profileName);


            // Push Data
            profileView.changeVisibility(false);

            AndroidNetworking.post(APIs.highScoreUrl)
                    .addHeaders("Content-Type", "application/json")
                    .addJSONObjectBody(PushObj)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            // Use response as source of Leaderboard
                            //Toast.makeText(ProfileActivity.this, "Data pushed successfully", Toast.LENGTH_SHORT).show();
                            profileView.changeVisibility(true);


                            // Show Leaderboard
                            // Show Data on Leaderboard
                            try {
                                // save list (Response) as string in shared pref for showing it offline
                                editor.putString(LeaderboardData, response);
                                editor.apply();

                                // Parsing Json
                                // student's rank
                                JSONObject obj = new JSONObject(response);
                                int profileRank = obj.getInt("Rank");
                                // leaderboard data
                                JSONArray leadArray = obj.getJSONArray("LeaderList");
                                Type listType = new TypeToken<ArrayList<Leaderboard>>() {
                                }.getType();
                                List<Leaderboard> leaderboardList = gson.fromJson(leadArray.toString(), listType);

                                // Sort List & then show
                                sortListReverse(leaderboardList);

                                // Adapter
                                profileView.setLeaderBoardListToAdapter(leaderboardList);
                                */
/*ProfileActivity.CustomAdapter adapter = new ProfileActivity.CustomAdapter(ProfileActivity.this, leaderboardList);
                                lv_Leaderboard.setAdapter(adapter);*//*


                                // Set Rank & Score
                                int HighScore = AppDatabase.getDatabaseInstance(context).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                                int Rank = profileRank;
                                profileView.displayRCHighScore(Rank, HighScore);
                            } catch (JSONException e) {
                                profileView.setNoData();
                               */
/* final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
                                lv_Leaderboard.setAdapter(adapter);*//*

                                // Set Rank & Score
                                int HighScore = AppDatabase.getDatabaseInstance(context).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                                int Rank = 0;
                                profileView.displayRCHighScore(Rank, HighScore);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            //Toast.makeText(ProfileActivity.this, "Data push failed", Toast.LENGTH_SHORT).show();
                            profileView.changeVisibility(true);
                            profileView.setNoData();
                            */
/*final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
                            lv_Leaderboard.setAdapter(adapter);*//*

                            int HighScore = AppDatabase.getDatabaseInstance(context).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                            int Rank = 0;
                            profileView.displayRCHighScore(Rank, HighScore);

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

}
