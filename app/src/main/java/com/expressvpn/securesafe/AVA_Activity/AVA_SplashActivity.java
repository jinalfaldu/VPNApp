package com.expressvpn.securesafe.AVA_Activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_AdsApiClient;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_AdsApiInterface;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_ADSharedPref;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_AdInterGD;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NewAppOpenAdManager;
import com.expressvpn.securesafe.R;
import com.facebook.ads.AdSettings;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AVA_SplashActivity extends AppCompatActivity {
    public FirebaseAnalytics mFirebaseAnalytics;
    public static int click;
    int flag;
    private AVA_NewAppOpenAdManager appOpenAdManager;
    public static String fb_nativebanner, fbadNative, admob_native;
    public static String admob_interstital, unity_intersitial, fbadInter;
    public static String unity_adid;
    public static String fb_banner;
    public static String unity_banner;
    private String admob_banner;
    public static String vlink, vid;
    public static String Privacy_police;
    private SharedPreferences preferences;
    private boolean isFirstRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.silentsurfer_splash_layout);

        try {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            AdSettings.addTestDevice("CBC793B922C9343E50A46F84B1C69AA1");
            AdSettings.setTestMode(false);

            preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
            isFirstRun = preferences.getBoolean("isFirstRun", true);

            getnetwork();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getnetwork() {
        if (isNetworkAvailable(this)) {
            new GetKeyid().execute();
        } else {
            Dialog nointer = new Dialog(AVA_SplashActivity.this);
            nointer.setContentView(R.layout.silentsurfer_dialog_internet);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            nointer.setCancelable(false);
            TextView btn_no = nointer.findViewById(R.id.btn_no);
            btn_no.setOnClickListener(v -> {
                if (isNetworkAvailable(AVA_SplashActivity.this)) {
                    new GetKeyid().execute();
                    nointer.dismiss();
                } else {
                    Toast.makeText(AVA_SplashActivity.this, "Required internet connection..", Toast.LENGTH_SHORT).show();
                }
            });
            nointer.show();
        }
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class GetKeyid extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String kk = "";
            try {
                ArrayList<String> urls = new ArrayList<>();
                URL url = new URL("https://myexternalip.com/raw");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;

                while ((str = in.readLine()) == null) {
                    urls.add(str);
                }
                in.close();
                kk = str;
                ArrayList<String> urls1 = new ArrayList<>();
                String u = "http://ip-api.com/php/" + kk;
                URL url1 = new URL(u);
                HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
                BufferedReader in1 = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
                String str1;
                while ((str1 = in1.readLine()) == null) {
                    urls1.add(str1);
                }
                in1.close();
                kk = str1;
            } catch (java.io.IOException e) {
                e.printStackTrace();

                try {
                    java.util.Scanner s = new java.util.Scanner(new URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A");
                    kk = s.next();

                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
            return kk;
        }

        @Override
        protected void onPostExecute(String kk1) {
            super.onPostExecute(kk1);
            getId(kk1, "");
        }
    }

    public void getId(String s, String ckk) {
        AVA_AdsApiInterface apiService = AVA_AdsApiClient.getClient().create(AVA_AdsApiInterface.class);
        Call call = apiService.getid(s, ckk);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                int statusCode = response.code();

                if (response.isSuccessful()) {
                    try {
                        if (statusCode == 200) {
                            String data = new Gson().toJson(response.body());
                            JSONObject jso = new JSONObject(data);

                            click = jso.getInt("inter_count");
                            flag = jso.getInt("open_inter_status");

                            admob_interstital = jso.getString("interstitial");
                            unity_intersitial = jso.getString("unity_intersitial");
                            fbadInter = jso.getString("fb_interstitial");

                            admob_native = jso.getString("native");

                            fb_nativebanner = jso.getString("fb_nativebanner");
                            fbadNative = jso.getString("fb_native");

                            admob_banner = jso.getString("banner");
                            fb_banner = jso.getString("fb_banner");
                            unity_banner = jso.getString("unity_banner");

                            unity_adid = jso.getString("unity_adid");


                            String appOpen = jso.getString("appopen");

                            String extrapage = jso.getString("extra_page");


                            Privacy_police = jso.getString("Privacy_police");

                            String native_dialog = jso.getString("native_dialog");

                            String is_back = jso.getString("back_status");


                            String app_status = jso.getString("applive_status");
                            String app_link = jso.getString("redirect_app_link");
                            vlink = jso.getString("vlink");
                            vid = jso.getString("vid");
                            
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.BACK, is_back);

                            AVA_ADSharedPref.setInteger(AVA_SplashActivity.this, AVA_ADSharedPref.CLICK, click);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.vlink_, vlink);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.vid_, vid);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.unity_intersitial, unity_intersitial);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.FBAD_INTER, fbadInter);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.admob_interstital, admob_interstital);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.fb_nativebanner, fb_nativebanner);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.FBAD_NATIVE, fbadNative);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.admob_native, admob_native);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.fb_banner, fb_banner);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.unity_banner, unity_banner);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.admob_banner, admob_banner);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.unity_adid, unity_adid);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.APP_STATUS, app_status);
                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.APP_LINK, app_link);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.extrapage, extrapage);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.AppOpen, appOpen);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.NATIVE_DIALOG, native_dialog);

                            AVA_ADSharedPref.setString(AVA_SplashActivity.this, AVA_ADSharedPref.Privacy_police, Privacy_police);

                            String stats = AVA_ADSharedPref.getString(AVA_SplashActivity.this, AVA_ADSharedPref.APP_STATUS, "0");
                            if (stats.equals("0")) {
                                next();
                            } else {
                                dialog();
                            }
                        }
                    } catch (Exception e) {

                        String stats = AVA_ADSharedPref.getString(AVA_SplashActivity.this, AVA_ADSharedPref.APP_STATUS, "0");
                        if (stats.equals("0")) {
                            next();
                        } else {
                            dialog();
                        }
                    }
                } else {
                    String stats = AVA_ADSharedPref.getString(AVA_SplashActivity.this, AVA_ADSharedPref.APP_STATUS, "0");
                    if (stats.equals("0")) {
                        next();
                    } else {
                        dialog();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                next();
            }
        });
    }

    public void next() {
        Load_next_intent();
    }

    private void Load_next_intent() {
        appOpenAdManager = new AVA_NewAppOpenAdManager(AVA_SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
                if (flag == 0) {
                    appOpenAdManager.showAdIfAvailable(AVA_SplashActivity.this, AVA_SplashActivity.this::MainIntent);
                } else {
                    MainIntent();
                }
//                    }
//                }, 5500);

            }
        }, 4500);
    }

    public void dialog() {
        String applink = AVA_ADSharedPref.getString(getApplicationContext(), AVA_ADSharedPref.APP_LINK, "0");
        try {
            Dialog redirect;
            redirect = new Dialog(AVA_SplashActivity.this);
            redirect.requestWindowFeature(Window.FEATURE_NO_TITLE);
            redirect.setContentView(R.layout.silentsurfer_dialog_redirect);
            redirect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = redirect.getWindow();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            if (window != null) {
                window.setGravity(Gravity.CENTER);
                window.setLayout((int) (0.9 * Resources.getSystem().getDisplayMetrics().widthPixels), android.widget.Toolbar.LayoutParams.WRAP_CONTENT);
            }
            redirect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            redirect.show();

            AppCompatButton btn = redirect.findViewById(R.id.btn_ok);
            btn.setOnClickListener(v -> {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(applink));
                startActivity(viewIntent);
                finishAffinity();
                redirect.dismiss();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void MainIntent() {
        if (isFirstRun) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.apply();
            if (flag == 0) {
                Intent intent = new Intent(AVA_SplashActivity.this, AVA_OnboardMainActivity.class);
                startActivity(intent);
            } else {
                AVA_AdInterGD.getInstance().Show_Inter_Ads(AVA_SplashActivity.this, AVA_OnboardMainActivity.class, new Bundle());

            }
        } else {
            if (flag == 0) {
                Intent intent = new Intent(AVA_SplashActivity.this, AVA_MainActivity.class);
                startActivity(intent);
            } else {
                AVA_AdInterGD.getInstance().Show_Inter_Ads(AVA_SplashActivity.this, AVA_MainActivity.class, new Bundle());

            }
        }


    }

}