package com.expressvpn.securesafe.AVA_Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import unified.vpn.sdk.*;
import com.expressvpn.securesafe.AVA_Adapter.AVA_RegionListAdapter;
import com.expressvpn.securesafe.databinding.SilentsurferDialogRegionChooserBinding;

public class AVA_RegionChooserDialog extends DialogFragment implements AVA_RegionListAdapter.RegionListAdapterInterface {

    public static final String TAG = AVA_RegionChooserDialog.class.getSimpleName();

    private AVA_RegionListAdapter regionAdapter;
    private RegionChooserInterface regionChooserInterface;

    public AVA_RegionChooserDialog() {
    }

    SilentsurferDialogRegionChooserBinding binding;
    public static AVA_RegionChooserDialog newInstance() {
        AVA_RegionChooserDialog frag = new AVA_RegionChooserDialog();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SilentsurferDialogRegionChooserBinding.inflate(inflater, container, false);
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

        binding.regionsRecyclerView.setHasFixedSize(true);
        binding.regionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        regionAdapter = new AVA_RegionListAdapter(this);
        binding.regionsRecyclerView.setAdapter(regionAdapter);

        loadServers();
    }

    private void loadServers() {

        showProgress();

        UnifiedSdk.getInstance().getBackend().countries(new Callback<AvailableCountries>() {
            @Override
            public void success(@NonNull final AvailableCountries countries) {
                hideProress();
                regionAdapter.setRegions(countries.getCountries());
            }

            @Override
            public void failure(VpnException e) {
                hideProress();
                dismiss();
            }
        });
    }

    private void showProgress() {
        binding.regionsProgress.setVisibility(View.VISIBLE);
        binding.regionsRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void hideProress() {
        binding.regionsProgress.setVisibility(View.GONE);
        binding.regionsRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCountrySelected(Country item) {
        regionChooserInterface.onRegionSelected(item);
        dismiss();
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        if (ctx instanceof RegionChooserInterface) {
            regionChooserInterface = (RegionChooserInterface) ctx;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        regionChooserInterface = null;
    }

    public interface RegionChooserInterface {
        void onRegionSelected(Country item);
    }
}
