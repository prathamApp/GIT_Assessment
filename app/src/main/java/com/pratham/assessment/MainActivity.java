package com.pratham.assessment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pratham.assessment.admin_pannel.admin_login.AdminPanelFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Assessment_Utility.showFragment(this, new AdminPanelFragment(), R.id.frame_attendance,
                null, AdminPanelFragment.class.getSimpleName());
    }
}
