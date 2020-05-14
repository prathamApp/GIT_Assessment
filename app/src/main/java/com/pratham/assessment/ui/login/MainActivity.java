package com.pratham.assessment.ui.login;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.admin_pannel.admin_login.AdminPanelFragment_;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity_;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.R;
import com.pratham.assessment.admin_pannel.admin_login.AdminPanelFragment;
import com.pratham.assessment.interfaces.DataPushListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements DataPushListener {

    @AfterViews
    public void init(){
        Assessment_Utility.showFragment(this, new AdminPanelFragment_(), R.id.frame_attendance,
                null, AdminPanelFragment.class.getSimpleName());

    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Assessment_Utility.showFragment(this, new AdminPanelFragment(), R.id.frame_attendance,
                null, AdminPanelFragment.class.getSimpleName());
    }*/

    @Override
    public void onBackPressed() {
        /*int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }*/
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
//            startActivity(new Intent(this, MenuActivity.class));
            startActivity(new Intent(this, SelectGroupActivity_.class));
            finish();
        }
    }

    @Override
    public void onResponseGet() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        Assessment_Utility.showFragment(this, new AdminPanelFragment_(), R.id.frame_attendance,
                null, AdminPanelFragment.class.getSimpleName());

    }
}
