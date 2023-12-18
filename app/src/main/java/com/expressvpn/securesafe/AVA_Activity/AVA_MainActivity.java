package com.expressvpn.securesafe.AVA_Activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.expressvpn.securesafe.AVA_Dialog.AVA_LoginDialog;
import com.expressvpn.securesafe.AVA_Dialog.AVA_RegionChooserDialog;
import com.expressvpn.securesafe.AVA_Exit_Dialog;
import com.expressvpn.securesafe.AVA_MainApplication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import unified.vpn.sdk.AuthMethod;
import unified.vpn.sdk.Callback;
import unified.vpn.sdk.CompletableCallback;
import unified.vpn.sdk.Country;
import unified.vpn.sdk.HydraTransport;
import unified.vpn.sdk.HydraVpnTransportException;
import unified.vpn.sdk.NetworkRelatedException;
import unified.vpn.sdk.OpenVpnTransport;
import unified.vpn.sdk.PartnerApiException;
import unified.vpn.sdk.RemainingTraffic;
import unified.vpn.sdk.SessionConfig;
import unified.vpn.sdk.SessionInfo;
import unified.vpn.sdk.TrackingConstants;
import unified.vpn.sdk.TrafficListener;
import unified.vpn.sdk.TrafficRule;
import unified.vpn.sdk.UnifiedSdk;
import unified.vpn.sdk.User;
import unified.vpn.sdk.VpnException;
import unified.vpn.sdk.VpnPermissionDeniedException;
import unified.vpn.sdk.VpnPermissionRevokedException;
import unified.vpn.sdk.VpnState;
import unified.vpn.sdk.VpnStateListener;

public class AVA_MainActivity extends AVA_UIActivity implements TrafficListener, VpnStateListener,
        AVA_LoginDialog.LoginConfirmationInterface, AVA_RegionChooserDialog.RegionChooserInterface {
    private String selectedCountry = "";
    public final static int RECIPE_CHOOSER = 101;


    @Override
    protected void onStart() {
        super.onStart();
        UnifiedSdk.addTrafficListener(this);
        UnifiedSdk.addVpnStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UnifiedSdk.removeVpnStateListener(this);
        UnifiedSdk.removeTrafficListener(this);

    }

    @Override
    public void onTrafficUpdate(long bytesTx, long bytesRx) {
        updateUI();
        updateTrafficStats(bytesTx, bytesRx);
    }


    @Override
    public void vpnStateChanged(VpnState vpnState) {
        updateUI();
    }


    @Override
    public void vpnError(VpnException e) {
        updateUI();
        handleError(e);
    }


    @Override
    protected void isLoggedIn(Callback<Boolean> callback) {
        UnifiedSdk.getInstance().getBackend().isLoggedIn(callback);
    }


    @Override
    protected void loginToVpn() {
        AuthMethod authMethod = AuthMethod.anonymous();
        UnifiedSdk.getInstance().getBackend().login(authMethod, new Callback<User>() {
            @Override
            public void success(User user) {
                updateUI();
            }

            @Override
            public void failure(VpnException e) {
                updateUI();
                handleError(e);
            }
        });
    }

    protected void logOutFromVnp() {
        UnifiedSdk.getInstance().getBackend().logout(new CompletableCallback() {
            @Override
            public void complete() {
                updateUI();
            }

            @Override
            public void error(VpnException e) {
                updateUI();
            }
        });
        selectedCountry = "";
    }

    @Override
    protected void isConnected(Callback<Boolean> callback) {
        UnifiedSdk.getVpnState(new Callback<VpnState>() {
            @Override
            public void success(@NonNull VpnState vpnState) {
                callback.success(vpnState == VpnState.CONNECTED);
            }

            @Override
            public void failure(@NonNull VpnException e) {
                callback.success(false);
            }
        });
    }

    @Override
    protected void connectToVpn() {
        isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                    fallbackOrder.add(OpenVpnTransport.TRANSPORT_ID_TCP);
                    fallbackOrder.add(OpenVpnTransport.TRANSPORT_ID_UDP);
                    List<String> bypassDomains = new LinkedList<>();
                    bypassDomains.add("*domain1.com");
                    bypassDomains.add("*domain2.com");
                    UnifiedSdk.getInstance().getVpn().start(new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .withTransportFallback(fallbackOrder)
                            .withTransport(HydraTransport.TRANSPORT_ID)
                            .withVirtualLocation(selectedCountry)
                            .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                            .build(), new CompletableCallback() {
                        @Override
                        public void complete() {
                            startUIUpdateTask();
                        }

                        @Override
                        public void error(@NonNull VpnException e) {
                            updateUI();
                            handleError(e);
                        }
                    });
                } else {
                    showMessage("Something went wrong, Please restart app...");
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
            }
        });
    }

    @Override
    public void disconnectFromVnp() {
        UnifiedSdk.getInstance().getVpn().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                stopUIUpdateTask(true);
            }

            @Override
            public void error(VpnException e) {
                updateUI();
                handleError(e);
            }
        });
    }

    @Override
    protected void chooseServer() {
        isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    AVA_RegionChooserDialog.newInstance().show(getSupportFragmentManager(), AVA_RegionChooserDialog.TAG);
                } else {
                    showMessage("Something went wrong, Please restart app...");
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }

    @Override
    protected void getCurrentServer(final Callback<String> callback) {
        UnifiedSdk.getVpnState(new Callback<VpnState>() {
            @Override
            public void success(@NonNull VpnState state) {
                if (state == VpnState.CONNECTED) {
                    UnifiedSdk.getStatus(new Callback<SessionInfo>() {
                        @Override
                        public void success(@NonNull SessionInfo sessionInfo) {
                            callback.success(sessionInfo.getCredentials().getFirstServerIp());
                        }

                        @Override
                        public void failure(@NonNull VpnException e) {
                            callback.success(selectedCountry);
                        }
                    });
                } else {
                    callback.success(selectedCountry);
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {
                callback.failure(e);
            }
        });
    }

    @Override
    protected void checkRemainingTraffic() {
        UnifiedSdk.getInstance().getBackend().remainingTraffic(new Callback<RemainingTraffic>() {
            @Override
            public void success(RemainingTraffic remainingTraffic) {
                updateRemainingTraffic(remainingTraffic);
            }

            @Override
            public void failure(VpnException e) {
                updateUI();

                handleError(e);
            }
        });
    }


    @Override
    public void setLoginParams(String hostUrl, String carrierId) {
        ((AVA_MainApplication) getApplication()).setNewHostAndCarrier(hostUrl, carrierId);
    }

    @Override
    public void loginUser() {
        loginToVpn();
    }

    @Override
    public void onRegionSelected(Country item) {
        selectedCountry = item.getCountry();
        updateUI();

        UnifiedSdk.getVpnState(new Callback<VpnState>() {
            @Override
            public void success(@NonNull VpnState state) {
                if (state == VpnState.CONNECTED) {
                    showMessage("Reconnecting to VPN");
                    UnifiedSdk.getInstance().getVpn().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {
                            connectToVpn();
                        }

                        @Override
                        public void error(VpnException e) {
                            selectedCountry = "";
                            connectToVpn();
                        }
                    });
                }
            }

            @Override
            public void failure(@NonNull VpnException e) {

            }
        });
    }


    public void handleError(Throwable e) {
        Log.w(TAG, e);
        if (e instanceof NetworkRelatedException) {
            showMessage("Check internet connection");
        } else if (e instanceof VpnException) {
            if (e instanceof VpnPermissionRevokedException) {
                showMessage("User revoked vpn permissions");
            } else if (e instanceof VpnPermissionDeniedException) {
                showMessage("User canceled to grant vpn permissions");
            } else if (e instanceof HydraVpnTransportException) {
                HydraVpnTransportException hydraVpnTransportException = (HydraVpnTransportException) e;
                if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_ERROR_BROKEN) {
                    showMessage("Connection with vpn server was lost");
                } else if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_DCN_BLOCKED_BW) {
                    showMessage("Client traffic exceeded");
                } else {
                    showMessage("Error in VPN transport");
                }
            } else if (e instanceof PartnerApiException) {
                switch (((PartnerApiException) e).getContent()) {
                    case PartnerApiException.CODE_NOT_AUTHORIZED:
                        showMessage("User unauthorized");
                        break;
                    case PartnerApiException.CODE_TRAFFIC_EXCEED:
                        showMessage("Server unavailable");
                        break;
                    default:
                        showMessage("Other error. Check PartnerApiException constants");
                        break;
                }
            }
        } else {
            showMessage("Error in VPN Service");
        }
    }


    @Override
    public void onBackPressed() {
        try {
            AVA_Exit_Dialog mediaDialog = new AVA_Exit_Dialog(new AVA_Exit_Dialog.ExitListener() {
                @Override
                public void onexit(View view, Dialog dialog) {
                    dialog.dismiss();
                    try {
                        finish();
                        finishAffinity();
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onRate(View view, Dialog dialog) {
                    dialog.dismiss();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }

                @Override
                public void onCancle(View view, Dialog dialog) {
                    dialog.dismiss();
                }

            });
            mediaDialog.show(getSupportFragmentManager(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (AVA_UIActivity.mCountDownTimer != null) {
                AVA_UIActivity.mCountDownTimer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RECIPE_CHOOSER:
                if (resultCode == RECIPE_CHOOSER) {
                    String code = data.getStringExtra("code");
                    String name = data.getStringExtra("name");
                    Country mCountry = new Country(code);
                    onRegionSelected(mCountry);
                }
                break;
        }
    }

}
