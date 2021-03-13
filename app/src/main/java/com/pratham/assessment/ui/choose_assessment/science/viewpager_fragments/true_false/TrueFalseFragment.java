package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.true_false;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pratham.assessment.AssessmentApplication;
import com.pratham.assessment.R;
import com.pratham.assessment.custom.gif_viewer.GifView;
import com.pratham.assessment.database.AppDatabase;
import com.pratham.assessment.domain.ScienceQuestion;
import com.pratham.assessment.domain.ScienceQuestionChoice;
import com.pratham.assessment.ui.choose_assessment.science.ScienceAssessmentActivity;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.AssessmentAnswerListener;
import com.pratham.assessment.constants.Assessment_Constants;
import com.pratham.assessment.utilities.Assessment_Utility;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.pratham.assessment.utilities.Assessment_Utility.getFileName;
import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_true_false_row)
public class TrueFalseFragment extends Fragment implements TrueFalseContract.TrueFalseView {
    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.rg_true_false)
    RadioGroup rg_true_false;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.rb_true)
    Button radioButtonTrue;
    @ViewById(R.id.rb_false)
    Button radioButtonFalse;
    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;
    private List<ScienceQuestionChoice> options;

    AssessmentAnswerListener assessmentAnswerListener;

    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;

    @Bean(TrueFalsePresenter.class)
    TrueFalseContract.TrueFalsePresenter presenter;

    public TrueFalseFragment() {
        // Required empty public constructor
    }

    @AfterViews
    public void init() {
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();
        }
        presenter.setView(TrueFalseFragment.this);
        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        setTrueFalseQuestion();

    }

    public static TrueFalseFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        TrueFalseFragment_ trueFalseFragment = new TrueFalseFragment_();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        args.putSerializable("scienceQuestion", scienceQuestion);
        trueFalseFragment.setArguments(args);
        return trueFalseFragment;
    }

/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
            assessmentAnswerListener = (ScienceAssessmentActivity) getActivity();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_true_false_row, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setTrueFalseQuestion();
    }
*/

    public void setTrueFalseQuestion() {
        options = new ArrayList<>();

        setOdiaFont(getActivity(), question);
        question.setText(Html.fromHtml(scienceQuestion.getQname()));
        String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
        final String localPath;
        if (scienceQuestion.getIsQuestionFromSDCard())
            localPath = scienceQuestion.getPhotourl();
        else
            localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;

        if (scienceQuestion.getPhotourl() != null && !scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);
//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {



            String path = scienceQuestion.getPhotourl();
            String[] imgPath = path.split("\\.");
            int len;
            if (imgPath.length > 0)
                len = imgPath.length - 1;
            else len = 0;
            if (imgPath[len].equalsIgnoreCase("gif")) {
                try {
                    InputStream gif;
                    /*if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
                        Glide.with(getActivity()).asGif()
                                .load(path)
                                .apply(new RequestOptions()
                                        .placeholder(Drawable.createFromPath(localPath)))
                                .into(questionImage);
//                    zoomImg.setVisibility(View.VISIBLE);
                    } else {*/
                        gif = new FileInputStream(localPath);
                        questionImage.setVisibility(View.GONE);
                        questionGif.setVisibility(View.VISIBLE);
                        questionGif.setGifResource(gif);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Glide.with(getActivity())
                        .load(localPath)
                        .apply(new RequestOptions()
                                .placeholder(Drawable.createFromPath(localPath))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(questionImage);
            }
/*            } else {
                String fileName = getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
//                String localPath = Environment.getExternalStorageDirectory() + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                String localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
                Bitmap bitmap = BitmapFactory.decodeFile(localPath);
                questionImage.setImageBitmap(bitmap);
            }*/
        } else questionImage.setVisibility(View.GONE);


        questionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
            }
        });
        questionGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showZoomDialog(getActivity(), scienceQuestion.getPhotourl(), localPath, "");
            }
        });



        options.clear();
        presenter.getOptions(scienceQuestion.getQid());
        //  options = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        if (options.get(0).getChoicename() != null && !options.get(0).getChoicename().equalsIgnoreCase(""))
            radioButtonTrue.setText(Html.fromHtml(options.get(0).getChoicename()));
        if (options.get(1).getChoicename() != null && !options.get(1).getChoicename().equalsIgnoreCase(""))
            radioButtonFalse.setText(Html.fromHtml(options.get(1).getChoicename()));
        radioButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                questionTypeListener.setAnswer("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);

                List<ScienceQuestionChoice> ans = new ArrayList<>();
                if (options != null)
                    ans.add(options.get(0));
                scienceQuestion.setMatchingNameList(ans);
//                assessmentAnswerListener.setAnswerInActivity("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);
                assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), ans);

                radioButtonTrue.setSelected(true);
                radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
                radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
                radioButtonTrue.setTextColor(Assessment_Utility.selectedColor);
                radioButtonFalse.setSelected(false);
                radioButtonFalse.setTextColor(getActivity().getResources().getColor(R.color.white));
            }
        });

        radioButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ScienceQuestionChoice> ans = new ArrayList<>();
                if (options != null)
                    ans.add(options.get(1));
                scienceQuestion.setMatchingNameList(ans);
//                assessmentAnswerListener.setAnswerInActivity("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);
                assessmentAnswerListener.setAnswerInActivity("", "", scienceQuestion.getQid(), ans);

                radioButtonFalse.setSelected(true);
                radioButtonFalse.setTextColor(Assessment_Utility.selectedColor);
                radioButtonTrue.setSelected(false);
                radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
                radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
                radioButtonTrue.setTextColor(getActivity().getResources().getColor(R.color.white));
            }
        });
       /* rg_true_false.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonTrue.getId()*//* && (!isFirstLoad)*//*) {
                    questionTypeListener.setAnswer("", radioButtonTrue.getText().toString(), scienceQuestion.getQid(), null);
                    radioButtonTrue.setSelected(true);
                    radioButtonTrue.setTextColor(Assessment_Utility.selectedColor);
                    radioButtonFalse.setSelected(false);
                    radioButtonFalse.setTextColor(context.getResources().getColor(R.color.white));

                } else if (checkedId == radioButtonFalse.getId() *//*&& (!isFirstLoad)*//*) {
                    questionTypeListener.setAnswer("", radioButtonFalse.getText().toString(), scienceQuestion.getQid(), null);
                    radioButtonFalse.setSelected(true);
                    radioButtonFalse.setTextColor(Assessment_Utility.selectedColor);
                    radioButtonTrue.setSelected(false);
                    radioButtonTrue.setTextColor(context.getResources().getColor(R.color.white));


                } else {
                    Toast.makeText(context, "Select Answer", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        if (scienceQuestion.getUserAnswer().equalsIgnoreCase(options.get(0).getChoicename())) {
            radioButtonTrue.setSelected(true);
            radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
            radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
            radioButtonTrue.setTextColor(Assessment_Utility.selectedColor);
            radioButtonFalse.setSelected(false);
            radioButtonFalse.setTextColor(getActivity().getResources().getColor(R.color.white));


        } else if (scienceQuestion.getUserAnswer().equalsIgnoreCase(options.get(1).getChoicename())) {
            radioButtonFalse.setSelected(true);
            radioButtonFalse.setTextColor(Assessment_Utility.selectedColor);
            radioButtonTrue.setSelected(false);
            radioButtonTrue.setBackground(getActivity().getResources().getDrawable(R.drawable.ripple_rectangle));
            radioButtonFalse.setBackground(getActivity().getResources().getDrawable(R.drawable.rounded_corner_dialog));
            radioButtonTrue.setTextColor(getActivity().getResources().getColor(R.color.white));

        } else {
            radioButtonFalse.setSelected(false);
            radioButtonTrue.setSelected(false);
            radioButtonTrue.setTextColor(getActivity().getResources().getColor(R.color.white));
            radioButtonFalse.setTextColor(getActivity().getResources().getColor(R.color.white));

        }

    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE
        String para = "";
        if (scienceQuestion != null) {
            ScienceQuestion scienceQuestion = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getQuestionByQID(this.scienceQuestion.getQid());
            if (scienceQuestion.isParaQuestion()) {
                btn_view_hint.setVisibility(View.VISIBLE);
//                para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
            } else btn_view_hint.setVisibility(View.GONE);
//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        } else {
            btn_view_hint.setVisibility(View.GONE);

//            assessmentAnswerListener.setParagraph(para, scienceQuestion.isParaQuestion());

        }


    }

    @Click(R.id.btn_view_hint)
    public void showPara() {
        if (scienceQuestion != null) {
            if (scienceQuestion.isParaQuestion()) {
                String para = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionDao().getParabyRefId(scienceQuestion.getRefParaID());
                showZoomDialog(getActivity(), "", "", para);
            }
        }
    }

    @Override
    public void setOptions(List<ScienceQuestionChoice> options) {
        this.options = options;
    }
}
