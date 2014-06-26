package com.aisles.datamodels;

import java.util.ArrayList;

public class Aisle {
    
    public String mSuggesterName;
    public String mFindAtPrice;
    public String mAisleDescription;
    public String mAisleTitle;
    public String mAisleOwnerFirstName;
    public String mAisleOwnerLastName;
    public String mAisleOwnerProfilePicUrl;
    public ArrayList<ImageDetails> mAisleImageList = new ArrayList<ImageDetails>();
    public int mAisleId;
    public int mCardHeight;
    public int mCardFinalHeight;
}
