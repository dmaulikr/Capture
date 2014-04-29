package com.cis350.argame;

import com.cis350.argame.util.SystemUiHider;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;
import com.parse.SaveCallback;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.webkit.WebChromeClient;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageView profilePic;
    private int PIC_SQUARE_WIDTH = 180;
    private boolean PUSH_ENABLE = true; // set to true for Android testing

    private Uri mImageCaptureUri;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

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

        // Initialize view references
        TextView coinsText = (TextView)findViewById(R.id.coinstext);
        TextView armiesText = (TextView)findViewById(R.id.armiestext);
        TextView nameText = (TextView)findViewById(R.id.playerName);
        ImageView profilePic = (ImageView)findViewById(R.id.profilePicture);

        this.coinsText = coinsText;
        this.armiesText = armiesText;
        this.nameText = nameText;
        this.profilePic = profilePic;

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

        // Set parse notifications to appear for this activity
        if( PUSH_ENABLE) {
            PushService.setDefaultPushCallback(this, GameActivity.class);
            ParseAnalytics.trackAppOpened(getIntent());
        }
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

        //refreshValues();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);

        // Set parse installation.

        if (ParseInstallation.getCurrentInstallation() != null) {
            if (PUSH_ENABLE ) {
                    ParseInstallation installation = ParseInstallation
                            .getCurrentInstallation();
                    ParseManager.enablePush(true);
                    installation.put("user", ParseManager.getCurrentUser());
                    installation.saveInBackground();
                try {
                    installation.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else return;
        }
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

    public void refreshValues() {
        TextView coinsText = (TextView)findViewById(R.id.coinstext);
        TextView armiesText = (TextView)findViewById(R.id.armiestext);
        TextView nameText = (TextView)findViewById(R.id.playerName);
        ImageView profilePic = (ImageView)findViewById(R.id.profilePicture);

        this.coinsText = coinsText;
        this.armiesText = armiesText;
        this.nameText = nameText;
        this.profilePic = profilePic;

        Integer currentCoins = PlayerProfile.getGold();
        Integer currentArmies = PlayerProfile.getArmy();

        coinsText.setText(currentCoins.toString() + "\nCoins"); // Set coins to player amt.
        armiesText.setText(currentArmies.toString() + "\nArmy"); // Same with armies.
        nameText.setText(PlayerProfile.getName()); // Set player name.
    }

    public void onBuyArmiesClick(View v) {
        Log.v("onBuyArmiesClick", "clicked");
        if(PlayerProfile.getGold() >= 10) {
            PlayerProfile.ARMY += 1;
            PlayerProfile.GOLD -= 10;
            Integer currentCoins = PlayerProfile.getGold();
            Integer currentArmies = PlayerProfile.getArmy();

            coinsText.setText("Coins:\n " + currentCoins.toString());
            armiesText.setText("Armies:\n " + currentArmies.toString());
        }
    }

    public void onBuyCoinsClick(View v) {
        Toast.makeText(this, "Buy coins unimplemented!", Toast.LENGTH_LONG).show();
    }

    public void onNameClick(View v) {

        refreshValues();
    }

    public void onProfilePictureClick(View v) {
        final String[] items = new String[] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) { // Pick from camera
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tempAvatar" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { // Pick from a file
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(
                            intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;
            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    mImageCaptureUri = data.getData();
                    profilePic.setImageURI(mImageCaptureUri);
                    Log.v("CROP_FROM_CAMERA", "Picture set");
                    Bitmap imageBMP = profilePic.getDrawingCache();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        imageBMP.compress(Bitmap.CompressFormat.PNG, 10, out);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    ParseFile image = new ParseFile("userImage.png", out.toByteArray());
                    final ParseObject imageObject = ParseObject.create("Photo");
                    imageObject.put("fullSize", image);
                    imageObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            ParseManager.getCurrentUser().put("photo", imageObject);
                        }
                    });
                }
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete(); // Delete the temporary file.
                }
                Toast.makeText(this, "Profile picture set.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Crop the picture passed in by the user.
     */
    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Cannot find image crop app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", PIC_SQUARE_WIDTH);
            intent.putExtra("outputY", PIC_SQUARE_WIDTH);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(
                            res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (mImageCaptureUri != null) {
                            getContentResolver().delete(mImageCaptureUri, null, null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        ParseManager.updateCurrentUserArmy(PlayerProfile.ARMY, PlayerProfile.GOLD);
    }
}
