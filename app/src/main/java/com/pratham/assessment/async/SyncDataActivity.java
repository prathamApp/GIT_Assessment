package com.pratham.assessment.async;

import android.support.v7.app.AppCompatActivity;


import com.pratham.assessment.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_sync_data)
public class SyncDataActivity extends AppCompatActivity {
    @Bean(PushDataToServer.class)
    PushDataToServer pushDataToServer;

    @AfterViews
    public void init() {
        pushDataToServer.setValue(this, false);
        pushDataToServer.doInBackground();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/
}
