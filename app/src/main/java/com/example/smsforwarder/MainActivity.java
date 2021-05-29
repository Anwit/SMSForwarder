package com.example.smsforwarder;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smsforwarder.listener.SmsCatchListener;
import com.example.smsforwarder.listener.SmsListener;
import com.example.smsforwarder.utils.Constants;
import com.example.smsforwarder.utils.GMailSender;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements SmsCatchListener {
    private String fromEmailValue, passwordValue, receiverValue, senderSmsOriginValue;
    private Button sendButton;

    private SmsListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        listener = new SmsListener();
        listener.setSMSCatchListener(this);
        getApplicationContext().registerReceiver(listener, filter);

        addListenerOnButton();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(listener);
    }

    @Override
    public void onSmsCatch(String message, String receiver) {
        sendEmail(message, receiver);
    }

    private void sendEmail(String emailBody, String toEmail) {
        String fromEmail = fromEmailValue;
        String fromPassword = passwordValue;
        String emailSubject = "Message from SMS Forwarder";

        try {
            new SendMailTask(MainActivity.this).execute(fromEmail, fromPassword, toEmail, emailSubject, emailBody);
        } catch (Exception ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addListenerOnButton() {
        sendButton = (Button) findViewById(R.id.startListener);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromEmailValue = ((TextView) findViewById(R.id.fromEmailValue)).getText().toString();
                passwordValue = ((TextView) findViewById(R.id.passwordValue)).getText().toString();
                receiverValue = ((TextView) findViewById(R.id.receiverValue)).getText().toString();
                senderSmsOriginValue = ((TextView) findViewById(R.id.senderSmsOriginValue)).getText().toString();

                SharedPreferences sharedPref = getSharedPreferences(Constants.GLOBAL_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constants.senderSmsOriginValue, senderSmsOriginValue);
                editor.putString(Constants.receiverValue, receiverValue);
                editor.putString(Constants.fromEmailValue, fromEmailValue);
                editor.putString(Constants.passwordValue, passwordValue);
                editor.apply();
                sendButton.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Listener is running do not close this app", Toast.LENGTH_LONG).show();
            }
        });
    }
}

