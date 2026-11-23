package com.mohamadalqaaq.alrafiqadmin;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ValueCallback<Uri[]> filePathCallback;
    private final ActivityResultLauncher<Intent> picker = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), res -> {
        if (filePathCallback == null) return;
        Uri[] out = (res.getResultCode() == RESULT_OK && res.getData() != null && res.getData().getData() != null) ? new Uri[]{res.getData().getData()} : null;
        filePathCallback.onReceiveValue(out);
        filePathCallback = null;
    });
    @SuppressLint("SetJavaScriptEnabled")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setDatabaseEnabled(true); s.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> cb, FileChooserParams p) {
                filePathCallback = cb; picker.launch(p.createIntent()); return true;
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
    }
}