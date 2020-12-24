package com.pratham.assessment.ui.choose_assessment.science.bottomFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.adapters.QuestionTrackerAdapter;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTrackerListener;
import com.pratham.assessment.utilities.Assessment_Utility;

import java.util.List;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;*/


public class BottomQuestionFragment extends DialogFragment {


    //    @BindView(R.id.rv_questions)
    RecyclerView rvQuestion;
    //    @BindView(R.id.btn_save)
    Button saveAssessment;
    //    @BindView(R.id.tv_not_answered_color)
    TextView tv_not_answered_color;
    //    @BindView(R.id.tv_answered_color)
    TextView tv_answered_color;
    //    @BindView(R.id.rl_root)
    RelativeLayout rl_root;
    /*Context context;*/
    List<ScienceQuestion> scienceQuestionList;
    QuestionTrackerListener questionTrackerListener;



   /* public BottomQuestionFragment(Context context, List<ScienceQuestion> scienceQuestionList) {
        this.context = context;
        this.scienceQuestionList = scienceQuestionList;
    }*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_list_fragment, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ButterKnife.bind(this, view);
        scienceQuestionList = (List<ScienceQuestion>) getArguments().get("questionList");

        rvQuestion = view.findViewById(R.id.rv_questions);
        saveAssessment = view.findViewById(R.id.btn_save);
        tv_not_answered_color = view.findViewById(R.id.tv_not_answered_color);
        tv_answered_color = view.findViewById(R.id.tv_answered_color);
        rl_root = view.findViewById(R.id.rl_root);
        rl_root.setBackgroundColor(Assessment_Utility.selectedColor);

        saveAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionTrackerListener.onSaveAssessmentClick();
            }
        });
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


   /* @OnClick(R.id.btn_save)
    public void addStudent() {
//        getActivity().finish();
        questionTrackerListener.onSaveAssessmentClick();
//        SplashActivity.fragmentAddStudentOpenFlg = true;
//        AddStudentFragment addStudentFragment = AddStudentFragment.newInstance(this);
//        addStudentFragment.show(getActivity().getSupportFragmentManager(), AddStudentFragment.class.getSimpleName());

    }*/

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


    /*public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog alertD = builder.create();
//        alertD.setView(view);
        Dialog dialog = alertD.create();
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        //Show the dialog!
        dialog.show();

        //Set the dialog to immersive sticky mode
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //Clear the not focusable flag from the window
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        return alertD;
    }*/


  /*  private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }*/


}
