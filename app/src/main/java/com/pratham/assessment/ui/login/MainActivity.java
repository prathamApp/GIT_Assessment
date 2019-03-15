package com.pratham.assessment.ui.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.R;
import com.pratham.assessment.admin_pannel.admin_login.AdminPanelFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Assessment_Utility.showFragment(this, new AdminPanelFragment(), R.id.frame_attendance,
                null, AdminPanelFragment.class.getSimpleName());
    }
    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}
