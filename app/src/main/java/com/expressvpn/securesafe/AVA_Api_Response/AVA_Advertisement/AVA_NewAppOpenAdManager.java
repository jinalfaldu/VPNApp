package com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AVA_NewAppOpenAdManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    AVA_MyApplicationAppOpen myApplication;
    private String AD_UNIT_ID = "";
    int checkAppOpen = 1;
    private AppOpenAd appOpenAd = null;
    private boolean isLoadingAd = false;
    public boolean isShowingAd = false;
    private long loadTime = 0;

    public AVA_NewAppOpenAdManager(Context activity) {
        AD_UNIT_ID = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.AppOpen, "");
        AVA_ADSharedPref.openads = AD_UNIT_ID;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        loadAd(activity);

    }

    public void loadAd(Context context) {
        if (isLoadingAd || isAdAvailable()) {
            return;
        }
        isLoadingAd = true;
        AdRequest request = new AdRequest.Builder().build();
        AppOpenAd.load(context, AD_UNIT_ID, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, new AppOpenAd.AppOpenAdLoadCallback() {

            @Override
            public void onAdLoaded(@NonNull AppOpenAd ad) {
                appOpenAd = ad;
                isLoadingAd = false;
                loadTime = (new Date()).getTime();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isLoadingAd = false;
                loadAd(context);
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

    public void showAdIfAvailable(@NonNull final Activity activity, @NonNull AVA_MyApplicationAppOpen.OnShowAdCompleteListener onShowAdCompleteListener) {
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
                loadAd(activity);
            }

            @Override
            public void onAdShowedFullScreenContent() {
            }
        });

        isShowingAd = true;
        appOpenAd.show(activity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

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
}
