package com.example.zyx.weather.Adapter;
//areaAdapter
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.zyx.weather.R;
import com.example.zyx.weather.db.cityList;

import java.util.ArrayList;

public class areaAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<cityList> array;

    public areaAdapter(LayoutInflater inf,ArrayList<cityList> arry){
        this.inflater=inf;
        this.array=arry;
    }

    public int getCount() {
        return array.size();
    } //适配器中数据集的数据个数

    public Object getItem(int position) {
        return array.get(position);
    } //获取数据集中与索引对应的数据项

    public long getItemId(int position) {
        return position;
    } //获取指定行对应的ID

    //代码块中包含了对listview的效率优化
    public View getView(int position, View convertView, ViewGroup parent) {  //获取每一行Item的显示内容。
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=inflater.inflate(R.layout.item,null);//加载listview子项
            vh.tv1=(TextView) convertView.findViewById(R.id.city_name_view);
            convertView.setTag(vh);
        }
        vh=(ViewHolder) convertView.getTag();
        vh.tv1.setText(array.get(position).getCity_name());
        return convertView;
    }

    //内部类，对控件进行缓存
    class ViewHolder{
        TextView tv1;
    }
}
