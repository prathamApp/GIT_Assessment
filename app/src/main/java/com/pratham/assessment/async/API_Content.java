package com.pratham.assessment.async;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.pratham.assessment.interfaces.API_Content_Result;

import org.json.JSONArray;


public class API_Content {

    Context mContext;
    API_Content_Result apiContentResult;

    public API_Content(Context context, API_Content_Result apiContentResult) {
        this.mContext = context;
        this.apiContentResult = apiContentResult;
    }

    public void getAPIContent(final String requestType, String url, String nodeId) {
        try {
            String url_id;
            url_id = url + "" + nodeId;
            AndroidNetworking.get(url_id)
                    .addHeaders("Content-Type", "application/json")
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if (apiContentResult != null)
                                apiContentResult.receivedContent(requestType, response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            try {
                                Log.d("Error:", anError.getErrorDetail());
                               // Log.d("Error::", anError.getResponse().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (apiContentResult != null)
                                apiContentResult.receivedError(requestType);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pullFromKolibri(final String header, String url) {
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", getAuthHeader())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (apiContentResult != null)
                            apiContentResult.receivedContent(header, response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        if (apiContentResult != null)
                            apiContentResult.receivedError(header);
                    }
                });
    }

    public void pullFromInternet(final String header, String url) {
        AndroidNetworking.get(url)
                .addHeaders("Content-Type", "application/json")
//                .addHeaders("Authorization", getAuthHeader("pratham", "pratham"))
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (apiContentResult != null)
                            apiContentResult.receivedContent(header, response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        if (apiContentResult != null)
                            apiContentResult.receivedError(header);
                    }
                });
    }

    private String getAuthHeader() {
        String encoded = Base64.encodeToString(("pratham" + ":" + "pratham").getBytes(), Base64.NO_WRAP);
        return "Basic " + encoded;
    }
}
