package com.example.googlenowcard;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.aisles.datamodels.AisleWindowContent;
import com.googlenowcard.utils.AppConstants;
import com.googlenowcard.utils.Utils;
import com.imagedownload.ImageLoader;

public class AisleLoader {
    private Context mContext;
    private static AisleLoader sAisleLoaderInstance = null;
    public static int sTrendingSwipeCount = 0;
    AisleBrowserHolder mAisleBrowserHolder;
    ImageLoader imageLoader;
    private ActivityFragmentCommunicator mActivityComunicator;
    
    // the information flow by requesting top aisles in batches. As the aisle
    // details start coming through the adapter notifies the view of changes in
    // data set which in turn triggers the getView() callback.
    // The complexity starts at this point: we are dealing with
    // an incredibly large amount of data. Each aisle window makes up
    // one item in the SGV. Each of this window consists of an image, below
    // which we can description of the image, the profile of the owner, the
    // context,
    // the occasion etc. On top of this, the image itself can be flicked to
    // reveal
    // a carousel of images that a user can swipe through. We can't possibly
    // download all of these and more importantly, we want to have top
    // performance for a couple of very important scenarios:
    // 1. When the user flings the SGV up & down the scrolling needs to be
    // smooth
    // 2. User should be able to swipe across as AisleWindow and browse the
    // content.
    // Here is what we will do:
    // 1. When an AisleWindowContent needs to be loaded up, we also get
    // the view into which it goes. Inside this viewFlipper we will store the
    // id of the AisleWindowContent.
    // 2. When the viewFlipper is being recycled the if of the
    // AisleWindowContent
    // and the id that ViewFlipper points to will be different. At this point
    // we will cancel all image download requests started for this ViewFlipper
    // 3. If the ids match then we don't need to do anything.
    
    public ActivityFragmentCommunicator getmActivityComunicator() {
        return mActivityComunicator;
    }

    public void setmActivityComunicator(
            ActivityFragmentCommunicator mActivityComunicator) {
        this.mActivityComunicator = mActivityComunicator;
    }

    // we handle getView() and thats definitely hurting us!
    public static AisleLoader getInstance(Context context) {
        if (null == sAisleLoaderInstance) {
            sAisleLoaderInstance = new AisleLoader(context);
        }
        return sAisleLoaderInstance;
        
    }
    
    private AisleLoader(Context context) {
        // we don't want everyone creating an instance of this. We will
        // instead use a factory pattern and return an instance using a
        // static method
        mContext = context;
        imageLoader = ImageLoader.getInstance(context);
        mAisleBrowserHolder = AisleBrowserHolder.getInstance(mContext);
        
    }
    
    public void returnUsedView(View view) {
        mAisleBrowserHolder.returnUsedView(view);
    }
    
    public void loadImage(ImageView imageView, String imgUrl) {
        if (imgUrl.startsWith("http")) {
            imageLoader.DisplayImage(imgUrl, 0, imageView, 0,
                    AppConstants.SCREEN_WIDTH);
        }
    }
    
    /**
     * To improve the performance of the getview() method,browser(flipper) will
     * have one static image card. if the user starts swiping in the
     * browser(Horizontal swiping)at that time add more image card views to the
     * browser with some animation, these views are added dynamically from the
     * pool of views. If the user swipes vertically then if the browser have any
     * views that are added dynamically remove all those and returned to the
     * pool.
     * 
     */
    public void loadAisleContentIntoView(ViewHolder viewHolder, int position) {
        viewHolder.aisleOwnerProfilePic.setImageBitmap(null);
        for (int i = 0; i < viewHolder.aisleContentBrowser.getChildCount(); i++) {
            View view = viewHolder.aisleContentBrowser.getChildAt(i);
            if (i == 0) {
                // static view
                ImageView imageView = (ImageView) view
                        .findViewById(R.id.pager_image_one);
                if (imageView != null) {
                    imageView.setImageBitmap(null);
                }
                continue;
            }
            if (i >= 1) {
                // After first view remaining views are dynamically added so
                // return these views to pool.
                ImageView imageView = (ImageView) view
                        .findViewById(R.id.pager_image_one);
                imageView.setImageBitmap(null);
                returnUsedView(view);
                viewHolder.aisleContentBrowser.removeView(view);
            }
        }
        loadImage(viewHolder.ImgCardPhotoPicOne, viewHolder.aisleWindowContent
                .getImageList().get(0).mImageUrl);
        setCustomAdapter(viewHolder.aisleContentBrowser,
                viewHolder.aisleWindowContent);
        viewHolder.aisleContentBrowser
                .setImageListCount(viewHolder.aisleWindowContent.getImageList()
                        .size());
        viewHolder.aisleContentBrowser
                .setImageList(viewHolder.aisleWindowContent.getImageList());
        viewHolder.aisleContentBrowser
                .setUniqueId(viewHolder.aisleWindowContent.getAisleId());
        if (!TextUtils
                .isEmpty(viewHolder.aisleWindowContent.getAisleContext().mAisleOwnerImageURL)) {
            imageLoader
                    .loadProfileImage(
                            viewHolder.aisleWindowContent.getAisleContext().mAisleOwnerImageURL,
                            (Activity) mContext,
                            viewHolder.aisleOwnerProfilePic,
                            Utils.getPixel(mContext, 8));
        }
    }
    
    public void setCustomAdapter(AisleContentBrowser browser,
            AisleWindowContent aisleWindowContent) {
        browser.setAisleBrowserHolder(mAisleBrowserHolder);
        browser.setImageLoader(imageLoader);
        browser.setCustomAdapter(AisleContentAdapter.getInstance());
    }
}
