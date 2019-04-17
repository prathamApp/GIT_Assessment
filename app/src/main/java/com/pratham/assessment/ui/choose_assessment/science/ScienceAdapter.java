package com.pratham.assessment.ui.choose_assessment.science;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.ArrangeSequenceViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.FillInTheBlanksWithoutOptionsViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.MatchThePairViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.McqFillInTheBlanksViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.MultipleSelectViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.TrueFalseViewHolder;

import java.util.List;

/*
        1	Multiple Choice  *
        2	Multiple Select *
        3	True or False *
        4	Matching Pair*
        5	Fill-in-the-blank (With Option) *
        6	Fill-in-the-blank *
        7	Arrange sequence
        8	video
        9	audio
*/

public class ScienceAdapter extends RecyclerView.Adapter {
    Context context;
    List<ScienceQuestion> scienceQuestionList;
    String loadImagePath = "http://pef1.prathamskills.org//";
    public static final int MULTIPLE_CHOICE = 1;
    public static final int MULTIPLE_SELECT = 2;
    public static final int TRUE_FALSE = 3;
    public static final int MATCHING_PAIR = 4;
    public static final int FILL_IN_THE_BLANK_WITH_OPTION = 5;
    public static final int FILL_IN_THE_BLANK = 6;
    public static final int ARRANGE_SEQUENCE = 7;
    public static final int VIDEO = 8;
    public static final int AUDIO = 9;

    public class MyViewHolder extends ViewHolder {
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

    @Override
    public int getItemViewType(int position) {
        String questionType = scienceQuestionList.get(position).getQtid();
        switch (questionType) {
            case "0":
                return super.getItemViewType(position);
            case "1":
                return MULTIPLE_CHOICE;
            case "2":
                return MULTIPLE_SELECT;
            case "3":
                return TRUE_FALSE;
            case "4":
                return MATCHING_PAIR;
            case "5":
                return FILL_IN_THE_BLANK_WITH_OPTION;
            case "6":
                return FILL_IN_THE_BLANK;
            case "7":
                return ARRANGE_SEQUENCE;
            case "8":
                return VIDEO;
            case "9":
                return AUDIO;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v;

        switch (viewType) {
            case MULTIPLE_CHOICE:
                LayoutInflater mcq = LayoutInflater.from(viewGroup.getContext());
                v = mcq.inflate(R.layout.layout_mcq_fill_in_the_blanks_with_options_row, viewGroup, false);
                return new McqFillInTheBlanksViewHolder(v, context);

            case MULTIPLE_SELECT:
                LayoutInflater multipleSelect = LayoutInflater.from(viewGroup.getContext());
                v = multipleSelect.inflate(R.layout.layout_multiple_select_row, viewGroup, false);
                return new MultipleSelectViewHolder(v, context);

            case TRUE_FALSE:
                LayoutInflater trueFalse = LayoutInflater.from(viewGroup.getContext());
                v = trueFalse.inflate(R.layout.layout_true_false_row, viewGroup, false);
                return new TrueFalseViewHolder(v, context);

            case MATCHING_PAIR:
                LayoutInflater matchPair = LayoutInflater.from(viewGroup.getContext());
                v = matchPair.inflate(R.layout.layout_match_the_pair_row, viewGroup, false);
                return new MatchThePairViewHolder(v, context);

            case FILL_IN_THE_BLANK:
                LayoutInflater fill = LayoutInflater.from(viewGroup.getContext());
                v = fill.inflate(R.layout.layout_fill_in_the_blanks_wo_option_row, viewGroup, false);
                return new FillInTheBlanksWithoutOptionsViewHolder(v, context);

            case FILL_IN_THE_BLANK_WITH_OPTION:
                LayoutInflater fillInWithOption = LayoutInflater.from(viewGroup.getContext());
                v = fillInWithOption.inflate(R.layout.layout_mcq_fill_in_the_blanks_with_options_row, viewGroup, false);
                return new McqFillInTheBlanksViewHolder(v, context);

            case ARRANGE_SEQUENCE:
                LayoutInflater arrangeSeq = LayoutInflater.from(viewGroup.getContext());
                v = arrangeSeq.inflate(R.layout.layout_arrange_seq_row, viewGroup, false);
                return new ArrangeSequenceViewHolder(v, context);

           /* case VIDEO:
                LayoutInflater video = LayoutInflater.from(viewGroup.getContext());
                v = video.inflate(R.layout.item_content_header, viewGroup, false);
                return new EmptyHolder(v);

            case AUDIO:
                LayoutInflater audio = LayoutInflater.from(viewGroup.getContext());
                v = audio.inflate(R.layout.item_content_header, viewGroup, false);
                return new EmptyHolder(v);*/

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder myViewHolder, int i) {
        ScienceQuestion scienceQuestion = scienceQuestionList.get(i);
        String questionType = scienceQuestion.getQtid();

        switch (Integer.parseInt(questionType)) {
            case 0:
                break;
            case MULTIPLE_CHOICE:
                McqFillInTheBlanksViewHolder mcqFillInTheBlanksViewHolder = (McqFillInTheBlanksViewHolder) myViewHolder;
                mcqFillInTheBlanksViewHolder.setMcqsQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;
            case MULTIPLE_SELECT:
                MultipleSelectViewHolder multipleSelectViewHolder = (MultipleSelectViewHolder) myViewHolder;
                multipleSelectViewHolder.setMultipleSelectQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;
            case TRUE_FALSE:
                TrueFalseViewHolder trueFalseViewHolder = (TrueFalseViewHolder) myViewHolder;
                trueFalseViewHolder.setTrueFalseQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;

            case MATCHING_PAIR:
                MatchThePairViewHolder matchThePairViewHolder = (MatchThePairViewHolder) myViewHolder;
                matchThePairViewHolder.setMatchPairQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;

            case FILL_IN_THE_BLANK:
                FillInTheBlanksWithoutOptionsViewHolder fill = (FillInTheBlanksWithoutOptionsViewHolder) myViewHolder;
                fill.setFillInTheBlanksQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;

            case FILL_IN_THE_BLANK_WITH_OPTION:
                McqFillInTheBlanksViewHolder mcqFillInTheBlanksViewHolder1 = (McqFillInTheBlanksViewHolder) myViewHolder;
                mcqFillInTheBlanksViewHolder1.setMcqsQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;

            case ARRANGE_SEQUENCE:
                ArrangeSequenceViewHolder arrangeSequenceViewHolder = (ArrangeSequenceViewHolder) myViewHolder;
                arrangeSequenceViewHolder.setArrangeSeqQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;

            /*case VIDEO:
                MultipleSelectViewHolder multipleSelectViewHolder = (MultipleSelectViewHolder) myViewHolder;
                multipleSelectViewHolder.setMultipleSelectQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;

            case AUDIO:
                MultipleSelectViewHolder multipleSelectViewHolder = (MultipleSelectViewHolder) myViewHolder;
                multipleSelectViewHolder.setMultipleSelectQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                break;*/

        }
    }


//        myViewHolder.que_cnt.setText(i + 1 + "/" + scienceQuestionList.size());
    //       String questionType = scienceQuestion.getQtid();
    //      RadioGroup radioGroup;

     /*   myViewHolder.question.setText(scienceQuestion.getQname());
        if (!scienceQuestion.getPhotourl().equalsIgnoreCase("")) {


            Glide.with(context).asBitmap().
                    load(loadImagePath +).into(new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    Drawable bd = new BitmapDrawable(resource);
                    myViewHolder.questionImage.setCompoundDrawablesWithIntrinsicBounds(null, null, bd, null);
                }
            });


        }
        myViewHolder.llAnswer.removeAllViews();

        switch (questionType) {
            case "1":
                myViewHolder.ll_match_pair.setVisibility(View.GONE);
                myViewHolder.answerEditText.setVisibility(View.GONE);
                List<ScienceQuestionChoice> options = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
                if (options != null) {
                    radioGroup = new RadioGroup(context);
                    radioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
                    for (int r = 0; r < options.size(); r++) {
                        final RadioButton radioButton = new RadioButton(context);
                        radioButton.setId(r);
                        if (!options.get(r).getChoiceurl().equalsIgnoreCase("")) {

                            int myWidth = 100;
                            int myHeight = 100;
                            Glide.with(context).asBitmap().
                                    load(loadImagePath + options.get(r).getChoiceurl()).into(new SimpleTarget<Bitmap>(myWidth, myHeight) {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    Drawable bd = new BitmapDrawable(resource);
                                    radioButton.setCompoundDrawablesWithIntrinsicBounds(null, null, bd, null);
                                }
                            });
                        } else
                            radioButton.setText(options.get(r).getChoicename());
                        radioGroup.addView(radioButton);

                    }
                    myViewHolder.llAnswer.addView(radioGroup);
                }
                break;
            case "2":
                myViewHolder.ll_match_pair.setVisibility(View.GONE);
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
                myViewHolder.ll_match_pair.setVisibility(View.GONE);

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

                myViewHolder.llAnswer.setVisibility(View.VISIBLE);
                myViewHolder.answerEditText.setVisibility(View.GONE);
                myViewHolder.ll_match_pair.setVisibility(View.VISIBLE);
                myViewHolder.optionsRecycler1.setVisibility(View.VISIBLE);
                myViewHolder.optionsRecycler2.setVisibility(View.VISIBLE);
                List list1 = new ArrayList();
                List list2 = new ArrayList();
                List<ScienceQuestionChoice> pairList = new ArrayList<>();

                try {

                    pairList = AppDatabase.getDatabaseInstance(context).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
                    Log.d("wwwwwwwwwww", pairList.size() + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int p = 0; p < pairList.size(); p++) {
                    list1.add(pairList.get(p).getChoicename());
                    list2.add(pairList.get(p).getMatchingname());
                }
                MatchPairAdapter matchPairAdapter = new MatchPairAdapter(list1, context);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context.getApplicationContext());
                myViewHolder.optionsRecycler1.setLayoutManager(linearLayoutManager);
                myViewHolder.optionsRecycler1.setAdapter(matchPairAdapter);

                Collections.shuffle(list2);
                DragDropAdapter dragDropAdapter = new DragDropAdapter(list2);
                ItemTouchHelper.Callback callback =
                        new ItemMoveCallback(dragDropAdapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(myViewHolder.optionsRecycler2);
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context.getApplicationContext());
                myViewHolder.optionsRecycler2.setLayoutManager(linearLayoutManager1);
                myViewHolder.optionsRecycler2.setAdapter(dragDropAdapter);
                Log.d("wwwwwwwwwww", pairList.size() + "");


                break;
            case "5":*/
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
               /* break;
            case "6":
                myViewHolder.ll_match_pair.setVisibility(View.GONE);
                myViewHolder.answerEditText.setVisibility(View.VISIBLE);
                break;
            case "7":
                break;
            case "8":
                break;
            case "9":
                break;

        }
}*/

    @Override
    public int getItemCount() {
        return scienceQuestionList.size();
    }


}
