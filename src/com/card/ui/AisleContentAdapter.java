package com.card.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aisles.datamodels.AisleImageDetails;
import com.aisles.datamodels.AisleWindowContent;
import com.card.utils.AppConstants;
import com.card.ui.R;

public class AisleContentAdapter implements IAisleContentAdapter {
    static AisleContentAdapter sAisleContentAdapter;
    
    public AisleContentAdapter() {
        
    }
    
    public static AisleContentAdapter getInstance() {
        if (sAisleContentAdapter == null) {
            sAisleContentAdapter = new AisleContentAdapter();
        }
        return sAisleContentAdapter;
    }
    
    @Override
    public void setContentSource(String uniqueAisleId,
            AisleWindowContent windowContent) {
        
    }
    
    @Override
    public void releaseContentSource() {
        
    }
    
    @Override
    public void setPivot(int index) {
        
    }
    
    @Override
    public int getAisleItemsCount() {
        
        return 0;
    }
    
    @Override
    public boolean setAisleContent(AisleContentBrowser contentBrowser,
            int currentIndex, int wantedIndex, boolean shiftPivot,
            int imagesCount) {
        
        View view = null;
        AisleImageDetails itemDetails = null;
        if (wantedIndex >= imagesCount)
            return false;
        if (0 >= currentIndex && wantedIndex < currentIndex)
            return false;
        if (null != contentBrowser.mImageList && imagesCount != 0) {
            itemDetails = contentBrowser.mImageList.get(wantedIndex);
            view = contentBrowser.getAisleBrowserHolder().getEmptyBrowserView();
            RelativeLayout shopCardLay = (RelativeLayout) view
                    .findViewById(R.id.card_shop_cart_id);
            RelativeLayout createAisleId = (RelativeLayout) view
                    .findViewById(R.id.card_create_aisle_id);
            shopCardLay.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    // Toast.makeText(mContext, "click received",
                    // Toast.LENGTH_LONG).show();
                    
                }
            });
            createAisleId.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    // Toast.makeText(mContext, "click received",
                    // Toast.LENGTH_LONG).show();
                    
                }
            });
            contentBrowser.addView(view);
        }
        return true;
    }
    
    public void loadImage(ImageView imageView, String imgUrl,
            AisleContentBrowser contentBrowser) {
        contentBrowser.mImageLoader.DisplayImage(imgUrl, 0, imageView, 0,
                AppConstants.SCREEN_WIDTH);
    }
    
    @Override
    public String getAisleId() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String getImageId(int mCurrentIndex) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public AisleWindowContent getWindowContent() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
