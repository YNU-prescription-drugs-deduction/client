package com.example.termproject;

public final class RequestURLHelper {
    public static final String HOST = "http://10.0.2.2:8080";
    public static final String VERSION = "/v1";

    public static final String LOGIN = HOST + VERSION + "/users/login";

    public static final String ITEM = HOST + VERSION + "/items";
    public static final String INSERTITEM = ITEM + "/insert";
    public static final String UPDATEITEM = ITEM + "/update";
    public static final String UPDATEITEMSTATUS = ITEM + "/update/status";
    public static final String DELETEITEM = ITEM + "/delete";

}
