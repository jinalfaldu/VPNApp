package com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AVA_MyApplicationAppOpen extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        MultiDex.install(this);

        MobileAds.initialize(this, initializationStatus -> {

        });

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager(AVA_MyApplicationAppOpen.this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {
        appOpenAdManager.showAdIfAvailable(currentActivity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);

    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }


    public class AppOpenAdManager {
        private static final String LOG_TAG = "AppOpenAdManager";
        private String AD_UNIT_ID;
        private String AD_UNIT_ID_Failed;
        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;
        private long loadTime = 0;

        public AppOpenAdManager(Context context) {
            //   loadAd(context);

        }

        private void loadAd(Context context) {
            AD_UNIT_ID = AVA_ADSharedPref.getString(getApplicationContext(), AVA_ADSharedPref.AppOpen, "");

            // AD_UNIT_ID = App_ADSharedPref.openads;

            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(context, AD_UNIT_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = (new Date()).getTime();
                    //Toast.makeText(context, "onAdLoaded Aop", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;
                }
            });
        }

        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        private boolean isAdAvailable() {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(activity, new OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    // Empty because the user will go back to the DD_activity that shows the ad.
                }
            });
        }

        private void showAdIfAvailable(@NonNull final Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            if (isShowingAd) {
                return;
            }

            if (!isAdAvailable()) {
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    appOpenAd = null;
                    isShowingAd = false;

                    onShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    appOpenAd = null;
                    isShowingAd = false;
                    onShowAdCompleteListener.onShowAdComplete();
                    loadAdFailed(activity);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                }
            });

            isShowingAd = true;
            appOpenAd.show(activity);
        }

        public void loadAdFailed(Context context) {
            AD_UNIT_ID_Failed = AVA_ADSharedPref.getString(getApplicationContext(), AVA_ADSharedPref.openads, "");
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(context, AD_UNIT_ID_Failed, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = (new Date()).getTime();

                }
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;
                }
            });
        }
    }

}
