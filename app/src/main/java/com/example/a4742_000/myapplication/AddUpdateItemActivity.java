package com.example.a4742_000.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a4742_000.myapplication.Database.ListItem;
import com.example.a4742_000.myapplication.Database.ListItemModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddUpdateItemActivity extends AppCompatActivity {
    private static final int OPEN_DOCUMENT_CODE = 2;

    private ListItem mItem;

    private EditText name_edit;
    private EditText description_edit;
    private RecyclerView images_view;
    private AutoCompleteTextView category_view;
    private ArrayStringAdapter categoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageListAdapter imageListAdapter;
    ListItemModel model;
    AppCompatActivity mContext;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = ViewModelProviders.of(this).get(ListItemModel.class);
        mContext = this;

        setContentView(R.layout.new_item_view);

        category_view = findViewById(R.id.categories_dropdown);
        categoryAdapter = new ArrayStringAdapter(this, R.layout.my_text_view);
        category_view.setAdapter(categoryAdapter);
        model.getAllCategories().observe(this, (@Nullable final List<String> categories) -> {
            categoryAdapter.updateData(categories);
        });
        name_edit = findViewById(R.id.product_name_add);
        description_edit = findViewById(R.id.product_description_add);
        images_view = (RecyclerView) findViewById(R.id.imageList_add_view);
        // Performance if size does not change
        images_view.setHasFixedSize(true);
        // use a grid layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        images_view.setLayoutManager(mLayoutManager);

        imageListAdapter = new ImageListAdapter();
        images_view.setAdapter(imageListAdapter);

        mItem = new ListItem();

        if (getIntent().getExtras().get("category") != null) {
            mItem.setCategory((String)getIntent().getExtras().get("category"));
            category_view.setText(mItem.getCategory());
        }
        if (getIntent().getExtras().get("name") != null) {
            mItem.setName((String)getIntent().getExtras().get("name"));
            name_edit.setText(mItem.getName());
        }
        if (getIntent().getExtras().get("description") != null) {
            mItem.setDescription((String)getIntent().getExtras().get("description"));
            description_edit.setText(mItem.getDescription());
        }
        if (getIntent().getExtras().get("image") != null) {
            mItem.setImageUrisWrapper((ArrayList)getIntent().getExtras().get("image"));
            imageListAdapter.updateData(mItem.getImageUrisWrapper());
        }

        if (getIntent().getExtras().get("uid")!= null) {
            mItem.setUid((Integer) getIntent().getExtras().get("uid"));
        }

        name_edit.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {}
             @Override
             public void afterTextChanged(Editable s) {
                mItem.setName(s.toString());
             }
        });

        description_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mItem.setDescription(s.toString());
            }
        });

        category_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) { mItem.setCategory(s.toString()); }
        });

        Button addPic_Button = (Button) findViewById(R.id.choose_pic_button);

        addPic_Button.setOnClickListener(view -> {
            imageListAdapter.deleteModeEnd();
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, OPEN_DOCUMENT_CODE);
        });

        Button delPic_Button = (Button) findViewById(R.id.delete_pic_button);
        delPic_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageListAdapter.getDeleteMode()) {
                    List<Integer> toBeDeleted = imageListAdapter.deleteModeEnd();
                    List<String> newImageList = new ArrayList<String>(mItem.getImageUrisWrapper());
                    for (int delete_index :toBeDeleted) {
                        newImageList.remove(delete_index);
                    }
                    mItem.setImageUrisWrapper(newImageList);
                    imageListAdapter.updateData(mItem.getImageUrisWrapper());
                }
                else {
                    imageListAdapter.deleteModeStart();
                }
            }
        });

        Button add_Button = (Button) findViewById(R.id.add_submit_button);
        add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ListItem> items = new ArrayList<ListItem>();
                items.add(mItem);
                model.insert(items);
                mContext.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                Uri imageUri = resultData.getData();
                List<String> new_ImageUris = new ArrayList(this.mItem.getImageUrisWrapper());
                new_ImageUris.add(imageUri.toString());
                this.mItem.setImageUrisWrapper(new_ImageUris);
                this.imageListAdapter.updateData(this.mItem.getImageUrisWrapper());
            }
        }
    }
}
