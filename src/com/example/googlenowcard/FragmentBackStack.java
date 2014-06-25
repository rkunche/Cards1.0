package com.example.googlenowcard;

import java.util.ArrayList;

public class FragmentBackStack implements FragementTransition{
    ArrayList<String> fragmentStack = new ArrayList<String>();
    static FragmentBackStack sFragmentBackStack;
    public static FragmentBackStack getInstance(){
         if(sFragmentBackStack == null){
             sFragmentBackStack = new FragmentBackStack();
         }
         return sFragmentBackStack;
     }
    @Override
    public void addFragment(String name) {
        fragmentStack.add(name);
        
    }

    @Override
    public String getTopFragment() {
        if (isFragmentStackEmpty()) {
            return null;
        }
        return fragmentStack.remove(fragmentStack.size() - 1);
        
    }

    @Override
    public boolean isFragmentStackEmpty() {
         if(fragmentStack.size() == 0){
             return true;
         }
        return false;
    }
    
}
