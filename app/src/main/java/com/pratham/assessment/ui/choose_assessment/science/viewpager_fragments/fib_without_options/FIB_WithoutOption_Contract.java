package com.pratham.assessment.ui.choose_assessment.science.viewpager_fragments.fib_without_options;


import java.util.ArrayList;

public interface FIB_WithoutOption_Contract {

    public interface FIB_WithoutOption_View {
        void reInitCurrentItems();

        void appendText(String sttResultStr);
    }

    public interface FIB_WithoutOptionPresenter {
        void processSTT_Result(String answer, ArrayList<String> sttResult);

        void setView(FIB_WithoutOption_View view);
    }

}
