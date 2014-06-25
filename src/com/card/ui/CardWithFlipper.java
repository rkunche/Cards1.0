package com.card.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aisles.datamodels.AisleContext;
import com.aisles.datamodels.AisleImageDetails;
import com.aisles.datamodels.AisleWindowContent;
import com.card.utils.AppConstants;
import com.card.utils.Utils;
import com.card.ui.R;

public class CardWithFlipper extends DataAdapter {
    
    private final int EMPTY_AISLE_ID = 1234;
    final int MAX_ANIM_COUNT = 20;
    private Context mContext;
    private LayoutInflater mInflater;
    private Bitmap mIcon;
    private Animation mAnimFadein;
    private Animation mAnimFadeOut;
    private int mAnimSpeed;
    private int mAnimCount;
    private boolean mDontShowNextAnim;
    private int mPagerCardBottomMargin = 22 + 48;
    ArrayList<AisleImageDetails> mEmptyImageList;
    private ActivityFragmentCommunicator mActivityFragmentCommunicatorListner;
    
    private AisleLoader mAisleLoader;
    
    CardWithFlipper(Context context, ActivityFragmentCommunicator listener) {
        super(context);
        mContext = context;
        mEmptyImageList = new ArrayList<AisleImageDetails>();
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mIcon = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.index);
        // alpha animations for next arrow on card.
        mAnimFadein = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        mAnimFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        Resources resources = mContext.getResources();
        mAnimSpeed = resources.getInteger(R.integer.anim_speed);
        mAisleLoader = AisleLoader.getInstance(mContext);
        mAisleLoader.setmActivityComunicator(listener);
        mActivityFragmentCommunicatorListner = listener;
        
        // when no cards available show one default card.
        setTempCards();
        
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.card_flipper, null);
            // aisleContentBrowser(flipper) has only one static child, if user
            // swipe horizontally adds more child from the pool of inflated
            // Views.
            viewHolder.aisleContentBrowser = (AisleContentBrowser) convertView
                    .findViewById(R.id.pager);
            viewHolder.ImgCardPhotoPicOne = (ImageView) convertView
                    .findViewById(R.id.pager_image_one);
            viewHolder.imgCardProfilePicOne = (ImageView) convertView
                    .findViewById(R.id.pager_suggester_image_id_one);
            viewHolder.imgCardSuggesterOne = (TextView) convertView
                    .findViewById(R.id.imgcard_suggester_name_one);
            viewHolder.imgCardfindAtOne = (TextView) convertView
                    .findViewById(R.id.pagercard_find_id_one);
            viewHolder.imgCardDescOne = (TextView) convertView
                    .findViewById(R.id.card_bottom_text);
            viewHolder.aisleOwnerCardTitleTview = (TextView) convertView
                    .findViewById(R.id.card_user_heading_id);
            viewHolder.aisleOwnerCardOwnerNameTview = (TextView) convertView
                    .findViewById(R.id.card_user_name_id);
            viewHolder.ailseImageCardLayout = (RelativeLayout) convertView
                    .findViewById(R.id.pager_layout_id);
            viewHolder.aisleContentBrowser.setId(EMPTY_AISLE_ID);
            viewHolder.infLayout = (RelativeLayout) convertView
                    .findViewById(R.id.info_card_lay);
            viewHolder.aisleOwnerProfilePic = (ImageView) convertView
                    .findViewById(R.id.card_user_id);
            viewHolder.shopCardLay = (RelativeLayout) convertView
                    .findViewById(R.id.card_shop_cart_id);
            viewHolder.createAisleId = (RelativeLayout) convertView
                    .findViewById(R.id.card_create_aisle_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.aisleWindowContent = mAisleWindowList.get(position);
        if (position == 2) {
            // show the info card at this position
            viewHolder.aisleWindowContent.setBestLargestHeightForWindow(Utils
                    .getMaxCardHeight(mContext));
            viewHolder.infLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.infLayout.setVisibility(View.GONE);
        }
        viewHolder.aisleOwnerCardTitleTview.setText(mAisleWindowList.get(
                position).getAisleContext().mLookingForItem);
        viewHolder.aisleOwnerCardOwnerNameTview.setText(mAisleWindowList.get(
                position).getAisleContext().mFirstName
                + " "
                + mAisleWindowList.get(position).getAisleContext().mLastName);
        viewHolder.imgCardDescOne.setText(mAisleWindowList.get(position)
                .getAisleContext().mDescription);
        // set the params based on the best image height in the aisle.
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                mAisleWindowList.get(position).getBestLargetHeightForWindow()
                        + Utils.getPixel(mContext, mPagerCardBottomMargin));
        viewHolder.aisleContentBrowser.setLayoutParams(params);
        // gets the current aisle from the list.
        int itemId = mAisleWindowList.get(position).mTagId;
        if (!CardFragment.sIsListScrolling
                && !CardFragment.sIsTouchScrolingCall) {
            /*
             * To improve the scrolling performance of the getview() method,
             * don't load the aisles into the getview() if the user scrolls
             * vertically.
             */
            if (itemId != viewHolder.aisleContentBrowser.getId()) {
                // load the browser if it is not new one
                viewHolder.aisleContentBrowser
                        .setId(viewHolder.aisleWindowContent.mTagId);
                mAisleLoader.loadAisleContentIntoView(viewHolder, position);
            }
        } else {
            if (itemId != viewHolder.aisleContentBrowser.getId()) {
                // clear the browser if it not the current one.
                viewHolder.aisleOwnerProfilePic.setImageBitmap(null);
                viewHolder.aisleContentBrowser.setId(EMPTY_AISLE_ID);
                for (int i = 0; i < viewHolder.aisleContentBrowser
                        .getChildCount(); i++) {
                    View view = viewHolder.aisleContentBrowser.getChildAt(i);
                    if (view != null) {
                        if (i == 0) {
                            // static view
                            ImageView imageView = (ImageView) view
                                    .findViewById(R.id.pager_image_one);
                            if (imageView != null) {
                                imageView.setImageBitmap(null);
                            }
                        }
                        if (i >= 1) {
                            // After first view remaining views are dynamically
                            // added, so return these
                            // views to pool.
                            ImageView iamgeView = (ImageView) view
                                    .findViewById(R.id.pager_image_one);
                            
                            iamgeView.setImageBitmap(null);
                            mAisleLoader.returnUsedView(view);
                            viewHolder.aisleContentBrowser.removeView(view);
                        }
                    }
                }
            }
        }
        viewHolder.shopCardLay.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
            }
        });
        viewHolder.createAisleId.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
            }
        });
        return convertView;
    }
    
    /**
     * 
     * shows menu items when user clicks on the menu in the card.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(mContext, v);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,
                popupMenu.getMenu());
        
        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(mContext, item.toString(),
                                Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
        
        popupMenu.show();
    }
    
    // animation for the next button on the imageCard.
    private void nextButtonAnim(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAnimation(mAnimFadeOut);
        view.startAnimation(mAnimFadeOut);
        mAnimFadeOut.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                view.setAnimation(mAnimFadein);
                view.startAnimation(mAnimFadein);
            }
        });
        mAnimFadein.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                mAnimCount++;
                if (mAnimCount < MAX_ANIM_COUNT) {
                    view.setAnimation(mAnimFadeOut);
                    view.startAnimation(mAnimFadeOut);
                } else {
                    mDontShowNextAnim = true;
                    view.setVisibility(View.GONE);
                }
                
            }
        });
        
    }
    
    // notifiy the listview with new data.
    public void setWindowList(ArrayList<AisleWindowContent> windowList) {
        mAisleWindowList = windowList;
        for (int i = 0; i < mAisleWindowList.size(); i++) {
            mAisleWindowList.get(i).mTagId = i;
        }
        notifyDataSetChanged();
    }
    
    /**
     * if the data is not available, show a temp card with some text information
     * until data is available.
     */
    private void setTempCards() {
        ArrayList<AisleImageDetails> aisleImageDetailsList = new ArrayList<AisleImageDetails>();
        AisleImageDetails aisleImageDetails = new AisleImageDetails();
        aisleImageDetails.mAvailableHeight = Utils.getMaxCardHeight(mContext);
        aisleImageDetails.mAvailableWidth = AppConstants.SCREEN_WIDTH;
        aisleImageDetails.mImageUrl = "No ImageUrl";
        aisleImageDetailsList.add(aisleImageDetails);
        AisleContext aisleContext = new AisleContext();
        aisleContext.mAisleId = "123456789";
        aisleContext.mAisleOwnerImageURL = "";
        aisleContext.mDescription = "Your aisle has 20 likes";
        aisleContext.mLookingForItem = "Looking for watches";
        aisleContext.mFirstName = "Stuwart";
        aisleContext.mLastName = "Black";
        aisleImageDetailsList.add(aisleImageDetails);
        AisleWindowContent aisleWindowContent = new AisleWindowContent(
                aisleContext.mAisleId);
        aisleWindowContent.mTagId = EMPTY_AISLE_ID;
        aisleWindowContent.addAisleContent(aisleContext, aisleImageDetailsList);
        mAisleWindowList.add(aisleWindowContent);
    }
}
