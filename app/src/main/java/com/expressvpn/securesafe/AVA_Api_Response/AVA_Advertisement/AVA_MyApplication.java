package com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement;

import android.content.Context;


public class AVA_MyApplication extends AVA_MyApplicationAppOpen {
    public static Context context;
    public static Boolean isAdsShow = true;
    public static Boolean isCrop, isSavePng;
    AVA_AppOpenManager appOpenManager;

    Class aClass;

    public void setClass(Class cls) {
        this.aClass = cls;
    }

    static {
        isCrop = true;
        isSavePng = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        appOpenManager = new AVA_AppOpenManager(this);
    }

    public static Context getContext() {
        return context;
    }
}
