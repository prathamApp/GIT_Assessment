package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_without_options;

import android.content.Context;
import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Arrays;

import static com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_without_options.FillInTheBlanksWithoutOptionFragment.correctArr;
import static com.pratham.assessment.constants.Assessment_Constants.STT_REGEX_3;

@EBean
public class FIB_WithoutOption_Presenter implements FIB_WithoutOption_Contract.FIB_WithoutOptionPresenter{

    Context context;
    private FIB_WithoutOption_Contract.FIB_WithoutOption_View fib_view;


    public FIB_WithoutOption_Presenter(Context context){
        this.context = context;
    }

    @Override
    public void setView(FIB_WithoutOption_Contract.FIB_WithoutOption_View fib_view) {
        this.fib_view = fib_view;
    }

    @Override
    public void processSTT_Result(String answer, ArrayList<String> sttResult) {
        String sttResultStr = "";
        for (int i = 0; i < sttResult.size(); i++) {
            System.out.println("LogTag" + " onResults :  " + sttResult.get(i));

            if (sttResult.get(i).equalsIgnoreCase(answer))
                sttResultStr = sttResult.get(i);
            else sttResultStr = sttResult.get(0);
        }

        String[] splitQues = answer.replaceAll(STT_REGEX_3, "").split(" ");
        String[] splitRes = sttResultStr.split(" ");

        for (String splitRe : splitRes)
            for (int i = 0; i < splitQues.length; i++)
                if (splitRe.equalsIgnoreCase(splitQues[i])) {
                    correctArr[i] = true;
                    break;
                }

        int correctCnt = 0;
        for (boolean b : correctArr)
            if (b)
                correctCnt++;

        float perc = ((float) correctCnt / (float) correctArr.length) * 100;
        Log.d("Punctu", "onResults: " + perc);
        if (perc >= 75) {
            Arrays.fill(correctArr, true);
        }
        fib_view.reInitCurrentItems();
        fib_view.appendText(sttResultStr);
    }
}