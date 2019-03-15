package com.pratham.assessment.ui.splash_activity;


import com.pratham.assessment.database.AppDatabase;

/**
 * Created by Ameya on 23-Nov-17.
 */

public interface SplashContract {

    interface SplashView {

       /* void startApp();

        void showUpdateDialog();

        void showButton();



        void showBottomFragment();


*/
        void gotoNextActivity(); void showProgressDialog();  void dismissProgressDialog();
    }

    interface SplashPresenter {

        /*  void checkVersion();

          void pushData();


          void copyZipAndPopulateMenu();

          void versionObtained(String latestVersion);

          void copyDataBase();*/        void doInitialEntries(AppDatabase appDatabase);

    }

}
