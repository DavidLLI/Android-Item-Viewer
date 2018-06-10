package com.example.a4742_000.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4742_000.myapplication.Database.AppDatabase;
import com.example.a4742_000.myapplication.Database.ListItem;
import com.example.a4742_000.myapplication.Database.ListItemModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpecificItemActivity extends AppCompatActivity {
    private ListItem mItem;

    private TextView name_view;
    private RecyclerView images_view;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageListAdapter imageListAdapter;
    private TextView description_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_specific_view);

        name_view = findViewById(R.id.item_name_id);

        images_view = (RecyclerView) findViewById(R.id.imageList_specific_view);
        // Performance if size does not change
        images_view.setHasFixedSize(true);
        // use a grid layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        images_view.setLayoutManager(mLayoutManager);

        imageListAdapter = new ImageListAdapter();
        images_view.setAdapter(imageListAdapter);

        description_view = findViewById(R.id.item_description_id);

        int uid = (Integer) getIntent().getExtras().get("uid");
        String name = (String) getIntent().getExtras().get("name");
        ArrayList<String> imageUris = (ArrayList<String>) getIntent().getExtras().get("image");
        String description = (String) getIntent().getExtras().get("description");
        mItem = new ListItem(uid, name, imageUris, description);

        AppDatabase.getInstance(getApplicationContext()).listItemDao().findById(uid).observe(this, (@Nullable final ListItem item) -> {
            this.updateData(item);
        });

        name_view.setText(mItem.getName());
        description_view.setText(mItem.getDescription());

        ListItemModel model = ViewModelProviders.of(this).get(ListItemModel.class);
        AppCompatActivity mContext = this;

        FloatingActionButton edit_button = (FloatingActionButton) findViewById(R.id.edit_button);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBundle = new Intent(view.getContext(), AddUpdateItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", mItem.getUid());
                bundle.putString("name", mItem.getName());
                bundle.putStringArrayList("image", new ArrayList<String>(mItem.getImageUrisWrapper()));
                bundle.putString("description", mItem.getDescription());
                intentBundle.putExtras(bundle);
                view.getContext().startActivity(intentBundle);
            }
        });

        FloatingActionButton delete_Button = (FloatingActionButton) findViewById(R.id.delete_button);
        delete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListItem item = new ListItem(mItem.getUid(), mItem.getName(),mItem.getImageUrisWrapper(), mItem.getDescription());
                model.delete(item);
                mContext.finish();
            }
        });
    }

    public void updateData(ListItem item) {
        this.mItem = item;
        if (mItem != null) {
            name_view.setText(mItem.getName());
            imageListAdapter.updateData(item.getImageUrisWrapper());
            description_view.setText(mItem.getDescription());
        }
    }
}
