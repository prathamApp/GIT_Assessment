package com.pratham.assessment.ui.login_menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pratham.assessment.R;
import com.pratham.assessment.custom.SelectAgeGroupDialog;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.Groups;
import com.pratham.assessment.domain.Student;
import com.pratham.assessment.ui.login.MainActivity;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity;
import com.pratham.assessment.ui.login.group_selection.fragment_select_group.FragmentSelectGroup;
import com.pratham.assessment.ui.login.qr_scan.QRScanActivity;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MenuFragment extends Fragment {
    /*  @BindView(R.id.btn_qr)
      ImageButton btn_qr;
      @BindView(R.id.btn_grp)
      ImageButton btn_grp;*/
    @BindView(R.id.btn_admin)
    ImageButton btn_admin;
    @BindView(R.id.rl_admin)
    RelativeLayout rl_admin;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    @OnClick({R.id.ib_age3to6, R.id.rl_age3to6})
    public void gotoAge3to6() {

        checkGroupAssigned(Assessment_Constants.GROUP_AGE_BELOW_7);

        // ButtonClickSound.start();
//        startActivity(new Intent(getActivity(), QRScanActivity.class));
//        if (Assessment_Constants.GROUPID1.equalsIgnoreCase("") && Assessment_Constants.GROUPID2.equalsIgnoreCase("") && Assessment_Constants.GROUPID3.equalsIgnoreCase("") && Assessment_Constants.GROUPID4.equalsIgnoreCase("") && Assessment_Constants.GROUPID2.equalsIgnoreCase("")) {

    }

    @OnClick({R.id.ib_age8to14, R.id.rl_age8to14})
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
            Assessment_Utility.showFragment(getActivity(), new FragmentSelectGroup(), R.id.frame_group,
                    bundle, FragmentSelectGroup.class.getSimpleName());
        } else {
            Toast.makeText(getActivity(), "No groups assigned..", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.btn_admin, R.id.rl_admin})
    public void goto_btn_admin() {
//        ButtonClickSound.start();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @OnClick({R.id.btn_qr, R.id.rl_qr})
    public void goto_btn_qr() {
//        ButtonClickSound.start();
        startActivity(new Intent(getActivity(), QRScanActivity.class));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }
}
