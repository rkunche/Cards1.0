package com.aisles.datamodels;

import java.util.ArrayList;

public class Aisle {
  public  String suggesterName;
  public String findAtPrice;
  public String aisleDescription;
  public String aisleTitle;
  public String aisleOwnerFirstName;
  public String aisleOwnerLastName;
  public  String aisleOwnerProfilePicUrl;
  public ArrayList<ImageDetails> aisleImageList = new ArrayList<ImageDetails>();
  public int aisleId;
  public int cardHeight;
  public int cardFinalHeight;
}
