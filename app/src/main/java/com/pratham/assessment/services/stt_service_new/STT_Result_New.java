package com.pratham.assessment.services.stt_service_new;

import java.util.ArrayList;


public interface STT_Result_New {

    interface sttService {
        void resetHandler(boolean resetActivityFlg);
    }

    interface sttView {
        void Stt_onResult(ArrayList<String> sttResult);
        void Stt_onPartialResult(String sttResult);
        void silenceDetected();

        void stoppedPressed();

        void sttEngineReady();
    }
}
