package com.example.googlenowcard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fourmob.panningview.PanningView;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RatingFragment extends Fragment {
    LayoutInflater mInflater;
    Animation mScaleMoveAnimation, mScaleMoveAnimationTwo,
            mScaleMoveAnimationTwoReverse;
    private boolean mFirstSelection;
    private boolean mSecondSelection;
    private boolean mThirdSelection;
    private TextView mCancelOneId, mCancelTwoId, mCancelThreeId;
    private TextView mRankOneId, mRankTwoId, mRankThreeId;
    private TextView mRankSmallOneId, mRankSmallTwoId, mRankSmallThreeId;
    private TextView mDoneId;
    private ImageView mOneImageId, mTwoImageId, mThreeImageId;
    int mCancelPosition = -1;
    private ActivityFragmentCommunicator mActivityFragmentCommunicatorListner;
    
    private Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityFragmentCommunicatorListner = (ActivityFragmentCommunicator) activity;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpScaleAnims();
        mScaleMoveAnimation.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                checkSelectionStatus();
                
            }
        });
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.rating_grid_layout, null);
        mRankOneId = (TextView) view.findViewById(R.id.rank_one_id);
        mRankTwoId = (TextView) view.findViewById(R.id.rank_two_id);
        mRankThreeId = (TextView) view.findViewById(R.id.rank_three_id);
        
        mRankSmallOneId = (TextView) view.findViewById(R.id.rank_one_small_id);
        mRankSmallTwoId = (TextView) view.findViewById(R.id.rank_two_small_id);
        mRankSmallThreeId = (TextView) view
                .findViewById(R.id.rank_three_small_id);
        mRankSmallOneId.setVisibility(View.GONE);
        mRankSmallTwoId.setVisibility(View.GONE);
        mRankSmallThreeId.setVisibility(View.GONE);
        
        mCancelOneId = (TextView) view.findViewById(R.id.cancle_one_id);
        mCancelOneId.setVisibility(View.INVISIBLE);
        mCancelTwoId = (TextView) view.findViewById(R.id.cancle_two_id);
        mCancelTwoId.setVisibility(View.INVISIBLE);
        mCancelThreeId = (TextView) view.findViewById(R.id.cancle_three_id);
        mCancelThreeId.setVisibility(View.INVISIBLE);
        mDoneId = (TextView) view.findViewById(R.id.done_id);
        RelativeLayout cancelOneLayId = (RelativeLayout) view
                .findViewById(R.id.cancle_one_lay_id);
        RelativeLayout cancelTwoLayId = (RelativeLayout) view
                .findViewById(R.id.cancle_two_lay_id);
        RelativeLayout cancelThreeLayId = (RelativeLayout) view
                .findViewById(R.id.cancle_three_lay_id);
        mOneImageId = (ImageView) view.findViewById(R.id.image_one_id);
        mTwoImageId = (ImageView) view.findViewById(R.id.image_two_id);
        mThreeImageId = (ImageView) view.findViewById(R.id.image_three_id);
        final GridAdapter adapter = new GridAdapter();
        final PanningView panningView = (PanningView) view.findViewById(R.id.panningView);
        panningView.setImageResource(R.drawable.bg_default_artist_art2);
        panningView.startPanning();
        cancelOneLayId.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mCancelOneId.getVisibility() == View.VISIBLE) {
                    mCancelOneId.setVisibility(View.INVISIBLE);
                    mCancelPosition = 1;
                    mFirstSelection = false;
                    positionMap.put(1, -1);
                    viewAlphaAnim(mOneImageId, 1.0f, 0.0f);
                    imageLableAnim(mRankOneId, mRankSmallOneId,
                            mScaleMoveAnimationTwoReverse, adapter);
                }
                
            }
        });
        cancelTwoLayId.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mCancelTwoId.getVisibility() == View.VISIBLE) {
                    mCancelTwoId.setVisibility(View.INVISIBLE);
                    mCancelPosition = 2;
                    mSecondSelection = false;
                    positionMap.put(2, -1);
                    viewAlphaAnim(mTwoImageId, 1.0f, 0.0f);
                    imageLableAnim(mRankTwoId, mRankSmallTwoId,
                            mScaleMoveAnimationTwoReverse, adapter);
                }
                
            }
        });
        cancelThreeLayId.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mCancelThreeId.getVisibility() == View.VISIBLE) {
                    mCancelThreeId.setVisibility(View.INVISIBLE);
                    mCancelPosition = 3;
                    mThirdSelection = false;
                    positionMap.put(3, -1);
                    viewAlphaAnim(mThreeImageId, 1.0f, 0.0f);
                    imageLableAnim(mRankThreeId, mRankSmallThreeId,
                            mScaleMoveAnimationTwoReverse, adapter);
                }
            }
        });
        mDoneId.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                mActivityFragmentCommunicatorListner
                        .onDoneButtonClickFromRationScreen();
                
            }
        });
        mDoneId.setVisibility(View.GONE);
        GridView grid = (GridView) view.findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                
                final TextView view = (TextView) arg1
                        .findViewById(R.id.rating_view_id);
                boolean applyAnim = false;
                if (!mFirstSelection) {
                    if (!isItemSelected(arg2)) {
                        int lablePosition = 1;
                        mFirstSelection = true;
                        applyAnim = true;
                        makePrimeTextViewVisible(view, lablePosition);
                        onItemSelectionCancelAnim(mCancelOneId, lablePosition,
                                arg2);
                        onItemSelection(mRankSmallOneId, mRankOneId,
                                mScaleMoveAnimationTwo, mOneImageId);
                        
                    }
                } else if (!mSecondSelection) {
                    if (!isItemSelected(arg2)) {
                        int lablePosition = 2;
                        mSecondSelection = true;
                        applyAnim = true;
                        view.setVisibility(View.VISIBLE);
                        makePrimeTextViewVisible(view, lablePosition);
                        onItemSelectionCancelAnim(mCancelTwoId, lablePosition,
                                arg2);
                        onItemSelection(mRankSmallTwoId, mRankTwoId,
                                mScaleMoveAnimationTwo, mTwoImageId);
                        
                    }
                } else if (!mThirdSelection) {
                    if (!isItemSelected(arg2)) {
                        int lablePosition = 3;
                        mThirdSelection = true;
                        applyAnim = true;
                        makePrimeTextViewVisible(view, lablePosition);
                        onItemSelectionCancelAnim(mCancelThreeId,
                                lablePosition, arg2);
                        onItemSelection(mRankSmallThreeId, mRankThreeId,
                                mScaleMoveAnimationTwo, mThreeImageId);
                    }
                }
                if (!mFirstSelection) {
                    mOneImageId.setImageResource(0);
                }
                if (!mSecondSelection) {
                    mTwoImageId.setImageResource(0);
                }
                if (!mThirdSelection) {
                    mThreeImageId.setImageResource(0);
                }
                // scale animation
                if (applyAnim) {
                    view.startAnimation(mScaleMoveAnimation);
                }
                
            }
        });
        
        return view;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private class GridAdapter extends BaseAdapter {
        GridViewHolder gridViewHolder;
        
        @Override
        public int getCount() {
            return 20;
        }
        
        @Override
        public Object getItem(int position) {
            return position;
        }
        
        @Override
        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            
            if (convertView == null) {
                gridViewHolder = new GridViewHolder();
                convertView = mInflater.inflate(R.layout.grid_inflate_layout,
                        null);
                gridViewHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.grid_image_id);
                gridViewHolder.retingView = (TextView) convertView
                        .findViewById(R.id.rating_view_id);
                convertView.setTag(gridViewHolder);
            }
            gridViewHolder = (GridViewHolder) convertView.getTag();
            gridViewHolder.imageView.setImageResource(R.drawable.index);
            
            if (isItemSelected(position)) {
                gridViewHolder.retingView.setVisibility(View.VISIBLE);
            } else {
                gridViewHolder.retingView.setVisibility(View.GONE);
            }
            return convertView;
        }
        
    }
    
    private static class GridViewHolder {
        ImageView imageView;
        TextView retingView;
    }
    
    private boolean isItemSelected(int position) {
        Iterator entries = positionMap.entrySet().iterator();
        while (entries.hasNext()) {
            Entry posEntryEntry = (Entry) entries.next();
            int value = (Integer) posEntryEntry.getValue();
            if (position == value) {
                return true;
            }
        }
        return false;
    }
    
    private void checkSelectionStatus() {
        if (mFirstSelection && mSecondSelection && mThirdSelection) {
            animDoneView();
        }
    }
    
    private void goneDoneView() {
        if (mDoneId.getVisibility() == View.VISIBLE) {
            animGoneDoneView();
        }
    }
    
    private void setUpScaleAnims() {
        mScaleMoveAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.scale_anim);
        mScaleMoveAnimationTwo = AnimationUtils.loadAnimation(getActivity(),
                R.anim.scaleanimtwo);
        mScaleMoveAnimationTwoReverse = AnimationUtils.loadAnimation(
                getActivity(), R.anim.scaleanimtworeverse);
    }
    
    private void imageLableAnim(View visibleView, View goneView,
            Animation animation, BaseAdapter adapter) {
        goneView.setVisibility(View.GONE);
        visibleView.setVisibility(View.VISIBLE);
        visibleView.startAnimation(animation);
        visibleView.setBackgroundResource(R.drawable.round_corners);
        if (visibleView instanceof TextView) {
            TextView tv = (TextView) visibleView;
            tv.setTextColor(Color.parseColor("#000000"));
        }
        goneDoneView();
        adapter.notifyDataSetChanged();
        
    }
    
    private void onItemSelection(View visibleView, View goneView,
            Animation animation, ImageView imageView) {
        goneView.setVisibility(View.GONE);
        visibleView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.index);
        viewAlphaAnim(imageView, 0.0f, 1.0f);
        visibleView.startAnimation(animation);
    }
    
    private void onItemSelectionCancelAnim(TextView cancelText,
            int lablePosition, int value) {
        cancelText.setVisibility(View.VISIBLE);
        positionMap.put(lablePosition, value);
        viewAlphaAnim(cancelText, 0.0f, 1.0f);
    }
    
    private void viewAlphaAnim(View view, float start, float end) {
        int animDuration = 500;
        ObjectAnimator.ofFloat(view, "alpha", start, end).setDuration(animDuration)
                .start();
    }
    
    private void animGoneDoneView() {
        mDoneId.setVisibility(View.GONE);
        int animValue = 100;
        translateAnim(mDoneId, 0, animValue, 0, animValue);
    }
    
    private void animDoneView() {
        mDoneId.setVisibility(View.VISIBLE);
        int animValue = 100;
        translateAnim(mDoneId, animValue, 0, animValue, 0);
    }
    
    private void translateAnim(View view, int fromX, int toX, int fromY, int toY) {
        TranslateAnimation transAnimation = new TranslateAnimation(fromX, toX,
                fromY, toY);
        int animDuration = 300;
        transAnimation.setDuration(animDuration);
        view.startAnimation(transAnimation);
    }
    
    private void makePrimeTextViewVisible(TextView tView, int position) {
        tView.setText(String.valueOf(position));
        tView.setVisibility(View.VISIBLE);
    }
}
