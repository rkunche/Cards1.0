/**
 * @author Vinodh Sundararajan
 * 
 * An aisle is a combination of a given user submitting images while looking for
 * for something for an occasion. For example, Joe Dawes is looking for
 * tuxedos for a christmas party.
 * Although this combination makes an AisleWindowContent it is not neccesarily unique.
 * Every aisle is identified by a unique identifier which we will use to keep track
 * of the aisles.
 */
package com.aisles.datamodels;

import java.util.ArrayList;
import java.util.Random;

import com.example.googlenowcard.MainActivity;
import com.googlenowcard.utils.Utils;

public class AisleWindowContent {
    public static final String EMPTY_AISLE_CONTENT_ID = "EmptyAisleWindow";
    private static final String IMAGE_RES_SPEC_REGEX = ".jpg"; // this is the
                                                               // string
                                                               // pattern we
                                                               // look for
    private String mImageFormatSpecifier = "._SY%d.jpg";
    boolean mAisleBookmarkIndicator = false;
    public int mTrendingBestHeight = 0;
    public int mAisleCardHeight;
    private Random randomNumGenerator = new Random();
    private static final String AISLE_STAGE_FOUR = "completed";
    public int mTotalLikesCount;
    public String mAisleCureentStage = VueConstants.AISLE_STATGE_ONE;
    private boolean mShareIndicator = false;
    
    
    
    
    public int mTagId;
    
    // these two should be based on device with & height
    private String mAisleId;
    
    int mWindowSmallestHeight = 0;
    int mWindowSamllestWidth = 0;
    private int mWindowLargestHeight = 0;
    
    public AisleWindowContent(String aisleId) {
        mAisleId = aisleId;
    }
    
    public AisleWindowContent(String aisleId, boolean createPlaceHolders) {
        mAisleId = aisleId;
        if (createPlaceHolders) {
            mContext = new AisleContext();
            mAisleImagesList = new ArrayList<AisleImageDetails>();
        } 
    }
    
    public AisleWindowContent(AisleContext context,
            ArrayList<AisleImageDetails> items) {
    }
    
    public void setAisleId(String aisleId) {
        mAisleId = aisleId;
    } 
    
    @SuppressWarnings("unchecked")
    public void addAisleContent(AisleContext context,
            ArrayList<AisleImageDetails> items) {
        if (null != mAisleImagesList) {
            mAisleImagesList = null;
        }
        if (null != mContext) {
            mContext = null;
        }
        if (items != null) {
            mAisleImagesList = (ArrayList<AisleImageDetails>) items.clone();
        }
        mAisleId = context.mAisleId;
        mContext = context;
       
    
        if (mAisleImagesList != null) {
            udpateImageUrlsForDevice();
        }
    }
    
    public ArrayList<AisleImageDetails> getImageList() {
        return mAisleImagesList;
    }
    
    public int getSize() {
        return mAisleImagesList.size();
    }
    
    private boolean udpateImageUrlsForDevice() {
        AisleImageDetails imageDetails;
        mWindowSmallestHeight = 0;
        if (mAisleImagesList == null) {
            return true;
        }
        for (int i = 0; i < mAisleImagesList.size(); i++) {
            imageDetails = mAisleImagesList.get(i);
            if (imageDetails.mAvailableHeight < mWindowSmallestHeight
                    || mWindowSmallestHeight == 0) {
                mWindowSmallestHeight = imageDetails.mAvailableHeight;
                mWindowSamllestWidth = imageDetails.mAvailableWidth;
            }
        }
        for (int i = 0; i < mAisleImagesList.size(); i++) {
            prepareCustomUrl(mAisleImagesList.get(i));
        }
        findBestHeight();
        return true;
    }
    
    public void prepareCustomUrl(AisleImageDetails imageDetails) {
        StringBuilder sb = new StringBuilder();
        String urlReusablePart;
        String customFittedSizePart;
        String regularUrl = imageDetails.mImageUrl;
        int index = -1;
        index = regularUrl.indexOf(IMAGE_RES_SPEC_REGEX);
        if (-1 != index) {
            // we have a match
            urlReusablePart = regularUrl.split(IMAGE_RES_SPEC_REGEX)[0];
            sb.append(urlReusablePart);
            customFittedSizePart = String.format(mImageFormatSpecifier,
                    mWindowSmallestHeight);
            sb.append(customFittedSizePart);
            imageDetails.mCustomImageUrl = sb.toString();
        } else {
            imageDetails.mCustomImageUrl = regularUrl;
        }
 
    }
    
    public AisleContext getAisleContext() {
        return mContext;
    }
    
    public void setAisleContext(AisleContext context) {
        mContext = context;
    }
    
    public String getAisleId() {
        return mAisleId;
    }
    
    public int getBestHeightForWindow() {
        return mWindowSmallestHeight;
    }
    
    public void setBestHeightForWindow(int height) {
        mWindowSmallestHeight = height;
        
    }
    
    public int getBestLargetHeightForWindow() {
        return mWindowLargestHeight;
    }
    
    public void setBestLargestHeightForWindow(int largestHeight) {
        mWindowLargestHeight = largestHeight;
    }
    
 private void findBestHeight(){
     int bestHeight = 0;
     for(int i=0;i<mAisleImagesList.size();i++){
         if(bestHeight < mAisleImagesList.get(i).mAvailableHeight){
             bestHeight = mAisleImagesList.get(i).mAvailableHeight;
         }
     }
     mWindowLargestHeight =  Utils.getCurrentCardHeight(bestHeight,MainActivity.mCurrentInstance);
 }
    
 
 
    
 
    
    private int getRandomNumber() {
        
        int i1 = randomNumGenerator.nextInt(10 - 0) + 0;
        return i1;
    }
    
    private AisleContext mContext;
    private ArrayList<AisleImageDetails> mAisleImagesList;
}
