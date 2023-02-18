package com.example.termproject;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Item implements Serializable {
    private String _id;
    private String userId;
    private String date;
    private String status;
    private String accountBank; // 은행
    private String accountName; // 예금주
    private String accountNumber; // 계좌번호
    private Bitmap perscription; // 처방전 이미지
    private Bitmap receipt; // 약제비 영수증 이미지

    public Item() {
        this._id = "";
        this.userId = "";
        this.date = "";
        this.status = "";
        this.accountBank = "";
        this.accountName = "";
        this.accountNumber = "";
//        this.perscription = null;
//        this.receipt = null;
    }

    public Item(String _id, String userId, String date, String status, String accountBank, String accountName, String accountNumber) {
        this._id = _id;
        this.userId = userId;
        this.date = date;
        this.status = status;
        this.accountBank = accountBank;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
//        this.perscription = perscription;
//        this.receipt = receipt;
    }

    public String getId() {
        return _id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Bitmap getPerscription() { return perscription; }

    public Bitmap getReceipt() { return receipt; }

    public void setId(String _id) {
        this._id = _id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatuse(String status) {
        this.status = status;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
