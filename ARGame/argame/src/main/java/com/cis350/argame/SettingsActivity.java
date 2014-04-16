package com.cis350.argame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Alan on 4/16/2014.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
    }

    public void onBuyArmiesClick(View v) {
        Toast.makeText(this, "Buy armies unimplemented!", Toast.LENGTH_LONG).show();
    }

    public void onBuyCoinsClick(View v) {
        Toast.makeText(this, "Buy coins unimplemented!", Toast.LENGTH_LONG).show();
    }

    public void onHelpButtonClick(View v) {
        Toast.makeText(this, "Help screen unimplemented!", Toast.LENGTH_LONG).show();
    }
}
