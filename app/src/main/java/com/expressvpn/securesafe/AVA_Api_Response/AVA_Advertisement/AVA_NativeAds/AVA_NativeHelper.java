package com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds;


import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.admob_native;
import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.fbadNative;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_ADSharedPref;
import com.expressvpn.securesafe.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;

public class AVA_NativeHelper {

    Dialog dialog;

    void show_dialog(Context context) {
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

    void dismiss_dialog() {
        dialog.dismiss();
    }

    public void admob_full(Activity activity, final ViewGroup viewGroup, NativeAdLayout nativeAdLayout, ShimmerFrameLayout shimmerViewContainer) {
        String native_dialog = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.NATIVE_DIALOG, "0");
        if (native_dialog.equals("1")) {
            show_dialog(activity);

        }

        String admobnative = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.admob_native, admob_native);
//        String admobnative = AVA_ADSharedPref.getString(activity, "ca-app-pub-3940256099942544/224769611000", "ca-app-pub-3940256099942544/224769611000");
        AdLoader.Builder builder = new AdLoader.Builder(activity, admobnative);
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                new AVA_InflatAds(activity).inflat_admobnative(nativeAd, viewGroup);
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.hideShimmer();
                if (native_dialog.equals("1")) {
                    dismiss_dialog();
                }
            }

        });
        AdLoader adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.hideShimmer();
                showFBNativeADs(activity, nativeAdLayout, shimmerViewContainer);


            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }


    public void showFBNativeADs(Activity activity, NativeAdLayout nativeAdLayout, ShimmerFrameLayout shimmerViewContainer) {

        AudienceNetworkAds.initialize(activity);
        String FB_Native = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.FBAD_NATIVE, fbadNative);
//        String FB_Native = AVA_ADSharedPref.getString(activity, "YOUR_PLACEMENT_ID", "YOUR_PLACEMENT_ID");
        NativeAd nativeAd = new NativeAd(activity, FB_Native);
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(Ad ad) {
                            }

                            @Override
                            public void onError(Ad ad, AdError adError) {
                                shimmerViewContainer.stopShimmer();
                                shimmerViewContainer.hideShimmer();
                                nativeAdLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdLoaded(Ad ad) {
                                shimmerViewContainer.stopShimmer();
                                shimmerViewContainer.hideShimmer();
                                new AVA_InflatAds(activity).inflate_NATIV_FB_Big(nativeAd, nativeAdLayout);

                                //   dismiss_dialog();


                            }

                            @Override
                            public void onAdClicked(Ad ad) {
                                // Native ad clicked
                            }

                            @Override
                            public void onLoggingImpression(Ad ad) {
                                // Native ad impression
                            }
                        })
                        .build());
    }

    public void shownativeads_small(Activity activity, final ViewGroup viewGroup, NativeAdLayout nativeAdLayout, ShimmerFrameLayout shimmerViewContainer) {
        String native_dialog = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.NATIVE_DIALOG, "0");
        if (native_dialog.equals("1")) {
            show_dialog(activity);
        }
        String admobnative = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.admob_native, admob_native);
//        String admobnative = AVA_ADSharedPref.getString(activity, "ca-app-pub-3940256099942544/224769611000", "ca-app-pub-3940256099942544/224769611000");
        AdLoader.Builder builder = new AdLoader.Builder(activity, admobnative);
        builder.forNativeAd(new com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                new AVA_InflatAds(activity).inflat_admobnativesmall(nativeAd, viewGroup);
                if (native_dialog.equals("1")) {
                    dismiss_dialog();
                }
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.hideShimmer();
            }

        });
        AdLoader adLoader = builder.withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                small_fb(activity, nativeAdLayout, shimmerViewContainer);
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.hideShimmer();

            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void small_fb(Activity activity, NativeAdLayout nativeAdLayout, ShimmerFrameLayout shimmerViewContainer) {
//        String small_fb = AVA_ADSharedPref.getString(activity, AVA_ADSharedPref.FBAD_NATIVE, fbadNative);
        String small_fb = AVA_ADSharedPref.getString(activity, "YOUR_PLACEMENT_ID", "YOUR_PLACEMENT_ID");
        NativeAd nativeAd = new NativeAd(activity, small_fb);
        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.hideShimmer();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (dialog != null) {
                    dismiss_dialog();
                }
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.hideShimmer();
                new AVA_InflatAds(activity).inflate_NATIV_FB(nativeAd, nativeAdLayout);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        }).build());

    }
}
