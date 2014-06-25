package com.card.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.card.utils.AppConstants;
import com.card.utils.StaticUrls;
import com.card.utils.Utils;
import com.card.ui.R;
import com.imagedownload.ImageLoader;

public class DetailsViewCard extends BaseAdapter {
    Context mContext;
    int mPosition = -1;
    LayoutInflater mInflater;
    ImageLoader imageLoader;
    ArrayList<String> commentList = new ArrayList<String>();
    String url[];
    int pagerHeight;
    
    public DetailsViewCard(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 8; i++) {
            commentList.add("Comment: " + i);
        }
        url = StaticUrls.getUrls();
        imageLoader = ImageLoader.getInstance(context);
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        
        return url.length;
    }
    
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DetailsViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new DetailsViewHolder();
            convertView = mInflater.inflate(R.layout.card_details_view, null);
            viewHolder.menuId = (ImageView) convertView
                    .findViewById(R.id.details_image_view);
            viewHolder.commentList = (ListView) convertView
                    .findViewById(R.id.comment_list_id);
            viewHolder.shareText = (TextView) convertView
                    .findViewById(R.id.share_text_id);
            viewHolder.bookMarkText = (TextView) convertView
                    .findViewById(R.id.bookmark_text_id);
            viewHolder.commentText = (TextView) convertView
                    .findViewById(R.id.comment_text_id);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (DetailsViewHolder) convertView.getTag();
        }
        viewHolder.commentText.setText("" + Utils.randInt(1, 5));
        viewHolder.shareText.setText("" + Utils.randInt(1, 5));
        viewHolder.bookMarkText.setText("" + Utils.randInt(1, 5));
        viewHolder.commentList.setVisibility(View.GONE);
        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
            }
        });
        
        imageLoader.DisplayImage(url[position], 0, viewHolder.menuId,
                AppConstants.SCREEN_HEIGHT, AppConstants.SCREEN_WIDTH);
        
        return convertView;
    }
}
