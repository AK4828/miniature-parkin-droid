package org.meruvian.esales.collector;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import java.io.File;

/**
 * Created by ludviantoovandi on 12/12/14.
 */
public class SignageAppication extends Application {
    private static SignageAppication instance;
    private static ObjectMapper objectMapper;
    private ObjectMapper jsonMapper;
    private JobManager jobManager;

    public SignageAppication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .resetViewBeforeLoading(true)  // default
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();

        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO) // default
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(10)
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1) //always keep at least one consumer alive
                .maxConsumerCount(3) //up to 3 consumers at a time
                .loadFactor(3) //3 jobs per consumer
                .consumerKeepAlive(120) //wait 2 minute
                .build();

        jobManager = new JobManager(this, configuration);
        jsonMapper = objectMapper;

        Log.d(getClass().getName(), "onCreate");
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static SignageAppication getInstance() {
        return instance;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public ObjectMapper getJsonMapper() {
        return jsonMapper;
    }

}
