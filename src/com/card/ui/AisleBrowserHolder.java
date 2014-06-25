package com.card.ui;

import java.util.ArrayList;

import com.card.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/*
 * Cache the image card views, expands if necessary
 * released views will be added back to the pool.
 */
public class AisleBrowserHolder {
    static AisleBrowserHolder sAisleBrowserHolder;
    LayoutInflater mInflater;
    ArrayList<View> mViewList = new ArrayList<View>();
    private static final int INITIAL_BROWSER_COUNT = 10;
    private static final int NEXT_BATCH_COUNT = 10;
    
    public AisleBrowserHolder(Context context) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
        }
        for (int index = 0; index < INITIAL_BROWSER_COUNT; index++) {
            mViewList.add(mInflater.inflate(R.layout.pager_card_one, null));
        }
    }
    
    public static AisleBrowserHolder getInstance(Context context) {
        if (sAisleBrowserHolder == null) {
            sAisleBrowserHolder = new AisleBrowserHolder(context);
        }
        return sAisleBrowserHolder;
    }
    
    public View getEmptyBrowserView() {
        synchronized (this) {
            if (mViewList.size() < 1) {
                return expandBrowserView();
            } else {
                return mViewList.remove(0);
            }
        }
        
    }
    
    public View expandBrowserView() {
        for (int index = 0; index < NEXT_BATCH_COUNT; index++) {
            mViewList.add(mInflater.inflate(R.layout.pager_card_one, null));
        }
        return mViewList.remove(0);
    }
    
    public void returnUsedView(View view) {
        if (view != null) {
            mViewList.add(view);
        }
    }
}
