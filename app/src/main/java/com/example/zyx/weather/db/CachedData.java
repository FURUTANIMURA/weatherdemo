package com.example.zyx.weather.db;
//ChacheData.class
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CachedData extends SQLiteOpenHelper {
    private final String CREATE_TABLE = "CREATE TABLE cached (" +
            "city_id text primary key,"+
            "favorite text default 0, "+
            "city_name tetx,"+
            "json longtext)";
    public CachedData(Context context){
        super(context, "cached", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}
}
