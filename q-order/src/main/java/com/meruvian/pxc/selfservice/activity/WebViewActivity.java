package com.meruvian.pxc.selfservice.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.meruvian.pxc.selfservice.R;

/**
 * Created by akm on 07/01/16.
 */
public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(WebViewActivity.this);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                webView.stopLoading();
            }
        });

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setDatabaseEnabled(false);
        webView.clearSslPreferences();
        webView.clearMatches();
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.loadUrl(getIntent().getStringExtra("url"));

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("URLEE", url);
                String code = Uri.parse(url).getQueryParameter("code");

                if (code != null && !"".equalsIgnoreCase(code)) {
                    Intent resultIntent = new Intent();
                    resultIntent.setData(Uri.parse(url));
                    setResult(RESULT_OK, resultIntent);

                    finish();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode == 404) {
                    view.loadUrl("file:///android_asset/404.html");
                }
            }
        });
    }

//    @Override
//    public void onViewCreated(Bundle bundle) {
//        super.onCreate(bundle);
//
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        progressDialog = new ProgressDialog(WebViewActivity.this);
//        progressDialog.setMessage(getString(R.string.loading));
//        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                webView.stopLoading();
//            }
//        });
//
//        webView = (WebView) findViewById(R.id.webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAppCacheEnabled(false);
//        webView.getSettings().setSaveFormData(false);
//        webView.getSettings().setDatabaseEnabled(false);
//        webView.clearSslPreferences();
//        webView.clearMatches();
//        webView.clearCache(true);
//        webView.clearFormData();
//        webView.clearHistory();
//        webView.loadUrl(getIntent().getStringExtra("url"));
//
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                if (!progressDialog.isShowing())
//                    progressDialog.show();
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                String code = Uri.parse(url).getQueryParameter("code");
//
//                if (code != null && !"".equalsIgnoreCase(code)) {
//                    Intent resultIntent = new Intent();
//                    resultIntent.setData(Uri.parse(url));
//                    setResult(RESULT_OK, resultIntent);
//
//                    finish();
//                }
//
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                if (errorCode == 404) {
//                    view.loadUrl("file:///android_asset/404.html");
//                }
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_webview, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_refresh) {
            webView.loadUrl(getIntent().getStringExtra("url"));
        }

        return super.onOptionsItemSelected(item);
    }
}
