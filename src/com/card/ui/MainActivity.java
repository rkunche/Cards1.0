package com.card.ui;

import java.util.ArrayList;

import junit.framework.Assert;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aisles.datamodels.AisleWindowContent;
import com.card.utils.AppConstants;
import com.datafetcher.FetchTrendingDataTask;
import com.lateralthoughts.vue.notification.NotificationAisle;
import com.lateralthoughts.vue.notification.NotificationManager;
import com.lateralthoughts.vue.notification.PopupFragment;
import com.vue.logs.LogTags;
import com.vue.logs.Logging;

public class MainActivity extends Activity implements
        ActivityFragmentCommunicator {
    
    private Fragment mLandingAilsesFrag;
    private Fragment mDetailsFragment;
    private Fragment mPopupFragment;
    private Fragment mRatingFragment;
    private boolean mIsDetailsFragmentAdded;
    private boolean mIsNotificationListShowing;
    private TextView mNotificationTview;
    private static int sAisleLimit = 30;
    public static MainActivity mCurrentInstance;
    private static final String sNotificationFrag = "NOTIFICATION";
    private static final String sDetailsFrag = "DETAILS";
    private static final String sRatingFrag = "RATING";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        boolean emergencyFlag = false;
        //loggin the events
        Logging.i(LogTags.LAUNCH_ACTIVITY,"Launcher Activity started", emergencyFlag);
        getActionBar().setHomeButtonEnabled(true);
        mCurrentInstance = this;
        // inflate the card fragment into main screen
        mLandingAilsesFrag = new CardFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mLandingAilsesFrag).commit();
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(
                        R.color.actionbar_backround)));
        measuerScreenDimensions();
        invalidateOptionsMenu();
        if (isNetworkAvailable()) {
            FetchTrendingDataTask task = new FetchTrendingDataTask(mHandler,
                    sAisleLimit, 0);
            task.execute();
        } else {
            Toast.makeText(this, "Check your network settings",
                    Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // get the actionView from the system action bar.
        MenuItem menuItem = menu.findItem(R.id.notification);
        RelativeLayout rel = (RelativeLayout) menuItem.getActionView()
                .findViewById(R.id.notification_icon_id);
        mNotificationTview = (TextView) menuItem.getActionView().findViewById(
                R.id.notification_count_id);
        rel.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                if (!mIsNotificationListShowing) {
                    showNotificationListFragment();
                } else {
                    removeNotificationListFragment();
                    FragmentBackStack.getInstance().getTopFragment();
                }
            }
        });
        getActionBar().setHomeButtonEnabled(true);
        
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowCustomEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setCustomView(null);
        getActionBar().setDisplayShowTitleEnabled(true);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        
        switch (itemId) {
        case android.R.id.home:
            String fragName = FragmentBackStack.getInstance().getTopFragment();
            if (fragName == null) {
                
            } else if (fragName
                    .equalsIgnoreCase(MainActivity.sNotificationFrag)) {
                if (mIsNotificationListShowing) {
                    removeNotificationListFragment();
                    return true;
                }
            } else if (fragName.equalsIgnoreCase(MainActivity.sDetailsFrag)) {
                if (mIsDetailsFragmentAdded) {
                    removeDetailsFragement();
                    return true;
                }
            } else if (fragName.equalsIgnoreCase(MainActivity.sRatingFrag)) {
                removeRatingFragment();
                return true;
            }
            break;
        case R.id.notification:
            
            break;
        
        }
        
        return true;
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*
             * if more than one fragment is attached to the main activity, needs
             * to find which one is the top and removed in the same order in
             * which they are added.
             */
            String fragName = FragmentBackStack.getInstance().getTopFragment();
            if (fragName == null) {
                return super.onKeyUp(keyCode, event);
            } else if (fragName
                    .equalsIgnoreCase(MainActivity.sNotificationFrag)) {
                if (mIsNotificationListShowing) {
                    removeNotificationListFragment();
                    return true;
                }
            } else if (fragName.equalsIgnoreCase(MainActivity.sDetailsFrag)) {
                if (mIsDetailsFragmentAdded) {
                    removeDetailsFragement();
                    return true;
                }
            } else if (fragName.equalsIgnoreCase(MainActivity.sRatingFrag)) {
                removeRatingFragment();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    
    /**
     * adds the details fragment to the main activity, this fragment show the
     * full image of the selected aisle.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDetailsFragment() {
        if (!mIsDetailsFragmentAdded) {
            mIsDetailsFragmentAdded = true;
            mDetailsFragment = new DetailsCardViewFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right);
            transaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.content_frame, mDetailsFragment);
            transaction.commit();
            FragmentBackStack.getInstance().addFragment(sDetailsFrag);
        }
    }
    
    /**
     * Removes the details fragment from the main activity.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void removeDetailsFragement() {
        if (mIsDetailsFragmentAdded) {
            mIsDetailsFragmentAdded = false;
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right);
            transaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.remove(mDetailsFragment);
            transaction.commit();
            
        }
    }
    
    /**
     * shows the notification fragment in the main activity, these notifications
     * are received from gcm, notifications are belongs to user events such as
     * likes, comments,add image and user uploads from vue.
     * 
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showNotificationListFragment() {
        try {
            mIsNotificationListShowing = true;
            mPopupFragment = new PopupFragment(getUserNotifacation());
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right);
            transaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(R.id.content_frame, mPopupFragment);
            transaction.commit();
            FragmentBackStack.getInstance().addFragment(sNotificationFrag);
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * removes the notification list fragment from the main activity with left
     * out animation.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void removeNotificationListFragment() {
        mIsNotificationListShowing = false;
        if (mPopupFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right);
            transaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.remove(mPopupFragment);
            transaction.commit();
        }
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showRatingFragment() {
        try {
            if (!mIsNotificationListShowing) {
                mIsNotificationListShowing = true;
                mRatingFragment = new RatingFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager
                        .beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_out_right);
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(R.id.content_frame, mRatingFragment);
                transaction.commit();
                FragmentBackStack.getInstance().addFragment(sRatingFrag);
            }
            
        } catch (Throwable e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * removes the Rating fragment from the main activity with left out
     * animation.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void removeRatingFragment() {
        mIsNotificationListShowing = false;
        if (mRatingFragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in_left,
                    R.animator.slide_out_right);
            transaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.remove(mRatingFragment);
            transaction.commit();
        }
    }
    
    /**
     * finds the screen dimensions of the device.
     */
    private void measuerScreenDimensions() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        AppConstants.SCREEN_HEIGHT = metrics.heightPixels;
        AppConstants.SCREEN_WIDTH = metrics.widthPixels;
    }
    
    /**
     * 
     * returns all the user notifications if no notifications are found, then
     * return a dummy notification aisle in list will be return.
     */
    private ArrayList<NotificationAisle> getUserNotifacation() {
        NotificationManager notificationManager = new NotificationManager();
        return notificationManager.getUserNotifications();
        
    }
    
    /**
     * receives the aisles from the back end and notifies the list view adapter.
     */
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            @SuppressWarnings("unchecked")
            ArrayList<AisleWindowContent> aisleContentArray = (ArrayList<AisleWindowContent>) msg.obj;
            // on receiving aisles from the network make sure the list is not
            // null.
            Assert.assertFalse("aisle items are null",
                    aisleContentArray == null);
            // sets the list to the adapter.
            ((CardFragment) mLandingAilsesFrag)
                    .setWindowList(aisleContentArray);
        };
    };
    
    /**
     * handles the click event from the browser and its responsibility is to
     * take to the details fragment.
     */
    @Override
    public void onBrowserClickEvent(AisleWindowContent aisle) {
        showRatingFragment();
        // Toast.makeText(this, "click received", Toast.LENGTH_LONG).show();
        // showDetailsFragment();
    }
    
    @Override
    public void onDoneButtonClickFromRatingScreen() {
        removeRatingFragment();
    }
    
    private boolean isNetworkAvailable() {
        @SuppressWarnings("static-access")
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
