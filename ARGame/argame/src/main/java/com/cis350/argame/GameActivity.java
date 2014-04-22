package com.cis350.argame;

import com.cis350.argame.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
//import org.osmdroid.views.MapController;
//import org.osmdroid.views.MapView;


import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class GameActivity extends Activity {

    private TextView coinsText;
    private TextView armiesText;
    private TextView nameText;

    WebView myWebView;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gameactivity_layout);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        TextView coinsText = (TextView)findViewById(R.id.coinstext);
        TextView armiesText = (TextView)findViewById(R.id.armiestext);
        TextView nameText = (TextView)findViewById(R.id.playerName);

        this.coinsText = coinsText;
        this.armiesText = armiesText;
        this.nameText = nameText;

        Integer currentCoins = PlayerProfile.getGold();
        Integer currentArmies = PlayerProfile.getArmy();

        coinsText.setText(currentCoins.toString() + "\nCoins");
        armiesText.setText(currentArmies.toString() + "\nArmies");
        nameText.setText(PlayerProfile.getName());

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
        showMap();
    }

    //@Override
    protected void showMap() {
       // super.onStart();

        //--------- LEAFLET ----------------//

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.clearCache(true); // clear the cached javascript

        // add web interface
        myWebView.addJavascriptInterface(new WebAppInterface(this, myWebView), "Android");

        WebSettings webSettings = myWebView.getSettings();

        // Permission to get user's geolocation
        webSettings.setGeolocationEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webSettings.setDomStorageEnabled(true);

        // Enable javascript in the html file of WebView
        webSettings.setJavaScriptEnabled(true);

        //----------File Location for loadUrl ------------//
        //use "file:///android_asset/map.html" for device
        //use "http://poroawards.net/Geolocation/map.html" for web or emulator, change hosting later

        myWebView.loadUrl("http://poroawards.net/Geolocation/map.html");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    // Player settings methods

    public void onBuyArmiesClick(View v) {
        //Toast.makeText(this, "Buy armies unimplemented!", Toast.LENGTH_LONG).show();
        if(PlayerProfile.getGold() >= 10) {
            PlayerProfile.ARMY += 1;
            PlayerProfile.GOLD -= 10;
            Integer currentCoins = PlayerProfile.getGold();
            Integer currentArmies = PlayerProfile.getArmy();

            coinsText.setText("Coins: " + currentCoins.toString());
            armiesText.setText("Armies: " + currentArmies.toString());
        }
    }

    public void onBuyCoinsClick(View v) {
        Toast.makeText(this, "Buy coins unimplemented!", Toast.LENGTH_LONG).show();
    }

    public void onProfilePictureClick(View v) {
        Toast.makeText(this, "Picture changing unimplemented!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected  void onStop() {
        super.onStop();

        ParseManager.updateCurrentUserArmy(PlayerProfile.ARMY, PlayerProfile.GOLD);
    }
}
