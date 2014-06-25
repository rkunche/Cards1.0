package com.example.googlenowcard;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
/*
 * Cache the image card views, expands if necessary
 * released views will be added back to the pool.
 */
public class AisleBrowserHolder {
    static AisleBrowserHolder aisleBrowserHolder;
    LayoutInflater mInflater;
    ArrayList<View> viewList = new ArrayList<View>();
    private static final int INITIAL_BROWSER_COUNT = 10;
    private static final int NEXT_BATCH_COUNT = 10;
 public AisleBrowserHolder(Context context) {
     if (mInflater == null) {
         mInflater = (LayoutInflater)context.getSystemService(
                 Context.LAYOUT_INFLATER_SERVICE);
         
     }
     for(int index = 0;index < INITIAL_BROWSER_COUNT;index++){
         viewList.add(mInflater.inflate(R.layout.pager_card_one, null));
     }
}   
public static AisleBrowserHolder getInstance(Context context){
    if(aisleBrowserHolder == null){
        aisleBrowserHolder = new AisleBrowserHolder(context);  
    }
    return aisleBrowserHolder;
}
public View getEmptyBrowserView(){
    synchronized (this) {
        if(viewList.size()<1){
            return  expandBrowserView();
          } else {
              return viewList.remove(0);
          }
    }
  
}
public View expandBrowserView(){
    for(int index = 0;index < NEXT_BATCH_COUNT;index++){
        viewList.add(mInflater.inflate(R.layout.pager_card_one, null));
    }
    return viewList.remove(0);
}
public void returnUsedView(View view){
    if(view != null){
        viewList.add(view);
    }
}
}
