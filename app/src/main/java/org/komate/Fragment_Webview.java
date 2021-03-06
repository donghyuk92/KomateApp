package org.komate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by gellston on 2016-05-21.
 */
public class Fragment_Webview extends Fragment implements View.OnClickListener {

    private String mAddress = "http://komate.org";

    private WebView mWebView;
    private WebSettings mWebSettings;
    private ProgressBar mProgressBar;
    private InputMethodManager mInputMethodManager;

    private Button btn_back;
    private Button btn_front;
    private Button btn_exit;

    private NetworkReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mWebView = (WebView) view.findViewById(R.id.webView);
        btn_back = (Button) view.findViewById(R.id.btn_back);
        btn_front = (Button) view.findViewById(R.id.btn_front);
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        btn_back.setOnClickListener(this);
        btn_front.setOnClickListener(this);
        btn_exit.setOnClickListener(this);

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
        //mWebView.loadUrl(mAddress);

        receiver = new NetworkReceiver(new Handler(), view); // Create the receiver
        getActivity().registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")); // Register receiver

        if(!isConnected()) {
            String customHtml = "<html><body> Network Error! </body></html>";
            mWebView.loadData(customHtml, "text/html", "UTF-8");
        } else {
            mWebView.loadUrl(mAddress);
        }


/*        view.setOnKeyListener( new View.OnKeyListener() {
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
*/
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

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                mWebView.goBack();
                break;
            case R.id.btn_front:
                mWebView.goForward();
                break;
            case R.id.btn_exit:
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mWebView.removeAllViews();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.clearCache(true);
        mWebView.destroy();
    }

    class NetworkReceiver extends BroadcastReceiver {

        private final Handler handler; // Handler used to execute code on the UI thread
        private final View view;

        public NetworkReceiver(Handler handler, View view) {
            this.handler = handler;
            this.view = view;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            // Post the UI updating code to our Handler
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                    if (activeNetwork != null) { // connected to the internet

                        Log.d("TAG", activeNetwork.getTypeName());

                    } else { // not connected to the internet
                        Log.d("TAG", "activeNetwork is null!");
                        String customHtml = "<html><body> Network Error! </body></html>";
                        ((WebView) view.findViewById(R.id.webView)).loadData(customHtml, "text/html", "UTF-8");
                    }
                }
            });
        }
    }
}