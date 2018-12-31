package com.example.zyx.weather;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.OkHttpClient;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private final OkHttpClient client = new OkHttpClient();
    private EditText editText;
    private Button search;
    private Button select;
    private Button favo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        editText = (EditText)findViewById(R.id.edit_city);
        select = (Button)findViewById(R.id.select);
        search = (Button)findViewById(R.id.btn_search);
        favo = (Button)findViewById(R.id.favorites);
        favo.setOnClickListener(this);
        search.setOnClickListener(this);
        select.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                final String city_code = editText.getText().toString();
                if (city_code.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "城市不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SearchActivity.this, WeatherActivity.class);
                intent.putExtra("city_code",city_code);
                startActivity(intent);
                break;
            case R.id.select:
                Intent citiListIntent = new Intent(this, MainActivity.class);
                startActivity(citiListIntent);
                break;
            case R.id.favorites:
                Intent intent1 = new Intent(SearchActivity.this, favoriteList.class);
                startActivity(intent1);
                break;
        }
    }
}
