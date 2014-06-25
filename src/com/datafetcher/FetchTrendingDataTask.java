package com.datafetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.aisles.datamodels.AisleWindowContent;
import com.aisles.datamodels.UrlConstants;
import com.lateralthoughts.vue.parser.Parser;

public class FetchTrendingDataTask extends AsyncTask<Void, Void, Void> {
    private Handler mHandler;
    final String requestUrl;
    
    public FetchTrendingDataTask(Handler handler, int limit, int offset) {
        mHandler = handler;
        requestUrl = UrlConstants.GET_TRENDINGAISLES_RESTURL + "/" + limit
                + "/" + offset;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    
    @Override
    protected Void doInBackground(Void... params) {
        String josonArray = getInputStream(requestUrl);
        ArrayList<AisleWindowContent> aisleWindowList = new Parser()
                .parseTrendingAislesResultData(josonArray, false);
        Message msg = new Message();
        msg.obj = aisleWindowList;
        mHandler.sendMessage(msg);
        
        return null;
    }
    
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
    
    private String getInputStream(String url) {
        HttpURLConnection conn = null;
        try {
            URL imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            
            InputStream is = conn.getInputStream();
            return convertStreamToString(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
