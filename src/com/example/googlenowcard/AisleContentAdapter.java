package com.example.googlenowcard;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aisles.datamodels.AisleImageDetails;
import com.aisles.datamodels.AisleWindowContent;
import com.googlenowcard.utils.AppConstants;

public class AisleContentAdapter implements IAisleContentAdapter{
    static AisleContentAdapter aisleContentAdapter;
     public AisleContentAdapter() {
        
    }
     public static AisleContentAdapter getInstance(){
         if(aisleContentAdapter == null){
             aisleContentAdapter = new AisleContentAdapter();
         }
         return aisleContentAdapter;
     }
    @Override
    public void setContentSource(String uniqueAisleId,
            AisleWindowContent windowContent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void releaseContentSource() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPivot(int index) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getAisleItemsCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean setAisleContent(AisleContentBrowser contentBrowser,
            int currentIndex, int wantedIndex, boolean shiftPivot,int imagesCount) {
        
        View view = null;
        AisleImageDetails itemDetails = null;
        if (wantedIndex >= imagesCount)
            return false;
        if (0 >= currentIndex && wantedIndex < currentIndex)
            return false;
        if (null != contentBrowser.imageList && imagesCount != 0) {
            itemDetails = contentBrowser.imageList.get(wantedIndex);
            view = contentBrowser.getAisleBrowserHolder().getEmptyBrowserView();
             //ImageView imageView = (ImageView) view.findViewById(R.id.pager_image);
            // loadImage(imageView,itemDetails.mImageUrl,contentBrowser);
           RelativeLayout shopCardLay = (RelativeLayout) view.findViewById(R.id.card_shop_cart_id);
           RelativeLayout createAisleId = (RelativeLayout) view.findViewById(R.id.card_create_aisle_id);
            shopCardLay.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                  //  Toast.makeText(mContext, "click received", Toast.LENGTH_LONG).show();
                    
                }
            });
            createAisleId.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                   // Toast.makeText(mContext, "click received", Toast.LENGTH_LONG).show();
                    
                }
            });
                contentBrowser.addView(view);
        }
        return true;
    }
    public void loadImage(ImageView  imageView,String imgUrl,AisleContentBrowser contentBrowser){
        contentBrowser.imageLoader.DisplayImage(imgUrl, 0, imageView, 0, AppConstants.SCREEN_WIDTH);
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
