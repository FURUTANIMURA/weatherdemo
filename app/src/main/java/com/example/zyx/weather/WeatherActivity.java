package com.example.zyx.weather;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zyx.weather.db.CachedData;
import com.example.zyx.weather.db.WeatherInfo;
import com.example.zyx.weather.db.cityList;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends Activity implements View.OnClickListener {

    private  String city_code;

    private SimpleAdapter adapter;
    private SQLiteDatabase db;
    private CachedData cached;
    private WeatherInfo weatherInfo;
    //用于显示城市名
    private TextView city_name_text;
    //用于显示日期
    private TextView current_date;
    //用于显示更新时间
    private TextView upadate_date;
    //用于显示天气情况
    private TextView today_weather;
    //用于显示当天低温
    private TextView today_low;
    //用于显示当天高温
    private TextView today_high;
    //用于显示当天湿度
    private TextView today_humidity;
    //用于显示当天PM2.5
    private TextView today_pm25;
    //用于显示天气情况
    private TextView tomorrow_weather;
    //用于显示明天低温
    private TextView tomorrow_low;
    //用于显示明天高温
    private TextView tomorrom_high;
    //用于显示天气情况
    private TextView aftomorrow_type;
    //用于显示后天低温
    private TextView aftomorrow_low;
    //用于显示后天高温
    private TextView aftomorrow_high;
    //更新天气按钮
    private Button refresh;
    private Button addtofav;
    private final OkHttpClient client = new OkHttpClient();
    private String weatherJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);
        //初始化各控件
        city_name_text = (TextView) findViewById(R.id.city_name_text);
         cached = new CachedData(this);
         db = cached.getReadableDatabase();
        current_date = (TextView) findViewById(R.id.current_date);
        upadate_date = (TextView) findViewById(R.id.upadate_date);
        today_weather = (TextView) findViewById(R.id.today_type);
        today_low = (TextView) findViewById(R.id.today_low);
        today_high = (TextView) findViewById(R.id.today_high);
        today_humidity = (TextView) findViewById(R.id.humidity_txet);
        today_pm25 = (TextView) findViewById(R.id.PM25_txet);
        tomorrow_weather = (TextView) findViewById(R.id.tom_type);
        tomorrow_low = (TextView) findViewById(R.id.tom_low);
        tomorrom_high = (TextView) findViewById(R.id.tom_high);
        aftomorrow_type = (TextView) findViewById(R.id.tom2_type);
        aftomorrow_low = (TextView) findViewById(R.id.tom2_low);
        aftomorrow_high = (TextView) findViewById(R.id.tom2_high);
        refresh = (Button) findViewById(R.id.refressh);
        addtofav = (Button)findViewById(R.id.add_to_favorite);
        addtofav.setOnClickListener(this);
        refresh.setOnClickListener(this);


        //获取传来的参数
        Intent intent = getIntent();
        int amount = 0;
        city_code = intent.getStringExtra("city_code");
//        Cursor cursor = db.rawQuery("select * from cached where city_id = ?", new String[]{city_code});
        Cursor cursor = db.rawQuery("select * from cached where city_id = ?",new String[]{city_code});
        amount = cursor.getCount();
        if(amount==0) {
            Request.Builder cityidBuilder = new Request.Builder();
            cityidBuilder.url("http://t.weather.sojson.com/api/weather/city/" + city_code);
            Request cityRequest = cityidBuilder.build();
            Call cityCall = client.newCall(cityRequest);
            cityCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WeatherActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    weatherJson = response.body().string();
                    Gson gson = new Gson();
                    weatherInfo = gson.fromJson(weatherJson, WeatherInfo.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!weatherInfo.message.equals("Success !"))
                                Toast.makeText(WeatherActivity.this, "不存在的城市", Toast.LENGTH_SHORT).show();
                            else {
                                setText(weatherInfo);
                                ContentValues values = new ContentValues();
                                values.put("json", weatherJson);
                                values.put("city_id", city_code);
                                db.insert("cached", null, values);
                            }
                        }
                    });
                }
            });
        }
        else {
            String js = "";
            Cursor cursor1 = db.rawQuery("select * from cached where city_id = ?", new String[]{city_code});
            if(cursor1.moveToFirst()) {
                do {
                    js = cursor1.getString(cursor.getColumnIndex("json"));
                } while (cursor1.moveToNext());
                cursor1.close();
            }
            Gson gson1 = new Gson();
            weatherInfo = gson1.fromJson(js, WeatherInfo.class);
            setText(weatherInfo);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_to_favorite:
                String favorited = "0";
                Cursor cur = db.rawQuery("select * from cached where city_id = ?", new String[]{city_code});
                ContentValues values = new ContentValues();
                if(cur.moveToFirst()) {
                    do {
                        favorited = cur.getString(cur.getColumnIndex("favorite"));
                    } while (cur.moveToNext());
                    cur.close();
                }
                if(favorited.equals("1")) {
                    values.put("favorite", 0);
                    values.put("city_name", weatherInfo.cityInfo.city);
                    values.put("city_id", city_code);
                    values.put("json", weatherJson);
                    db.update("cached", values, "city_id = ?", new String[]{city_code});
                    Toast.makeText(WeatherActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                }else{
                    values.put("favorite", 1);
                    values.put("city_name", weatherInfo.cityInfo.city);
                    values.put("city_id", city_code);
                    values.put("json", weatherJson);
                    db.update("cached", values, "city_id = ?", new String[]{city_code});
                    Toast.makeText(WeatherActivity.this, "收藏成功，再次点击取消收藏", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.refressh:
                Request.Builder cityidBuilder = new Request.Builder();
                cityidBuilder.url("http://t.weather.sojson.com/api/weather/city/" + city_code);
                Request cityRequest = cityidBuilder.build();
                Call cityCall = client.newCall(cityRequest);
                cityCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WeatherActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        weatherJson = response.body().string();
                        Gson gson = new Gson();
                        weatherInfo = gson.fromJson(weatherJson, WeatherInfo.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!weatherInfo.message.equals("Success !"))
                                    Toast.makeText(WeatherActivity.this, "不存在的城市", Toast.LENGTH_SHORT).show();
                                else {
                                    setText(weatherInfo);
                                }
                            }
                        });
                    }
                });
                break;
        }
    }


    public void setText(WeatherInfo weatherInfo){
        city_name_text.setText(weatherInfo.cityInfo.city);
        current_date.setText(weatherInfo.date);
        upadate_date.setText(weatherInfo.time);
        today_pm25.setText(weatherInfo.data.pm25);
        today_humidity.setText(weatherInfo.data.shidu);
        today_low.setText(weatherInfo.data.forecast.get(0).low + " ");
        today_high.setText(weatherInfo.data.forecast.get(0).high + " ");
        today_weather.setText(weatherInfo.data.forecast.get(0).type + " ");
        tomorrom_high.setText(weatherInfo.data.forecast.get(1).high + " ");
        tomorrow_low.setText(weatherInfo.data.forecast.get(1).low + " ");
        tomorrow_weather.setText(weatherInfo.data.forecast.get(1).type + " ");
        aftomorrow_high.setText(weatherInfo.data.forecast.get(2).high + " ");
        aftomorrow_low.setText(weatherInfo.data.forecast.get(2).low + " ");
        aftomorrow_type.setText(weatherInfo.data.forecast.get(2).type + " ");
    }
}
