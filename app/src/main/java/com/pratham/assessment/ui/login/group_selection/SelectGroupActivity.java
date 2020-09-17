package com.pratham.assessment.ui.login.group_selection;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pratham.assessment.BaseActivity;
import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.ui.login_menu.MenuFragment;
import com.pratham.assessment.ui.login_menu.MenuFragment_;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_select_group)
public class SelectGroupActivity extends BaseActivity {
    @AfterViews
    public void init() {
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Assessment_Utility.showFragment(this, new MenuFragment_(), R.id.frame_group,
                    null, MenuFragment.class.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SelectGroupActivity@@@", e.getMessage());
        }

    }
/*

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_select_group);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Assessment_Utility.showFragment(this, new MenuFragment(), R.id.frame_group,
                    null, MenuFragment.class.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SelectGroupActivity@@@", e.getMessage());
        }

//    showAgeGroupDialog();

    }
*/

    /* private void showAgeGroupDialog() {
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

        *//* selectAgeGroupDialog.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAgeGroupDialog.dismiss();
                finish();
//                startActivity(new Intent(SelectGroupActivity.this, MenuActivity.class));
                //         Toast.makeText(SelectGroupActivity.this, "age group selected", Toast.LENGTH_SHORT).show();
            }
        });*//*
        selectAgeGroupDialog.setCancelable(true);
        selectAgeGroupDialog.show();
    }
*/
    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
//            finish();
            showExitDialog();
        } else {
            if (fragments > 1) {
                // FragmentManager.BackStackEntry first = getSupportFragmentManager().getBackStackEntryAt(0);
                getSupportFragmentManager().popBackStack(0, 0);
            } else {
                super.onBackPressed();
            }
        }
    }


    public void showExitDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView title = dialog.findViewById(R.id.dia_title);
        Button exit_btn = dialog.findViewById(R.id.dia_btn_exit);
        Button restart_btn = dialog.findViewById(R.id.dia_btn_restart);

        title.setText(R.string.do_you_want_to_exit);
        restart_btn.setText(R.string.no);
        exit_btn.setText(R.string.yes);
        dialog.show();

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String curSession = AppDatabase.getDatabaseInstance(SelectGroupActivity.this).getStatusDao().getValue("CurrentSession");
                String toDateTemp = AppDatabase.getDatabaseInstance(SelectGroupActivity.this).getSessionDao().getToDate(curSession);

                Log.d("AppExitService:", "curSession : " + curSession + "      toDateTemp : " + toDateTemp);

                if (toDateTemp != null) {
                    if (toDateTemp.equalsIgnoreCase("na"))
                        AppDatabase.getDatabaseInstance(SelectGroupActivity.this).getSessionDao().UpdateToDate(curSession, Assessment_Utility.getCurrentDateTime());
                }
                finishAffinity();

            }
        });

        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
