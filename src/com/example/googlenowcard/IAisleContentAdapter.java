package com.example.googlenowcard;

//internal imports
import com.aisles.datamodels.AisleWindowContent;

public interface IAisleContentAdapter {
    public void setContentSource(String uniqueAisleId,
            AisleWindowContent windowContent);
    
    public void releaseContentSource();
    
    public void setPivot(int index);
    
    public int getAisleItemsCount();
    
    public boolean setAisleContent(AisleContentBrowser contentBrowser,
            int currentIndex, int wantedIndex, boolean shiftPivot,int imagesCount);
    
    public String getAisleId();
    
    public String getImageId(int mCurrentIndex);
    
    public AisleWindowContent getWindowContent();
    
}
