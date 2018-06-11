package com.example.a4742_000.myapplication.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.Map;

public class ListItemRepository {
    private ListItemDao mListItemDao;
    private LiveData<List<ListItem>> items;
    private LiveData<List<String>> categories;

    ListItemRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        mListItemDao = db.listItemDao();
        items = mListItemDao.getAll();
        categories = mListItemDao.getAllCategories();
    }

    LiveData<List<String>> getAllCategories() { return this.categories; }

    LiveData<List<ListItem>> getAllItems() {
        return this.items;
    }

    public void insert (List<ListItem> items) {
        new insertAsyncTask(mListItemDao).execute(items);
    }

    private static class insertAsyncTask extends AsyncTask<List<ListItem>, Void, Void> {

        private ListItemDao mAsyncTaskDao;

        insertAsyncTask(ListItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<ListItem>... params) {
            mAsyncTaskDao.insertAll(params[0]);
            return null;
        }
    }

    public void delete (ListItem item) {
        new deleteAsyncTask(mListItemDao).execute(item);
    }

    private static class deleteAsyncTask extends AsyncTask<ListItem, Void, Void> {

        private ListItemDao mAsyncTaskDao;

        deleteAsyncTask(ListItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ListItem... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    public void deleteAll () {
        new deleteAllAsyncTask(mListItemDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private ListItemDao mAsyncTaskDao;

        deleteAllAsyncTask(ListItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
