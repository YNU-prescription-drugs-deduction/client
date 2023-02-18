package com.example.termproject;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StatusHelper {
    public static String STATUS_RAW = "접수";
    public static String STATUS_PROCESS = "처리대기";
    public static String STATUS_SUCCESS = "처리완료";
    public static String STATUS_FAILURE = "처리실패";

    public static void update(Context context, String _id, String status) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("_id", _id);
            jsonBody.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkOperations.sendPostRequest(context, RequestURLHelper.UPDATEITEMSTATUS, jsonBody, new NetworkOperations.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onError(String errorMessage) {

            }
        });

    }
}
