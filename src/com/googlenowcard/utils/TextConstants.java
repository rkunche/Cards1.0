package com.googlenowcard.utils;

public enum TextConstants {
    SMALL(18), MEDIUM(20), LARGE(22);
    int mSize;
    
    private TextConstants(int size) {
        mSize = size;
    }
    
    public int getSize() {
        return mSize;
    }
}
