package com.example.termproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText id_et, pw_et;
    Button userLogin_btn, staffLogin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        id_et = (EditText) findViewById(R.id.id_et);
        pw_et = (EditText) findViewById(R.id.pw_et);
        userLogin_btn = (Button) findViewById(R.id.userLogin_btn);
        staffLogin_btn = (Button) findViewById(R.id.staffLogin_btn);

        // 사용자 로그인 버튼 동작 설정
        userLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_et.getText().toString();
                String password = pw_et.getText().toString();

                if (id == null || id.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (password == null || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    login(id, password, "user");
                }

            }
        });

        // 관리자 로그인 버튼 동작 설정
        staffLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_et.getText().toString();
                String password = pw_et.getText().toString();

                if (id == null || id.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (password == null || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    login(id, password, "admin");
                }
            }
        });

    }

    private void login(String id, String password, String activated){
        // JSON 객체 생성
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", id);
            jsonBody.put("password", password);
            jsonBody.put("activated", activated);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // JSON 객체 전달
        NetworkOperations.sendPostRequest(getApplicationContext(), RequestURLHelper.LOGIN, jsonBody, new NetworkOperations.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                // 로그인 성공
                try {
                    // accessToken 받아오기
                    String name = response.getString("name");
                    String accessToken = response.getString("accessToken");

                    // 로그인 정보 유지
                    SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("activated", activated);
                    editor.putString("accessToken", accessToken);
                    editor.apply();

                    // 화면전환
                    startActivity(IntentHelper.ITEMLIST(getApplicationContext()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onError(String errorMessage) {
                // 로그인 실패
                // 비밀번호 오류인 경우
                if (errorMessage.equals("mismatch")) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
                // 사용자가 아닌 경우
                else if (errorMessage.equals("notuser")) {
                    Toast.makeText(getApplicationContext(), "건강공제 대상이 아닙니다.", Toast.LENGTH_SHORT).show();
                }
                // 관리자가 아닌 경우
                else if (errorMessage.equals("notadmin")) {
                    Toast.makeText(getApplicationContext(), "관리자 정보와 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                // 시스템 오류
                else {
                    Toast.makeText(getApplicationContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}