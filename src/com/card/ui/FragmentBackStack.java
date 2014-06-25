package com.card.ui;

import java.util.ArrayList;

public class FragmentBackStack implements FragementTransition {
    ArrayList<String> mFragmentStack = new ArrayList<String>();
    static FragmentBackStack sFragmentBackStack;
    
    public static FragmentBackStack getInstance() {
        if (sFragmentBackStack == null) {
            sFragmentBackStack = new FragmentBackStack();
        }
        return sFragmentBackStack;
    }
    
    @Override
    public void addFragment(String name) {
        mFragmentStack.add(name);
        
    }
    /*
     *  return top fragment  name.
     */
    @Override
    public String getTopFragment() {
        if (isFragmentStackEmpty()) {
            return null;
        }
        return mFragmentStack.remove(mFragmentStack.size() - 1);
        
    }
    
    @Override
    public boolean isFragmentStackEmpty() {
        if (mFragmentStack.size() == 0) {
            return true;
        }
        return false;
    }
    
}
