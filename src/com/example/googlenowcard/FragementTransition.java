package com.example.googlenowcard;

public interface FragementTransition {
  public void  addFragment(String name);
  public String getTopFragment();
  public boolean isFragmentStackEmpty();
}
