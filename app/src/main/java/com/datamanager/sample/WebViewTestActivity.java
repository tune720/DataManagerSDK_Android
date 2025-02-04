package com.datamanager.sample;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.enliple.datamanagersdk.ENDataManager;
import com.enliple.datamanagersdk.Utils.LogPrint;
import com.enliple.datamanagersdk.manager.CommonUtils;

public class WebViewTestActivity extends AppCompatActivity {

    private WebView newWebView;

    WebView mWebView;
    EditText editTextUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_test);

        mWebView = findViewById(R.id.webView);
        editTextUrl = findViewById(R.id.editTextUrl);
        editTextUrl.setText("{테스트를 위한 웹사이트 URL}");

        findViewById(R.id.buttonLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                loadWebView();
            }
        });

        setUpWebView();
        loadWebView();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextUrl.getWindowToken(), 0);
    }


    private void setUpWebView() {
        // 디버깅 모드 설정
        WebView.setWebContentsDebuggingEnabled(true);


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(false);

        // 웹뷰 - HTML5 창 속성 추가
        String path = getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(path);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkLoads(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// https 이미지.
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportMultipleWindows(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

                newWebView = new WebView(WebViewTestActivity.this);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);

                        browserIntent.setData(Uri.parse(url));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        try {
                            startActivity(browserIntent);
                        } catch (ActivityNotFoundException e) {
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!   url : " + url);
                            startActivity(Intent.createChooser(browserIntent, "dialogTitle"));
                        }


                        return true;
                    }
                });
                return true;
            }

            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (!url.startsWith("http")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                updateSessionID();
                ENDataManager.getInstance().onWebViewPageStarted(view, url);

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }


    private void loadWebView() {
        String url = editTextUrl.getText().toString();

        ENDataManager.getInstance().setWebView(mWebView, url);
        mWebView.loadUrl(url);
    }

    private void updateSessionID() {
        try {
            String message = "Session ID : " + CommonUtils.getSessionID(this) + "\nADID : " + CommonUtils.getADID(this);
            ((TextView)findViewById(R.id.textViewInfo)).setText(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
