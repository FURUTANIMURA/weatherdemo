package com.example.zyx.weather;
//favoriteList.class
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.zyx.weather.db.CachedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class favoriteList extends Activity implements AdapterView.OnItemClickListener {
    private ListView listview;
    private SimpleAdapter simple_adapter;
    private List<Map<String, Object>> dataList;
    private SQLiteDatabase DB;
    CachedData cachedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list);

        InitView();
    }

    //在activity显示的时候更新listview
    @Override
    protected void onStart() {
        super.onStart();
        RefreshNotesList();
    }


    private void InitView() {
        cachedData = new CachedData(this);
        listview = (ListView) findViewById(R.id.favorite);
        dataList = new ArrayList<Map<String, Object>>();
        listview.setOnItemClickListener(this);
        DB = cachedData.getReadableDatabase();

    }


    //刷新listview
    public void RefreshNotesList() {
        //如果dataList已经有的内容，全部删掉
        //并且更新simp_adapter
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            simple_adapter.notifyDataSetChanged();
        }

        //从数据库读取信息
        Cursor cursor = DB.rawQuery("select * from cached where favorite = ?",new String[]{"1"});
        startManagingCursor(cursor);
        while (cursor.moveToNext()) {
            String city_name = cursor.getString(cursor.getColumnIndex("city_name"));
            String id = cursor.getString(cursor.getColumnIndex("city_id"));
            String favorite = cursor.getString(cursor.getColumnIndex("favorite"));
            String json = cursor.getString(cursor.getColumnIndex("json"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("city_name", city_name);
            map.put("city_id",id);
            dataList.add(map);
        }
        simple_adapter = new SimpleAdapter(this, dataList, R.layout.item, new String[]{"city_name"}, new int[]{R.id.city_name_view});
        listview.setAdapter(simple_adapter);
    }

//     点击listview中某一项的点击监听事件
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //获取listview中此个item中的内容
        String content = listview.getItemAtPosition(arg2) + "";
        String city_code = content.substring(content.indexOf("city_id=")+8, content.indexOf(","));
        Intent myIntent = new Intent(this, WeatherActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city_code", city_code);
        myIntent.putExtras(bundle);
        startActivity(myIntent);
    }
}
