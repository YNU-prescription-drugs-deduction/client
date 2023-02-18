package com.example.termproject;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NetworkOperations {

    public static void sendGetRequest(Context context, String url, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response : " + response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error : " + error);
                try {
                    if (error.networkResponse.statusCode == 401) {
                        try {
                            String jsonString = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject jsonObject = new JSONObject(jsonString);
                            String errorMessage = jsonObject.getString("message");
                            callback.onError(errorMessage);
                            // use the error message as needed
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    callback.onError("error");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("accessToken", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public static void sendGetRequest_Array(Context context, String url, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a JSON response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("response : " + response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error : " + error);
                try {
                    if (error.networkResponse.statusCode == 401) {
                        try {
                            String jsonString = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject jsonObject = new JSONObject(jsonString);
                            String errorMessage = jsonObject.getString("message");
                            callback.onError(errorMessage);
                            // use the error message as needed
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    callback.onError("error");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("accessToken", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }


    public static void sendPostRequest(Context context, String url, JSONObject jsonBody, final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a JSON response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the response
                        System.out.println("response : " + response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error
                System.out.println("error : " + error);
                try {
                    if (error.networkResponse.statusCode == 401) {
                        try {
                            String jsonString = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject jsonObject = new JSONObject(jsonString);
                            String errorMessage = jsonObject.getString("message");
                            callback.onError(errorMessage);
                            // use the error message as needed
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    callback.onError("error");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("accessToken", null);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    // Define a callback interface for the response
    public interface VolleyCallback {
        void onSuccess(JSONObject response);

        void onSuccess(JSONArray response);

        void onError(String errorMessage);
    }
}
