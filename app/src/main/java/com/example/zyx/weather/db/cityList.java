package com.example.zyx.weather.db;
//cityList.class
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class cityList {
    private SQLiteDatabase database;
    private String _id;
    private String id;
    private String pid;
    private String city_code;
    private String city_name;

    public String get_id() {
        return _id;
    }

    public String getId() {
        return id;
    }

    public String getPid() {
        return pid;
    }

    public String getCity_code() {
        return city_code;
    }

    public String getCity_name() {
        return city_name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public ArrayList<cityList> getAll(){
        Cursor cursor = database.query("PROVINCE", null, null, null, null, null, null);
        ArrayList<cityList> list = new ArrayList<>();
        cityList province;
        if (cursor.moveToFirst()) {
            do {
                province = new cityList();
                province.setId(cursor.getString(cursor.getColumnIndex("id")));
                province.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                province.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
                province.setPid(cursor.getString(cursor.getColumnIndex("pid")));
                province.set_id(cursor.getString(cursor.getColumnIndex("_id")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        return list;
    }


}
