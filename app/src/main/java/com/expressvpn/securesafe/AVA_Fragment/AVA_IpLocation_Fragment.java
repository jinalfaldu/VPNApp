package com.expressvpn.securesafe.AVA_Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_AdsApiInterface;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_IntentPass;
import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_NativeAds.AVA_NativeHelper;
import com.expressvpn.securesafe.AVA_IpLocation.AVA_LocationRepose;
import com.expressvpn.securesafe.R;
import com.expressvpn.securesafe.AVA_Retrofit.AVA_ApiClientTwo;
import com.google.gson.Gson;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;

public class AVA_IpLocation_Fragment extends Fragment {

    TextView new_tv_city;
    TextView new_tv_region;
    TextView new_tv_country;
    TextView new_tv_latitude;
    TextView new_tv_Longitude;
    Dialog new_mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.silentsurfer_ip_location_layout, container, false);
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
        new_tv_city = view.findViewById(R.id.tv_city);
        new_tv_region = view.findViewById(R.id.tv_region);
        new_tv_country = view.findViewById(R.id.tv_country);
        new_tv_latitude = view.findViewById(R.id.tv_latitude);
        new_tv_Longitude = view.findViewById(R.id.tv_Longitude);
        try {
            loadDialod();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getLocations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLocations() {

        AVA_AdsApiInterface apiService = AVA_ApiClientTwo.getClientIp().create(AVA_AdsApiInterface.class);
        Call call = apiService.getIPLocation();
        call.enqueue(new retrofit2.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {

                    try {
                        if (statusCode == 200) {
                            String data = new Gson().toJson(response.body());
                            JSONObject jso = new JSONObject(data);
                            Gson mGson = new Gson();
                            AVA_LocationRepose mLocationRepose = mGson.fromJson(jso.toString(), AVA_LocationRepose.class);

                            new_tv_city.setText("" + mLocationRepose.city);
                            new_tv_country.setText("" + mLocationRepose.countryCode);
                            new_tv_region.setText("" + mLocationRepose.regionCode);
                            new_tv_latitude.setText("" + mLocationRepose.latitude);
                            new_tv_Longitude.setText("" + mLocationRepose.longitude);
                            try {
                                if (new_mDialog != null) {
                                    new_mDialog.dismiss();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        try {
                            if (new_mDialog != null) {
                                new_mDialog.dismiss();
                            }
                        } catch (Exception eaaa) {
                            eaaa.printStackTrace();
                        }
                    }

                } else {
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getContext(), "" + t, Toast.LENGTH_LONG).show();
                try {
                    if (new_mDialog != null) {
                        new_mDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void loadDialod() {
        try {
            new_mDialog = new Dialog(getContext());
            new_mDialog.setContentView(R.layout.silentsurfer_dialog_waiting);
            new_mDialog.setCanceledOnTouchOutside(false);
            new_mDialog.setCancelable(false);
            new_mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            new_mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
