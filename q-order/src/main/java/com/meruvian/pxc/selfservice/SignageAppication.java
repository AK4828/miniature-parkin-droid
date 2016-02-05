package com.meruvian.pxc.selfservice;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.meruvian.pxc.selfservice.interceptor.SecurityInterceptor;
import com.meruvian.pxc.selfservice.job.RefreshTokenJob;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.util.concurrent.TimeUnit;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ludviantoovandi on 12/12/14.
 */
public class SignageAppication extends Application {
    private static SignageAppication instance;
    private static ObjectMapper objectMapper;
    private ObjectMapper jsonMapper;
    private JobManager jobManager;
    private Retrofit retrofit;


    public SignageAppication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Iconify
                .with(new FontAwesomeModule())
                .with(new TypiconsModule());


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

        configureRestAdaper();

        if (AuthenticationUtils.getCurrentAuthentication() != null) {
            if (isAccess()) {
            } else {
                jobManager.addJobInBackground(new RefreshTokenJob());
            }
        }
    }

    private void configureRestAdaper() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new SecurityInterceptor());
        client.interceptors().add(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(SignageVariables.SERVER_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();
    }

    public boolean isAccess() {
        boolean access = false;
        long expiresIn = AuthenticationUtils.getCurrentAuthentication().getExpiresIn();
        long loginTime = AuthenticationUtils.getCurrentAuthentication().getLoginTime();
        long curentTime = System.currentTimeMillis();
        long realDuration = curentTime - loginTime;
        long realDurationInSecon = TimeUnit.MILLISECONDS.toSeconds(realDuration);

        if (expiresIn > realDurationInSecon){
            access = true;
        }else {
            access = false;
        }
        return access;
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

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
