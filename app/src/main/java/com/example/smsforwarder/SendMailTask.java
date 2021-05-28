package com.example.smsforwarder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.smsforwarder.utils.GMailSender;


public class SendMailTask extends AsyncTask {

    private ProgressDialog statusDialog;
    private Activity sendMailActivity;

    public SendMailTask(Activity activity) {
        sendMailActivity = activity;

    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity);
        statusDialog.setMessage("Getting ready...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    @Override
    protected Object doInBackground(Object... args) {
        try {
            publishProgress("Sending email....");
            String fromEmail = args[0].toString();
            String password = args[1].toString();
            String toEmail = args[2].toString();
            String emailSubject = args[3].toString();
            String emailBody = args[4].toString();

            GMailSender sender = new GMailSender(fromEmail, password);
            sender.sendMail(emailSubject, emailBody, fromEmail, toEmail);
            publishProgress("Email Sent.");
        } catch (Exception e) {
            publishProgress(e.getMessage());
            Log.e("SendMailTask Failed. ", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());

    }

    @Override
    public void onPostExecute(Object result) {
        statusDialog.dismiss();
    }
}