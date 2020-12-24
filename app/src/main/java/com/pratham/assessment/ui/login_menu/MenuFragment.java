package com.pratham.assessment.ui.login_menu;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pratham.assessment.R;
import com.pratham.assessment.custom.FastSave;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.ui.login.MainActivity_;
import com.pratham.assessment.ui.login.group_selection.fragment_select_group.FragmentSelectGroup;
import com.pratham.assessment.ui.login.group_selection.fragment_select_group.FragmentSelectGroup_;
import com.pratham.assessment.ui.login.qr_scan.QRScanActivity_;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;


@EFragment(R.layout.fragment_menu)
public class MenuFragment extends Fragment {
    /*  @BindView(R.id.btn_qr)
      ImageButton btn_qr;
      @BindView(R.id.btn_grp)
      ImageButton btn_grp;*/
    @ViewById(R.id.btn_admin)
    ImageButton btn_admin;
    @ViewById(R.id.rl_admin)
    RelativeLayout rl_admin;

    public MenuFragment() {
        // Required empty public constructor
    }


  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/

    /* @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_menu, container, false);
         return view;
     }
 */
    @Click({R.id.ib_age3to6, R.id.rl_age3to6})
    public void gotoAge3to6() {

        checkGroupAssigned(Assessment_Constants.GROUP_AGE_BELOW_7);

        // ButtonClickSound.start();
//        startActivity(new Intent(getActivity(), QRScanActivity.class));
//        if (Assessment_Constants.GROUPID1.equalsIgnoreCase("") && Assessment_Constants.GROUPID2.equalsIgnoreCase("") && Assessment_Constants.GROUPID3.equalsIgnoreCase("") && Assessment_Constants.GROUPID4.equalsIgnoreCase("") && Assessment_Constants.GROUPID2.equalsIgnoreCase("")) {

    }

    @Click({R.id.ib_age8to14, R.id.rl_age8to14})
    public void gotoAge8to14() {
     /*   if (Assessment_Constants.GROUPID1.equalsIgnoreCase("") && Assessment_Constants.GROUPID2.equalsIgnoreCase("") && Assessment_Constants.GROUPID3.equalsIgnoreCase("") && Assessment_Constants.GROUPID4.equalsIgnoreCase("") && Assessment_Constants.GROUPID2.equalsIgnoreCase("")) {
            Toast.makeText(getActivity(), "No groups assigned", Toast.LENGTH_SHORT).show();
        } else {


            Bundle bundle = new Bundle();
            bundle.putBoolean(Assessment_Constants.GROUP_AGE_ABOVE_7, true);
            Assessment_Utility.showFragment(getActivity(), new FragmentSelectGroup(), R.id.frame_group,
                    bundle, FragmentSelectGroup.class.getSimpleName());

        }*/
        checkGroupAssigned(Assessment_Constants.GROUP_AGE_ABOVE_7);
//        ButtonClickSound.start();
//        startActivity(new Intent(getActivity(), SelectGroupActivity.class));

       /*     final SelectAgeGroupDialog selectAgeGroupDialog = new SelectAgeGroupDialog(getActivity());
        selectAgeGroupDialog.iv_age_3_to_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAgeGroupDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Assessment_Constants.GROUP_AGE_BELOW_7, true);
                Assessment_Utility.showFragment(getActivity(), new FragmentSelectGroup(), R.id.frame_group,
                        bundle, FragmentSelectGroup.class.getSimpleName());
            }
        });
        selectAgeGroupDialog.iv_age_8_to_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAgeGroupDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putBoolean(Assessment_Constants.GROUP_AGE_ABOVE_7, true);
                Assessment_Utility.showFragment(getActivity(), new FragmentSelectGroup(), R.id.frame_group,
                        bundle, FragmentSelectGroup.class.getSimpleName());
            }
        });*/

       /* selectAgeGroupDialog.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAgeGroupDialog.dismiss();
//                startActivity(new Intent(SelectGroupActivity.this, MenuActivity.class));
       //         Toast.makeText(SelectGroupActivity.this, "age group selected", Toast.LENGTH_SHORT).show();
            }
        });
        selectAgeGroupDialog.setCancelable(true);
        selectAgeGroupDialog.show();
*/


    }

    public void checkGroupAssigned(String ageGroupConstant) {
        ArrayList<Groups> groups = new ArrayList<>();

//        Toast.makeText(getActivity(), "No groups assigned", Toast.LENGTH_SHORT).show();
        ArrayList<String> allGroups = new ArrayList<>();
        allGroups.add(AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getValue(Assessment_Constants.GROUPID1));
        allGroups.add(AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getValue(Assessment_Constants.GROUPID2));
        allGroups.add(AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getValue(Assessment_Constants.GROUPID3));
        allGroups.add(AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getValue(Assessment_Constants.GROUPID4));
        allGroups.add(AppDatabase.getDatabaseInstance(getActivity()).getStatusDao().getValue(Assessment_Constants.GROUPID5));

        for (String grID : allGroups) {
            // ArrayList<Student> students = (ArrayList<Student>) BaseActivity.studentDao.getGroupwiseStudents(grID);
            ArrayList<Student> students = (ArrayList<Student>) AppDatabase.getDatabaseInstance(getActivity()).getStudentDao().getGroupwiseStudents(grID);
            for (Student stu : students) {
                if (ageGroupConstant.equalsIgnoreCase(Assessment_Constants.GROUP_AGE_BELOW_7)) {
                    if (stu.getAge() < 7) {
                        //Groups group = BaseActivity.groupDao.getGroupByGrpID(grID);
                        Groups group = AppDatabase.getDatabaseInstance(getActivity()).getGroupsDao().getGroupByGrpID(grID);
                        groups.add(group);
                        break;
                    }
                } else {
                    if (stu.getAge() >= 7) {
                        //Groups group = BaseActivity.groupDao.getGroupByGrpID(grID);
                        Groups group = AppDatabase.getDatabaseInstance(getActivity()).getGroupsDao().getGroupByGrpID(grID);
                        groups.add(group);
                        break;
                    }
                }
            }
        }

        if (groups.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ageGroupConstant, true);
            Assessment_Utility.showFragment(getActivity(), new FragmentSelectGroup_(), R.id.frame_group,
                    bundle, FragmentSelectGroup.class.getSimpleName());
        } else {
            Toast.makeText(getActivity(), R.string.no_groups_assigned, Toast.LENGTH_SHORT).show();
        }
    }

    @Click({R.id.btn_admin, R.id.rl_admin})
    public void goto_btn_admin() {
//        ButtonClickSound.start();
        startActivity(new Intent(getActivity(), MainActivity_.class));
    }

    @Click({R.id.btn_qr, R.id.rl_qr})
    public void goto_btn_qr() {
//        ButtonClickSound.start();
        startActivity(new Intent(getActivity(), QRScanActivity_.class));
    }


    @AfterViews
    public void init() {
        if (!FastSave.getInstance().getBoolean(Assessment_Constants.VOICES_DOWNLOAD_INTENT, false))
            show_STT_Dialog();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

  /*  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (!FastSave.getInstance().getBoolean(Assessment_Constants.VOICES_DOWNLOAD_INTENT, false))
            show_STT_Dialog();
    }*/

    private void show_STT_Dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lang_custom_dialog);
/*        Bitmap map=COS_Utility.takeScreenShot(HomeActivity.this);
        Bitmap fast=COS_Utility.fastblur(map, 20);
        final Drawable draw=new BitmapDrawable(getResources(),fast);
        dialog.getWindow().setBackgroundDrawable(draw);*/
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        TextView dia_title = dialog.findViewById(R.id.dia_title);
        Button skip = dialog.findViewById(R.id.dia_btn_green);
        Button ok = dialog.findViewById(R.id.dia_btn_yellow);
        dia_title.setText(R.string.please_download_language_packs_offline_for_better_performance);
        ok.setText(R.string.ok);
        skip.setText(R.string.skip);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastSave.getInstance().saveBoolean(Assessment_Constants.VOICES_DOWNLOAD_INTENT, true);
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.google.android.googlequicksearchbox",
                        "com.google.android.voicesearch.greco3.languagepack.InstallActivity"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastSave.getInstance().saveBoolean(Assessment_Constants.VOICES_DOWNLOAD_INTENT, true);
                dialog.dismiss();
            }
        });

    }

}
