package com.example.googlenowcard;

import java.util.ArrayList;

import com.aisles.datamodels.AisleWindowContent;
/**
 * 
 *  
 *
 */
public interface DataContainer {
   public void notifyAdapters();
   public void addMoreData(ArrayList<AisleWindowContent> aisleList);
   public void clearAllData();
}
