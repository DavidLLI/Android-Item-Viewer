package com.example.a4742_000.myapplication;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ArrayStringAdapter extends ArrayAdapter<String> {
    private List<String> categories;

    public ArrayStringAdapter(Context context, int resource) {
        super(context, resource);
        this.categories = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public String getItem(int position) {
        return categories.get(position);
    }

    public void updateData(List<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }
}
