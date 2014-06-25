package com.example.googlenowcard;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aisles.datamodels.AisleWindowContent;

public class DataAdapter extends BaseAdapter implements DataContainer{
    ArrayList<AisleWindowContent> aisleWindowList = new ArrayList<AisleWindowContent>();
    DataAdapter(Context context){
        
    }
   
    public int getCount() {
        // TODO Auto-generated method stub
        return aisleWindowList.size();
    }

    
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

   
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

   
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void notifyAdapters() {
       notifyDataSetChanged();
        
    }

    @Override
    public void addMoreData(ArrayList<AisleWindowContent> aisleList) {
        aisleWindowList.addAll(aisleList);
        notifyDataSetChanged();
    }

    @Override
    public void clearAllData() {
        aisleWindowList.clear();
        notifyDataSetChanged();
        
    }
    
}
