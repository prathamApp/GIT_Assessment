package com.pratham.assessment.ui.login.group_selection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.SelectAgeGroupDialog;
import com.pratham.assessment.ui.login.group_selection.fragment_select_group.FragmentSelectGroup;

public class SelectGroupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final SelectAgeGroupDialog selectAgeGroupDialog = new SelectAgeGroupDialog(this);
        selectAgeGroupDialog.iv_age_3_to_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAgeGroupDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Assessment_Constants.GROUP_AGE_BELOW_7, true);
                Assessment_Utility.showFragment(SelectGroupActivity.this, new FragmentSelectGroup(), R.id.frame_group,
                        bundle, FragmentSelectGroup.class.getSimpleName());
            }
        });
        selectAgeGroupDialog.iv_age_8_to_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAgeGroupDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Assessment_Constants.GROUP_AGE_ABOVE_7, true);
                Assessment_Utility.showFragment(SelectGroupActivity.this, new FragmentSelectGroup(), R.id.frame_group,
                        bundle, FragmentSelectGroup.class.getSimpleName());
            }
        });

        selectAgeGroupDialog.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAgeGroupDialog.dismiss();
                finish();
//                startActivity(new Intent(SelectGroupActivity.this, MenuActivity.class));
                Toast.makeText(SelectGroupActivity.this, "age group selected", Toast.LENGTH_SHORT).show();
            }
        });
        selectAgeGroupDialog.setCancelable(false);
        selectAgeGroupDialog.show();
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
