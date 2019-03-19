package com.pratham.assessment.admin_pannel.PushOrAssign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pratham.assessment.R;
import com.pratham.assessment.async.PushDataToServer;
import com.pratham.assessment.async.PushDataToServerold;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PushOrAssignFragment extends Fragment {
    @BindView(R.id.btn_assign)
    Button assign;
    @BindView(R.id.btn_push)
    Button push;

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
        new PushDataToServer(getActivity(),false).execute();

    }
}
