package com.example.a4742_000.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4742_000.myapplication.Database.ListItem;

import java.io.FilterReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<ListItem> mDataset;
    private List<ListItem> filteredDataset;
    private String category;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView;
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mCardView = (CardView)v.findViewById(R.id.card_view);
            mTextView = (TextView)v.findViewById(R.id.text_in_card);
            mImageView = (ImageView)v.findViewById(R.id.image_view);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainAdapter() {
        mDataset = new ArrayList<ListItem>();
        filteredDataset = new ArrayList<>();
        category = new String();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(filteredDataset.get(position).getName());
        try {
            holder.mImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(holder.mImageView.getContext()
                    .getContentResolver(), Uri.parse(filteredDataset.get(position).getImageUrisWrapper().get(0))));
        } catch (Exception e) {
            holder.mImageView.setImageResource(R.drawable.captainamerica);
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ClickedView) {
                Intent intentBundle = new Intent(ClickedView.getContext(), SpecificItemActivity.class);

                // Put in data
                Bundle bundle = new Bundle();
                bundle.putInt("uid", filteredDataset.get(position).getUid());
                bundle.putString("category", filteredDataset.get(position).getCategory());
                bundle.putString("name", filteredDataset.get(position).getName());
                bundle.putStringArrayList("image", new ArrayList(filteredDataset.get(position).getImageUrisWrapper()));
                bundle.putString("description", filteredDataset.get(position).getDescription());
                intentBundle.putExtras(bundle);
                ClickedView.getContext().startActivity(intentBundle);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredDataset.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void dataChange(List<ListItem> items, String category) {
        if (items != null) {
            mDataset = items;
        }
        this.category = category;
        filteredDataset.clear();
        for (ListItem item : mDataset) {
            if (item.getCategory().equals(category) || category == null || category.isEmpty() || category.equals("全部")) {
                filteredDataset.add(item);
            }
        }
        notifyDataSetChanged();
    }
}
