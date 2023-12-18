package com.expressvpn.securesafe.AVA_Activity;

import static com.expressvpn.securesafe.AVA_Activity.AVA_MainActivity.RECIPE_CHOOSER;
import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.Privacy_police;
import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.vid;
import static com.expressvpn.securesafe.AVA_Activity.AVA_SplashActivity.vlink;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_AdInterGD;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_IntentPass;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds.AVA_NativeHelper;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NewAppOpenAdManager;
import com.expressvpn.securesafe.AVA_Fragment.AVA_IpLocation_Fragment;
import com.expressvpn.securesafe.AVA_Fragment.AVA_Network_Test_Fragment;
import com.expressvpn.securesafe.AVA_Fragment.AVA_Securuty_Report_Fragment;
import com.expressvpn.securesafe.AVA_utils.AVA_Converter;
import com.expressvpn.securesafe.AVA_utils.AVA_Utility;
import com.expressvpn.securesafe.BuildConfig;
import com.expressvpn.securesafe.R;
import com.expressvpn.securesafe.databinding.SilentsurferActivityMainTwoDuplicateBinding;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import unified.vpn.sdk.Callback;
import unified.vpn.sdk.ClientInfo;
import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.HydraTransportConfig;
import unified.vpn.sdk.OpenVpnTransportConfig;
import unified.vpn.sdk.RemainingTraffic;
import unified.vpn.sdk.SdkNotificationConfig;
import unified.vpn.sdk.TransportConfig;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.UnifiedSdkConfig;
import unified.vpn.sdk.VpnException;
import unified.vpn.sdk.VpnState;

public abstract class AVA_UIActivity extends AppCompatActivity {

    protected static final String TAG = AVA_MainActivity.class.getSimpleName();
    public static SilentsurferActivityMainTwoDuplicateBinding binding;
    UnifiedSdk unifiedSDK;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    final Runnable mUIUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            checkRemainingTraffic();
            mUIHandler.postDelayed(mUIUpdateRunnable, 10000);
        }
    };
    public static AVA_UIActivity mActivity;
    SharedPreferences onb;
    SharedPreferences.Editor onb_editor;
    AVA_NewAppOpenAdManager appOpenAdManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SilentsurferActivityMainTwoDuplicateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mActivity = this;
        onb = getSharedPreferences("onboarding", MODE_PRIVATE);
        try {
            onb_editor = onb.edit();
            int select = onb.getInt("flage", R.drawable.ava_ic_united_states);
            String county_name = onb.getString("county_name", "United States");
//          binding.imgServer.setImageResource(R.drawable.ava_ic_united_states);
            binding.imgServer.setImageResource(select);
            binding.countryName.setText(county_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoadSmallNative();
        binding.ivConnect.setOnClickListener(this::onConnectBtnClick);
        binding.bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(new Intent(getApplicationContext(), AVA_CountryListActivity.class), RECIPE_CHOOSER);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        next();
        setDrawable();

        binding.layHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.headerTxt.setText("VPN-All Proxy VPN");
                binding.homeLayout.setVisibility(View.VISIBLE);
                binding.ivHome.setImageResource(R.drawable.ava_ic_home_sel);
                binding.tvHome.setVisibility(View.GONE);
                binding.lnHome.setVisibility(View.VISIBLE);
                binding.ivSecurity.setImageResource(R.drawable.ava_ic_security_un);
                binding.tvSecurity.setVisibility(View.VISIBLE);
                binding.lnSecurity.setVisibility(View.GONE);
                binding.ivNetwork.setImageResource(R.drawable.ava_ic_network_test_un);
                binding.tvNetwork.setVisibility(View.VISIBLE);
                binding.lnNetwork.setVisibility(View.GONE);
                binding.ivLocation.setImageResource(R.drawable.ava_ic_loaction_un);
                binding.tvLocation.setVisibility(View.VISIBLE);
                binding.lnLocation.setVisibility(View.GONE);

            }
        });
        binding.laySecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.headerTxt.setText("Security Report");
                binding.homeLayout.setVisibility(View.GONE);
                binding.ivSecurity.setImageResource(R.drawable.ava_ic_security_sel);
                binding.tvSecurity.setVisibility(View.GONE);
                binding.lnSecurity.setVisibility(View.VISIBLE);
                binding.ivHome.setImageResource(R.drawable.ava_ic_home_unsel);
                binding.tvHome.setVisibility(View.VISIBLE);
                binding.lnHome.setVisibility(View.GONE);
                binding.ivNetwork.setImageResource(R.drawable.ava_ic_network_test_un);
                binding.tvNetwork.setVisibility(View.VISIBLE);
                binding.lnNetwork.setVisibility(View.GONE);
                binding.ivLocation.setImageResource(R.drawable.ava_ic_loaction_un);
                binding.tvLocation.setVisibility(View.VISIBLE);
                binding.lnLocation.setVisibility(View.GONE);
                replaceFragment(new AVA_Securuty_Report_Fragment());
            }
        });

        binding.layNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.headerTxt.setText("Network Test");
                binding.homeLayout.setVisibility(View.GONE);
                binding.ivNetwork.setImageResource(R.drawable.ava_ic_network_test_sel);
                binding.tvNetwork.setVisibility(View.GONE);
                binding.lnNetwork.setVisibility(View.VISIBLE);
                binding.ivHome.setImageResource(R.drawable.ava_ic_home_unsel);
                binding.tvHome.setVisibility(View.VISIBLE);
                binding.lnHome.setVisibility(View.GONE);
                binding.ivSecurity.setImageResource(R.drawable.ava_ic_security_un);
                binding.tvSecurity.setVisibility(View.VISIBLE);
                binding.lnSecurity.setVisibility(View.GONE);
                binding.ivLocation.setImageResource(R.drawable.ava_ic_loaction_un);
                binding.tvLocation.setVisibility(View.VISIBLE);
                binding.lnLocation.setVisibility(View.GONE);
                replaceFragment_network();


            }
        });

        binding.layLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.headerTxt.setText("IP Location");
                binding.homeLayout.setVisibility(View.GONE);
                binding.ivLocation.setImageResource(R.drawable.ava_ic_location_select);
                binding.tvLocation.setVisibility(View.GONE);
                binding.lnLocation.setVisibility(View.VISIBLE);
                binding.ivHome.setImageResource(R.drawable.ava_ic_home_unsel);
                binding.tvHome.setVisibility(View.VISIBLE);
                binding.lnHome.setVisibility(View.GONE);
                binding.ivSecurity.setImageResource(R.drawable.ava_ic_security_un);
                binding.tvSecurity.setVisibility(View.VISIBLE);
                binding.lnSecurity.setVisibility(View.GONE);
                binding.ivNetwork.setImageResource(R.drawable.ava_ic_network_test_un);
                binding.tvNetwork.setVisibility(View.VISIBLE);
                binding.lnNetwork.setVisibility(View.GONE);

                replaceFragment(new AVA_IpLocation_Fragment());

            }
        });
    }


    private void LoadSmallNative() {
        if (AVA_IntentPass.isNetworkAvailable(AVA_UIActivity.this)) {
            try {
                new AVA_NativeHelper().shownativeads_small(this, findViewById(R.id.Admob_Native_Frame_small), findViewById(R.id.native_ad_container), findViewById(R.id.shimmerFrameLayout));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    public void replaceFragment_network() {
        Bundle bundle = new Bundle();
        bundle.putString("ip", stcurrentServer);
        bundle.putString("donwload_speed", stDwSpeed);
        bundle.putString("upload_speed", stUploadSpeed);
        AVA_Network_Test_Fragment fragInfo = new AVA_Network_Test_Fragment();
        fragInfo.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragInfo);
        fragmentTransaction.commit();

    }

    public boolean vpn() {
        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp()) iface = networkInterface.getName();
                Log.d("DEBUG", "IFACE NAME: " + iface);
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    private static final String CHANNEL_ID = "vpn";

    private void initSDK() {
        ClientInfo clientInfo = ClientInfo.newBuilder().addUrl(vlink).carrierId(vid).build();
        List<TransportConfig> transportConfigList = new ArrayList<>();
        transportConfigList.add(HydraTransportConfig.create());
        transportConfigList.add(OpenVpnTransportConfig.tcp());
        transportConfigList.add(OpenVpnTransportConfig.udp());
        UnifiedSdk.update(transportConfigList, CompletableCallback.EMPTY);
        UnifiedSdkConfig config = UnifiedSdkConfig.newBuilder().build();
        unifiedSDK = UnifiedSdk.getInstance(clientInfo, config);
        SdkNotificationConfig notificationConfig = SdkNotificationConfig.newBuilder().title(getResources().getString(R.string.app_name)).channelId(CHANNEL_ID).build();
        UnifiedSdk.update(notificationConfig);
        UnifiedSdk.setLoggingLevel(Log.VERBOSE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isConnected(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    startUIUpdateTask();
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUIUpdateTask(false);
    }

    protected abstract void isLoggedIn(Callback<Boolean> callback);

    protected abstract void loginToVpn();

    public void onConnectBtnClick(View v) {

        Handler mHandler = new Handler();
        final Runnable r = () -> {
            if (unifiedSDK == null) {
                Toast.makeText(AVA_UIActivity.this, "SDK is not configured", Toast.LENGTH_LONG).show();
                return;
            }
            isConnected(new Callback<Boolean>() {
                @Override
                public void success(@NonNull Boolean aBoolean) {
                    if (aBoolean) {
                        AVA_Utility.checkVpnDis = true;
                        checkStartServer = false;
                        disconnectFromVnp();
                    } else {
                        connectToVpn();
                    }
                }

                @Override
                public void failure(@NonNull VpnException e) {

                }
            });
        };
        mHandler.postDelayed(r, 300);
    }

    protected abstract void isConnected(Callback<Boolean> callback);

    protected abstract void connectToVpn();

    protected abstract void disconnectFromVnp();

    public void onServerChooserClick(View v) {
        if (unifiedSDK == null) {
            Toast.makeText(this, "SDK is not configured", Toast.LENGTH_LONG).show();
            return;
        }
        chooseServer();
    }

    protected abstract void chooseServer();

    protected abstract void getCurrentServer(Callback<String> callback);

    protected void startUIUpdateTask() {
        stopUIUpdateTask(true);
        mUIHandler.post(mUIUpdateRunnable);
    }

    protected void stopUIUpdateTask(boolean b) {
        mUIHandler.removeCallbacks(mUIUpdateRunnable);
        if (b) {
            updateUI();
        }
    }

    protected abstract void checkRemainingTraffic();

    protected void updateUI() {
        UnifiedSdk.getVpnState(new Callback<VpnState>() {
            @Override
            public void success(@NonNull VpnState vpnState) {
                binding.trafficLimit.setVisibility(vpnState == VpnState.CONNECTED ? View.VISIBLE : View.INVISIBLE);

                switch (vpnState) {
                    case IDLE: {
                        binding.connectBtn.setEnabled(true);
                        binding.connectBtn.setText(R.string.disconnect);
                        binding.connectBtn.setTextColor(getResources().getColor(R.color.red));
                        binding.connectBtn.setTextColor(getResources().getColor(R.color.red));
                        binding.connectedIcnn.setImageResource(R.drawable.ava_disconnect_icn);
                        binding.ivConnect.setImageResource(R.drawable.ava_ic_disconnect);
                        checAknimatio = true;
                        stopAni();
                        if (AVA_Utility.checkVpnDis) {
                            try {
                                checkStartServer = true;
                                AVA_Utility.checkVpnDis = false;
                                Bundle bundle = new Bundle();
                                bundle.putString("time", sttimer);
                                bundle.putString("ip", stcurrentServer);
                                bundle.putString("donwload_speed", stDwSpeed);
                                bundle.putString("upload_speed", stUploadSpeed);
                                AVA_AdInterGD.getInstance().Show_Inter_Ads(AVA_UIActivity.this, AVA_DisconnectRepoerActivity.class, bundle);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case CONNECTED: {
                        if (checkDialogVpn) {
                            checkDialogVpn = false;
                            try {
                                if (mDialog != null) {
                                    mDialog.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        binding.connectBtn.setEnabled(true);
                        binding.connectBtn.setText(R.string.connected);
                        binding.connectBtn.setTextColor(getResources().getColor(R.color.green));
                        binding.connectedIcnn.setImageResource(R.drawable.ava_conect_icn);

                        if (checAknimatio) {
                            checAknimatio = false;
                            binding.ivConnect.setImageResource(R.drawable.ava_ic_connect);
                            startAni();
                            startTime();
                        }
                        break;
                    }
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS: {
                        binding.connectBtn.setText(R.string.connecting);
                        binding.connectBtn.setTextColor(getResources().getColor(R.color.green));
                        binding.ivConnect.setImageResource(R.drawable.ava_ic_connect);
                        binding.connectedIcnn.setImageResource(R.drawable.ava_conect_icn);

                        binding.connectBtn.setEnabled(false);
                        break;
                    }
                    case PAUSED: {
                        binding.connectBtn.setEnabled(false);
                        binding.connectBtn.setTextColor(getResources().getColor(R.color.red));
                        binding.connectBtn.setText(R.string.paused);
                        binding.connectedIcnn.setImageResource(R.drawable.ava_disconnect_icn);
                        binding.ivConnect.setImageResource(R.drawable.ava_ic_disconnect);
                        checAknimatio = true;
                        stopAni();
                        break;
                    }
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
        UnifiedSdk.getInstance().getBackend().isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean isLoggedIn) {
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });

        getCurrentServer(new Callback<String>() {
            @Override
            public void success(@NonNull final String currentServer) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (checkStartServer) {
                            stcurrentServer = currentServer != null ? currentServer : "UNKNOWN";
                        }
                    }
                });
            }

            @Override
            public void failure(@NonNull VpnException e) {
                binding.selectedServer.setText("UNKNOWN");
            }
        });
    }

    boolean checAknimatio = true;
    boolean checkStartServer = true;

    private void stopAni() {
        binding.ivConnect.setVisibility(View.VISIBLE);
        binding.lottieConnect.setVisibility(View.GONE);

        CountDownTimer countDownTimer = mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding.vpnConnectTime.setText("00:00:00");
    }

    private void startAni() {
        binding.ivConnect.setVisibility(View.VISIBLE);
        binding.lottieConnect.setVisibility(View.VISIBLE);
    }

    String stDwSpeed;
    String stUploadSpeed;
    public static String stcurrentServer;
    String sttimer;

    protected void updateTrafficStats(long outBytes, long inBytes) {
        String outString = AVA_Converter.humanReadableByteCountOld(outBytes, false);
        String inString = AVA_Converter.humanReadableByteCountOld(inBytes, false);
        binding.ivDwSpeed.setText("Download speed" + "\n" + outString);
        binding.ivUploadSpeed.setText("Upload speed" + "\n" + inString);
        stDwSpeed = outString;
        stUploadSpeed = inString;
    }

    protected void updateRemainingTraffic(RemainingTraffic remainingTrafficResponse) {
        if (remainingTrafficResponse.isUnlimited()) {
            binding.trafficLimit.setText("UNLIMITED available");
        } else {
            String trafficUsed = AVA_Converter.megabyteCount(remainingTrafficResponse.getTrafficUsed()) + "Mb";
            String trafficLimit = AVA_Converter.megabyteCount(remainingTrafficResponse.getTrafficLimit()) + "Mb";
            binding.trafficLimit.setText(getResources().getString(R.string.traffic_limit, trafficUsed, trafficLimit));
        }
    }

    public static CountDownTimer mCountDownTimer;

    public void startTime() {
        try {
            mCountDownTimer = new CountDownTimer(6000000, 1000) {
                public void onTick(long millisUntilFinished) {
                    try {
                        NumberFormat f = new DecimalFormat("00");
                        long hour = (millisUntilFinished / 3600000) % 24;
                        long min = (millisUntilFinished / 60000) % 60;
                        long sec = (millisUntilFinished / 1000) % 60;
                        binding.vpnConnectTime.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                        sttimer = f.format(hour) + ":" + f.format(min) + ":" + f.format(sec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onFinish() {
                    try {
                        binding.vpnConnectTime.setText("00:00:00");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void showMessage(String msg) {
        Toast.makeText(AVA_UIActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    SharedPreferences mysharedpreferences;


    boolean checkDialogVpn = true;
    Dialog mDialog;

    private void next() {
        if (vpn()) {
            initSDK();
            Handler mHandler = new Handler();
            Runnable runnable = () -> {
                try {
                    loginToVpn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            mHandler.postDelayed(runnable, 100);
        } else {
            initSDK();
            loginToVpn();
            mDialog = new Dialog(AVA_UIActivity.this);
            mDialog.setContentView(R.layout.silentsurfer_dialog_vpn_connect);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.show();
            TextView tv_msg = mDialog.findViewById(R.id.tv_msg);
            ImageView tv_close = mDialog.findViewById(R.id.tv_close);
            tv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        checkDialogVpn = false;
                        if (mDialog != null) {
                            mDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            ImageView iv_connect = mDialog.findViewById(R.id.iv_connect);
            iv_connect.setOnClickListener(v -> {
                iv_connect.setEnabled(false);
                iv_connect.setImageResource(R.drawable.ava_ic_connect);
                tv_msg.setText("Connecting...");
                tv_msg.setTextColor(getResources().getColor(R.color.green));
                try {
                    checkDialogVpn = true;
                    onConnectBtnClick(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void setDrawable() {
        binding.ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawer.openDrawer(GravityCompat.START);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawer, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                binding.drawer.bringChildToFront(drawerView);
                binding.drawer.requestLayout();
            }
        };


        toggle.setDrawerIndicatorEnabled(true);
        binding.drawer.setDrawerListener(toggle);
        toggle.syncState();

        try {


            LinearLayout ll_rate_menu = findViewById(R.id.ll_rate_menu);
            ll_rate_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        binding.drawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }
            });

            LinearLayout ll_change_contry = findViewById(R.id.ll_change_contry);
            ll_change_contry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        binding.drawer.closeDrawer(GravityCompat.START);
                        mysharedpreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), MODE_PRIVATE);
                        startActivityForResult(new Intent(getApplicationContext(), AVA_CountryListActivity.class), RECIPE_CHOOSER);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            LinearLayout ll_security = findViewById(R.id.ll_security);
            ll_security.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        binding.headerTxt.setText("Security Report");
                        binding.drawer.closeDrawer(GravityCompat.START);
                        binding.homeLayout.setVisibility(View.GONE);
                        binding.ivSecurity.setImageResource(R.drawable.ava_ic_security_sel);
                        binding.tvSecurity.setVisibility(View.GONE);
                        binding.lnSecurity.setVisibility(View.VISIBLE);
                        binding.ivHome.setImageResource(R.drawable.ava_ic_home_unsel);
                        binding.tvHome.setVisibility(View.VISIBLE);
                        binding.lnHome.setVisibility(View.GONE);
                        binding.ivNetwork.setImageResource(R.drawable.ava_ic_network_test_un);
                        binding.tvNetwork.setVisibility(View.VISIBLE);
                        binding.lnNetwork.setVisibility(View.GONE);
                        binding.ivLocation.setImageResource(R.drawable.ava_ic_loaction_un);
                        binding.tvLocation.setVisibility(View.VISIBLE);
                        binding.lnLocation.setVisibility(View.GONE);
                        replaceFragment(new AVA_Securuty_Report_Fragment());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            LinearLayout ll_ip_location = findViewById(R.id.ll_ip_location);
            ll_ip_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        binding.headerTxt.setText("IP Location");
                        binding.drawer.closeDrawer(GravityCompat.START);
                        binding.homeLayout.setVisibility(View.GONE);
                        binding.ivLocation.setImageResource(R.drawable.ava_ic_location_select);
                        binding.tvLocation.setVisibility(View.GONE);
                        binding.lnLocation.setVisibility(View.VISIBLE);
                        binding.ivHome.setImageResource(R.drawable.ava_ic_home_unsel);
                        binding.tvHome.setVisibility(View.VISIBLE);
                        binding.lnHome.setVisibility(View.GONE);
                        binding.ivSecurity.setImageResource(R.drawable.ava_ic_security_un);
                        binding.tvSecurity.setVisibility(View.VISIBLE);
                        binding.lnSecurity.setVisibility(View.GONE);
                        binding.ivNetwork.setImageResource(R.drawable.ava_ic_network_test_un);
                        binding.tvNetwork.setVisibility(View.VISIBLE);
                        binding.lnNetwork.setVisibility(View.GONE);
                        replaceFragment(new AVA_IpLocation_Fragment());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            LinearLayout ll_network_report = findViewById(R.id.ll_network_report);
            ll_network_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        binding.headerTxt.setText("Network Test");

                        binding.drawer.closeDrawer(GravityCompat.START);
                        binding.homeLayout.setVisibility(View.GONE);
                        binding.ivNetwork.setImageResource(R.drawable.ava_ic_network_test_sel);
                        binding.tvNetwork.setVisibility(View.GONE);
                        binding.lnNetwork.setVisibility(View.VISIBLE);
                        binding.ivHome.setImageResource(R.drawable.ava_ic_home_unsel);
                        binding.tvHome.setVisibility(View.VISIBLE);
                        binding.lnHome.setVisibility(View.GONE);
                        binding.ivSecurity.setImageResource(R.drawable.ava_ic_security_un);
                        binding.tvSecurity.setVisibility(View.VISIBLE);
                        binding.lnSecurity.setVisibility(View.GONE);
                        binding.ivLocation.setImageResource(R.drawable.ava_ic_loaction_un);
                        binding.tvLocation.setVisibility(View.VISIBLE);
                        binding.lnLocation.setVisibility(View.GONE);
                        replaceFragment_network();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            LinearLayout ll_share_app_menu = findViewById(R.id.ll_share_app_menu);
            ll_share_app_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        binding.drawer.closeDrawer(GravityCompat.START);
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                        String shareMessage = "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                    }
                }
            });

            LinearLayout ll_pp_menu = findViewById(R.id.ll_pp_menu);
            ll_pp_menu.setOnClickListener(v -> {
                binding.drawer.closeDrawer(GravityCompat.START);
                if (AVA_IntentPass.isNetworkAvailable(AVA_UIActivity.this)) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Privacy_police)));
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Privacy_police)));
                    }
                } else {
                    Toast.makeText(AVA_UIActivity.this, "Connect Internet Please...", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
