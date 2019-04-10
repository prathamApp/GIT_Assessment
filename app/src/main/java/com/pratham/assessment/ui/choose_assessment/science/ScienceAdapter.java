package com.pratham.assessment.ui.choose_assessment.science;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        TextView textView;                RadioGroup radioGroup;


        switch (questionType) {
            case "trueOrFalse":
                textView = createQuestionTextView();
                textView.setText(scienceModalClass.getQuestion());
                myViewHolder.queAns.addView(textView);

                radioGroup = new RadioGroup(context);
                radioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
                RadioButton radioButtonTrue = new RadioButton(context);
                radioButtonTrue.setText("True");
                radioGroup.addView(radioButtonTrue);
                RadioButton radioButtonFalse = new RadioButton(context);
                radioButtonFalse.setText("False");
                radioGroup.addView(radioButtonFalse);
                myViewHolder.queAns.addView(radioGroup);
                break;

            case "fillInTheBlanks":
                textView = createQuestionTextView();
                textView.setText(scienceModalClass.getQuestion());
                myViewHolder.queAns.addView(textView);

                String[] options=scienceModalClass.getOptions();
                if(options.length>0){
                    radioGroup = new RadioGroup(context);
                    radioGroup.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_rectangle));
                    for (int r = 0; r < options.length; r++) {
                        RadioButton radioButton = new RadioButton(context);
                        radioButton.setId(r);

                        radioButton.setText(options[r]);
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
                }
}
    }

    private TextView createQuestionTextView() {
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45);
        textView.setTextColor(context.getResources().getColor(R.color.colorBlack));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        params.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(params);
        return textView;
    }


    @Override
    public int getItemCount() {
        return scienceModalClassList.size();
    }


}
