package com.riyadhbank.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.LoginService;
import com.riyadhbank.R;
import com.riyadhbank.Retrofit.ApiRequestHelper;
import com.riyadhbank.Retrofit.App;
import com.riyadhbank.Retrofit.GeneralModel;
import com.riyadhbank.Retrofit.LoginModel;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.CustomDialog;
import com.riyadhbank.Utility.Interface;
import com.riyadhbank.Utility.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements Interface.OnLogin {

    RelativeLayout mobileLout, emailLout, empIdLout;
    ImageView mobile, email, empId, imgEng, imgAr;
    TextView btnLogin, btnSignUp, btnForgotPass;
    EditText edtUsername, edtPassword;
    String Username, Password, Logintype, InvalidUserMsg;
    LinearLayout engLout, arLout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FindById();
        app = (App) getApplication();

        SharedPreferences shared = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = shared.getString(Constants.LanguageCode, "en");

        if (GlobalClass.languageCode.equals("en")) {
            edtUsername.setGravity(Gravity.LEFT);
            edtPassword.setGravity(Gravity.LEFT);
            imgAr.setBackground(getResources().getDrawable(R.drawable.radio));
            imgEng.setBackground(getResources().getDrawable(R.drawable.radio_select));
        } else {
            edtUsername.setGravity(Gravity.RIGHT);
            edtPassword.setGravity(Gravity.RIGHT);
            imgAr.setBackground(getResources().getDrawable(R.drawable.radio_select));
            imgEng.setBackground(getResources().getDrawable(R.drawable.radio));
        }

        loginType("0");

        mobileLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType("0");
            }
        });

        emailLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType("1");
            }
        });

        empIdLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType("2");
            }
        });

        engLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLangType("0");
            }
        });

        arLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLangType("1");

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Username = edtUsername.getText().toString().trim();
                Password = edtPassword.getText().toString().trim();

                if (Username.equals("")) {

                    if (Logintype.equals("phonenumber")) {

                        edtUsername.setError(getResources().getString(R.string.entermobile));


                    } else if (Logintype.equals("email")) {

                        edtUsername.setError(getResources().getString(R.string.enteremail));

                    }

                } else if (Password.equals("")) {
                    edtPassword.setError(getResources().getString(R.string.enterpassword));
                } else {
                    if (GlobalClass.isNetworkConnected(LoginActivity.this)) {
                        login();
                        //new LoginService(LoginActivity.this, LoginActivity.this, Username, Password, Logintype).execute();
                    } else {
                        Toast.makeText(LoginActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
            }
        });


    }
    App app;

    void login() {
        final CustomDialog customDialog = new CustomDialog(LoginActivity.this);
        if (GlobalClass.isNetworkConnected(LoginActivity.this)) {
            customDialog.show();

            Map<String, String> params = new HashMap<>();

            params.put(Constants.action, Constants.login);
            params.put(Constants.email, Username);
            params.put(Constants.loginwith, Logintype);
            params.put(Constants.password, Password);
            params.put(Constants.notificationtoken, "11111");
            params.put(Constants.udid, "125");
            params.put(Constants.appid, GlobalClass.Riyadh);


            app.getApiRequestHelper ().login (params, new ApiRequestHelper.OnRequestComplete () {
                @Override
                public void onSuccess(Object object) {
                    customDialog.dismiss();
                    LoginModel loginModel= (LoginModel) object;
                    Log.e ("in", "success");
                    if (loginModel != null) {
                        Toast.makeText (LoginActivity.this, loginModel.getMsg(),Toast.LENGTH_LONG).show();


                        SharedPreferences.Editor editor = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE).edit();
                        editor.putString(Constants.userid, loginModel.getUserdetails().getUserid());
                        editor.putString(Constants.email, loginModel.getUserdetails().getEmail());
                        editor.putString(Constants.firstname, loginModel.getUserdetails().getFirstname());
                        editor.putString(Constants.lastname, loginModel.getUserdetails().getLastname());
                        editor.putString(Constants.displayname, loginModel.getUserdetails().getDisplayname());
                        editor.putString(Constants.phonenumber, loginModel.getUserdetails().getPhonenumber());
                        editor.putString(Constants.employeeid, loginModel.getUserdetails().getEmployeeid());
                        editor.putString(Constants.notificationtoken, loginModel.getUserdetails().getNotificationtoken());
                        editor.putString(Constants.password,loginModel.getUserdetails().getPassword());
                        editor.putString(Constants.udid, loginModel.getUserdetails().getUdid());
                        editor.putString(Constants.appid, loginModel.getUserdetails().getAppid());
                        editor.putString(Constants.lat, loginModel.getUserdetails().getLat());
                        editor.putString(Constants.longi, loginModel.getUserdetails().getLong());
                        editor.putString(Constants.userrole, loginModel.getUserdetails().getUserrole());
                        editor.apply();

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("isFrom", GlobalClass.HOME);
                        startActivity(i);
                        finish();


                    } else {
                        Toast.makeText (LoginActivity.this, "UNPROPER_RESPONSE",Toast.LENGTH_LONG).show();
                    }
                    // dismisProgressHUD ();
                }

                @Override
                public void onFailure(String apiResponse) {
                    //  dismisProgressHUD ();
                    customDialog.dismiss();
                    Log.e ("in", "error " + apiResponse);
                    Toast.makeText (LoginActivity.this, apiResponse,Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(LoginActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLogin(JSONObject jsonObject) {

        try {

            JSONObject userObject = new JSONObject(jsonObject.get("userdetails").toString());

            SharedPreferences.Editor editor = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE).edit();
            editor.putString(Constants.userid, userObject.get(Constants.userid).toString());
            editor.putString(Constants.email, userObject.get(Constants.email).toString());
            editor.putString(Constants.firstname, userObject.get(Constants.firstname).toString());
            editor.putString(Constants.lastname, userObject.get(Constants.lastname).toString());
            editor.putString(Constants.displayname, userObject.get(Constants.displayname).toString());
            editor.putString(Constants.phonenumber, userObject.get(Constants.phonenumber).toString());
            editor.putString(Constants.employeeid, userObject.get(Constants.employeeid).toString());
            editor.putString(Constants.notificationtoken, userObject.get(Constants.notificationtoken).toString());
            editor.putString(Constants.password, userObject.get(Constants.password).toString());
            editor.putString(Constants.udid, userObject.get(Constants.udid).toString());
            editor.putString(Constants.appid, userObject.get(Constants.appid).toString());
            editor.putString(Constants.lat, userObject.get(Constants.lat).toString());
            editor.putString(Constants.longi, userObject.get(Constants.longi).toString());
            editor.putString(Constants.userrole, userObject.get(Constants.userrole).toString());
            editor.apply();

            Toast.makeText(this, (jsonObject.get("msg").toString()), Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("isFrom", GlobalClass.HOME);
            startActivity(i);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void changeLangType(String LangType) {

        if (LangType.equals("0")) {
            GlobalClass.languageCode = "en";
            GlobalClass.changeLanguage(LoginActivity.this, "en");
            imgAr.setBackground(getResources().getDrawable(R.drawable.radio));
            imgEng.setBackground(getResources().getDrawable(R.drawable.radio_select));
        } else {
            GlobalClass.languageCode = "ar";
            GlobalClass.changeLanguage(LoginActivity.this, "ar");
            imgAr.setBackground(getResources().getDrawable(R.drawable.radio_select));
            imgEng.setBackground(getResources().getDrawable(R.drawable.radio));
        }

        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    //region User select login type
    private void loginType(String type) {

        edtUsername.setText("");

        mobileLout.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_circle_border));
        emailLout.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_circle_border));
        empIdLout.setBackgroundDrawable(getResources().getDrawable(R.drawable.black_circle_border));

        mobile.setImageDrawable(getResources().getDrawable(R.drawable.call_unselect));
        email.setImageDrawable(getResources().getDrawable(R.drawable.email_unselect));
        empId.setImageDrawable(getResources().getDrawable(R.drawable.contact_unselect));

        if (type.equals("0")) {
            mobileLout.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_circle_border));
            mobile.setImageDrawable(getResources().getDrawable(R.drawable.call_select));
            edtUsername.setHint(getResources().getString(R.string.mobile_number));
            InvalidUserMsg = getResources().getString(R.string.check_empty);
            Logintype = "phonenumber";
            edtUsername.setError(null);
            edtUsername.clearFocus();
        } else if (type.equals("1")) {
            emailLout.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_circle_border));
            email.setImageDrawable(getResources().getDrawable(R.drawable.email_select));
            edtUsername.setHint(getResources().getString(R.string.email));
            InvalidUserMsg = getResources().getString(R.string.check_empty);
            Logintype = "email";
            edtUsername.setError(null);
            edtUsername.clearFocus();
        } else if (type.equals("2")) {
            empIdLout.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_circle_border));
            empId.setImageDrawable(getResources().getDrawable(R.drawable.contact_select));
            edtUsername.setHint(getResources().getString(R.string.employee_id));
            InvalidUserMsg = getResources().getString(R.string.check_empty);
            Logintype = "employeeid";
        }

    }
    //endregion

    //region Intialize Ids

    private void FindById() {

        mobileLout = (RelativeLayout) findViewById(R.id.phoneLout);
        emailLout = (RelativeLayout) findViewById(R.id.msgLout);
        empIdLout = (RelativeLayout) findViewById(R.id.peopleLout);

        mobile = (ImageView) findViewById(R.id.mobile);
        email = (ImageView) findViewById(R.id.email);
        empId = (ImageView) findViewById(R.id.empId);
        imgEng = (ImageView) findViewById(R.id.imgEng);
        imgAr = (ImageView) findViewById(R.id.imgAr);

        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        btnForgotPass = (TextView) findViewById(R.id.btnForgotPass);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        engLout = (LinearLayout) findViewById(R.id.engLout);
        arLout = (LinearLayout) findViewById(R.id.arLout);

    }

    //endregion

}
