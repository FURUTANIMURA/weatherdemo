package com.example.zyx.weather.db;
//provonce.class
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Province extends SQLiteOpenHelper {
    private final String CREATE_TABLE = "CREATE TABLE PROVINCE (" +
            "_id text," +
            "id text," +
            "pid text," +
            "city_code text," +
            "city_name text)";
    public Province(Context context){
        super(context, "PROVINCE", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}


}
