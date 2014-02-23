package com.cis350.argame;

import com.cis350.argame.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LaunchScreenActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchscreen_layout);
    }

    public void onRegisterButtonClick(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    public void onQuitButtonClick(View v) {
        finish();
    }
}
