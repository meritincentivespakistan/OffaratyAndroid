package com.riyadhbank.Async;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.CustomDialog;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class VerifyOtpService extends AsyncTask<String, String, String> {

    CustomDialog customDialog;
    Activity activity;
    String Result;
    Interface.OnVerifyOTP onVerifyOTP;
    String mobile, OTP;

    public VerifyOtpService(Activity activity, Interface.OnVerifyOTP onVerifyOTP, String mobile, String OTP) {
        this.activity = activity;
        this.onVerifyOTP = onVerifyOTP;
        this.mobile = mobile;
        this.OTP = OTP;

        Log.e("AA_S_V",mobile + " -- " + OTP);

    }

    @Override
    protected void onPreExecute() {
        customDialog = new CustomDialog(activity);
        customDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            Form form = new Form()
                    .add(Constants.action, Constants.otpverify)
                    .add(Constants.phone, mobile)
                    .add(Constants.otp, OTP);

            Request request = Bridge
                    .post(Constants.URL + "/" + Constants.DashBoardProcess)
                    .body(form)
                    .request();

            Response response = request.response();

            if (response.isSuccess()) {
                Result = response.asString();
            } else {
                Result = "Error";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Result = "Error";
        }

        return Result;

    }

    @Override
    protected void onPostExecute(String s) {

        customDialog.dismiss();

        try {
            if (!s.equals("Error")) {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("status") == Constants.Success) {
                    onVerifyOTP.onVerifyOTP(jsonObject);
                } else {
                    Toast.makeText(activity, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

}
