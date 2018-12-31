package com.example.zyx.weather.db;
//database.class
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class database extends SQLiteOpenHelper {
    private final String CREATE_TABLE = "CREATE TABLE AREA (" +
            "_id text," +
            "id text," +
            "pid text," +
            "city_code text," +
            "city_name text)";
    public database(Context context){
        super(context, "area", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}