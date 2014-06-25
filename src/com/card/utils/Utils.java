package com.card.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.aisles.datamodels.ScreenDimensions;
import com.card.ui.MainActivity;
import com.card.ui.R;
 

public class Utils {
    static int maxCardHeight = 0;
    
    public static int randInt(int min, int max) {
        
        // Usually this should be a field rather than a method variable so
        // that it is not re-seeded every call.
        Random rand = new Random();
        
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
    public static int getPixel(Context cosntext, int dp) {
        Resources r = cosntext.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
        return px;
    }
    
    public static ScreenDimensions getScreenDimensions(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        ScreenDimensions dimensions = new ScreenDimensions();
        dimensions.mScreenWidth = metrics.widthPixels;
        dimensions.mScreenHeight = metrics.heightPixels;
        
        return dimensions;
        
    }
    
    public static int getMaxCardHeight(Context context) {
        // 24 notification bar
        // 48 action bar
        // 64 card heading info
        // 72 card bottom info
        // 48 card bottom text card_botoom_text_height
        // 8 shading space.
        if (maxCardHeight == 0) {
            int cardHeadingInfo = (int) context.getResources().getInteger(
                    R.integer.top_card_height);
            int cardBottomInfo = (int) context.getResources().getInteger(
                    R.integer.image_card_botoom_layout_height);
            int cardBottomText = (int) context.getResources().getInteger(
                    R.integer.card_botoom_text_height);
            ScreenDimensions dimensions = getScreenDimensions(context);
            
            int deductValue = Utils.getPixel(context, (24 + 48
                    + cardHeadingInfo + cardBottomInfo + cardBottomText + 8));
            maxCardHeight = dimensions.mScreenHeight - deductValue;
        }
        return maxCardHeight;
        
    }
    
    public static int getCurrentCardHeight(int currentCardHeight,
            Context context) {
        int currentCardFinalHeight;
        int maxCardHeight = getMaxCardHeight(context);
        if (currentCardHeight > maxCardHeight) {
            currentCardFinalHeight = maxCardHeight;
        } else {
            currentCardFinalHeight = currentCardHeight;
        }
        
        return currentCardFinalHeight;
        
    }
    
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,
            final float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        
        return output;
    }
    
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
    
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    
    private Utils() {
    };
    
    public static boolean isExternalStorageRemovable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }
    
    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }
        
        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName()
                + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath()
                + cacheDir);
    }
    
    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
    
    /** A method to download json data from url */
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            
            // Connecting to url
            urlConnection.connect();
            
            // Reading data from url
            iStream = urlConnection.getInputStream();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));
            
            StringBuffer sb = new StringBuffer();
            
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            data = sb.toString();
            
            br.close();
            
        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
