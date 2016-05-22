package org.komate;

        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.webkit.JsResult;
        import android.webkit.WebChromeClient;
        import android.webkit.WebSettings;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.widget.EditText;
        import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainframe, new Fragment_Flash()).commit();
        }
    }
}