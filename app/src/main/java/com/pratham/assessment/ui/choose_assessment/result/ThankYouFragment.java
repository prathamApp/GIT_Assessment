package com.pratham.assessment.ui.choose_assessment.result;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratham.assessment.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_thank_you)
public class ThankYouFragment extends Fragment {

    @ViewById(R.id.tv_name)
    TextView studentName;

    @ViewById(R.id.tv_thanks)
    TextView thanks;

    public ThankYouFragment() {
        // Required empty public constructor
    }


    @AfterViews
    public void init() {
        String studName = getArguments().getString("studentName");
        studentName.setText(studName);

    }

    @Click(R.id.btn_ok)
    public void onOkCLick() {
        getActivity().finish();

    }
}
