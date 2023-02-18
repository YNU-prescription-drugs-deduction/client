package com.example.termproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FormActivity extends AppCompatActivity {

    Item item;
    String bank;

    Spinner accountBank_spn;
    TextView title_txt;
    ImageButton home_imgbtn, prescription_imgbtn, receipt_imgbtn;
    EditText accountName_et, accountNumber_et;
    LinearLayout btns;
    Button submit_btn, update_btn, delete_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        title_txt = (TextView) findViewById(R.id.formTitle_txt);
        accountBank_spn = (Spinner) findViewById(R.id.accountBank_spn);
        accountName_et = (EditText) findViewById(R.id.accountName_et);
        accountNumber_et = (EditText) findViewById(R.id.accountNumber_et);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        btns = (LinearLayout) findViewById(R.id.formBtns);
        update_btn = (Button) findViewById(R.id.update_btn);
        delete_btn = (Button) findViewById(R.id.delete_btn);
        home_imgbtn = (ImageButton) findViewById(R.id.formHome_btn);
        prescription_imgbtn = (ImageButton) findViewById(R.id.prescription_imgbtn);
        receipt_imgbtn = (ImageButton) findViewById(R.id.receipt_imgbtn);

        // 화면전환 시 데이터 수신
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");

        // 은행선택하는 spinner 설정
        String[] banks = getResources().getStringArray(R.array.banks);
        ArrayAdapter spn_adapter = new ArrayAdapter(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, banks);
        accountBank_spn.setAdapter(spn_adapter);

        // 기존에 작성된 신청서라면 기존 데이터로 초기값 설정
        if (!item.getId().equals("")) {
            // 신청하기 버튼 말고 저장, 삭제 버튼을 띄운다.
            submit_btn.setVisibility(View.GONE);
            btns.setVisibility(View.VISIBLE);

            // 날짜 설정
            title_txt.setText(item.getDate());

            // 환불계좌 설정
            accountBank_spn.setSelection(getIndex(accountBank_spn, item.getAccountBank()));
            accountName_et.setText(item.getAccountName());
            accountNumber_et.setText(item.getAccountNumber());

            // 이미지 설정
            prescription_imgbtn.setImageBitmap(item.getPerscription());
            receipt_imgbtn.setImageBitmap(item.getReceipt());
        }

        // 은행 선택하는 spinner 동작
        accountBank_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bank = banks[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 처방전 이미치 업로드
        prescription_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult_forPrescription.launch(intent);
            }
        });

        // 약제비 영수증 이미치 업로드
        receipt_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult_forReceipt.launch(intent);
            }
        });

        // 신청하기 버튼 눌렀을 때 동작
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = accountName_et.getText().toString();
                String number = accountNumber_et.getText().toString();
                Bitmap prescription = ((BitmapDrawable) prescription_imgbtn.getDrawable()).getBitmap();
                Bitmap receipt = ((BitmapDrawable) receipt_imgbtn.getDrawable()).getBitmap();

                if (getIndex(accountBank_spn, bank) == 0) {
                    Toast.makeText(getApplicationContext(), "은행을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "예금주를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "계좌번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (prescription == null) {
                    Toast.makeText(getApplicationContext(), "처방전을 업로드해주세요.", Toast.LENGTH_SHORT).show();
                } else if (receipt == null) {
                    Toast.makeText(getApplicationContext(), "영수증을 업로드해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // item 추가
                    insertItem(bank, name, number);
                    Toast.makeText(getApplicationContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();

                    // 화면전환
                    startActivity(IntentHelper.ITEMLIST(getApplicationContext()));
                }
            }
        });

        // 저장하기 버튼 눌렀을 때 동작
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = accountName_et.getText().toString();
                String number = accountNumber_et.getText().toString();

                if (getIndex(accountBank_spn, bank) == 0) {
                    Toast.makeText(getApplicationContext(), "은행을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "예금주를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "계좌번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // item 수정
                    updateItem(item.getId(), bank, name, number);
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();

                    // 화면전환
                    startActivity(IntentHelper.ITEMLIST(getApplicationContext()));
                }
            }
        });

        // 삭제하기 버튼 눌렀을 때 동작
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // item 삭제
                deleteItem(item.getId());
                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

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

    private int getIndex(Spinner spinner, String accountBank) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(accountBank)) {
                return i;
            }
        }
        return 0;
    }

    ActivityResultLauncher<Intent> startActivityResult_forPrescription = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            prescription_imgbtn.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> startActivityResult_forReceipt = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            receipt_imgbtn.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        return data;
    }

    public Bitmap getBitmapFromByteArray(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    private void insertItem(String accountBank, String accountName, String accountNumber) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("accountBank", accountBank);
            jsonBody.put("accountName", accountName);
            jsonBody.put("accountNumber", accountNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkOperations.sendPostRequest(getApplicationContext(), RequestURLHelper.INSERTITEM, jsonBody, new NetworkOperations.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Toast.makeText(getApplicationContext(), "신청되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONArray response) {

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

                startActivity(IntentHelper.LOGOUT(getApplicationContext()));
            }
        });
    }

    private void updateItem(String _id, String accountBank, String accountName, String accountNumber) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("_id", _id);
            jsonBody.put("accountBank", accountBank);
            jsonBody.put("accountName", accountName);
            jsonBody.put("accountNumber", accountNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkOperations.sendPostRequest(getApplicationContext(), RequestURLHelper.UPDATEITEM, jsonBody, new NetworkOperations.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONArray response) {

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

                startActivity(IntentHelper.LOGOUT(getApplicationContext()));
            }
        });
    }

    private void deleteItem(String _id) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("_id", _id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetworkOperations.sendPostRequest(getApplicationContext(), RequestURLHelper.DELETEITEM, jsonBody, new NetworkOperations.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONArray response) {

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

                startActivity(IntentHelper.LOGOUT(getApplicationContext()));
            }
        });
    }


}
