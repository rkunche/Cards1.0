package com.card.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.aisles.datamodels.AisleImageDetails;
import com.aisles.datamodels.AisleWindowContent;
import com.card.utils.AppConstants;
import com.card.ui.R;
import com.imagedownload.ImageLoader;

public class AisleContentBrowser extends ViewFlipper {
    private String mAisleUniqueId;
    private String mSourceName;
    int mCurrentIndex;
    private ImageView mStarIcon;
    TextView mLikesCountView;
    TextView mBookmarksCountView;
    ImageView mLikesImage;
    int mCount;
    AisleBrowserHolder mAisleBrowserHolder;
    ImageLoader mImageLoader;
    boolean mLoadImage;
    
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
    
    public void setImageLoader(ImageLoader imageLoader) {
        this.mImageLoader = imageLoader;
    }
    
    public AisleBrowserHolder getAisleBrowserHolder() {
        return mAisleBrowserHolder;
    }
    
    public void setAisleBrowserHolder(AisleBrowserHolder aisleBrowserHolder) {
        this.mAisleBrowserHolder = aisleBrowserHolder;
    }
    
  
    
    public ImageView getmStarIcon() {
        return mStarIcon;
    }
    
    public void setmStarIcon(ImageView mStarIcon) {
        this.mStarIcon = mStarIcon;
    }
    
    public String getmSourceName() {
        return mSourceName;
    }
    
    public void setmSourceName(String mSourceName) {
        this.mSourceName = mSourceName;
    }
    
    private int mScrollIndex;
    private Context mContext;
    
    public boolean mAnimationInProgress;
    private int mDebugTapCount = 0;
    private long mDownPressStartTime = 0;
    private final int MAX_ELAPSED_DURATION_FOR_TAP = 200;
    public static final int SWIPE_MIN_DISTANCE = 30;
    private IAisleContentAdapter mSpecialNeedsAdapter;
    
    public int mFirstX;
    public int mLastX;
    public int mFirstY;
    public int mLastY;
    private boolean mTouchMoved;
    private int mTapTimeout;
    private String holderName;
    private String mBrowserArea;
    private boolean mSetPosition;
    public boolean isLeft;
    public boolean isRight;
    ArrayList<AisleImageDetails> mImageList;
    private GestureDetector mDetector;
    
    public AisleContentBrowser(Context context) {
        super(context);
        mContext = context;
        mAisleUniqueId = AisleWindowContent.EMPTY_AISLE_CONTENT_ID;
        mScrollIndex = 0;
    }
    
    public AisleContentBrowser(Context context, AttributeSet attribs) {
        super(context, attribs);
        mAisleUniqueId = AisleWindowContent.EMPTY_AISLE_CONTENT_ID;
        mScrollIndex = 0;
        mAnimationInProgress = false;
        mContext = context;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        mTapTimeout = ViewConfiguration.getTapTimeout();
        this.setBackgroundColor(Color.WHITE);
        mDetector = new GestureDetector(AisleContentBrowser.this.getContext(),
                new mListener());
    }
    
    public void setUniqueId(String id) {
        mAisleUniqueId = id;
    }
    
    public String getUniqueId() {
        return mAisleUniqueId;
    }
    
    public void setScrollIndex(int scrollIndex) {
        mScrollIndex = scrollIndex;
        mCurrentIndex = scrollIndex;
    }
    
    public int getScrollIndex() {
        return mScrollIndex;
    }
    
    public int getCurrentIndex() {
        return mCurrentIndex;
    }
    
    public void setImageList(ArrayList<AisleImageDetails> imageList) {
        this.mImageList = imageList;
    }
    
    public ArrayList<AisleImageDetails> getImageList() {
        return mImageList;
    }
    
    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final AisleContentBrowser aisleContentBrowser = (AisleContentBrowser) this;
        mSpecialNeedsAdapter = AisleContentAdapter.getInstance();
        boolean result = mDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mCurrentIndex = aisleContentBrowser
                    .indexOfChild(aisleContentBrowser.getCurrentView());
            mAnimationInProgress = false;
            mFirstX = (int) event.getX();
            mFirstY = (int) event.getY();
            mDownPressStartTime = System.currentTimeMillis();
            return super.onTouchEvent(event);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mTouchMoved) {
                mTouchMoved = false;
                return true;
            }
            mAnimationInProgress = false;
            
            mFirstX = 0;
            mLastX = 0;
            return super.onTouchEvent(event);
        }
        
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mLastX = (int) event.getX();
            mLastY = (int) event.getY();
            if (mFirstY - mLastY > SWIPE_MIN_DISTANCE
                    || mLastY - mFirstY > SWIPE_MIN_DISTANCE) {
                return super.onTouchEvent(event);
            }
            if (mFirstX - mLastX > SWIPE_MIN_DISTANCE) {
                
                // In this case, the user is moving the finger right to left
                // The current image needs to slide out left and the "next"
                // image
                // needs to fade in
                mTouchMoved = true;
                requestDisallowInterceptTouchEvent(true);
                if (false == mAnimationInProgress) {
                    View nextView = null;
                    final int currentIndex = aisleContentBrowser
                            .indexOfChild(aisleContentBrowser.getCurrentView());
                    nextView = (View) aisleContentBrowser
                            .getChildAt(currentIndex + 1);
                    
                    mLoadImage = false;
                    if (/* null != mSpecialNeedsAdapter && */null == nextView
                            || getImageListCount() == 1) {
                        mLoadImage = true;
                       
                        if (!mSpecialNeedsAdapter.setAisleContent(
                                AisleContentBrowser.this, currentIndex,
                                currentIndex + 1, true, getImageListCount())) {
                            mAnimationInProgress = true;
                            Animation cantWrapRight = AnimationUtils
                                    .loadAnimation(mContext,
                                            R.anim.cant_wrap_right);
                            cantWrapRight
                                    .setAnimationListener(new Animation.AnimationListener() {
                                        public void onAnimationEnd(
                                                Animation animation) {
                                            Animation cantWrapRightPart2 = AnimationUtils
                                                    .loadAnimation(
                                                            mContext,
                                                            R.anim.cant_wrap_right2);
                                            aisleContentBrowser
                                                    .getCurrentView()
                                                    .startAnimation(
                                                            cantWrapRightPart2);
                                            if (mSwipeListener != null) {
                                                
                                                mSwipeListener
                                                        .onAllowListResponse();
                                            }
                                            
                                        }
                                        
                                        public void onAnimationStart(
                                                Animation animation) {
                                            
                                        }
                                        
                                        public void onAnimationRepeat(
                                                Animation animation) {
                                            
                                        }
                                    });
                            aisleContentBrowser.getCurrentView()
                                    .startAnimation(cantWrapRight);
                            return super.onTouchEvent(event);
                        }
                    }
                    
                    Animation currentGoLeft = AnimationUtils.loadAnimation(
                            mContext, R.anim.right_out);
                    final Animation nextFadeIn = AnimationUtils.loadAnimation(
                            mContext, R.anim.fade_in);
                    mAnimationInProgress = true;
                    aisleContentBrowser.setInAnimation(nextFadeIn);
                    aisleContentBrowser.setOutAnimation(currentGoLeft);
                    currentGoLeft
                            .setAnimationListener(new Animation.AnimationListener() {
                                public void onAnimationEnd(Animation animation) {
                                    if (mLoadImage) {
                                        //load image and its data if it is the new view to the browser.
                                        AisleImageDetails imageDetails = getImageList()
                                                .get(currentIndex + 1);
                                        ImageView imageVIew = (ImageView) aisleContentBrowser
                                                .getCurrentView().findViewById(
                                                        R.id.pager_image_one);
                                        mImageLoader.DisplayImage(
                                                imageDetails.mImageUrl, 0,
                                                imageVIew, 0,
                                                AppConstants.SCREEN_WIDTH);
                                    }
                                    mCurrentIndex = currentIndex + 1;
                                }
                                
                                public void onAnimationStart(Animation animation) {
                                    
                                }
                                
                                public void onAnimationRepeat(
                                        Animation animation) {
                                    
                                }
                            });
                    
                    aisleContentBrowser.setDisplayedChild(currentIndex + 1);
                    // aisleContentBrowser.invalidate();
                    return super.onTouchEvent(event);
                }
            } else if (mLastX - mFirstX > SWIPE_MIN_DISTANCE) {
                
                requestDisallowInterceptTouchEvent(true);
                mTouchMoved = true;
                if (false == mAnimationInProgress) {
                    final int currentIndex = aisleContentBrowser
                            .indexOfChild(aisleContentBrowser.getCurrentView());
                    View nextView = null;
                    nextView = (View) aisleContentBrowser
                            .getChildAt(currentIndex - 1);
                    mLoadImage = false;
                    if (/* null != mSpecialNeedsAdapter && */null == nextView
                            || getImageListCount() == 1) {
                        mLoadImage = true;
                        if (!mSpecialNeedsAdapter.setAisleContent(
                                AisleContentBrowser.this, currentIndex,
                                currentIndex - 1, true, getImageListCount())) {
                            
                            Animation cantWrapLeft = AnimationUtils
                                    .loadAnimation(mContext,
                                            R.anim.cant_wrap_left);
                            
                            cantWrapLeft
                                    .setAnimationListener(new Animation.AnimationListener() {
                                        public void onAnimationEnd(
                                                Animation animation) {
                                            Animation cantWrapLeftPart2 = AnimationUtils
                                                    .loadAnimation(
                                                            mContext,
                                                            R.anim.cant_wrap_left2);
                                            aisleContentBrowser
                                                    .getCurrentView()
                                                    .startAnimation(
                                                            cantWrapLeftPart2);
                                            if (mSwipeListener != null) {
                                                
                                                mSwipeListener
                                                        .onAllowListResponse();
                                            }
                                        }
                                        
                                        public void onAnimationStart(
                                                Animation animation) {
                                            
                                        }
                                        
                                        public void onAnimationRepeat(
                                                Animation animation) {
                                            
                                        }
                                    });
                            aisleContentBrowser.getCurrentView()
                                    .startAnimation(cantWrapLeft);
                            return super.onTouchEvent(event);
                        }
                    }
                    
                    Animation currentGoRight = AnimationUtils.loadAnimation(
                            mContext, R.anim.left_in);
                    final Animation nextFadeIn = AnimationUtils.loadAnimation(
                            mContext, R.anim.fade_in);
                    mAnimationInProgress = true;
                    aisleContentBrowser.setInAnimation(nextFadeIn);
                    aisleContentBrowser.setOutAnimation(currentGoRight);
                    currentGoRight
                            .setAnimationListener(new Animation.AnimationListener() {
                                public void onAnimationEnd(Animation animation) {
                                    if (mLoadImage) {
                                        //load image and its data if it is the new view to the browser.
                                        AisleImageDetails imageDetails = getImageList()
                                                .get(currentIndex - 1);
                                        ImageView imageVIew = (ImageView) aisleContentBrowser
                                                .getCurrentView().findViewById(
                                                        R.id.pager_image_one);
                                        mImageLoader.DisplayImage(
                                                imageDetails.mImageUrl, 0,
                                                imageVIew, 0,
                                                AppConstants.SCREEN_WIDTH);
                                    }
                                    mCurrentIndex = currentIndex - 1;
                                    // aisleContentBrowser.setDisplayedChild(currentIndex-1);
                                }
                                
                                public void onAnimationStart(Animation animation) {
                                    
                                }
                                
                                public void onAnimationRepeat(
                                        Animation animation) {
                                    
                                }
                            });
                    
                    aisleContentBrowser.setDisplayedChild(currentIndex - 1);
                    return super.onTouchEvent(event);
                }
            }
        }
        return super.onTouchEvent(event);
    }
    
    public void setCustomAdapter(IAisleContentAdapter adapter) {
        mSpecialNeedsAdapter = adapter;
    }
    
    public IAisleContentAdapter getCustomAdapter() {
        return mSpecialNeedsAdapter;
    }
    
    class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (detailImgClickListenr != null && null != mSpecialNeedsAdapter) {
                detailImgClickListenr.onImageDoubleTap();
            }
            if (mClickListener != null && null != mSpecialNeedsAdapter) {
                mClickListener.onDoubleTap(mAisleUniqueId);
            }
            return super.onDoubleTap(e);
        }
        
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
           AisleLoader.getInstance(mContext).getmActivityComunicator().onBrowserClickEvent(null);
            
            return true;
        }
        
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }
    }
    
    public interface AisleContentClickListener {
        public void onAisleClicked(String id, int count, int currentPosition);
        
        public boolean isFlingCalled();
        
        public boolean isIdelState();
        
        public boolean onDoubleTap(String id);
        
        public void refreshList();
        
        public void hideProgressBar(int count);
        
        public void showProgressBar(int count);
    }
    
    public interface DetailClickListener {
        public void onImageClicked();
        
        public void onImageLongPress();
        
        public void onImageSwipe(int position);
        
        public void onImageDoubleTap();
        
        public void onSetBrowserArea(String area);
        
        public void onRefreshAdaptaers();
    }
    
    DetailClickListener detailImgClickListenr;
    
    public void setDetailImageClickListener(DetailClickListener detailLestener) {
        detailImgClickListenr = detailLestener;
    }
    
    public void setAisleContentClickListener(AisleContentClickListener listener) {
        mClickListener = listener;
    }
    
    public interface AisleDetailSwipeListener {
        public void onAisleSwipe(String id, int position,
                boolean editLayVisibility, boolean starLayVisibility,
                boolean isMostLikedImage);
        
        public void onReceiveImageCount(int count);
        
        public void onResetAdapter();
        
        public void onAddCommentClick(EditText editText, ImageView commentSend,
                FrameLayout editLay, int position, TextView textCount);
        
        public void onDissAllowListResponse();
        
        public void onAllowListResponse();
        
        public void setFindAtText(String findAt);
        
        public void setOccasion(String occasion);
        
        public void hasToShowEditIcon(boolean hasToShow);
        
        public void onEditAisle();
        
        public void onUpdateLikeStatus(boolean editLayVisibility,
                boolean starLayVisibility, boolean isMostLikedImage);
        
        public void onCloseKeyBoard();
        
        public void onImageAddEvent();
        
        public void finishScreen();
        
    }
    
    public void setAisleDetailSwipeListener(
            AisleDetailSwipeListener swipListener) {
        mSwipeListener = swipListener;
    }
 
    
    private AisleContentClickListener mClickListener;
    public AisleDetailSwipeListener mSwipeListener;
    
    public void setImageListCount(int count) {
        mCount = count;
    }
    
    public int getImageListCount() {
        return mCount;
    }
    
    public int getImageCount() {
        if (mSpecialNeedsAdapter != null
                && mSpecialNeedsAdapter.getWindowContent().getImageList() != null) {
            return mSpecialNeedsAdapter.getWindowContent().getImageList()
                    .size();
        }
        return 0;
    }
}
