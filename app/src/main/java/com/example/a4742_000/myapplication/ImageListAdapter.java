package com.example.a4742_000.myapplication;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4742_000.myapplication.Database.ListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<String> mImageUris;
    private SparseBooleanArray checkedState;
    private boolean deleteMode;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public CheckBox deleteCheckBox;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView)v.findViewById(R.id.list_image_view);
            deleteCheckBox = v.findViewById(R.id.delete_check_box);
        }
    }

    public ImageListAdapter() {
        mImageUris = new ArrayList<String>();
        checkedState = new SparseBooleanArray();
        deleteMode = false;
    }

    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_image_view, parent, false);
        ImageListAdapter.ViewHolder vh = new ImageListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ImageListAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            holder.mImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(holder.mImageView.getContext()
                    .getContentResolver(), Uri.parse(mImageUris.get(position))));
        } catch (Exception e) {
            holder.mImageView.setImageResource(R.drawable.captainamerica);
        }
        holder.deleteCheckBox.setEnabled(deleteMode);
        if (deleteMode) {
            holder.deleteCheckBox.setVisibility(View.VISIBLE);
        }
        else {
            holder.deleteCheckBox.setVisibility(View.INVISIBLE);
            holder.deleteCheckBox.setChecked(false);
        }
        holder.deleteCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            checkedState.put(position, isChecked);
            holder.deleteCheckBox.setChecked(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return mImageUris.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateData(List<String> imageUris) {
        mImageUris = imageUris;
        notifyDataSetChanged();
    }

    public void deleteModeStart() {
        deleteMode = true;
        checkedState = new SparseBooleanArray();
        for (int i = 0; i < mImageUris.size(); i++) {
            checkedState.put(i, false);
        }
        notifyDataSetChanged();
    }

    // Returns list of images to be deleted
    public List<Integer> deleteModeEnd() {
        List<Integer> toBeDeleted = new ArrayList<Integer>();
        for (int i = 0; i < checkedState.size(); i++) {
            if (checkedState.get(i)) {
                toBeDeleted.add(i);
            }
        }
        deleteMode = false;
        notifyDataSetChanged();
        return toBeDeleted;
    }

    public Boolean getDeleteMode() {
        return this.deleteMode;
    }
}
