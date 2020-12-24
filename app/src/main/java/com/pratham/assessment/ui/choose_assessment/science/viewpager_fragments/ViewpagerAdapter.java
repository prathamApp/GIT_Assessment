package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.pratham.assessment.domain.ScienceQuestion;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends FragmentPagerAdapter {
    //    Context context;
    private static int NUM_ITEMS = 0;
    private List<Fragment> fragmentList;
    Fragment currentFragment;

    public ViewpagerAdapter(FragmentManager fm, Context context, List<ScienceQuestion> scienceQuestionList) {
        super(fm);
//        this.context = context;
        NUM_ITEMS = scienceQuestionList.size();
        fragmentList = new ArrayList<>();
        for (int i = 0; i < scienceQuestionList.size(); i++) {
            ScienceQuestion scienceQuestion = scienceQuestionList.get(i);
            String questionType = scienceQuestion.getQtid();
            switch (questionType) {
                case "1":
                case "5":
                    fragmentList.add(McqFillInTheBlanksFragment.newInstance(i, scienceQuestion));
                    break;
                case "2":
                    fragmentList.add(MultipleSelectFragment.newInstance(i, scienceQuestion));
                    break;
                case "3":
                    fragmentList.add(com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.true_false.TrueFalseFragment.newInstance(i, scienceQuestion));
                    break;
                case "4":
                    fragmentList.add(com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.match_the_pair.MatchThePairFragment.newInstance(i, scienceQuestion));
                    break;
                case "6":
                case "11":
//                    fragmentList.add(FillInTheBlanksWithoutOptionFragment.newInstance(i, scienceQuestion));
                    fragmentList.add(com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_without_options.FillInTheBlanksWithoutOptionFragment.newInstance(i, scienceQuestion));

                    break;
                case "7":
                    fragmentList.add(com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.arrange_sequence.ArrangeSequenceFragment.newInstance(i, scienceQuestion));
                    break;
                case "8":
                    fragmentList.add(VideoFragment.newInstance(i, scienceQuestion));
                    break;
                case "9":
                    fragmentList.add(AudioFragment.newInstance(i, scienceQuestion));
                    break;
             /*   case "11":
                    fragmentList.add(com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_with_keywords.FillInTheBlanksWithKeywordsFragment.newInstance(i, scienceQuestion));
                    break;
           */     case "12":
                    fragmentList.add(ImageAnswerFragment.newInstance(i, scienceQuestion));
                    break;
                 case "13":
                    fragmentList.add(TextParagraphFragment.newInstance(i, scienceQuestion));
                    break;
                 case "14":
                    fragmentList.add(ParagraphBasedQuestionsFragment.newInstance(i, scienceQuestion));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        currentFragment = fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
