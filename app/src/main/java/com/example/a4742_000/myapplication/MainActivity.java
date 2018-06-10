package com.example.a4742_000.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a4742_000.myapplication.Database.AppDatabase;
import com.example.a4742_000.myapplication.Database.ListItem;
import com.example.a4742_000.myapplication.Database.ListItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListItemModel mListItemViewModel;
    private RecyclerView mainListView;
    private MainAdapter mainListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Database
    private AppDatabase db;

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 200);
        return noOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainListView = (RecyclerView) findViewById(R.id.MainList);
        // Performance if size does not change
        mainListView.setHasFixedSize(true);

        // use a grid layout manager
        int numberOfColumns = calculateNoOfColumns(getApplicationContext());
        mLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mainListView.setLayoutManager(mLayoutManager);

        // Data Block
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "item_database").build();

        // specify an adapter (see also next example)
        mainListAdapter = new MainAdapter();
        mainListView.setAdapter(mainListAdapter);

        // Initiate view model
        mListItemViewModel = ViewModelProviders.of(this).get(ListItemModel.class);
        mListItemViewModel.getAllItems().observe(this, (@Nullable final List<ListItem> items) -> {
            mainListAdapter.dataChange(items);
        });

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBundle = new Intent(view.getContext(), AddUpdateItemActivity.class);
                Bundle bundle = new Bundle();
                intentBundle.putExtras(bundle);
                view.getContext().startActivity(intentBundle);
            }
        });

        FloatingActionButton deleteAllButton = (FloatingActionButton) findViewById(R.id.delete_all_button);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListItemViewModel.deleteAll();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
