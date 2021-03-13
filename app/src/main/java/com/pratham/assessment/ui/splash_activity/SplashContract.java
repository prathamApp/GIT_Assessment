package com.pratham.assessment.ui.splash_activity;


import com.pratham.assessment.database.AppDatabase;

/**
 * Created by Ameya on 23-Nov-17.
 */

public interface SplashContract {

    interface SplashView {
        void startApp();

        void showUpdateDialog();

        void showButton();

        void gotoNextActivity();

        void showProgressDialog();

        void dismissProgressDialog();
    }

    interface SplashPresenter {
        void checkVersion();

//        void doInitialEntries(AppDatabase appDatabase);

        void versionObtained(String latestVersion);

        void copyZipAndPopulateMenu();

//        void pushData();

        void copyDataBase();

        void copySDCardDB();

        boolean getSdCardPath();

        void populateSDCardMenu();

        void updateNewEntriesInStatusTable();

    }

}
