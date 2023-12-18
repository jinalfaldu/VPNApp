package com.expressvpn.securesafe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_MyApplicationAppOpen;
//import com.expressvpn.securesafe.Response.MyApplication;

import java.util.ArrayList;
import java.util.List;

import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.HydraTransportConfig;
import unified.vpn.sdk.OpenVpnTransportConfig;
import unified.vpn.sdk.SdkNotificationConfig;
import unified.vpn.sdk.TransportConfig;
import unified.vpn.sdk.UnifiedSdk;

public class AVA_MainApplication extends AVA_MyApplicationAppOpen {
    
    private static final String CHANNEL_ID = "vpn";

    @Override
    public void onCreate() {
        super.onCreate();
        initHydraSdk();
    }
    public void initHydraSdk() {
        createNotificationChannel();
        List<TransportConfig> transportConfigList = new ArrayList<>();
        transportConfigList.add(HydraTransportConfig.create());
        transportConfigList.add(OpenVpnTransportConfig.tcp());
        transportConfigList.add(OpenVpnTransportConfig.udp());
        UnifiedSdk.update(transportConfigList, CompletableCallback.EMPTY);

        SdkNotificationConfig notificationConfig = SdkNotificationConfig.newBuilder()
                .title(getResources().getString(R.string.app_name))
                .channelId(CHANNEL_ID)
                .build();

        UnifiedSdk.update(notificationConfig);
        UnifiedSdk.setLoggingLevel(Log.VERBOSE);

    }

    public void setNewHostAndCarrier(String hostUrl, String carrierId) {
        SharedPreferences prefs = getPrefs();
        if (TextUtils.isEmpty(hostUrl)) {
            prefs.edit().remove(STORED_HOST_URL_KEY).apply();
        } else {
            prefs.edit().putString(STORED_HOST_URL_KEY, hostUrl).apply();
        }

        if (TextUtils.isEmpty(carrierId)) {
            prefs.edit().remove(STORED_CARRIER_ID_KEY).apply();
        } else {
            prefs.edit().putString(STORED_CARRIER_ID_KEY, carrierId).apply();
        }
        initHydraSdk();
    }

    public SharedPreferences getPrefs() {
        return getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }
    public static final String SHARED_PREFS = "NORTHGHOST_SHAREDPREFS";
    public static final String STORED_CARRIER_ID_KEY = "com.expressvpn.securesafe.CARRIER_ID_KEY";
    public static final String STORED_HOST_URL_KEY = "com.expressvpn.securesafe.STORED_HOST_KEY";
    public static final String BASE_HOST = "https://backend.northghost.com";
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sample VPN";
            String description = "VPN notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
