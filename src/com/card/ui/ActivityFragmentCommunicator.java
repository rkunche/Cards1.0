package com.card.ui;

import com.aisles.datamodels.AisleWindowContent;

public interface ActivityFragmentCommunicator {
    public void onBrowserClickEvent(AisleWindowContent aisle);
    
    public void onDoneButtonClickFromRatingScreen();
}
