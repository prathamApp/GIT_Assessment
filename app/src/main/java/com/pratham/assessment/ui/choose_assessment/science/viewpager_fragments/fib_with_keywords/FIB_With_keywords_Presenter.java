package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_with_keywords;

import android.content.Context;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

@EBean
public class FIB_With_keywords_Presenter implements FIB_With_keywords_Contract.FIB_WithoutOptionPresenter {

    Context context;
    private FIB_With_keywords_Contract.FIB_WithoutOption_View fib_view;


    public FIB_With_keywords_Presenter(Context context) {
        this.context = context;
    }

    @Override
    public void setView(FIB_With_keywords_Contract.FIB_WithoutOption_View fib_view) {
        this.fib_view = fib_view;
    }

    @Override
    public void processSTT_Result(String answer, ArrayList<String> sttResult) {
        String sttResultStr = "";
        try {

            for (int i = 0; i < sttResult.size(); i++) {
                System.out.println("LogTag" + " onResults :  " + sttResult.get(i));

                if (sttResult.get(i).equalsIgnoreCase(answer))
                    sttResultStr = sttResult.get(i);
                else sttResultStr = sttResult.get(0);
            }

     /*   String[] splitQues = answer.replaceAll(STT_REGEX_3, "").split(" ");
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
        }*/
            fib_view.reInitCurrentItems();
            fib_view.appendText(sttResultStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}