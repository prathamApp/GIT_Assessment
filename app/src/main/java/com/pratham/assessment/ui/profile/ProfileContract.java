package com.pratham.assessment.ui.profile;

import com.pratham.assessment.domain.Assessment;

import java.util.List;

public interface ProfileContract {
    public interface profilePresenter {

/*        void displaySentenceProgress();
        void displayWordProgress();
        int getRCHighScore(String currentStudentID);
        void parseLeaderboardJson(String data);
        String getDataFromPref(String leaderboardData, String value);
        void pushData(String stdID, String profileName);
        void callTranslateApi(String word, String langCode);
        List<LearntWords> getLearntWords(String currentStudentID);
        List<LearntWords> getLearntSentences(String currentStudentID);*/

        String displayProfileName();

        String displayProfileImage();

        boolean checkConnectivity();

        List<Assessment> getCertificates(String currentStudentID, String certificateLbl);

        String getStudentName(String sId);

    }

    public interface profileView {

/*        void displaySentenceProgress(float progress, int sentence, int totalEntries, int totalSentence);
        void displayWordProgress(float wordsProgress, int learntWords);
        void displayRCHighScore(int rank, int score);
        void showException();
        void changeVisibility(boolean showLeaderBoard);
        void setLeaderBoardListToAdapter(List<Leaderboard> leaderboardList);
        void setNoData();
        void setTranslatedWord(String s);*/
    }
}
