package com.example.googlenowcard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aisles.datamodels.AisleWindowContent;
import com.fourmob.panningview.PanningView;
import com.googlenowcard.utils.Utils;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.lateralthoughts.vue.parser.PlaceJSONParser;

public class CardFragment extends Fragment {
    
    private ListView mCardList;
    private CardWithFlipper mCard;
    public static boolean sIsListScrolling;
    public static boolean sIsTouchScrolingCall;
    public static boolean sIsFlingCall;
    private SwingBottomInAnimationAdapter mSwingBottomInAnimationAdapter;    
    private PlacesTask mPlacesTask;
    private ParserTask mParserTask;
    private AutoCompleteTextView mNameSuggestion;
    private AutoCompleteTextView mCitySuggestion;
    private LayoutInflater mInflater;
    private EditText mHeaderUserInfoId;
    private PopupWindow popupWindow;
    private ListView mListViewCityNames;
    private CityNameAdapter mCityNamesAdapter;
    private ActivityFragmentCommunicator mActivityFragmentCommunicatorListner;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivityFragmentCommunicatorListner = (ActivityFragmentCommunicator) activity;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.activity_main, null);
        mCardList = (ListView) view.findViewById(R.id.card_list);
        mCard = new CardWithFlipper(getActivity(),mActivityFragmentCommunicatorListner);
        mSwingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                mCard);
        View cardHeaderView = inflater.inflate(
                R.layout.main_fragment_listview_header, null);
    /*    final PanningView panningView = (PanningView)cardHeaderView.findViewById(R.id.panningView);
        panningView.setImageResource(R.drawable.bg_default_artist_art2);
        panningView.startPanning();*/
        mCardList.addHeaderView(cardHeaderView);
        mSwingBottomInAnimationAdapter.setListView(mCardList);
        mCardList.setAdapter(mSwingBottomInAnimationAdapter);
        mNameSuggestion = (AutoCompleteTextView) cardHeaderView
                .findViewById(R.id.user_name_id);
        
        mNameSuggestion.setThreshold(1);// will start working from first
                                        // character
        mCitySuggestion = (AutoCompleteTextView) cardHeaderView
                .findViewById(R.id.user_city_id);
        mCitySuggestion.setThreshold(1);// will start working from first
                                        // character
        mCitySuggestion.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                mPlacesTask = new PlacesTask();
                mPlacesTask.execute(s.toString());
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        
        mCardList.setOnScrollListener(new OnScrollListener() {
            
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                case SCROLL_STATE_FLING:
                    sIsListScrolling = true;
                    break;
                case SCROLL_STATE_TOUCH_SCROLL:
                    sIsTouchScrolingCall = true;
                    break;
                case SCROLL_STATE_IDLE:
                    sIsListScrolling = false;
                    sIsTouchScrolingCall = false;
                    if (!sIsListScrolling && !sIsTouchScrolingCall) {
                        mSwingBottomInAnimationAdapter.notifyDataSetChanged();
                    }
                    break;
                
                }
                
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
                
            }
        });
        return view;
    }
    
    public void setWindowList(ArrayList<AisleWindowContent> list) {
        mCard.setWindowList(list);
    }
    
    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {
        
        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";
            
            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyAk7gb7R6rUc82DdF1biyGZjBN8JK6AcOM";
            
            String input = "";
            
            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            
            // place type to be searched
            String types = "types=geocode";
            
            // Sensor enabled
            String sensor = "sensor=false";
            
            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;
            
            // Output format
            String output = "json";
            
            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                    + output + "?" + parameters;
            
            try {
                // Fetching the data from web service in background
                data = Utils.downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            
            // Creating ParserTask
            mParserTask = new ParserTask();
            
            // Starting Parsing the JSON string returned by Web Service
            mParserTask.execute(result);
        }
    }
    

    
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends
            AsyncTask<String, Integer, List<HashMap<String, String>>> {
        
        JSONObject jObject;
        
        @Override
        protected List<HashMap<String, String>> doInBackground(
                String... jsonData) {
            
            List<HashMap<String, String>> places = null;
            
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
            
            try {
                jObject = new JSONObject(jsonData[0]);
                
                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);
                
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }
        
        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            
            String[] from = new String[] { "description" };
            int[] to = new int[] { android.R.id.text1 };
            ArrayList<String> cityNames = new ArrayList<String>();
            for (int i = 0; i < result.size(); i++) {
                cityNames.add(result.get(i).get("description"));
            }
            
            // Creating a SimpleAdapter for the AutoCompleteTextView
            /*
             * final SimpleAdapter adapter = new SimpleAdapter(getActivity(),
             * result, android.R.layout.simple_list_item_1, from, to);
             * 
             * citySuggestion.setAdapter(adapter);
             */
            
            PlacesAutoCompleteAdapter adapter2 = new PlacesAutoCompleteAdapter(
                    getActivity(), R.layout.adapter_city_names, cityNames);
            mCitySuggestion.setAdapter(adapter2);
        }
    }
    
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
            implements Filterable {
        private ArrayList<String> resultList;
        
        public PlacesAutoCompleteAdapter(Context context,
                int textViewResourceId, ArrayList<String> resultList) {
            super(context, textViewResourceId);
            this.resultList = resultList;
        }
        
        @Override
        public int getCount() {
            return resultList.size();
        }
        
        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                mInflater = (LayoutInflater) getActivity().getLayoutInflater();
                convertView = mInflater.inflate(R.layout.adapter_city_names,
                        null);
                holder.cityName = (TextView) convertView
                        .findViewById(R.id.cit_name_id);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.cityName.setText(resultList.get(position));
            return convertView;
        }
        
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(
                        final CharSequence constraint) {
                    final FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        ArrayList<String> matchList = new ArrayList<String>();
                        for (int i = 0; i < resultList.size(); i++) {
                            if (resultList.get(i).toLowerCase()
                                    .contains(constraint)) {
                                matchList.add(resultList.get(i));
                            }
                        }
                        resultList = matchList;
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                    
                }
                
                @Override
                protected void publishResults(CharSequence constraint,
                        FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
    
   private class Holder {
        TextView cityName;
    }
    
    class CityNameAdapter extends BaseAdapter {
        ArrayList<String> sList;
        
        public CityNameAdapter(ArrayList<String> sList) {
            this.sList = sList;
        }
        
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return sList.size();
        }
        
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                mInflater = (LayoutInflater) getActivity().getLayoutInflater();
                convertView = mInflater.inflate(R.layout.adapter_city_names,
                        null);
                holder.cityName = (TextView) convertView
                        .findViewById(R.id.cit_name_id);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.cityName.setText(sList.get(position));
            return convertView;
            
        }
        
    }
    
    public PopupWindow popupWindowDogs() {
        
        // initialize a pop up window type
        popupWindow = new PopupWindow(getActivity());
        
        // the drop down list is a list view
        mListViewCityNames = new ListView(getActivity());
        mCityNamesAdapter = new CityNameAdapter(new ArrayList<String>());
        // set our adapter and pass our pop up window contents
        mListViewCityNames.setAdapter(mCityNamesAdapter);
        
        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        
        // set the list view as pop up window content
        popupWindow.setContentView(mListViewCityNames);
        
        return popupWindow;
    }
    
    private void createNamesList(ArrayList<String> sList) {
        if (sList != null && sList.size() >= 0) {
            mCityNamesAdapter = new CityNameAdapter(sList);
            mListViewCityNames.setAdapter(mCityNamesAdapter);
            popupWindow.showAsDropDown(mHeaderUserInfoId);
        } else {
            popupWindow.dismiss();
        }
    }
}
