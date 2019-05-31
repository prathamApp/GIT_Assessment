package com.pratham.assessment.ui.choose_assessment.science.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.DragDropListener;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.QuestionTypeListener;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.ArrangeSequenceViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.FillInTheBlanksWithoutOptionsViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.MatchThePairViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.McqFillInTheBlanksViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.MultipleSelectViewHolder;
import com.pratham.assessment.ui.choose_assessment.science.viewHolders.TrueFalseViewHolder;

import java.util.List;

/*
    []    1	Multiple Choice  * *
    []    2	Multiple Select * *
        3	True or False * *
     []   4	Matching Pair*
    []    5	Fill-in-the-blank (With Option) * *
        6	Fill-in-the-blank * *
    []    7	Arrange sequence
        8	video
        9	audio
*/

public class ScienceAdapter extends RecyclerView.Adapter implements QuestionTypeListener, DragDropListener {
    Context context;
    List<ScienceQuestion> scienceQuestionList;
    QuestionTypeListener questionTypeListener;
    AssessmentAnswerListener assessmentAnswerListener;
    String answer = "";
    int listSize = 0;
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

    @Override
    public void setType(int QType, String answer) {

    }

    @Override
    public void setAnswer(String ansId, String answer, String qid,List<ScienceQuestionChoice> list) {

            this.answer = answer;
            assessmentAnswerListener.setAnswerInActivity(ansId, answer,qid,list);
    }

    @Override
    public void setList(List<ScienceQuestionChoice> list,String qid) {
        assessmentAnswerListener.setAnswerInActivity("", list.toString(), qid, list);
    }

   /* public class MyViewHolder extends ViewHolder {
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
*/
    public ScienceAdapter(Context context, List<ScienceQuestion> scienceQuestionList) {
        this.context = context;
        this.scienceQuestionList = scienceQuestionList;
        this.assessmentAnswerListener = (AssessmentAnswerListener) context;
        listSize = scienceQuestionList.size();
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
                return new McqFillInTheBlanksViewHolder(v, context, this);

            case MULTIPLE_SELECT:
                LayoutInflater multipleSelect = LayoutInflater.from(viewGroup.getContext());
                v = multipleSelect.inflate(R.layout.layout_multiple_select_row, viewGroup, false);
                return new MultipleSelectViewHolder(v, context, this);

            case TRUE_FALSE:
                LayoutInflater trueFalse = LayoutInflater.from(viewGroup.getContext());
                v = trueFalse.inflate(R.layout.layout_true_false_row, viewGroup, false);
                return new TrueFalseViewHolder(v, context, this);

            case MATCHING_PAIR:
                LayoutInflater matchPair = LayoutInflater.from(viewGroup.getContext());
                v = matchPair.inflate(R.layout.layout_match_the_pair_row, viewGroup, false);
                return new MatchThePairViewHolder(v, context, this);

            case FILL_IN_THE_BLANK:
                LayoutInflater fill = LayoutInflater.from(viewGroup.getContext());
                v = fill.inflate(R.layout.layout_fill_in_the_blanks_wo_option_row, viewGroup, false);
                return new FillInTheBlanksWithoutOptionsViewHolder(v, context, this);

            case FILL_IN_THE_BLANK_WITH_OPTION:
                LayoutInflater fillInWithOption = LayoutInflater.from(viewGroup.getContext());
                v = fillInWithOption.inflate(R.layout.layout_mcq_fill_in_the_blanks_with_options_row, viewGroup, false);
                return new McqFillInTheBlanksViewHolder(v, context, this);

            case ARRANGE_SEQUENCE:
                LayoutInflater arrangeSeq = LayoutInflater.from(viewGroup.getContext());
                v = arrangeSeq.inflate(R.layout.layout_arrange_seq_row, viewGroup, false);
                return new ArrangeSequenceViewHolder(v, context,this);

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
                // questionTypeListener.setType(MULTIPLE_CHOICE, answer);
                break;
            case MULTIPLE_SELECT:
                MultipleSelectViewHolder multipleSelectViewHolder = (MultipleSelectViewHolder) myViewHolder;
                multipleSelectViewHolder.setMultipleSelectQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                //  questionTypeListener.setType(MULTIPLE_SELECT, answer);
                break;
            case TRUE_FALSE:
                myViewHolder.setIsRecyclable(false);
                TrueFalseViewHolder trueFalseViewHolder = (TrueFalseViewHolder) myViewHolder;
                trueFalseViewHolder.setTrueFalseQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                //   questionTypeListener.setType(TRUE_FALSE, answer);

                break;

            case MATCHING_PAIR:
                MatchThePairViewHolder matchThePairViewHolder = (MatchThePairViewHolder) myViewHolder;
                matchThePairViewHolder.setMatchPairQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                //     questionTypeListener.setType(MATCHING_PAIR, answer);

                break;

            case FILL_IN_THE_BLANK:
                FillInTheBlanksWithoutOptionsViewHolder fill = (FillInTheBlanksWithoutOptionsViewHolder) myViewHolder;
                fill.setFillInTheBlanksQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                //      questionTypeListener.setType(FILL_IN_THE_BLANK, answer);

                break;

            case FILL_IN_THE_BLANK_WITH_OPTION:
                McqFillInTheBlanksViewHolder mcqFillInTheBlanksViewHolder1 = (McqFillInTheBlanksViewHolder) myViewHolder;
                mcqFillInTheBlanksViewHolder1.setMcqsQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                //       questionTypeListener.setType(FILL_IN_THE_BLANK_WITH_OPTION, answer);

                break;

            case ARRANGE_SEQUENCE:
                ArrangeSequenceViewHolder arrangeSequenceViewHolder = (ArrangeSequenceViewHolder) myViewHolder;
                arrangeSequenceViewHolder.setArrangeSeqQuestion(scienceQuestion, myViewHolder.getAdapterPosition());
                //        questionTypeListener.setType(ARRANGE_SEQUENCE, answer);

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

    @Override
    public int getItemCount() {
        return scienceQuestionList.size();
    }


}
