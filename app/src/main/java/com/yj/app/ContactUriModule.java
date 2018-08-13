package com.yj.app;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * TODO: please input the description for this class.
 *
 * @author yangjing @ Zhihu Inc.
 * @since 2018-08-13
 */
@GlideModule
public class ContactUriModule extends AppGlideModule {
    private static final String TAG = "ContactUriModule";

    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024 * 4;
    private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 200;
    // Intentionally keyFailed.

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

        builder.setBitmapPool(new LruBitmapPool(getCacheSize(context)));
        builder.setMemoryCache(new LruResourceCache(getCacheSize(context)));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "cache", DEFAULT_DISK_CACHE_SIZE));
        builder.setLogLevel(Log.ERROR);

        super.applyOptions(context, builder);
    }

    private long getCacheSize(Context context) {
        long cacheSize = DEFAULT_CACHE_SIZE;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        if (am != null) {
            am.getMemoryInfo(mi);
            long availMem = mi.availMem;
            cacheSize = availMem / 40;
        }
        if (cacheSize < DEFAULT_CACHE_SIZE) {
            cacheSize = DEFAULT_CACHE_SIZE;
        }
        Log.i(TAG, "getCacheSize: " + cacheSize);
        return cacheSize;
    }
}