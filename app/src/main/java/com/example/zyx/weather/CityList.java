package com.example.zyx.weather;
//CityList.class
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.zyx.weather.db.cityList;
import com.example.zyx.weather.Adapter.areaAdapter;
import com.example.zyx.weather.db.database;

import java.util.ArrayList;

public class CityList extends AppCompatActivity {
    private ListView listview;
    private SQLiteDatabase db;
    private database database;
    private areaAdapter adapter;
    private cityList selected;
    private ArrayList<cityList> areaList;
    private String pid;
    LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater = getLayoutInflater();
        setContentView(R.layout.city_list);
        listview = (ListView) findViewById(R.id.city_list_view);
        final Intent intent = getIntent();
        pid = intent.getStringExtra("id");
        database = new database(this);
        db = database.getReadableDatabase();
        areaList = loadCities(pid);

        adapter = new areaAdapter(layoutInflater,areaList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = areaList.get(position);
                if(selected.getCity_code().equals(""))
                    Toast.makeText(CityList.this,"city code error!", Toast.LENGTH_SHORT).show();
                else {
                    String cityCode=areaList.get(position).getCity_code();
                    Intent intent=new Intent(CityList.this,WeatherActivity.class);
                    intent.putExtra("city_code",cityCode);
                    startActivity(intent);
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selected=areaList.get(position);
                if(selected.getCity_code().equals("")){
                    Toast.makeText(CityList.this,"该地区缺失地区代码，请等待之后的更新",Toast.LENGTH_SHORT).show();
                }else {
                    String city_code=areaList.get(position).getCity_code();
                }
                Intent intent1 = new Intent(CityList.this, WeatherActivity.class);
                intent1.putExtra("city_code", areaList.get(position).getCity_code());
                startActivity(intent1);
            }
        });
    }

    public ArrayList<cityList> loadCities(String pid) {
        ArrayList<cityList> list = new ArrayList<cityList>();
        Cursor cursor = db.query("area", null, "pid=?", new String[]{String.valueOf(pid)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                cityList area = new cityList();
                area.set_id(cursor.getString(cursor.getColumnIndex("_id")));
                area.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                area.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
                area.setId(cursor.getString(cursor.getColumnIndex("id")));
                area.setPid(cursor.getString(cursor.getColumnIndex("pid")));
                list.add(area);
            } while (cursor.moveToNext());
        }
        return list;
    }
}