package com.pratham.assessment.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.gson.Gson;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.services.TTSService;
import com.pratham.assessment.ui.certificate.CertificateActivity;
import com.pratham.assessment.utilities.Assessment_Constants;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//https://android-arsenal.com/details/1/1130

public class ProfileActivity extends BaseActivity implements ProfileContract.profileView {

    @BindView(R.id.s_iv)
    ImageView s_iv;
    @BindView(R.id.tv_name)
    TextView tv_name;
/*    @BindView(R.id.tv_Word)
    TextView tv_Word;
    @BindView(R.id.tv_sentence)
    TextView tv_sentence;*/
    @BindView(R.id.tv_certificates)
    TextView tv_certificates;

/*    @BindView(R.id.word_progress)
    DonutProgress word_progress;
    @BindView(R.id.sentence_progress)
    DonutProgress sentence_progress;
    @BindView(R.id.conv_progress)
    DonutProgress conv_progress;
    @BindView(R.id.story_progress)
    DonutProgress story_progress;*/
    @BindView(R.id.certificate_progress)
    DonutProgress certificate_progress;

/*    @BindView(R.id.tv_MyRCS)
    TextView tv_MyRCS;*/
    @BindView(R.id.ll_Leaderboard)
    LinearLayout ll_Leaderboard;
    @BindView(R.id.tv_Leaderboard)
    TextView tv_Leaderboard;
    @BindView(R.id.lv_Leaderboard)
    ListView lv_Leaderboard;

    @BindView(R.id.progressView)
    LinearLayout progressView;

    ProfileContract.profilePresenter profilePresenter;


    boolean isConnectedToInternet = false;
    int totalEntries, totalSentence, totalWords;
    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    boolean CertificateGained = false, LeaderBoard = true, LearntWord = false, LearntSentence = false;
    Gson gson = new Gson();

    String MyPREFERENCES = "MyPrefs";
    String LeaderboardData = "LeaderboardData";
    SharedPreferences pref;
    public static Assessment assessmentProfile;
    SharedPreferences.Editor editor;
    String profileName = "";
    String sImage = "";
    String StdID = "";
    String translatedWord = "";
//    TranslationDialog translationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        editor = pref.edit();

        ttsService = new TTSService(AssessmentApplication.getInstance());
        profilePresenter = new ProfilePresenterImpl(this);
        isConnectedToInternet = profilePresenter.checkConnectivity();

        displayProfileNameAndImage();
        getCertificates();
//        profilePresenter.displaySentenceProgress();
//        profilePresenter.displayWordProgress();
//        displayLeaderBoard();
    }

    private void getCertificates() {
        // Display Word Progress
        List<Assessment> assessmentList = profilePresenter.getCertificates(Assessment_Constants.currentStudentID, Assessment_Constants.CERTIFICATE_LBL);
        // List<Assessment> assessmentList = AppDatabase.getDatabaseInstance(ProfileActivity.this).getAssessmentDao().getCertificates(Assessment_Constants.currentStudentID, Assessment_Constants.CERTIFICATE_LBL);
        if (assessmentList != null) {
            certificate_progress.setText("" + assessmentList.size());
        }
        displayEarnedCertificateList();
//        tv_Word.setText(Html.fromHtml("<sup>" + Math.round(wordsProgress) + "</sup>&frasl;<sub>" + totalWords + "</sub>"));
    }

    @OnClick({R.id.certificate_progress})
    public void displayEarnedCertificateList() {
//        ButtonClickSound.start();
//        if (!CertificateGained) {
        // Display Learnt Sentences
        LeaderBoard = false;
        LearntWord = false;
        LearntSentence = false;
        CertificateGained = true;
/*            word_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            sentence_progress.setInnerBackgroundColor(Color.TRANSPARENT);*/
        certificate_progress.setInnerBackgroundColor(getResources().getColor(R.color.transparent_white_btn));
        tv_Leaderboard.setText("Certificates Received");
        List<Assessment> assessmentList = profilePresenter.getCertificates(Assessment_Constants.currentStudentID, Assessment_Constants.CERTIFICATE_LBL);
//            List<Assessment> assessmentList = AppDatabase.getDatabaseInstance(ProfileActivity.this).getAssessmentDao().getCertificates(Assessment_Constants.currentStudentID, Assessment_Constants.CERTIFICATE_LBL);
        CustomAdapterForCertificate adapter = new CustomAdapterForCertificate(this, assessmentList);
        certificate_progress.setText("" + assessmentList.size());
        lv_Leaderboard.setAdapter(adapter);
/*        } else {
            // Display Leaderboard
            LeaderBoard = true;
            LearntWord = false;
            LearntSentence = false;
            CertificateGained = false;
            word_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            sentence_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            certificate_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            displayLeaderBoard();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        isConnectedToInternet = profilePresenter.checkConnectivity();
    }


    public void displayProfileNameAndImage() {
        profileName = profilePresenter.displayProfileName();
        sImage = profilePresenter.displayProfileImage();
        //profileName = AppDatabase.getDatabaseInstance(this).getStudentDao().getFullName(Assessment_Constants.currentStudentID);
        tv_name.setText(profileName);
        //sImage = AppDatabase.getDatabaseInstance(this).getStudentDao().getStudentAvatar(Assessment_Constants.currentStudentID);
        if(sImage!=null) {
            if (sImage.equalsIgnoreCase("group_icon"))
                s_iv.setImageResource(R.drawable.ic_group_black_24dp);
/*
            else if (!Assessment_Constants.GROUP_LOGIN && Assessment_Constants.SD_CARD_Content)
                Glide.with(this).load(Assessment_Constants.ext_path + "/.LLA/English/LLA_Thumbs/b2.png").into(s_iv);
*/
            else
                Glide.with(this).load(AssessmentApplication.assessPath+ Assessment_Constants.THUMBS_PATH + sImage).into(s_iv);
        }else
            s_iv.setImageResource(R.drawable.b2);

    }

    //adapterForCertificate
    class CustomAdapterForCertificate extends ArrayAdapter<Assessment> {
        private Context mContext;
        private List<Assessment> assessmentLists;
        public TextView tv_assessment;
        public TextView tv_timestamp;
        public RelativeLayout certi_root;

        CustomAdapterForCertificate(Context c, List<Assessment> assessmentLists) {
            super(c, R.layout.layout_certificate_row, assessmentLists);
            this.mContext = c;
            this.assessmentLists = assessmentLists;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = vi.inflate(R.layout.layout_certificate_row, parent, false);
            tv_assessment = row.findViewById(R.id.tv_assessment);
            tv_timestamp = row.findViewById(R.id.tv_timestamp);
            certi_root = row.findViewById(R.id.certi_root);

/*            if(Assessment_Constants.GROUP_LOGIN) {
                String sId = assessmentLists.get(position).getStartDateTimea().split("_")[0];
                String sName = profilePresenter.getStudentName(sId);
                tv_assessment.setText("#" + (position + 1)+ " "+sName);
            }else*/
                tv_assessment.setText("Certificate  #" + (position + 1));
            tv_timestamp.setText(assessmentLists.get(position).getEndDateTime());
            final int finalposition = position;
            certi_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoCertificate(assessmentLists.get(finalposition));
                }
            });

            position++;
            return row;
        }
    }

    private void gotoCertificate(Assessment assessment) {

        Intent intent = new Intent(ProfileActivity.this, CertificateActivity.class);
        intent.putExtra("nodeId", "na");
        intent.putExtra("CertiTitle", "" + assessment.getLevela());
        intent.putExtra("display", "display");
        assessmentProfile = assessment;
        startActivity(intent);

    }


        /*  private void sortList(List<Leaderboard> list) {
          Collections.sort(list, new Comparator<Leaderboard>() {
              public int compare(Leaderboard ideaVal1, Leaderboard ideaVal2) {
                  // avoiding NullPointerException in case name is null
                  Integer idea1 = new Integer(ideaVal1.getScoredMarks());
                  Integer idea2 = new Integer(ideaVal2.getScoredMarks());
                  return idea1.compareTo(idea2);
              }
          });
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
  */
  /*  private JSONArray fillScoreData(List<Score> scoreList) {
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
*/
/*    private void displayLeaderBoard() {

        if (!isConnectedToInternet) {
            // NO INTERNET
            String data = profilePresenter.getDataFromPref(LeaderboardData, "Empty");
            // String data = pref.getString(LeaderboardData, "Empty");
            if (data.equalsIgnoreCase("Empty")) {
                // Display Message as connect to the internet to view the Leaderboard
                tv_Leaderboard.setText("Please Connect to the Internet !");
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
                lv_Leaderboard.setAdapter(adapter);
                String HighScoreTemp = "" + profilePresenter.getRCHighScore(Assessment_Constants.currentStudentID);
                tv_MyRCS.setText("High Score : " + HighScoreTemp);
            } else {
                // Show locally stored data
//                try {
                // Parsing Json
                // student's rank
                profilePresenter.parseLeaderboardJson(data);
                   *//* JSONObject obj = new JSONObject(data);
                    int profileRank = obj.getInt("Rank");
                    // leaderboard data
                    JSONArray leadArray = obj.getJSONArray("LeaderList");
                    Type listType = new TypeToken<ArrayList<Leaderboard>>() {
                    }.getType();
                    List<Leaderboard> leaderboardList = gson.fromJson(leadArray.toString(), listType);
*//*
                // Sort List & then show

                // Adapter

                   *//* CustomAdapter adapter = new CustomAdapter(ProfileActivity.this, leaderboardList);
                    lv_Leaderboard.setAdapter(adapter);*//*

                // Set Rank & Score
                   *//* int HighScore = AppDatabase.getDatabaseInstance(ProfileActivity.this).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                    int Rank = profileRank;
                    displayRCHighScore(Rank, HighScore);*//*

     *//*} catch (JSONException e) {
                    e.printStackTrace();

                    tv_Leaderboard.setText("Something went Wrong ! Please Connect to the Internet !");
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
                    lv_Leaderboard.setAdapter(adapter);
                    int HighScore = AppDatabase.getDatabaseInstance(this).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                    int Rank = 0;
                    displayRCHighScore(Rank, HighScore);
                }*//*
            }
        } else {
            // CONNECTED TO INTERNET
            // show Leaderboard content fetched from server
            tv_Leaderboard.setText("Leaderboard");
            *//* try {*//*
            // Push Data
            StdID = Assessment_Constants.currentStudentID;

            profilePresenter.pushData(StdID, profileName);

               *//* List<Score> scoreList = AppDatabase.getDatabaseInstance(this).getScoreDao().getScoreByStdID(StdID);

                JSONArray scoreData = new JSONArray();
                scoreData = fillScoreData(scoreList);

                JSONObject PushObj = new JSONObject();
                PushObj.put("scoreData", scoreData);
                PushObj.put("studentId", StdID);
                PushObj.put("studentName", profileName);


                // Push Data
                progressView.setVisibility(View.VISIBLE);
                lv_Leaderboard.setVisibility(View.GONE);
                AndroidNetworking.post(APIs.highScoreUrl)
                        .addHeaders("Content-Type", "application/json")
                        .addJSONObjectBody(PushObj)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                // Use response as source of Leaderboard
                                //Toast.makeText(ProfileActivity.this, "Data pushed successfully", Toast.LENGTH_SHORT).show();
                                progressView.setVisibility(View.GONE);
                                lv_Leaderboard.setVisibility(View.VISIBLE);

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
                                    CustomAdapter adapter = new CustomAdapter(ProfileActivity.this, leaderboardList);
                                    lv_Leaderboard.setAdapter(adapter);

                                    // Set Rank & Score
                                    int HighScore = AppDatabase.getDatabaseInstance(ProfileActivity.this).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                                    int Rank = profileRank;
                                    displayRCHighScore(Rank, HighScore);
                                } catch (JSONException e) {
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
                                    lv_Leaderboard.setAdapter(adapter);
                                    // Set Rank & Score
                                    int HighScore = AppDatabase.getDatabaseInstance(ProfileActivity.this).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                                    int Rank = 0;
                                    displayRCHighScore(Rank, HighScore);
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                //Toast.makeText(ProfileActivity.this, "Data push failed", Toast.LENGTH_SHORT).show();
                                progressView.setVisibility(View.GONE);
                                lv_Leaderboard.setVisibility(View.VISIBLE);

                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
                                lv_Leaderboard.setAdapter(adapter);
                                int HighScore = AppDatabase.getDatabaseInstance(ProfileActivity.this).getScoreDao().getRCHighScore(Assessment_Constants.currentStudentID);
                                int Rank = 0;
                                displayRCHighScore(Rank, HighScore);

                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }*//*
     *//*final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
            lv_Leaderboard.setAdapter(adapter);
        *//*
        }

    }

    @Override
    public void showException() {
        tv_Leaderboard.setText("Something went Wrong ! Please Connect to the Internet !");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
        lv_Leaderboard.setAdapter(adapter);
    }

    @Override
    public void changeVisibility(boolean showLeaderBoard) {
        if (showLeaderBoard) {
            progressView.setVisibility(View.GONE);
            lv_Leaderboard.setVisibility(View.VISIBLE);
        } else {
            progressView.setVisibility(View.VISIBLE);
            lv_Leaderboard.setVisibility(View.GONE);
        }
    }

    @Override
    public void setLeaderBoardListToAdapter(List<Leaderboard> leaderboardList) {
        CustomAdapter adapter = new CustomAdapter(ProfileActivity.this, leaderboardList);
        lv_Leaderboard.setAdapter(adapter);

    }

    @Override
    public void setNoData() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.no_data));
        lv_Leaderboard.setAdapter(adapter);
    }

    @Override
    public void setTranslatedWord(String translatedWord) {
        translationDialog.meaning_1.setText(translatedWord);
    }


    // Show fetched Leaderboard in custom view
    class CustomAdapter extends ArrayAdapter<Leaderboard> {
        Context context;
        List<Leaderboard> title;

        CustomAdapter(Context c, List<Leaderboard> title) {

            super(c, R.layout.listitem, title);
            this.context = c;
            this.title = title;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = vi.inflate(R.layout.listitem, parent, false);
            TextView serial = (TextView) row.findViewById(R.id.tv_serial);
            TextView name = (TextView) row.findViewById(R.id.tv_name);
            TextView rank = (TextView) row.findViewById(R.id.tv_rank);
            int pos = position + 1;
            serial.setText(String.valueOf(pos));
            name.setText(title.get(position).getStudentname());
            rank.setText("" + title.get(position).getScoredMarks());
            pos++;
            return row;
        }
    }
    // Show fetched Leaderboard in custom view

    class CustomAdapterForTranslation extends ArrayAdapter<LearntWords> {
        private Context mContext;

        private List<LearntWords> words;
        public TextView tv_learnt_word;
        public ImageView iv_play_word;
        public String type = "";
        public ImageView iv_translate;


        CustomAdapterForTranslation(Context c, List<LearntWords> words, String type) {
            super(c, R.layout.layout_learnt_word_row, words);
            this.mContext = c;
            this.words = words;
            this.type = type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = vi.inflate(R.layout.layout_learnt_word_row, parent, false);
            tv_learnt_word = row.findViewById(R.id.tv_learnt_word);
            iv_play_word = row.findViewById(R.id.iv_play_word);
            iv_translate = row.findViewById(R.id.iv_translate);

            if (type.equalsIgnoreCase("sentence"))
                iv_translate.setVisibility(View.GONE);

            tv_learnt_word.setText(words.get(position).getWord());
            int finalPosition = position;
            iv_play_word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ttsService.play(words.get(finalPosition).getWord());
                }
            });

            iv_translate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //translateWord(words.get(finalPosition).getWord());
                    isConnectedToInternet = profilePresenter.checkConnectivity();
                    if (isConnectedToInternet)
                        showTranslationDialog(words.get(finalPosition).getWord());
                    else
                        Toast.makeText(ProfileActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();

                }
            });

            *//*serial.setText(String.valueOf(position));
            tv_learnt_word.setText(wordsList.get(position).());
            rank.setText("" + title.get(position).getScoredMarks());*//*
            position++;
            return row;
        }
    }

    private void showTranslationDialog(String word) {
        translationDialog = new TranslationDialog(ProfileActivity.this);
        translationDialog.show();
        translationDialog.wordToTranslate.setText(word);
        translationDialog.lang_spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String langCode = getResources().getStringArray(R.array.language_codes)[position];
                profilePresenter.callTranslateApi(word, langCode);
                //                    String translatedWord = callTranslateApi(word, langCode, 1);
                //translationDialog.meaning_1.setText(translatedWord);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       *//* translationDialog.lang_spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String langCode = getResources().getStringArray(R.array.language_codes)[position];
                     callTranslateApi(word, langCode, 2);
                    //translationDialog.meaning_2.setText(translatedWord);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*//*

    }*/

  /*  private void translateWord(String textToBeTranslated,String languageCode) {
        String yandexKey = "trnsl.1.1.20180113T102250Z.941f2a23f7d88084.0e9f4357f3500975fac5d2cbb11c2a946f72faa1";
        String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + yandexKey
                + "&text=" + textToBeTranslated + "&lang=" + languageCode;

         callTranslateApi(yandexUrl);

    }*/

    /*private void callTranslateApi(String textToBeTranslated, String languageCode, int spinnerNo) {
        String yandexKey = "trnsl.1.1.20180113T102250Z.941f2a23f7d88084.0e9f4357f3500975fac5d2cbb11c2a946f72faa1";
        String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + yandexKey
                + "&text=" + textToBeTranslated + "&lang=" + languageCode;
        final String[] resultString = {""};

        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
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
                            *//*if(listener!=null){
                                listener.onPostExecute(resultString);
                            }*//*
                            translatedWord = resultString[0];
//                            if (spinnerNo == 1)
                            translationDialog.meaning_1.setText(translatedWord);
                          *//*  else if (spinnerNo == 2)
                                translationDialog.meaning_2.setText(translatedWord);
*//*

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
        //  return translatedWord;
    }
*/

/*
    @Override
    public void displayRCHighScore(int rank, int score) {
        if (rank == 0)
            tv_MyRCS.setText("High Score : " + score);
        else
            tv_MyRCS.setText("Rank : " + rank + "\t\t\t\t\t" + "High Score : " + score);
    }

    @OnClick({R.id.word_progress})
    public void displayLearntWordList() {
        ButtonClickSound.start();
        if (!LearntWord) {
            // Display Learnt Words
            LeaderBoard = false;
            LearntWord = true;
            LearntSentence = false;
            CertificateGained = false;
            word_progress.setInnerBackgroundColor(getResources().getColor(R.color.yellow_sand));
            sentence_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            certificate_progress.setInnerBackgroundColor(Color.TRANSPARENT);

            tv_Leaderboard.setText("Learnt Words");
            List<LearntWords> learntWords = profilePresenter.getLearntWords(Assessment_Constants.currentStudentID);
//            List<LearntWords> learntWords = AppDatabase.getDatabaseInstance(ProfileActivity.this).getLearntWordDao().getLearntWords(Assessment_Constants.currentStudentID);
//            final ArrayAdapter<LearntWords> adapter = new ArrayAdapter<LearntWords>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, learntWords);
            CustomAdapterForTranslation adapter = new CustomAdapterForTranslation(this, learntWords, "");
            lv_Leaderboard.setAdapter(adapter);
           */
/* lv_Leaderboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String word=((TextView)view).getText().toString();
                    ttsService.play(word);
                }
            });*//*

        } else {
            // Display Leaderboard
            LeaderBoard = true;
            LearntWord = false;
            LearntSentence = false;
            CertificateGained = false;
            word_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            sentence_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            certificate_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            displayLeaderBoard();
        }
    }

    @OnClick({R.id.sentence_progress})
    public void displayLearntSentenceList() {
        ButtonClickSound.start();
        if (!LearntSentence) {
            // Display Learnt Sentences
            LeaderBoard = false;
            LearntWord = false;
            LearntSentence = true;
            CertificateGained = false;
            word_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            certificate_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            sentence_progress.setInnerBackgroundColor(getResources().getColor(R.color.yellow_sand));

            tv_Leaderboard.setText("Learnt Sentences");
            List<LearntWords> learntSentences = profilePresenter.getLearntSentences(Assessment_Constants.currentStudentID);
//            List<LearntWords> learntSentences = AppDatabase.getDatabaseInstance(ProfileActivity.this).getLearntWordDao().getLearntSentences(Assessment_Constants.currentStudentID);
            CustomAdapterForTranslation adapter = new CustomAdapterForTranslation(this, learntSentences, "sentence");

            //            final ArrayAdapter<LearntWords> adapter = new ArrayAdapter<LearntWords>(ProfileActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, learntSentences);
            lv_Leaderboard.setAdapter(adapter);
        } else {
            // Display Leaderboard
            LeaderBoard = true;
            LearntWord = false;
            LearntSentence = false;
            CertificateGained = false;
            word_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            sentence_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            certificate_progress.setInnerBackgroundColor(Color.TRANSPARENT);
            displayLeaderBoard();
        }
    }

    @Override
    public void displaySentenceProgress(float sentenceProgress, int learntSentence, int totalEntries, int totalSentence) {
        // Total Entries
      */
/*  totalEntries = AppDatabase.getDatabaseInstance(this).getEnglishWordDao().getTotalEntries();
        totalSentence = AppDatabase.getDatabaseInstance(this).getEnglishWordDao().getSentenceCount();*//*

        this.totalEntries = totalEntries;
        this.totalSentence = totalSentence;
        totalWords = totalEntries - totalSentence;
        // Display Sentence Progress
      */
/*  int learntSentence = AppDatabase.getDatabaseInstance(this).getLearntWordDao().getLearntSentenceCount(Assessment_Constants.currentStudentID);
        float sentenceProgress;
        if (totalSentence == 0) {
            sentenceProgress = 0.0f;
        } else {
            sentenceProgress = ((float) learntSentence / (float) totalSentence) * 100;
        }
       *//*

        sentence_progress.setProgress(Float.parseFloat(decimalFormat.format(sentenceProgress)));
        sentence_progress.setText(String.valueOf(Html.fromHtml("<sup>" + learntSentence + "</sup>&frasl;<sub>" + totalSentence + "</sub>")));
//        tv_sentence.setText(Html.fromHtml("<sup>" + Math.round(sentenceProgress) + "</sup>&frasl;<sub>" + totalSentence + "</sub>"));
    }

    private void getCertificates() {
        int certificateCount = AppDatabase.getDatabaseInstance(this).getAssessmentDao().getAssessmentCount(Assessment_Constants.currentStudentID);
        tv_Certificates

    }

    @Override
    public void displayWordProgress(float wordsProgress, int learntWords) {
        // Display Word Progress
        // int learntWords = AppDatabase.getDatabaseInstance(this).getLearntWordDao().getLearntWordCount(Assessment_Constants.currentStudentID);
     */
/*   float wordsProgress;
        if (totalWords == 0) {
            wordsProgress = 0.0f;
        } else {
            wordsProgress = ((float) learntWords / (float) totalWords) * 100;
        }*//*

        word_progress.setProgress(Float.parseFloat(decimalFormat.format(wordsProgress)));
        word_progress.setText(String.valueOf(Html.fromHtml("<sup>" + learntWords + "</sup>&frasl;<sub>" + totalWords + "</sub>")));
//        tv_Word.setText(Html.fromHtml("<sup>" + Math.round(wordsProgress) + "</sup>&frasl;<sub>" + totalWords + "</sub>"));
    }
*/

}
