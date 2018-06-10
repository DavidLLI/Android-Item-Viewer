package com.example.a4742_000.myapplication.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class ListItem {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo( name = "name" )
    private String name;

    @ColumnInfo( name = "imageUris" )
    private String imageUris;

    @ColumnInfo( name = "description" )
    private String description;

    public ListItem() {}

    private String convertListOfStringtoString(List<String> sList) {
        StringBuilder sb = new StringBuilder();
        for (String s : sList) {
            sb.append(s);
            sb.append(",");
        }
        return sb.toString();
    }

    private List<String> convertStringtoListOfString(String longs) {
        if (longs == null) {
            return new ArrayList<String>();
        }
        List<String> sList = Arrays.asList(longs.split(","));
        return sList;
    }

    public ListItem (int uid, String name, List<String> imageUris, String description) {
        this.uid = uid;
        this.name = name;
        this.imageUris = convertListOfStringtoString(imageUris);
        this.description = description;
    }

    public ListItem (String name, List<String> imageUris, String description) {
        this.name = name;
        this.imageUris = convertListOfStringtoString(imageUris);
        this.description = description;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return this.uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setImageUrisWrapper(List<String> imageUris) {
        this.imageUris = convertListOfStringtoString(imageUris);
    }

    public List<String> getImageUrisWrapper() {
        return convertStringtoListOfString(this.imageUris);
    }

    public void setImageUris(String imageUris) {
        this.imageUris = imageUris;
    }

    public String getImageUris() {
        return this.imageUris;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
