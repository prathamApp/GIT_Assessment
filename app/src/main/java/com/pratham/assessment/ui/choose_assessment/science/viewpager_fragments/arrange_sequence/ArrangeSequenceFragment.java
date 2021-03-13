package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.arrange_sequence;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.pratham.assessment.ui.choose_assessment.science.ItemMoveCallback;
import com.pratham.assessment.ui.choose_assessment.science.adapters.ArrangeSeqDragDropAdapter;
import com.pratham.assessment.ui.choose_assessment.science.interfaces.StartDragListener;
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

import static com.pratham.assessment.utilities.Assessment_Utility.setOdiaFont;
import static com.pratham.assessment.utilities.Assessment_Utility.showZoomDialog;

@EFragment(R.layout.layout_arrange_seq_row)
public class ArrangeSequenceFragment extends Fragment implements StartDragListener, ArrangeSequenceContract.ArrangeSeqView {
    @ViewById(R.id.tv_question)
    TextView question;
    @ViewById(R.id.iv_question_image)
    ImageView questionImage;
    @ViewById(R.id.iv_question_gif)
    GifView questionGif;
    @ViewById(R.id.rl_arrange_seq)
    RecyclerView recyclerArrangeSeq;
    @ViewById(R.id.btn_view_hint)
    Button btn_view_hint;

    @Bean(ArrangeSeqPresenter.class)
    ArrangeSequenceContract.ArrangeSeqPresenter presenter;

    private static final String POS = "pos";
    private static final String SCIENCE_QUESTION = "scienceQuestion";

    private int pos;
    private ScienceQuestion scienceQuestion;
    ItemTouchHelper touchHelper;
    List<ScienceQuestionChoice> shuffledList = new ArrayList<>();

    ArrangeSeqDragDropAdapter dragDropAdapter;

    public ArrangeSequenceFragment() {
        // Required empty public constructor
    }

    public static ArrangeSequenceFragment newInstance(int pos, ScienceQuestion scienceQuestion) {
        ArrangeSequenceFragment_ arrangeSequenceFragment = new ArrangeSequenceFragment_();
        Bundle args = new Bundle();
        args.putInt(POS, pos);
        args.putSerializable(SCIENCE_QUESTION, scienceQuestion);
        arrangeSequenceFragment.setArguments(args);
        return arrangeSequenceFragment;
    }


    @AfterViews
    public void init() {
        if (getArguments() != null) {
            pos = getArguments().getInt(POS, 0);
            scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
        }
        if (question != null)
            question.setMovementMethod(new ScrollingMovementMethod());
        presenter.setView(this);
        setArrangeSeqQuestion();

    }

    /*  @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          if (getArguments() != null) {
              pos = getArguments().getInt(POS, 0);
              scienceQuestion = (ScienceQuestion) getArguments().getSerializable(SCIENCE_QUESTION);
          }
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          // Inflate the layout for this fragment
          return inflater.inflate(R.layout.layout_arrange_seq_row, container, false);
      }

      @Override
      public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          ButterKnife.bind(this, view);
          setArrangeSeqQuestion();

      }
  */
    public void setArrangeSeqQuestion() {
        question.setText(Html.fromHtml(scienceQuestion.getQname()));
        setOdiaFont(getActivity(), question);

        final String fileName = Assessment_Utility.getFileName(scienceQuestion.getQid(), scienceQuestion.getPhotourl());
        final String localPath;
        if (scienceQuestion.getIsQuestionFromSDCard())
            localPath = scienceQuestion.getPhotourl();
        else
            localPath = AssessmentApplication.assessPath + Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH + "/" + fileName;
        if (scienceQuestion.getPhotourl() != null && !scienceQuestion.getPhotourl().equalsIgnoreCase("")) {
            questionImage.setVisibility(View.VISIBLE);


//            if (AssessmentApplication.wiseF.isDeviceConnectedToMobileOrWifiNetwork()) {
            //                    zoomImg.setVisibility(View.VISIBLE);
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
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .placeholder(Drawable.createFromPath(localPath)))
                        .into(questionImage);
            }
           /* } else {
           //                String localPath= Environment.getExternalStorageDirectory()+Assessment_Constants.STORE_DOWNLOADED_MEDIA_PATH+"/"+fileName;
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

        presenter.getShuffledList(scienceQuestion);
       /* List<ScienceQuestionChoice> AnswerList = new ArrayList<>();


        if (!scienceQuestion.getUserAnswer().equalsIgnoreCase("")) {
            String[] ansIds = scienceQuestion.getUserAnswer().split(",");
            for (int i = 0; i < ansIds.length; i++) {
                if (ansIds[i].equalsIgnoreCase(scienceQuestion.getMatchingNameList().get(i).getQcid()))
                    AnswerList.add(scienceQuestion.getMatchingNameList().get(i));
            }

        }

        List list1 = new ArrayList();
        List<ScienceQuestionChoice> shuffledList = new ArrayList<>();
        List<ScienceQuestionChoice> pairList = AppDatabase.getDatabaseInstance(getActivity()).getScienceQuestionChoicesDao().getQuestionChoicesByQID(scienceQuestion.getQid());
        Log.d("wwwwwwwwwww", pairList.size() + "");
        if (!pairList.isEmpty()) {
*//*  for (int p = 0; p < pairList.size(); p++) {
                list1.add(pairList.get(p).getChoicename());
            }*//*


            if (scienceQuestion.getMatchingNameList() == null) {
                shuffledList.clear();

                shuffledList.addAll(pairList);
                while (shuffledList.equals(pairList)) {
                    Collections.shuffle(shuffledList);
                }
//                Collections.shuffle(shuffledList);
            } else {
                if (AnswerList.size() > 0)
                    shuffledList.addAll(AnswerList);
                else {
                    shuffledList = scienceQuestion.getMatchingNameList();
                    Collections.shuffle(shuffledList);
                }

            }


//        presenter.getShuffledList(scienceQuestion);

            dragDropAdapter = new ArrangeSeqDragDropAdapter(this, shuffledList, scienceQuestion.getQtid(), getActivity());
            ItemTouchHelper.Callback callback =
                    new ItemMoveCallback(dragDropAdapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(null);
            touchHelper.attachToRecyclerView(recyclerArrangeSeq);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerArrangeSeq.setLayoutManager(linearLayoutManager1);
            recyclerArrangeSeq.setAdapter(dragDropAdapter);

        }*/
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);

    }

    @Override
    public void onItemDragged(List<ScienceQuestionChoice> draggedList) {
        dragDropAdapter.notifyDataSetChanged();
        shuffledList = draggedList;
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
    public void setShuffledList(List<ScienceQuestionChoice> shuffledList) {
        dragDropAdapter = new ArrangeSeqDragDropAdapter(this, shuffledList, scienceQuestion.getQtid(), getActivity());
        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(dragDropAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(null);
        touchHelper.attachToRecyclerView(recyclerArrangeSeq);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerArrangeSeq.setLayoutManager(linearLayoutManager1);
        recyclerArrangeSeq.setAdapter(dragDropAdapter);
    }
  /*  @Override
    public void setShuffledList(List<ScienceQuestionChoice> shuffledList) {
        this.shuffledList = shuffledList;
    }*/
}
