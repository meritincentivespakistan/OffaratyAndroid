package com.riyadhbank.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.riyadhbank.R;
import com.riyadhbank.Utility.GlobalClass;

public class WebviewActivity extends AppCompatActivity {

    ImageView back;
    TextView txtTitle;
    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_home, img_logout;
    TextView txt_home, txt_logout;
    WebView webView;
    String URL;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        FindById();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitle.setText(getResources().getString(R.string.offaraty));

        img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_select));
        txt_home.setTextColor(getResources().getColor(R.color.themeColor));

        clickMenuType();

        URL = getIntent().getStringExtra("URL");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
               /* handler.proceed();
                super.onReceivedSslError(view, handler, error);*/
                final AlertDialog.Builder builder = new AlertDialog.Builder(WebviewActivity.this);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }


            public void onLoadResource(WebView view, String url) {

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("error ", "onReceivedError: "+errorCode+" desc= "+description );
            }


            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void FindById() {

        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        img_home = (ImageView) findViewById(R.id.img_home);
        txt_home = (TextView) findViewById(R.id.txt_home);

        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

    }

    private void clickMenuType() {

        lout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.HOME);
            }
        });

        lout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.PROFILE);
            }
        });

        lout_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.FAVOURITE);
            }
        });

        lout_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.ABOUT_US);
            }
        });

        lout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_logout.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout_select));
                txt_logout.setTextColor(getResources().getColor(R.color.themeColor));
                img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
                txt_home.setTextColor(getResources().getColor(R.color.grey));
                GlobalClass.logOutUser(WebviewActivity.this, GlobalClass.HOME, img_logout, txt_logout, img_home, txt_home, chandLout);
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(WebviewActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

}
