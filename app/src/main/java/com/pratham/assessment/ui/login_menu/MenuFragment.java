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

import com.pratham.assessment.R;
import com.pratham.assessment.custom.SelectAgeGroupDialog;
import com.pratham.assessment.ui.login.MainActivity;
import com.pratham.assessment.ui.login.group_selection.SelectGroupActivity;
import com.pratham.assessment.ui.login.group_selection.fragment_select_group.FragmentSelectGroup;
import com.pratham.assessment.ui.login.qr_scan.QRScanActivity;
import com.pratham.assessment.utilities.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

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

    @OnClick(R.id.ib_age3to6)
    public void gotoQRActivity() {
        // ButtonClickSound.start();
//        startActivity(new Intent(getActivity(), QRScanActivity.class));

        Bundle bundle = new Bundle();
        bundle.putBoolean(Assessment_Constants.GROUP_AGE_BELOW_7, true);
        Assessment_Utility.showFragment(getActivity(), new FragmentSelectGroup(), R.id.frame_group,
                bundle, FragmentSelectGroup.class.getSimpleName());


    }

    @OnClick(R.id.ib_age8to14)
    public void gotoGroupLogin() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Assessment_Constants.GROUP_AGE_ABOVE_7, true);
        Assessment_Utility.showFragment(getActivity(), new FragmentSelectGroup(), R.id.frame_group,
                bundle, FragmentSelectGroup.class.getSimpleName());


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

    @OnClick({R.id.btn_admin, R.id.rl_admin})
    public void goto_btn_admin() {
//        ButtonClickSound.start();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }
}
