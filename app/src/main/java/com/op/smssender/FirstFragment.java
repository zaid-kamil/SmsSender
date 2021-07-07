package com.op.smssender;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.op.smssender.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private SmsManager smsManager;
    private SentReceiver receiver;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        receiver = new SentReceiver();
        binding.btnSend.setOnClickListener(view1 -> {
            String phone = binding.editPhone.getText().toString();
            String msg = binding.editMsg.getText().toString();
            if (phone.length() >= 10 && msg.length() >= 2 && msg.length() <= 160) {
                sendSMS(phone, msg);
            } else {
                Toast.makeText(getContext(), "invalid details", Toast.LENGTH_SHORT).show();
            }

        });
        binding.editMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.textCount.setText(charSequence.length()+"/160 chars");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void sendSMS(String phone, String msg) {
        String scAddress = null;
        Intent intent = new Intent("SMS_SENT");
        PendingIntent sentIntent = PendingIntent.getBroadcast(getActivity(),
                98,
                intent,
                0);
        PendingIntent deliveryIntent = null;
        smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, scAddress, msg, sentIntent, deliveryIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(receiver,new IntentFilter("SMS sent"));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
    }

    class SentReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            binding.editMsg.setText("");
            binding.editPhone.setBackgroundColor(Color.green(255));
        }
    }
}