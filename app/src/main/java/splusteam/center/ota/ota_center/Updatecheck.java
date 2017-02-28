package splusteam.center.ota.ota_center;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Updatecheck extends AppCompatActivity {

    private static WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatecheck);
        browser = (WebView) findViewById(R.id.myWebView);
        String url = "http://otacloudsplus.hol.es/owncloud/index.php/s/2uXIbzZLPdJ5fTm";
        browser.getSettings().getJavaScriptEnabled();
        browser.loadUrl(url);
        browser.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        browser.getSettings().setAppCacheEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.setWebViewClient(new MyWebviewClient());
        WebSettings webSettings = browser.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
    }


    public class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("http://otacloudsplus.hol.es/owncloud/index.php/s/2uXIbzZLPdJ5fTm")) {
                //open url contents in webview
                return false;
            } else {
            }

            return false;
        }
    }
}
