package com.example.termproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    ListView listView;
    TextView name_txt;
    Button logout_btn, form_btn;

    ArrayList<Item> items;
    String name, activated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemlist);

        listView = (ListView) findViewById(R.id.listView);
        name_txt = (TextView) findViewById(R.id.name_txt);
        logout_btn = (Button) findViewById(R.id.logout_btn);
        form_btn = (Button) findViewById(R.id.form_btn);

        // 로그인 정보 - session 기능
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name", null);
        activated = sharedPreferences.getString("activated", null);

        // 관리자 로그인인 경우
        if (activated.equals("admin")) {
            // 사용자 이름 출력
            name_txt.setText("관리자 " + name);

            // 리스트뷰 출력
            getItems();

            // 리스트뷰 아이템 선택 시 동작 설정
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // 관리자가 확인한 내역은 처리대기로 변경된다.
                    if (items.get(i).getStatus().equals(StatusHelper.STATUS_RAW)) {
                        StatusHelper.update(getApplicationContext(), items.get(i).getId(), StatusHelper.STATUS_PROCESS);
                    }

                    // 화면전환
                    startActivity(IntentHelper.DOCUMENT(getApplicationContext(), items.get(i)));
                }
            });

            // 신청하기 버튼 안보이게 하기
            form_btn.setVisibility(View.GONE);
        }
        // 사용자 로그인인 경우
        else {
            // 사용자 이름 출력
            name_txt.setText(name + "님");

            // 리스트뷰 출력
            getItems();

            // 리스트뷰 아이템 선택 시 동작 설정
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // 약제비 공제 내역를 누르는 이벤트 처리
                    // 접수, 처리대기 => 수정가능한 form을 띄운다.
                    // 처리실패, 처리완료 => 수정불가능한 text를 띄운다.
                    if (items.get(i).getStatus().equals(StatusHelper.STATUS_RAW) || items.get(i).getStatus().equals(StatusHelper.STATUS_PROCESS)) {
                        startActivity(IntentHelper.FORM(getApplicationContext(), items.get(i)));
                    } else if (items.get(i).getStatus().equals(StatusHelper.STATUS_FAILURE) || items.get(i).getStatus().equals(StatusHelper.STATUS_SUCCESS)) {
                        startActivity(IntentHelper.DOCUMENT(getApplicationContext(), items.get(i)));
                    }
                }
            });

            // 신청하기 버튼 선택 시 동작 설정
            form_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(IntentHelper.FORM(getApplicationContext(), new Item()));
                }
            });
        }

        // 로그아웃
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout() {
        // 로그인 정보 삭제
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();

        // 화면 전환
        startActivity(IntentHelper.LOGOUT(getApplicationContext()));
    }

    private void getItems() {
        items = new ArrayList<>();
        NetworkOperations.sendGetRequest_Array(getApplicationContext(), RequestURLHelper.ITEM, new NetworkOperations.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onSuccess(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String _id = jsonObject.getString("_id");
                        String userId = jsonObject.getString("userId");
                        String date = jsonObject.getString("date");
                        String status = jsonObject.getString("status");
                        String accountBank = jsonObject.getString("accountBank");
                        String accountName = jsonObject.getString("accountName");
                        String accountNumber = jsonObject.getString("accountNumber");
                        Item item = new Item(_id, userId, date, status, accountBank, accountName, accountNumber);
                        items.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ItemAdapter itemAdapter = new ItemAdapter(getApplicationContext(), items);
                listView.setAdapter(itemAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                // 토큰 에러
                if(errorMessage.equals("expired") || errorMessage.equals("invalid")){
                    Toast.makeText(getApplicationContext(), "토큰이 만료되었습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
                }
                // 시스템 오류
                else {
                    Toast.makeText(getApplicationContext(), "시스템 오류", Toast.LENGTH_SHORT).show();
                }

                logout();
            }
        });
    }
}
