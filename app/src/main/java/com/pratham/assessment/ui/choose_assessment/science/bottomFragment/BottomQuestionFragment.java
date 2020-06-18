package com.pratham.assessment.ui.choose_assessment.science.bottomFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.adapters.QuestionTrackerAdapter;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTrackerListener;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BottomQuestionFragment extends BottomSheetDialogFragment {


    @BindView(R.id.rv_questions)
    RecyclerView rvQuestion;
    @BindView(R.id.btn_save)
    Button saveAssessment;
    /*Context context;*/
    List<ScienceQuestion> scienceQuestionList;
    QuestionTrackerListener questionTrackerListener;

   /* public BottomQuestionFragment(Context context, List<ScienceQuestion> scienceQuestionList) {
        this.context = context;
        this.scienceQuestionList = scienceQuestionList;
    }*/


      /*  @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NORMAL, R.style.AppModalStyle);
        }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_list_fragment, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this, view);
        scienceQuestionList = (List<ScienceQuestion>) getArguments().get("questionList");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        questionTrackerListener = (QuestionTrackerListener) getActivity();
        QuestionTrackerAdapter questionTrackerAdapter = new QuestionTrackerAdapter(this, getActivity(), scienceQuestionList);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 5);
        rvQuestion.setLayoutManager(linearLayoutManager);
        rvQuestion.setAdapter(questionTrackerAdapter);

    }

//    private void setStudentsToRecycler() {
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onPause() {
        super.onPause();
//        SplashActivity.fragmentBottomPauseFlg = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
       /* try {
            getActivity().onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @OnClick(R.id.btn_save)
    public void addStudent() {
//        getActivity().finish();
        questionTrackerListener.onSaveAssessmentClick();
//        SplashActivity.fragmentAddStudentOpenFlg = true;
//        AddStudentFragment addStudentFragment = AddStudentFragment.newInstance(this);
//        addStudentFragment.show(getActivity().getSupportFragmentManager(), AddStudentFragment.class.getSimpleName());

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("BottomSheetCancel", "onCancel: aaa");
    }

    @Override
    public void onResume() {
        super.onResume();
//        SplashActivity.fragmentBottomPauseFlg = false;
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

}
