package com.example.googlenowcard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class DetailsCardViewFragment extends Fragment {
    LayoutInflater mInflater;
    ListView mCardList;
    DetailsViewCard mDetailCard;
    SwingBottomInAnimationAdapter mSwingBottomInAnimationAdapter;
    int mPosition = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        mCardList = (ListView)view.findViewById(R.id.card_list);
        mDetailCard = new DetailsViewCard(getActivity());
       /* mSwingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                mDetailCard);
        mSwingBottomInAnimationAdapter.setListView(mCardList);*/
         mCardList.setAdapter(mDetailCard);
        return  view;
}
 
}
