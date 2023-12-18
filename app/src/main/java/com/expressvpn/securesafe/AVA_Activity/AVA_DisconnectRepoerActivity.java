package com.expressvpn.securesafe.AVA_Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_AdInterGD;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_IntentPass;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds.AVA_NativeHelper;
import com.expressvpn.securesafe.R;

public class AVA_DisconnectRepoerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.silentsurfer_disconnect_report);
        initView();
        LoadSmallNative();
    }


    private void LoadSmallNative() {
        if (AVA_IntentPass.isNetworkAvailable(AVA_DisconnectRepoerActivity.this)) {
            try {
                new AVA_NativeHelper().shownativeads_small(this, findViewById(R.id.Admob_Native_Frame_small), findViewById(R.id.native_ad_container),findViewById(R.id.shimmerFrameLayout));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    TextView new_tv_time;
    TextView new_tv_ip;
    TextView new_tv_dw_sp;
    TextView new_tv_up_sp;

    private void initView() {
        ImageView iv_back = findViewById(R.id.iv_back);
        new_tv_time = findViewById(R.id.tv_time);
        new_tv_ip = findViewById(R.id.tv_ip);
        new_tv_dw_sp = findViewById(R.id.tv_dw_sp);
        new_tv_up_sp = findViewById(R.id.tv_up_sp);

        Bundle bundle = getIntent().getExtras();
        String speed = bundle.getString("upload_speed");
        String dw_speed = bundle.getString("donwload_speed");
        String ip_ = bundle.getString("ip");
        String time_ = bundle.getString("time");

        new_tv_up_sp.setText(speed);
        new_tv_dw_sp.setText(dw_speed);
        new_tv_ip.setText(ip_);
        new_tv_time.setText(time_);

        iv_back.setOnClickListener(v -> {

            try {
                onBackPressed();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        try {
            //ad
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            AVA_AdInterGD.getInstance().Show_Inter_Ads(AVA_DisconnectRepoerActivity.this, AVA_MainActivity.class, new Bundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
