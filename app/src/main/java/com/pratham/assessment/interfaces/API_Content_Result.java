package com.pratham.assessment.interfaces;

public interface API_Content_Result {

    void receivedContent(String header, String response);

    void receivedError(String header);

}
