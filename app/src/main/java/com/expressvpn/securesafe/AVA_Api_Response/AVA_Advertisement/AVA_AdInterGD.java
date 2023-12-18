package com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement;


import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.fbadInter;
import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.unity_adid;
import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.unity_banner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity;
import com.expressvpn.securesafe.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

public class AVA_AdInterGD {

    private InterstitialAd FBinterstitialAd;
    public com.google.android.gms.ads.interstitial.InterstitialAd interstitialOne;
    private static AVA_AdInterGD mInstance;
    public static int gclick = 1;

    public static AVA_AdInterGD getInstance() {
        if (mInstance == null) {
            mInstance = new AVA_AdInterGD();
        }
        return mInstance;
    }

    String unityGameID = "";
    Boolean testMode = false;
    String adUnitId = "";
    Dialog dialog;

    private void Show_Dialog(Activity context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.silentsurfer_dialog_layout);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }

    public void LoadUnityAd(Activity activity, Class<? extends Activity> openActivity, Bundle bundle) {
        String uGI = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.unity_adid, unity_adid);
        String adUnit = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.unity_banner, unity_banner);
//        String uGI = AVA_ADSharedPref.getString(activity, "14851", "14851");
//        String adUnit = AVA_ADSharedPref.getString(activity, "video", "video");
        adUnitId = adUnit;
        unityGameID = uGI;
        UnityAds.load(adUnit, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                UnityAds.show(activity, adUnitId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                    @Override
                    public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, openActivity);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onUnityAdsShowStart(String placementId) {

                    }

                    @Override
                    public void onUnityAdsShowClick(String placementId) {
                    }

                    @Override
                    public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                        dialog.dismiss();
                        Intent intent = new Intent(activity, openActivity);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                dialog.dismiss();
                Intent intent = new Intent(activity, openActivity);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                activity.startActivity(intent);

            }
        });

        UnityAds.initialize(activity, uGI, testMode, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {

            }

            @Override
            public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {

            }
        });
    }

    public void LoadFB_Intersitial(Activity activity, Class<? extends Activity> openActivity, Bundle bundle) {
        String FB_inter = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.FBAD_INTER, fbadInter);
//        String FB_inter = AVA_ADSharedPref.getString(activity, "YOUR_PLACEMENT_ID", "YOUR_PLACEMENT_ID");
        AudienceNetworkAds.initialize(activity);

        FBinterstitialAd = new InterstitialAd(activity, FB_inter);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                dialog.dismiss();
                Intent intent = new Intent(activity, openActivity);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                activity.startActivity(intent);

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                FBinterstitialAd = null;
                LoadUnityAd(activity, openActivity, bundle);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                FBinterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };
        FBinterstitialAd.loadAd(
                FBinterstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    public void Show_Inter_Ads(Activity activity, Class<? extends Activity> openActivity, Bundle bundle) {
        Show_Dialog(activity);

        MobileAds.initialize(activity, initializationStatus -> {
        });

        String admobinter = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.admob_interstital, AVA_SplashActivity.admob_interstital);
//        String admobinter = AVA_ADSharedPref.getString(activity, "ca-app-pub-3940256099942544/103317371200", "ca-app-pub-3940256099942544/103317371200");

        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, admobinter, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                interstitialOne = interstitialAd;
                interstitialAd.show(activity);
                interstitialOne.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        interstitialOne = null;

                        Intent intent = new Intent(activity, openActivity);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        interstitialOne = null;
                        Intent intent = new Intent(activity, openActivity);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        activity.startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                interstitialOne = null;
                LoadFB_Intersitial(activity, openActivity, bundle);

            }
        });

    }

}