package com.pratham.assessment.admin_pannel.PushOrAssign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pratham.assessment.R;
import com.pratham.assessment.async.PushDataToServer;
import com.pratham.assessment.async.PushDataToServerold;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.Assessment;
import com.pratham.assessment.utilities.Assessment_Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PushOrAssignFragment extends Fragment {
    @BindView(R.id.btn_assign)
    Button assign;
    @BindView(R.id.btn_push)
    Button push;
    @BindView(R.id.txt_video_toggle)
    TextView txt_video_toggle;
    @BindView(R.id.video_toggle)
    ToggleButton video_toggle;

    public PushOrAssignFragment() {
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
        return inflater.inflate(R.layout.fragment_push_or_assign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_assign)
    public void onAssignClick() {
        Intent intent = new Intent(getActivity(), Activity_AssignGroups.class);
        startActivityForResult(intent, 1);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.btn_push)
    public void onPushClick() {
        /*Intent intent = new Intent(getActivity(), PushDataActivity.class);
        startActivityForResult(intent, 1);
        getActivity().startActivity(intent);*/
        new PushDataToServer(getActivity(), false).execute();

    }

    @OnClick(R.id.btn_clear_data)
    public void onClear() {
        AlertDialog clearDataDialog = new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Clear Data")
                .setMessage("Are you sure you want to clear everything ?")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        clearData();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        clearDataDialog.show();
        clearDataDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }

    private void clearData() {
        AppDatabase appDatabase = AppDatabase.getDatabaseInstance(getActivity());
        appDatabase.getVillageDao().deleteAllVillages();
        appDatabase.getGroupsDao().deleteAllGroups();
        appDatabase.getStudentDao().deleteAll();
        appDatabase.getCrlDao().deleteAll();
    }

    @OnClick(R.id.video_toggle)
    public void onVideoToggle() {
        if (video_toggle.isChecked()) {
//            txt_video_toggle.setText("Video monitoring is on");
            Assessment_Constants.VIDEOMONITORING = true;
        } else {
//            txt_video_toggle.setText("Video monitoring is Off");
            Assessment_Constants.VIDEOMONITORING = false;

        }

    }

    /*@OnClick(R.id.btn_pull_exam_data)
    public void onPullExam() {
        Intent intent = new Intent(getActivity(), PushDataActivity.class);
        startActivityForResult(intent, 1);
        getActivity().startActivity(intent);
    }*/
}
