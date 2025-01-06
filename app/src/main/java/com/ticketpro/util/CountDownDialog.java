package com.ticketpro.util;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ticketpro.parking.R;

public class CountDownDialog extends DialogFragment {
    public interface versioncallback {
        void sendresult(boolean userInput, int which);

    }

    private TextView mCountdownView;
    private versioncallback setdataback;
    private Button btnDevelopment, btnProduction, btnStaging;

    CountDownTimer Timer;

    public CountDownDialog() {

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            setdataback = (versioncallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_countdowntimer_dialog, container);
        mCountdownView = view.findViewById(R.id.countdownTimer);
        btnProduction = view.findViewById(R.id.btn_production);
        btnDevelopment = view.findViewById(R.id.btn_development);
        btnStaging = view.findViewById(R.id.btn_stg);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        btnProduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdataback.sendresult(true, TPConstant.PRODUCTION);
                Timer.cancel();
                dismiss();

            }
        });
        btnStaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setdataback.sendresult(true, TPConstant.STAGING);
                Timer.cancel();
                dismiss();
            }
        });

        btnDevelopment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdataback.sendresult(true, TPConstant.DEVELOPMENT);
                Timer.cancel();
                dismiss();
            }
        });

        Timer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long l) {
                mCountdownView.setText("" + Math.round(l / 1000));

            }

            @Override
            public void onFinish() {
                setdataback.sendresult(true, TPConstant.PRODUCTION);
                try {
                    if(getDialog().isShowing()){
                        dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        };
        Timer.start();


    }


}

