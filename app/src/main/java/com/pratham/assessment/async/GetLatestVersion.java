package com.pratham.assessment.async;

import android.os.AsyncTask;
import android.util.Log;
import com.pratham.assessment.ui.splash_activity.SplashContract;

import org.jsoup.Jsoup;


public class GetLatestVersion extends AsyncTask<String, String, String> {

    String latestVersion;
    SplashContract.SplashPresenter splashPresenter;

    public GetLatestVersion(SplashContract.SplashPresenter splashPresenter) {
        this.splashPresenter = splashPresenter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        COS_Utility.showDialogInApiCalling(dialog, SplashActivity.this, "Checking if new version is available!");
    }


    @Override
    protected String doInBackground(String... params) {
        try {
            latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.pratham.cityofstories&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
            Log.d("latest::", latestVersion);
        } catch (Exception e) {
//            COS_Utility.dismissDialog(dialog);
            e.printStackTrace();
        }
        return latestVersion;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        splashPresenter.versionObtained(latestVersion);
    }
}
