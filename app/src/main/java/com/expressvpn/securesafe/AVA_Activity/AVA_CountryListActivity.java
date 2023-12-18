package com.expressvpn.securesafe.AVA_Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expressvpn.securesafe.AVA_Adapter.AVA_ClickListiner;
import com.expressvpn.securesafe.AVA_Adapter.AVA_CustomAdapter;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_AdInterGD;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_IntentPass;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds.AVA_NativeHelper;
import com.expressvpn.securesafe.AVA_Model.AVA_CountryList;
import com.expressvpn.securesafe.R;

import java.util.ArrayList;

public class AVA_CountryListActivity extends AppCompatActivity implements AVA_ClickListiner {
    SharedPreferences mysharedpreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.silentsurfer_country_list_layout);
        initView();
        LoadSmallNative();
    }

    SharedPreferences onb;
    SharedPreferences.Editor onb_editor;
    RecyclerView rl_list;

    private void LoadSmallNative() {
        try {
            if (AVA_IntentPass.isNetworkAvailable(AVA_CountryListActivity.this)) {
                try {
                    new AVA_NativeHelper().shownativeads_small(this, findViewById(R.id.Admob_Native_Frame_small), findViewById(R.id.native_ad_container), findViewById(R.id.shimmerFrameLayout));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        onb = getSharedPreferences("onboarding", MODE_PRIVATE);
        onb_editor = onb.edit();
        int select = onb.getInt("county", 0);
        ArrayList<AVA_CountryList> mCountryLists = getConutrt();
        rl_list = findViewById(R.id.rl_list);
        try {
            AVA_CustomAdapter mCustomAdapter = new AVA_CustomAdapter(this, select, mCountryLists, this);
            rl_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rl_list.setAdapter(mCustomAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            try {
                onBackPressed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private ArrayList<AVA_CountryList> getConutrt() {
        ArrayList<AVA_CountryList> mCountryLists = new ArrayList<>();

            mysharedpreferences = getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
            mCountryLists.add(new AVA_CountryList("CA", "Canada", R.drawable.ava_ic_canada));
            mCountryLists.add(new AVA_CountryList("TR", "Türkiye", R.drawable.ava_ic_turkiye));
            mCountryLists.add(new AVA_CountryList("US", "United States", R.drawable.ava_ic_united_states));
            mCountryLists.add(new AVA_CountryList("IE", "Ireland", R.drawable.ava_ic_ireland));
            mCountryLists.add(new AVA_CountryList("NL", "NetharLand", R.drawable.ava_ic_netherland));
            mCountryLists.add(new AVA_CountryList("SG", "Singapore", R.drawable.ava_ic_singapore));
            mCountryLists.add(new AVA_CountryList("MX", "Mexico", R.drawable.ava_ic_mexico));
            mCountryLists.add(new AVA_CountryList("AU", "Australia", R.drawable.ava_ic_australia));
            mCountryLists.add(new AVA_CountryList("JP", "Japan", R.drawable.ava_ic_japan));
            mCountryLists.add(new AVA_CountryList("DE", "Germany", R.drawable.ava_ic_germany));
            return mCountryLists;
    }


    @Override
    public void getPosition(int position, String code, String name, int icon) {
        try {
            onb_editor.putInt("county", position);
            onb_editor.putInt("flage", icon);
            onb_editor.putString("county_name", name);
            onb_editor.commit();
            Intent intent = new Intent();
            intent.putExtra("code", code);
            intent.putExtra("name", name);
            setResult(AVA_MainActivity.RECIPE_CHOOSER, intent);
            finish();
            try {
                AVA_UIActivity.binding.imgServer.setImageResource(icon);
                AVA_UIActivity.binding.countryName.setText(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            AVA_AdInterGD.getInstance().Show_Inter_Ads(AVA_CountryListActivity.this, AVA_MainActivity.class, new Bundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
