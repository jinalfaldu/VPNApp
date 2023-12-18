package com.expressvpn.securesafe.AVA_Dialog;

import static com.expressvpn.securesafe.AVA_MainApplication.STORED_CARRIER_ID_KEY;
import static com.expressvpn.securesafe.AVA_MainApplication.STORED_HOST_URL_KEY;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.expressvpn.securesafe.AVA_Api_Response.AVA_Advertisement.AVA_ADSharedPref;
import com.expressvpn.securesafe.AVA_MainApplication;
import com.expressvpn.securesafe.databinding.SilentsurferDialogLoginBinding;

public class AVA_LoginDialog extends DialogFragment {
    public static final String TAG = AVA_LoginDialog.class.getSimpleName();
    LoginConfirmationInterface loginConfirmationInterface;
    public AVA_LoginDialog() {
    }

    public static AVA_LoginDialog newInstance() {
        AVA_LoginDialog frag = new AVA_LoginDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    SilentsurferDialogLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = SilentsurferDialogLoginBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = ((AVA_MainApplication) getActivity().getApplication()).getPrefs();

        binding.hostUrlEd.setText(prefs.getString(STORED_HOST_URL_KEY, AVA_ADSharedPref.vlink_));
        binding.carrierIdEd.setText(prefs.getString(STORED_CARRIER_ID_KEY, AVA_ADSharedPref.vid_));
        binding.hostUrlEd.requestFocus();
        binding.loginBtn.setOnClickListener(this::onLoginBtnClick);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        if (ctx instanceof LoginConfirmationInterface) {
            loginConfirmationInterface = (LoginConfirmationInterface) ctx;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        loginConfirmationInterface = null;
    }

    public void onLoginBtnClick(View v) {
        String hostUrl = binding.hostUrlEd.getText().toString();
        if (hostUrl.equals("")) hostUrl = AVA_ADSharedPref.vlink_;
        String carrierId = binding.carrierIdEd.getText().toString();
        loginConfirmationInterface.setLoginParams(hostUrl, carrierId);
        loginConfirmationInterface.loginUser();
        dismiss();
    }

    public interface LoginConfirmationInterface {
        void setLoginParams(String hostUrl, String carrierId);
        void loginUser();
    }
}
