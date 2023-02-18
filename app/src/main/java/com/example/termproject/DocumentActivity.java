package com.example.termproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DocumentActivity extends AppCompatActivity {

    Item item;

    TextView title_txt, accountBank_txt, accountName_txt, accountNumber_txt;
    LinearLayout btns;
    Button accept_btn, reject_btn;
    ImageButton home_imgbtn;
//  ImageView prescription_img, receipt_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);

        title_txt = (TextView) findViewById(R.id.docTitle_txt);
        accountBank_txt = (TextView) findViewById(R.id.accountBank_txt);
        accountName_txt = (TextView) findViewById(R.id.accountName_txt);
        accountNumber_txt = (TextView) findViewById(R.id.accountNumber_txt);
        btns = (LinearLayout) findViewById(R.id.docBtns);
        accept_btn = (Button) findViewById(R.id.accept_btn);
        reject_btn = (Button) findViewById(R.id.reject_btn);
        home_imgbtn = (ImageButton) findViewById(R.id.docHome_btn);
//      prescription_img = (ImageView) findViewById(R.id.prescription_img);
//      receipt_img = (ImageView) findViewById(R.id.receipt_img);

        // 화면전환 시 데이터 수신
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");

        // item 상태가 처리실패, 처리완료라면 수락, 거절 버튼을 띄우지 않는다.
        if (item.getStatus().equals(StatusHelper.STATUS_FAILURE) || item.getStatus().equals(StatusHelper.STATUS_SUCCESS)) {
            btns.setVisibility(View.GONE);
        }

        // item 정보 화면에 출력
        title_txt.setText(item.getDate());
        accountBank_txt.setText(item.getAccountBank());
        accountName_txt.setText(item.getAccountName());
        accountNumber_txt.setText(item.getAccountNumber());
//      prescription_img.setImageBitmap(item.getPerscription());
//      receipt_img.setImageBitmap(item.getReceipt());

        // 수락 버튼을 눌렀을 때 동작
        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // item 상태를 처리완료로 변경
                StatusHelper.update(getApplicationContext(), item.getId(), StatusHelper.STATUS_SUCCESS);
                Toast.makeText(getApplicationContext(), "수락되었습니다.", Toast.LENGTH_SHORT).show();

                // 화면전환
                startActivity(IntentHelper.ITEMLIST(getApplicationContext()));
            }
        });

        // 거절 버튼을 눌렀을 때 동작
        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // item 상태를 처리실패로 변경
                StatusHelper.update(getApplicationContext(), item.getId(), StatusHelper.STATUS_FAILURE);
                Toast.makeText(getApplicationContext(), "거절되었습니다.", Toast.LENGTH_SHORT).show();

                // 화면전환
                startActivity(IntentHelper.ITEMLIST(getApplicationContext()));
            }
        });

        // 홈 버튼을 눌렀을 때 동작
        home_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(IntentHelper.ITEMLIST(getApplicationContext()));
            }
        });

    }

}
