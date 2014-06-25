package com.lateralthoughts.vue.notification;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.card.ui.R;
import com.lateralthoughts.vue.notification.SwipeDismissList.Undoable;

public class PopupFragment extends Fragment {
    Context mContext;
    LayoutInflater mInflater;
    private int mListWidthFactor = 20;
    BaseAdapter mNotificationAdapter;
    private ArrayList<NotificationAisle> mNotificationList;
    private SwipeDismissList mSwipeList;
    ListView mListView;
    EditText mFeedbackEditText;
    TextView mFeedbackHintTview;
    boolean animStarted = false;
    int mBeforeTextChangeCount = 0;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    
    public PopupFragment(ArrayList<NotificationAisle> notificationList) {
        mNotificationList = notificationList;
    }
    
    public PopupFragment() {
        
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popup_list_layout, container, false);
        RelativeLayout relBg = (RelativeLayout) v
                .findViewById(R.id.overflow_listlayout_layout);
        relBg.getBackground().setAlpha(85);
        
        View heder = inflater.inflate(R.layout.popup_header, null);
        
        mFeedbackEditText = (EditText) heder.findViewById(R.id.message_id);
        mFeedbackHintTview = (TextView) heder.findViewById(R.id.text_id);
        mFeedbackHintTview.setVisibility(View.GONE);
        final ColorStateList colors = mFeedbackEditText.getHintTextColors();
        mFeedbackHintTview.setTextColor(colors);
        ImageView sendButton = (ImageView) heder.findViewById(R.id.send_button);
        LinearLayout listLay = (LinearLayout) v.findViewById(R.id.list_lay_id);
        mListView = (ListView) v.findViewById(R.id.list_id);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getLayoutWidth(), LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        listLay.setLayoutParams(params);
        mListView.addHeaderView(heder);
        mNotificationAdapter = new NotificationListAdapter(mContext,
                mNotificationList);
        mListView.setAdapter(mNotificationAdapter);
        
        mFeedbackEditText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                mBeforeTextChangeCount = s.length();
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                if (mBeforeTextChangeCount < s.length() && s.length() == 1) {
                    moveUpAnim();
                } else if (s.length() == 0) {
                    
                    if (!animStarted) {
                        animStarted = true;
                        mFeedbackEditText.setText(" ");
                        moveDownAnim();
                    }
                    
                }
            }
        });
        sendButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final String feedBackText = mFeedbackEditText.getText()
                        .toString();
                if (feedBackText.length() > 0) {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            mFeedbackEditText.getWindowToken(), 0);
                    mFeedbackEditText.setText("");
                    // TODO: when clicks on send button do some action here.
                } else {
                    Toast.makeText(getActivity(), "Type your feedback",
                            Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        mListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mListView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFeedbackEditText.getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        mFeedbackEditText.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // to provide touch scroll for edit text
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        SwipeDismissList.UndoMode mode = SwipeDismissList.UndoMode.values()[0];
        // Create a new SwipeDismissList from the activities listview.
        mSwipeList = new SwipeDismissList(
        // 1st parameter is the ListView you want to use
                mListView,
                // 2nd parameter is an OnDismissCallback, that handles the
                // deletion
                // and undo of list items. It only needs to implement onDismiss.
                // This method can return an Undoable (then this deletion can be
                // undone)
                // or null (if the user shouldn't get the possibility to undo
                // the
                // deletion).
                new SwipeDismissList.OnDismissCallback() {
                    /**
                     * Will be called, whenever the user swiped out an list
                     * item.
                     * 
                     * @param listView
                     *            The {@link ListView} that the item was deleted
                     *            from.
                     * @param position
                     *            The position of the item, that was deleted.
                     * @return An {@link Undoable} or {@code null} if this
                     *         deletion shouldn't be undoable.
                     */
                    public SwipeDismissList.Undoable onDismiss(
                            AbsListView listView, final int position) {
                        // Delete that item from the adapter.
                        NotificationAisle notificationAisle = ((NotificationListAdapter) mNotificationAdapter)
                                .removeItem(position - 1);
                        int slNo = 0;
                        if (notificationAisle != null) {
                            slNo = notificationAisle.getId();
                        }
                        
                        if (slNo != 0) {/*
                                         * if
                                         * (notificationAisle.getAggregatedAisles
                                         * () != null &&
                                         * notificationAisle.getAggregatedAisles
                                         * () .size() > 0) { for
                                         * (NotificationAisle notificationAisle1
                                         * : notificationAisle
                                         * .getAggregatedAisles()) {
                                         * DataBaseManager.getInstance(mContext)
                                         * .deleteNotificationAisle(
                                         * notificationAisle1.getId()); } } else
                                         * {
                                         * DataBaseManager.getInstance(mContext)
                                         * .deleteNotificationAisle(slNo); }
                                         */
                        }
                        
                        // Return an Undoable, for that deletion. If you write
                        // return null
                        // instead, this deletion won't be undoable.
                        return new SwipeDismissList.Undoable() {
                            /**
                             * Optional method. If you implement this method,
                             * the returned String will be presented in the undo
                             * view to the user.
                             */
                            @Override
                            public String getTitle() {
                                return /* item + */" deleted";
                            }
                            
                            /**
                             * Will be called when the user hits undo. You want
                             * to reinsert the item to the adapter again. The
                             * library will always call undo in the reverse
                             * order the item has been deleted. So you can
                             * insert the item at the position it was deleted
                             * from, unless you have modified the list (added or
                             * removed items) somewhere else in your activity.
                             * If you do so, you might want to call
                             * {@link SwipeDismissList#discardUndo()}, so the
                             * user cannot undo the action anymore. If you still
                             * want the user to be able to undo the deletion
                             * (after you modified the list somewhere else) you
                             * will need to calculate the new position of this
                             * item yourself.
                             */
                            @Override
                            public void undo() {
                                // Reinsert the item at its previous position.
                                // mNotificationAdapter.insert(item, position);
                            }
                            
                            /**
                             * Will be called, when the user doesn't have the
                             * possibility to undo the action anymore. This can
                             * either happen, because the undo timed out or
                             * {@link SwipeDismissList#discardUndo()} was
                             * called. If you have stored your objects somewhere
                             * persistent (e.g. a database) you might want to
                             * use this method to delete the object from this
                             * persistent storage.
                             */
                            @Override
                            public void discard() {
                                // Just write a log message (use logcat to see
                                // the effect)
                                /*
                                 * Log.w("DISCARD", "item " + item "" +
                                 * " now finally discarded");
                                 */
                            }
                        };
                        
                    }
                },
                // 3rd parameter needs to be the mode the list is generated.
                mode);
        
        mListView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                ((NotificationListAdapter) mNotificationAdapter)
                        .loadScreenForNotificationAisle(arg2 - 1);
            }
        });
        return v;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
    }
    
    /**
     * 
     * returns % of the screen width. This is the width of the Notification
     * list.
     */
    private int getLayoutWidth() {
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int widthOfList = dm.widthPixels - (dm.widthPixels * mListWidthFactor)
                / 100;
        return widthOfList;
        
    }
    
    @Override
    public void onPause() {
        super.onPause();
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
    }
    
    public void moveDownAnim() {
        
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.translate);
        mFeedbackHintTview.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            
            @Override
            public void onAnimationStart(Animation animation) {
                
            }
            
            @Override
            public void onAnimationRepeat(Animation animation) {
                
            }
            
            @Override
            public void onAnimationEnd(Animation animation) {
                mFeedbackHintTview.setVisibility(View.GONE);
                mFeedbackEditText.setText("");
                animStarted = false;
                
            }
        });
    }
    
    public void moveUpAnim() {
        
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.translateback);
        mFeedbackHintTview.startAnimation(animation);
        mFeedbackHintTview.setVisibility(View.VISIBLE);
    }
}
