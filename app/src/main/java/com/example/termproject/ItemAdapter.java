package com.example.termproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    Context context = null;
    LayoutInflater layoutInflater = null;
    ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view= layoutInflater.inflate(R.layout.itembox, null);

        TextView date = (TextView) view.findViewById(R.id.date);
        TextView status = (TextView) view.findViewById(R.id.status);

        date.setText(items.get(i).getDate());
        status.setText(items.get(i).getStatus());

        String text = status.getText().toString();
        if (text.equals("접수")) {
            // 비율 맞추기
            status.setText("   " + items.get(i).getStatus() + "   ");
        } if (text.equals("처리실패")) {
            // 처리실패는 빨간색으로 출력
            status.setTextColor(Color.parseColor("#FF0000"));
        } else if (text.equals("처리완료")) {
            // 처리완료는 파란색으로 출력
            status.setTextColor(Color.parseColor("#0000FF"));
        }

        return view;
    }
}
