package com.pratham.assessment.ui.choose_assessment.science;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;

import java.util.ArrayList;
import java.util.List;

/*
        1	Multiple Choice  *
        2	Multiple Select *
        3	True or False *
        4	Matching Pair
        5	Fill-in-the-blank (With Option) *
        6	Fill-in-the-blank *
        7	Arrange sequence
        8	video
        9	audio
*/

public class ScienceAdapter extends RecyclerView.Adapter<ScienceAdapter.MyViewHolder> {
    Context context;
    List<ScienceQuestion> scienceQuestionList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        LinearLayout audioControls, llAnswer, ll_match_pair;
        ImageView questionImage, playVideo, playPause;
        SeekBar seekAudio;
        EditText answerEditText;
        RecyclerView optionsRecycler1, optionsRecycler2;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_question);
            questionImage = itemView.findViewById(R.id.tv_question_image);
            playVideo = itemView.findViewById(R.id.iv_play_video);
            audioControls = itemView.findViewById(R.id.ll_audio_conrols);
            playPause = itemView.findViewById(R.id.iv_audio_play_pause);
            seekAudio = itemView.findViewById(R.id.seek_audio);
            answerEditText = itemView.findViewById(R.id.et_answer);
            optionsRecycler1 = itemView.findViewById(R.id.rl_ans_options1);
            optionsRecycler2 = itemView.findViewById(R.id.rl_ans_options2);
            llAnswer = itemView.findViewById(R.id.ll_ans);
            ll_match_pair = itemView.findViewById(R.id.ll_match_pair);
        }
    }

    public ScienceAdapter(Context context, List<ScienceQuestion> scienceQuestionList) {
        this.context = context;
        this.scienceQuestionList = scienceQuestionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_science_question_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ScienceQuestion scienceQuestion = scienceQuestionList.get(i);
//        myViewHolder.que_cnt.setText(i + 1 + "/" + scienceQuestionList.size());
        String questionType = scienceQuestion.getQtid();
        RadioGroup radioGroup;

        myViewHolder.question.setText(scienceQuestion.getQname());
        myViewHolder.llAnswer.removeAllViews();

        switch (questionType) {
            case "1":
                myViewHolder.answerEditText.setVisibility(View.GONE);
                List<ScienceQuestionChoice> options = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
                if (options != null) {
                    radioGroup = new RadioGroup(context);
                    radioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
                    for (int r = 0; r < options.size(); r++) {
                        RadioButton radioButton = new RadioButton(context);
                        radioButton.setId(r);
                        radioButton.setText(options.get(r).getChoicename());
                        radioGroup.addView(radioButton);
                    }
                    myViewHolder.llAnswer.addView(radioGroup);
                }
                break;
            case "2":
                myViewHolder.answerEditText.setVisibility(View.GONE);
                List<ScienceQuestionChoice> choices = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());

                GridLayout gridLayout = new GridLayout(context);
                gridLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
                gridLayout.setColumnCount(1);

                for (int j = 0; j < choices.size(); j++) {
                    final CheckBox checkBox = new CheckBox(context);
                    checkBox.setText(choices.get(j).getChoicename());
                    GridLayout.LayoutParams paramGrid = new GridLayout.LayoutParams();
                    paramGrid.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    paramGrid.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    paramGrid.setGravity(Gravity.FILL_HORIZONTAL);
                    checkBox.setLayoutParams(paramGrid);
                    gridLayout.addView(checkBox);
                }
                myViewHolder.llAnswer.addView(gridLayout);
                break;
            case "3":
                myViewHolder.answerEditText.setVisibility(View.GONE);
                RadioButton radioButtonTrue = new RadioButton(context);
                radioButtonTrue.setText("True");
                radioGroup = new RadioGroup(context);
                radioGroup.addView(radioButtonTrue);
                RadioButton radioButtonFalse = new RadioButton(context);
                radioButtonFalse.setText("False");
                radioGroup.addView(radioButtonFalse);
                myViewHolder.llAnswer.addView(radioGroup);
                break;

            case "4":
                myViewHolder.answerEditText.setVisibility(View.GONE);
                myViewHolder.ll_match_pair.setVisibility(View.VISIBLE);
                List list1 = new ArrayList();
                List list2 = new ArrayList();
                List<ScienceQuestionChoice> pairList=new ArrayList<>();

                try {

                    pairList = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int p = 0; p < pairList.size(); p++) {
                    list1.add(pairList.get(p).getChoicename());
                    list2.add(pairList.get(p).getMatchingname());
                }
                MatchPairAdapter matchPairAdapter = new MatchPairAdapter(list1, context);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                myViewHolder.optionsRecycler1.setLayoutManager(linearLayoutManager);
                myViewHolder.optionsRecycler1.setAdapter(matchPairAdapter);
                break;
            case "5":
                //  textView = createQuestionTextView();
//                textView.setText(scienceQuestion.getQuestion());
//                myViewHolder.queAns.addView(textView);

                /*  List<ScienceQuestionChoice> options=scienceQuestion.getLstquestionchoice();
                if(options.size()>0){
                    radioGroup = new RadioGroup(context);
                    radioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
                    for (int r = 0; r < options.size(); r++) {
                        RadioButton radioButton = new RadioButton(context);
                        radioButton.setId(r);

                        radioButton.setText(options.get(r).getChoicename());
                        radioGroup.addView(radioButton);

                    }
                    myViewHolder.queAns.addView(radioGroup);
                }else {
                    EditText etAns = new EditText(context);
                    etAns.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    etAns.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    etAns.setHint("Enter answer");
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                    etAns.setBackground(context.getResources().getDrawable(R.drawable.answered_ece_card));
                    params.setMargins(10, 10, 10, 10);
                    etAns.setLayoutParams(params);
                    myViewHolder.queAns.addView(etAns);
                }*/
                break;
            case "6":
                myViewHolder.answerEditText.setVisibility(View.VISIBLE);
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                break;

        }
    }

    @Override
    public int getItemCount() {
        return scienceQuestionList.size();
    }


}
