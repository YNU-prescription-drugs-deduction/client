package com.example.termproject;

import android.content.Context;
import android.content.Intent;

public class IntentHelper {

    public static Intent LOGOUT(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    public static Intent ITEMLIST(Context context) {
        Intent intent = new Intent(context, ItemListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    public static Intent FORM(Context context, Item item) {
        Intent intent = new Intent(context, FormActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("item", item);
        return intent;
    }

    public static Intent DOCUMENT(Context context, Item item) {
        Intent intent = new Intent(context, DocumentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("item", item);
        return intent;
    }

}
