package com.expressvpn.securesafe.AVA_Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_IntentPass;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds.AVA_NativeHelper;
import com.expressvpn.securesafe.R;

import java.util.Random;

public class AVA_Network_Test_Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.silentsurfer_net_work_test_layout, container, false);
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

    TextView new_tv_delay;
    TextView new_tv_dw_sp;
    TextView new_tv_up_sp;

    private void initView(View view) {
        new_tv_delay = view.findViewById(R.id.tv_delay);
        new_tv_dw_sp = view.findViewById(R.id.tv_dw_sp);
        new_tv_up_sp = view.findViewById(R.id.tv_up_sp);
        Bundle bundle = this.getArguments();
        String speed = bundle.getString("upload_speed");
        String dw_speed = bundle.getString("donwload_speed");
        new_tv_up_sp.setText(speed);
        new_tv_dw_sp.setText(dw_speed);
        new_tv_delay.setText("" + getSingalStrenth());


    }

    public String getSingalStrenth() {
        String[] array = {"29ms", "31ms", "99ms", "45ms", "23ms", "56ms", "43ms", "25ms",};
        String randomStr = array[new Random().nextInt(array.length)];
        return randomStr;
    }
}
