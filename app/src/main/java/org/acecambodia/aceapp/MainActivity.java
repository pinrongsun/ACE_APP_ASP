package org.acecambodia.aceapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

//import com.idp.camtesol.R;
import org.acecambodia.aceapp.R;
//import  org.acecambodia.aceapp.R;
//import org.acecambodia.aceapp.*;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private WebView mWebView;

    private static boolean activityStarted;

    private String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android_id = Secure.getString(this.getBaseContext().getContentResolver(), Secure.ANDROID_ID);

        getSupportActionBar().hide();
        Log.d("FormLoad", "Start Upload");

        if (activityStarted
                && getIntent() != null
                && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) != 0) {
            finish();
            return;
        }

        activityStarted = true;
        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);

        setContentView(R.layout.activity_main);


        final ProgressDialog pd = ProgressDialog.show(MainActivity.this, "", "Wait...", true);

        final boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();
            //we are connected to a network


            //Toast.makeText(this, android_id, Toast.LENGTH_LONG).show();

            mWebView = (WebView) findViewById(R.id.webview1);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //mWebView.loadUrl("http://gooshopping.info");
            Intent myIntent = getIntent(); // gets the previously created intent
            String Redirect_url = "";
            Bundle extras = myIntent.getExtras();
            if (extras != null) {
                if (extras.containsKey("redirect_url")) {
                    Redirect_url = extras.getString("redirect_url");

                    // TODO: Do something with the value of isNew.
                }
            }

            if (Redirect_url == "") {
                mWebView.loadUrl("https://app.acecambodia.org/login.aspx?DeviceID=" + android_id);
            } else {
                mWebView.loadUrl(Redirect_url);
            }

            //mWebView.loadUrl("http://goshares.info");
            mWebView.setWebViewClient(new WebViewClient() {

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    pd.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    pd.dismiss();
                    if (url.contains("upload.aspx")) {
                        Log.d("ACECambodia", "1");
                        Intent intent = new Intent(MainActivity.this, Uploadphoto.class);
                        intent.putExtra("upload_url", url);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(), "hello uploader", Toast.LENGTH_SHORT).show();
                    } else {
                        String webUrl = mWebView.getUrl();
                    }

                }


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("browser=1")) {
                        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browser);
                        return true;
                    } else {
                        view.loadUrl(url);
                        return false;
                    }


                }
            });


        } else {
            //Toast.makeText(this, "No internet!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, NoInternet.class);

            startActivity(intent);
            //System.exit(0);
        }
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String customKey;

            Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG);

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);


            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken)
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            mWebView = (WebView) findViewById(R.id.webview1);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //mWebView.loadUrl("http://gooshopping.info");
            mWebView.loadUrl("https://app.acecambodia.org/login.aspx?DeviceID=" + android_id);

            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            // The following can be used to open an Activity of your choice.

            // Intent intent = new Intent(getApplication(), YourActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //  if you are calling startActivity above.
         /*
            <application ...>
              <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
            </application>
         */
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    private int mod(int x, int y) {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }
}
