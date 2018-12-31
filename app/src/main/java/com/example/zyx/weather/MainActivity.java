package com.example.zyx.weather;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.zyx.weather.db.Province;
import com.example.zyx.weather.db.database;
import com.example.zyx.weather.db.cityList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private SimpleAdapter adapter;
    private SQLiteDatabase db;
    private SQLiteDatabase db1;
    private ArrayList<cityList> provinces;
    private database database;
    LayoutInflater layoutInflater;
    private Province provincedb;
    private cityList selectedPro = new cityList();
    private List<Map<String, Object>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        provincedb = new Province(this);
        database = new database(this);
        db1 = database.getReadableDatabase();
        db = provincedb.getReadableDatabase();

        initView();
        int amount=0;
        Cursor c = db.rawQuery("select * from PROVINCE", null);
        amount=c.getCount();
        if(amount==0)
            setJsonParser();
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public void insertCity(cityList cities){
        if(cities!=null){
            ContentValues values = new ContentValues();
                values.put("_id", cities.get_id());
                values.put("id", cities.getId());
                values.put("pid", cities.getPid());
                values.put("city_code", cities.getCity_code());
                values.put("city_name", cities.getCity_name());
                if(cities.getPid().equals("0"))
                    db.insert("PROVINCE", null, values);
                else
                    db1.insert("area", null, values);
        }
    }

    public void setJsonParser() {
        String fileName = "_city.json";
        String myJson = getJson(MainActivity.this, fileName);

        Gson gson = new Gson();
        List<cityList> cities=gson.fromJson(myJson,new TypeToken<List<cityList>>(){}.getType());
        ArrayList<cityList> provinces=new ArrayList<cityList>();
        for(cityList city:cities){
            insertCity(city);
        }
    }



    @Override
    protected void onStart(){
        super.onStart();
        Refresh();
    }
    public ArrayList<cityList> loadProvinces(){
        ArrayList<cityList> list=new ArrayList<cityList>();
        ArrayList<cityList> list2=new ArrayList<cityList>();

        Cursor cursor = db.rawQuery("select _id,id,pid,city_name,city_code from PROVINCE",null);
        cursor.moveToFirst();//指的是查询结果的第一个
        Log.v("Tag",cursor.getColumnIndex("dbid")+"");
        while(!cursor.isAfterLast()){
            cityList province=new cityList();
            province.setPid(cursor.getString(cursor.getColumnIndex("pid")));
            province.set_id(cursor.getString(cursor.getColumnIndex("_id")));
            province.setId(cursor.getString(cursor.getColumnIndex("id")));
            province.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
            province.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
            list.add(province);
            cursor.moveToNext();
        }
        for (int i = list.size(); i >0; i--) {
            list2.add(list.get(i-1));
        }
        return list2;

    }

    private void initView() {
        listView = findViewById(R.id.listView);
        datalist = new ArrayList<Map<String, Object>>();
        provincedb = new Province(this);
        provincedb.getReadableDatabase();
        layoutInflater = getLayoutInflater();


        listView.setAdapter(adapter);
        provinces = new ArrayList<cityList>();

        provinces = loadProvinces();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPro = provinces.get((provinces.size()-1-position));
                Intent intent = new Intent(MainActivity.this, CityList.class);
                intent.putExtra("id", selectedPro.getId());
                startActivity(intent);
                finish();
            }
        });
    }

    public void Refresh(){
        int size = datalist.size();
        if(size>0){
            datalist.removeAll(datalist);
            adapter.notifyDataSetChanged();
        }
        Cursor cursor = db.query("PROVINCE",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("city_name", city_name);
            datalist.add(map);
        }
        adapter = new SimpleAdapter(this, datalist, R.layout.item, new String[]{"city_name"}, new int[]{R.id.city_name_view});
        listView.setAdapter(adapter);
}

}
