package org.komate;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by gellston on 2016-05-21.
 */
public class Fragment_Webview extends Fragment {
    private String mAddress = "http://komate.org";

    private WebView mWebView;
    private WebSettings mWebSettings;
    private ProgressBar mProgressBar;
    private InputMethodManager mInputMethodManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        mWebView = (WebView) view.findViewById(R.id.webView);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mWebSettings = mWebView.getSettings();
        mWebSettings.setSaveFormData(false);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebChromeClient(new webViewChrome());
        mWebView.setWebViewClient(new webViewClient());
        mWebView.loadUrl(mAddress);

        view.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
                    if(mWebView.canGoBack()){
                        mWebView.goBack();
                    }
                    return true;
                }
                return getActivity().onKeyDown(keyCode, event);
            }
        });

        return view;
    }

    public void GoButton(View v){
        //mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    public class webViewChrome extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if(newProgress < 100) {
                mProgressBar.setProgress(newProgress);
            }else{
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * 자바스크립트 경고(알림)를 표시할지 여부
         */
        @Override
        public boolean onJsAlert(WebView mView, String url, String message, JsResult result) {
            result.confirm();

            return true;
        }

        /**
         * 자바스크립트의 확인 대화상자를 표시할지 여부
         */
        @Override
        public boolean onJsConfirm(WebView view, String url, String msg, JsResult result){
            result.confirm();

            return true;
        }
    }

    public class webViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView mView, String url) {
            mProgressBar.setVisibility(View.VISIBLE);
            mView.loadUrl(url);

            return super.shouldOverrideUrlLoading(mView, url);
        }
    }
}