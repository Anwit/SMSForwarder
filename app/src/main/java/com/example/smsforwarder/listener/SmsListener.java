package com.example.smsforwarder.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.smsforwarder.utils.Constants;

import java.util.Map;

public class SmsListener extends BroadcastReceiver {
    private SharedPreferences preferences;

    private SmsCatchListener listener = null;

    public void setSMSCatchListener(Context context) {
        this.listener = (SmsCatchListener) context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            preferences = context.getSharedPreferences(Constants.GLOBAL_PREFERENCES, Context.MODE_PRIVATE);

            Map<String,String> sharedVariables = (Map<String, String>) preferences.getAll();
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        if (msg_from.equals(sharedVariables.get(Constants.senderSmsOriginValue))) {
                            if (listener != null) {
                                listener.onSmsCatch(msgBody, sharedVariables.get(Constants.receiverValue));
                            }
                        }
                    }
                }catch(Exception e){
                    Toast.makeText(context, "Something wentt wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
