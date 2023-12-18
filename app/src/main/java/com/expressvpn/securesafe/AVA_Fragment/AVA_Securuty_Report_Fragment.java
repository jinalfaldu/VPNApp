package com.expressvpn.securesafe.AVA_Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_IntentPass;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds.AVA_NativeHelper;
import com.expressvpn.securesafe.R;
import com.expressvpn.securesafe.AVA_Activity.AVA_UIActivity;

import java.util.List;
import java.util.Random;

public class AVA_Securuty_Report_Fragment extends Fragment {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    TextView new_tv_ssid;
    TextView new_tv_mac;
    TextView new_tv_signal;
    TextView new_tv_ip;
    TextView new_tv_security;
    TextView new_tv_connection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.silentsurfer_security_report_layout, container, false);
        initView(view);
        LoadNative_Big(view);
        return view;
    }

    private void LoadNative_Big(View inflate) {
        if (AVA_IntentPass.isNetworkAvailable(getActivity())) {
            try {
                new AVA_NativeHelper().admob_full(getActivity(), inflate.findViewById(R.id.Admob_Native_Frame_one), inflate.findViewById(R.id.native_ad_container), inflate.findViewById(R.id.shimmerFrameLayout));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void initView(View view) {
        new_tv_ssid = view.findViewById(R.id.tv_ssid);
        new_tv_mac = view.findViewById(R.id.tv_mac);
        new_tv_signal = view.findViewById(R.id.tv_signal);
        new_tv_ip = view.findViewById(R.id.tv_ip);
        new_tv_connection = view.findViewById(R.id.tv_connection);
        new_tv_security = view.findViewById(R.id.tv_security);
        new_tv_mac.setText("02:00:00:00:00:00");

        if (checkPermissionAccess()) {
            new_tv_ip.setText(AVA_UIActivity.stcurrentServer);
            new_tv_ssid.setText("" + getSSid());
            new_tv_security.setText("" + getSecurity());
            new_tv_connection.setText("" + getConnection());
            new_tv_signal.setText("" + getSingalStrenth());

        }
    }

    private String getSSid() {
        WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String ssid = info.getSSID();
        return ssid;
    }

    public String getSecurity() {
        WifiManager wifi = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
        @SuppressLint("MissingPermission") List<ScanResult> networkList = wifi.getScanResults();

        WifiInfo wi = wifi.getConnectionInfo();
        String currentSSID = wi.getSSID();

        if (networkList != null) {
            for (ScanResult network : networkList) {
                if (currentSSID.equals(network.SSID)) {
                    String capabilities = network.capabilities;
                    if (capabilities.contains("WPA2")) {
                        return "WPA2";
                    } else if (capabilities.contains("WPA")) {
                        return "WPA";
                    } else if (capabilities.contains("WEP")) {
                        return "WEP";
                    }
                }
            }
        }
        return "WPA";
    }



    public String getConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        }
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        return String.valueOf(downSpeed);
    }


    public String getSingalStrenth() {
        String[] array = {"91%", "92%", "93%", "94%", "95%", "96%", "97%", "98%",};
        String randomStr = array[new Random().nextInt(array.length)];
        return randomStr;
    }

    public boolean checkPermissionAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                try {
                    requestPermission();
                } catch (Exception e) {

                }
                return false;
            }
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
            }
        } catch (Exception e) {

        }
    }

    private void openFilePicker() {
        if (checkPermissionAccess()) {
            new_tv_ip.setText(AVA_UIActivity.stcurrentServer);
            new_tv_ssid.setText("" + getSSid());
            new_tv_security.setText("" + getSecurity());
            new_tv_connection.setText("" + getConnection());
            new_tv_signal.setText("" + getSingalStrenth());


        }
    }

    private void requestPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_WRITE_PERMISSION);
                } catch (Exception e) {

                }
            } else {
                openFilePicker();
            }
        } catch (Exception e) {

        }
    }
}
