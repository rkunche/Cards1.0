package com.imagedownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.card.utils.Utils;
import com.card.ui.R;

public class ImageLoader {
    static ImageLoader imageLoader;
    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    
    int width;
    int height;
    Context context;
    
    public static ImageLoader getInstance(Context context) {
        if (imageLoader == null) {
            imageLoader = new ImageLoader(context);
        }
        return imageLoader;
        
    }
    
    public ImageLoader(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
        Log.i("ImageLoader", "dimensions are: " + width + " h:" + height);
        this.context = context;
    }
    
    int stub_id = R.drawable.ic_launcher;
    /**
     * use this imageDownloader temperarily later which we can replace with Volley.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void DisplayImage(String url, int loader, ImageView imageView,
            int height, int width) {
        this.width = width;
        this.height = height;
        stub_id = loader;
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.getBitmapFromMemCache(url);
        Log.d("ImageLoader", "cache test11: " + bitmap + " " + url);
        if (bitmap != null) {
            /*
             * RelativeLayout.LayoutParams layoutParams = new
             * RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, height);
             * layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
             * imageView.setLayoutParams(layoutParams);
             */
            imageView.setImageBitmap(bitmap);
            // ObjectAnimator.ofFloat(imageView, "alpha", 0.0f,
            // 1.0f).setDuration(300).start();
        } else {
            queuePhoto(url, imageView);
            imageView.setImageResource(loader);
        }
    }
    
    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);
        
        // from SD cache
        Bitmap b = decodeFile(f);
        // Log.v("ImageLoader",
        // "cache from already exists : "+f.getPath()+"  img: "+b);
        if (b != null) {
            // Log.i("ImageLoader", "cache from already exists : " + url+ " W: "
            // + b.getWidth()+","+b.getHeight());
            return b;
        }
        
        // from web
        HttpURLConnection conn = null;
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            Log.d("Test", " code: " + conn.getResponseCode());
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            is.close();
            bitmap = decodeFile(f);
            // Log.v("ImageLoader", "cache from first time: "+ url+ " W: " +
            // b.getWidth()+","+b.getHeight());
            return bitmap;
        } catch (Exception ex) {
            Log.v("ImageLoader", "cache exception" + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            
            // Find the correct scale value. It should be the power of 2.
            
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            
            int ReqW = width_tmp;
            int ReqH = height_tmp;
            Log.v("ImageLoader", "vazeer sizes before w: " + width_tmp + " h: "
                    + height_tmp + "  device W:" + width + "  btmap: ");
            if (width_tmp >= width || width_tmp >= 175) {
                ReqW = width;
                ReqH = ((width * height_tmp) / width_tmp);
                Log.i("ImageLoader", "vazeer sizes require w: " + ReqW + " h: "
                        + ReqH);
            }
            
            int sampleSize = calculateInSampleSize(o, ReqW, ReqH);
            Log.d("ImageLoader", "vazeer info is samplesize:" + sampleSize
                    + " ReqW: " + ReqW + " ReqH: " + ReqH + " OrW:" + width_tmp
                    + " OrH: " + height_tmp);
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = sampleSize;
            // o2.outWidth = ReqW;
            // o2.outHeight = ReqH;
            
            Bitmap scaledbit = null;
            FileInputStream fis2 = new FileInputStream(f);
            Bitmap bmp = BitmapFactory.decodeStream(fis2, null, o2);
            fis2.close();
            if (bmp != null) {
                scaledbit = Bitmap.createScaledBitmap(bmp, ReqW, ReqH, false);
                // bmp.recycle();
                // bmp = null;
            }
            
            return scaledbit;
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        
        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        
        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }
        
        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url);
            memoryCache.addBitmapToMemoryCache(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url)) {
            Log.d("ImageLoder", "vazeer obj test: true " + photoToLoad.url
                    + "  tag: " + tag);
            return true;
        }
        Log.v("ImageLoder", "vazeer obj test: false");
        return false;
    }
    
    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        
        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }
        
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null) {
                /*
                 * RelativeLayout.LayoutParams layoutParams = new
                 * RelativeLayout.LayoutParams(
                 * LayoutParams.MATCH_PARENT,height);
                 * photoToLoad.imageView.setLayoutParams(layoutParams);
                 */
                photoToLoad.imageView.setImageBitmap(bitmap);
                ObjectAnimator
                        .ofFloat(photoToLoad.imageView, "alpha", 0.0f, 1.0f)
                        .setDuration(300).start();
            } else
                photoToLoad.imageView.setImageResource(stub_id);
            
        }
    }
    
    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
    
    public int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;
    }
    
    private int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        
        if (height > reqHeight || width > reqWidth) {
            
            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    
    public void loadProfileImage(final String profileUrl,
            final Activity context, final ImageView imageView, final float pixel) {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                Bitmap bitmap = memoryCache.getBitmapFromMemCache(profileUrl);
                if (bitmap == null) {
                    bitmap = getBitmap(profileUrl);
                    bitmap = Utils.getRoundedCornerBitmap(bitmap, pixel);
                    memoryCache.addBitmapToMemoryCache(profileUrl, bitmap);
                }
                final Bitmap bmap = bitmap;
                context.runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        ObjectAnimator.ofFloat(imageView, "alpha", 0.0f, 1.0f)
                                .setDuration(500).start();
                        imageView.setImageBitmap(bmap);
                    }
                });
            }
            
        }).start();
        
    }
}
