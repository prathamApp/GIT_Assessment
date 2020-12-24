package com.pratham.assessment.ui.content_player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.domain.CertificateModelClass;

import java.util.ArrayList;
import java.util.List;

//import butterknife.ButterKnife;



public class WebViewActivity extends BaseActivity implements WebViewInterface {

    public static int tMarks, sMarks;
    WebView webView;
    String gamePath, currentGameName, webViewLang = "NA";
    public static String webResId, gameLevel, mode, cCode;
    TextToSpeechCustom tts;
    static int gameCounter = 0, arraySize;
    int position;
    public static List<CertificateModelClass> certificateModelClassList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ButterKnife.bind(this);
        webView = (WebView) findViewById(R.id.loadPage);

        webResId = getIntent().getStringExtra("resId");
        gamePath = getIntent().getStringExtra("resPath");
        mode = getIntent().getStringExtra("mode");
        gameLevel = getIntent().getStringExtra("gameLevel");

        Log.d("WevViewLevel", "onCreate: " + gameLevel);

        tts = new TextToSpeechCustom(this, 0.6f);
        gameCounter = 0;

 /*       if (mode.equalsIgnoreCase("assessment")) {
            createWebView(gameWebViewList.get(gameCounter).getResourcePath());
            arraySize = gameWebViewList.size();
            webResId = gameWebViewList.get(gameCounter).getResourceId();
        } else
            createWebView(gamePath);*/

        certificateModelClassList = new ArrayList<>();

/*
        CertificateModelClass certificateModelClass=new CertificateModelClass();
        certificateModelClass.setScoredMarks(50);
        certificateModelClass.setTotalMarks(100);
        certificateModelClass.setCertiCode("2");
        certificateModelClassList.add(certificateModelClass);
        certificateModelClass=new CertificateModelClass();
        certificateModelClass.setScoredMarks(5);
        certificateModelClass.setTotalMarks(10);
        certificateModelClass.setCertiCode("4");
        certificateModelClassList.add(certificateModelClass);
        certificateModelClass=new CertificateModelClass();
        certificateModelClass.setScoredMarks(3);
        certificateModelClass.setTotalMarks(10);
        certificateModelClass.setCertiCode("4");
        certificateModelClassList.add(certificateModelClass);
        certificateModelClass=new CertificateModelClass();
        certificateModelClass.setScoredMarks(9);
        certificateModelClass.setTotalMarks(10);
        certificateModelClass.setCertiCode("3");
        certificateModelClassList.add(certificateModelClass);
*/
        //startActivity(new Intent(this,CertificateActivity.class));

    }

    @SuppressLint("JavascriptInterface")
    public void createWebView(String GamePath) {

        String myPath = GamePath;
        webView.loadUrl(myPath);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new JSInterface(this, webView, tts, this, WebViewActivity.this), "Android");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.clearCache(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }


    @Override
    public void onBackPressed() {
/*        super.onBackPressed();
        webView.loadUrl("about:blank");*/
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView dia_title = dialog.findViewById(R.id.dia_title);
        Button no_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button yes_btn = dialog.findViewById(R.id.dia_btn_restart);

        dia_title.setText(R.string.do_you_want_to_exit);
        no_btn.setText(R.string.no);
        yes_btn.setText(R.string.yes);

        dialog.show();

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("");
                finish();
                dialog.dismiss();
            }
        });
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onNextGame(final WebView w) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("cCode", cCode);
        returnIntent.putExtra("tMarks", tMarks);
        returnIntent.putExtra("sMarks", sMarks);
        setResult(Activity.RESULT_OK, returnIntent);
        //super.onBackPressed();
//        webView.loadUrl("about:blank");
        finish();

        /*gameCounter += 1;
        if (gameCounter < arraySize) {
            System.out.println(" gameCounter :::::::::::::::::::::::::::::::::: " + gameCounter);
            gamePath = gameWebViewList.get(gameCounter).getResourcePath();//
            webResId = gameWebViewList.get(gameCounter).getResourceId();
            System.out.println("gamePath :::::: " + gamePath + " :::::: " + webResId);
            w.post(new Runnable() {
                @Override
                public void run() {
                    w.loadUrl(gamePath);
                }
            });
        } else {
            startActivity(new Intent(this, CertificateActivity.class));
            super.onBackPressed();
        }*/
    }

    @Override
    protected void onPause() {
        if (webView != null) {
            webView.loadUrl("");
        }
        super.onPause();
/*        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(webView, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.loadUrl(gamePath);
        }

    }

}

