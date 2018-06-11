package com.example.a4742_000.myapplication.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ListItemModel extends AndroidViewModel {
    private ListItemRepository mRepository;

    private LiveData<List<ListItem>> AllItems;
    private LiveData<List<String>> AllCategories;

    public ListItemModel (Application application) {
        super(application);
        mRepository = new ListItemRepository(application);
        AllItems = mRepository.getAllItems();
        AllCategories = mRepository.getAllCategories();
    }

    public LiveData<List<String>> getAllCategories() { return this.AllCategories; }

    public LiveData<List<ListItem>> getAllItems() {
        return AllItems;
    }

    public void insert(List<ListItem> items) {
        mRepository.insert(items);
    }

    public void delete(ListItem item) {
        mRepository.delete(item);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
