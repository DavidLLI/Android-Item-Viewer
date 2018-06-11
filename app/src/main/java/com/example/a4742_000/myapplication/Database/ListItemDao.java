package com.example.a4742_000.myapplication.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ListItemDao {
    @Query("SELECT * FROM ListItem")
    LiveData<List<ListItem>> getAll();

    @Query("SELECT * FROM ListItem WHERE uid IN (:userIds)")
    LiveData<List<ListItem>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM ListItem WHERE uid == :userId")
    LiveData<ListItem> findById(int userId);

    @Query("SELECT * FROM ListItem WHERE name LIKE :name")
    LiveData<ListItem> findByName(String name);

    @Query("SELECT * FROM ListItem WHERE category == :category")
    LiveData<List<ListItem>> findByCategory(String category);

    @Query("SELECT DISTINCT category FROM ListItem")
    LiveData<List<String>> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ListItem> item);

    @Delete
    void delete(ListItem user);

    @Query("DELETE FROM ListItem")
    void deleteAll();
}
