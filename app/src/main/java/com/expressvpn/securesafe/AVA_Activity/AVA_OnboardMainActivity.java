package com.expressvpn.securesafe.AVA_Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_AdInterGD;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_IntentPass;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds.AVA_NativeHelper;
import com.expressvpn.securesafe.R;

public class AVA_OnboardMainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView new_txtnext;
    TextView new_txtnext2;
    TextView new_txt_letsstart;
    RelativeLayout new_one, new_two, new_three;
    SharedPreferences onb;
    SharedPreferences.Editor onb_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.silentsurfer_onboard_layout);
        LoadSmallNative();
        new_txtnext = findViewById(R.id.txt_next);
        new_txtnext2 = findViewById(R.id.txt_next2);
        new_one = findViewById(R.id.rl_one);
        new_two = findViewById(R.id.rl_two);
        new_three = findViewById(R.id.rl_three);
        new_txt_letsstart = findViewById(R.id.txt_lets_start);
        new_txtnext.setOnClickListener(this);
        new_txtnext2.setOnClickListener(this);
        new_txt_letsstart.setOnClickListener(this);
        onb = getSharedPreferences("onboarding", MODE_PRIVATE);
        onb_editor = onb.edit();

        if (onb.getString("onboarding", "nodata").equals("show")) {
            Intent intent = new Intent(AVA_OnboardMainActivity.this, AVA_MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void LoadSmallNative() {
        if (AVA_IntentPass.isNetworkAvailable(AVA_OnboardMainActivity.this)) {
            try {
                new AVA_NativeHelper().shownativeads_small(this, findViewById(R.id.Admob_Native_Frame_small), findViewById(R.id.native_ad_container),findViewById(R.id.shimmerFrameLayout));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_next:
                new_one.setVisibility(View.GONE);
                new_two.setVisibility(View.VISIBLE);
                new_three.setVisibility(View.GONE);
                break;
            case R.id.txt_next2:
                new_one.setVisibility(View.GONE);
                new_two.setVisibility(View.GONE);
                new_three.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_lets_start:
                new_one.setVisibility(View.GONE);
                new_two.setVisibility(View.GONE);
                new_three.setVisibility(View.VISIBLE);
                onb_editor.putString("onboarding", "show");
                onb_editor.putString("county_name", "United States");
                onb_editor.putInt("county", 0);
                onb_editor.putInt("flage", R.drawable.ava_ic_united_states);
                onb_editor.commit();
                AVA_AdInterGD.getInstance().Show_Inter_Ads(AVA_OnboardMainActivity.this, AVA_MainActivity.class, new Bundle());
                break;
        }
    }


}