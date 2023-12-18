package com.expressvpn.securesafe;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class AVA_Exit_Dialog extends DialogFragment {
    private Dialog new_dialog;
    private TextView new_iv_exit, new_iv_rate;
    ImageView new_iv_cancle;
    private ExitListener new_exitListener;

    public AVA_Exit_Dialog(ExitListener exitListener) {
        this.new_exitListener = exitListener;
    }

    public AVA_Exit_Dialog() {

    }

    public interface ExitListener {
        void onexit(View view, Dialog dialog);

        void onRate(View view, Dialog dialog);

        void onCancle(View view, Dialog dialog);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        new_dialog = super.onCreateDialog(bundle);
//        new_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        new_dialog.getWindow().setBackgroundDrawableResource(R.color.black_clr);

        new_dialog.setContentView(R.layout.silentsurfer_exit_screen);
        new_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        new_iv_exit = new_dialog.findViewById(R.id.iv_exit);
        new_iv_rate = new_dialog.findViewById(R.id.iv_rate);
        new_iv_cancle = new_dialog.findViewById(R.id.iv_cancle);
        new_iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_exitListener.onexit(new_iv_exit, new_dialog);
            }
        });
        new_iv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_exitListener.onRate(new_iv_rate, new_dialog);
            }
        });
        new_iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_exitListener.onCancle(new_iv_rate, new_dialog);
            }
        });
        return new_dialog;
    }
}
