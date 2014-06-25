package com.lateralthoughts.vue.notification;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.googlenowcard.R;

public class NotificationListAdapter extends BaseAdapter {
    ArrayList<NotificationAisle> mNotificationList;
    Context mContext;
    
    public NotificationListAdapter(Context context,
            ArrayList<NotificationAisle> notificationList) {
        this.mContext = context;
        this.mNotificationList = notificationList;
    }
    
    @Override
    public int getCount() {
        try {
            return mNotificationList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public Object getItem(int position) {
        return null;
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public NotificationAisle removeItem(int position) {
        NotificationAisle notificatinAisle = null;
        if (mNotificationList != null && mNotificationList.size() > 0) {
            notificatinAisle = mNotificationList.remove(position);
        }
        Log.i("notificationLIstSize", "notificationLIstSize: "
                + mNotificationList.size());
        
        if (mNotificationList.size() == 0) {
            addTempItem();
        }
        notifyDataSetChanged();
        return notificatinAisle;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.notification_popup_window_row, null);
            holder = new ViewHolder();
            holder.notificationDescription = (TextView) convertView
                    .findViewById(R.id.notification_description);
            holder.notificationAisleTitle = (TextView) convertView
                    .findViewById(R.id.notification_aisle_title);
            holder.bookmarks = (TextView) convertView
                    .findViewById(R.id.bookmarks);
            holder.likes = (TextView) convertView.findViewById(R.id.likes);
            holder.comments = (TextView) convertView
                    .findViewById(R.id.comments);
            holder.userImage = (ImageView) convertView
                    .findViewById(R.id.user_image);
            holder.overflow_listlayout_layout = (LinearLayout) convertView
                    .findViewById(R.id.overflow_listlayout_layout);
            holder.bookmarkId = (ImageView) convertView
                    .findViewById(R.id.bookmark_id);
            holder.commentId = (ImageView) convertView
                    .findViewById(R.id.comment_id);
            holder.likeId = (ImageView) convertView.findViewById(R.id.like_id);
            holder.bottom_lay_id = (RelativeLayout) convertView
                    .findViewById(R.id.bottom_lay_id);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mNotificationList.size() == 1
                && mNotificationList.get(position).ismEmptyNotification() == true) {
            holder.notificationAisleTitle.setText(mNotificationList.get(
                    position).getAisleTitle());
            holder.bookmarks.setVisibility(View.GONE);
            holder.likes.setVisibility(View.GONE);
            holder.bookmarkId.setVisibility(View.GONE);
            holder.commentId.setVisibility(View.GONE);
            holder.likeId.setVisibility(View.GONE);
            holder.bottom_lay_id.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            int pixel = 4;
            holder.notificationAisleTitle.setMaxLines(4);
            params.setMargins(pixel, pixel, pixel, pixel);
            holder.notificationAisleTitle.setSingleLine(false);
            holder.notificationAisleTitle.setLayoutParams(params);
            ((ImageView) holder.userImage).setVisibility(View.GONE);
        } else {
            if (mNotificationList.get(position).getNotificationText()
                    .equals("Your aisle is uploading")) {
                holder.bookmarks.setVisibility(View.GONE);
                holder.likes.setVisibility(View.GONE);
                holder.bookmarkId.setVisibility(View.GONE);
                holder.commentId.setVisibility(View.GONE);
                holder.likeId.setVisibility(View.GONE);
                holder.bottom_lay_id.setVisibility(View.GONE);
            } else {
                holder.bookmarks.setVisibility(View.VISIBLE);
                holder.likes.setVisibility(View.VISIBLE);
                holder.bookmarkId.setVisibility(View.VISIBLE);
                holder.commentId.setVisibility(View.VISIBLE);
                holder.likeId.setVisibility(View.VISIBLE);
                holder.bottom_lay_id.setVisibility(View.VISIBLE);
            }
            ((ImageView) holder.userImage).setImageResource(R.drawable.index);
            holder.bookmarks.setText(mNotificationList.get(position)
                    .getBookmarkCount() + "");
            holder.likes.setText(mNotificationList.get(position).getLikeCount()
                    + "");
            holder.comments.setText(mNotificationList.get(position)
                    .getCommentsCount() + "");
            holder.notificationDescription.setText(mNotificationList.get(
                    position).getNotificationText());
            holder.notificationAisleTitle.setText(mNotificationList.get(
                    position).getAisleTitle());
            if (mNotificationList.get(position).isReadStatus()) { // read
                holder.overflow_listlayout_layout.setBackgroundColor(Color
                        .parseColor("#C0C0C0"));
            } else { // unread
                holder.overflow_listlayout_layout.setBackgroundColor(Color
                        .parseColor("#FFFFFF"));
            }
        }
        return convertView;
    }
    
    public int getSerialNumber(int position) {
        if (mNotificationList != null && mNotificationList.size() > 0) {
            return mNotificationList.get(position).getId();
        }
        return -1;
    }
    
    public void loadScreenForNotificationAisle(int position) {
    }
    
    class ViewHolder {
        ImageView userImage;
        LinearLayout overflow_listlayout_layout;
        TextView notificationDescription, bookmarks, likes, comments,
                notificationAisleTitle;
        ImageView likeId, bookmarkId, commentId;
        RelativeLayout bottom_lay_id;
    }
    
    private void addTempItem() {
        mNotificationList.clear();
        NotificationAisle notificationAisle = new NotificationAisle();
        
        notificationAisle
                .setAisleTitle("Your notifications will be shown here.");
        notificationAisle.setReadStatus(false);
        notificationAisle.setAisleId("");
        notificationAisle.setmEmptyNotification(true);
        mNotificationList.add(notificationAisle);
    }
}
