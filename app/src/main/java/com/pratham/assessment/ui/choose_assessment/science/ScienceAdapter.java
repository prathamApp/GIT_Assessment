package com.pratham.assessment.ui.choose_assessment.science;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pratham.assessment.R;
import com.pratham.assessment.domain.ScienceModalClass;

import java.util.List;

public class ScienceAdapter extends RecyclerView.Adapter<ScienceAdapter.MyViewHolder> {
    Context context;
    List<ScienceModalClass> scienceModalClassList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView que_cnt;
        LinearLayout queAns;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.tv_science_question_title);
            que_cnt = itemView.findViewById(R.id.tv_que_cnt);
            queAns = itemView.findViewById(R.id.ll_que_ans);
        }
    }

    public ScienceAdapter(Context context, List<ScienceModalClass> scienceModalClassList) {
        this.context = context;
        this.scienceModalClassList = scienceModalClassList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_science_row, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        ScienceModalClass scienceModalClass = scienceModalClassList.get(i);
        myViewHolder.que_cnt.setText(i + 1 + "/" + scienceModalClassList.size());
        String questionType = scienceModalClass.getType();

        switch (questionType) {
            case "trueOrFalse":
//                myViewHolder.question.setText(scienceModalClass.getQuestion());
                TextView textView = new TextView(context);
                textView.setText(scienceModalClass.getQuestion());
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45);
                textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
                params.setMargins(10, 0, 0, 0);
                textView.setLayoutParams(params);
                myViewHolder.queAns.addView(textView);
                RadioGroup radioGroup = new RadioGroup(context);

                radioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
                RadioButton radioButtonTrue = new RadioButton(context);
//                    radioButton.setId(r);
                radioButtonTrue.setText("True");
                radioGroup.addView(radioButtonTrue);
                RadioButton radioButtonFalse = new RadioButton(context);
//                    radioButton.setId(r);
                radioButtonFalse.setText("False");
                radioGroup.addView(radioButtonFalse);
                myViewHolder.queAns.addView(radioGroup);
                break;


                       /* radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                                if (isSelected) {
                                    dde_questions.setAnswer(compoundButton.getTag().toString());
                                    LinearLayout layout = (LinearLayout) compoundButton.getParent().getParent();
                                    String tag = (String) layout.getTag();
                                    checkRuleCondition(tag, compoundButton.getTag().toString(), "singlechoice");
                                }
                            }
                        });*/
        }
//                    radioGroup.setLayoutParams(params);

    }


    @Override
    public int getItemCount() {
        return scienceModalClassList.size();
    }


}
