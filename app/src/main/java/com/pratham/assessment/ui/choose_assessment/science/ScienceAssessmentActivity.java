package com.pratham.assessment.ui.choose_assessment.science;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratham.assessment.R;
import com.pratham.assessment.discrete_view.DSVOrientation;
import com.pratham.assessment.discrete_view.DiscreteScrollView;
import com.pratham.assessment.discrete_view.ScaleTransformer;
import com.pratham.assessment.domain.ScienceModalClass;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScienceAssessmentActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener {
    List<ScienceModalClass> scienceModalClassList;
    @BindView(R.id.attendance_recycler_view)
    DiscreteScrollView discreteScrollView;

  /*  @BindView(R.id.ll_root)
    LinearLayout ll_root;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science_assessment);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scienceModalClassList = fetchJson("science.json");

        setQuestions();
        ScienceAdapter scienceAdapter = new ScienceAdapter(this, scienceModalClassList);
        discreteScrollView.setOrientation(DSVOrientation.HORIZONTAL);
        discreteScrollView.addOnItemChangedListener(this);
        discreteScrollView.setItemTransitionTimeMillis(200);
        discreteScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.5f)
                .build());
        discreteScrollView.setAdapter(scienceAdapter);
        scienceAdapter.notifyDataSetChanged();
    }

    private void setQuestions() {


        for (int i = 0; i < scienceModalClassList.size(); i++) {

            final LinearLayout layout = new LinearLayout(this);
            layout.setPadding(10, 10, 10, 50);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setTag(scienceModalClassList.get(i).getQuestionId());
            TextView textView = new TextView(this);
            textView.setText(scienceModalClassList.get(i).getQuestion());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            params.setMargins(10, 0, 0, 0);
            textView.setLayoutParams(params);
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.ripple_rectangle));

            layout.addView(textView);

            LinearLayout.LayoutParams paramsWrapContaint = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0);
            paramsWrapContaint.setMargins(10, 0, 0, 0);

            String questionType = scienceModalClassList.get(i).getType();
            switch (questionType) {
                case "singlechoice":
                    String[] options = scienceModalClassList.get(i).getOptions();
                    RadioGroup radioGroup = new RadioGroup(this);
                    radioGroup.setBackground(ContextCompat.getDrawable(this, R.drawable.ripple_rectangle));
                    for (int r = 0; r < options.length; r++) {
                        RadioButton radioButton = new RadioButton(this);
                        radioButton.setId(r);

                        radioButton.setText(options[r]);
                        radioGroup.addView(radioButton);
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
                    layout.addView(radioGroup);


                    break;
                case "multiple":
                    String[] multipleOptions = scienceModalClassList.get(i).getOptions();
                    for (int r = 0; r < multipleOptions.length; r++) {
                        CheckBox checkBox = new CheckBox(this);
                        checkBox.setId(r);

                        checkBox.setText(multipleOptions[r]);
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
                        layout.addView(checkBox);

                    }
//                    radioGroup.setLayoutParams(params);

                    break;
                case "fillblank":
                    break;
                case "multipleimages":

                    break;
            }
            View view = new View(this);
            view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            LinearLayout.LayoutParams view_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2, 0);
            view_params.setMargins(0, 10, 0, 10);
            view.setLayoutParams(view_params);
            layout.addView(view);
//            ll_root.addView(layout);
        }
    }

    private List<ScienceModalClass> fetchJson(String jasonName) {
        List<ScienceModalClass> scienceModalClasses = new ArrayList<>();
        try {
            //InputStream is = new FileInputStream(COSApplication.pradigiPath + "/.LLA/English/RC/" + jasonName);
            InputStream is = this.getAssets().open("" + jasonName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ScienceModalClass>>() {
            }.getType();
            scienceModalClasses = gson.fromJson(jsonStr, listType);

            // jsonArr = new JSONArray(jsonStr);
            //returnStoryNavigate = jsonObj.getJSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scienceModalClasses;
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }
}
