package com.cis350.argame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Alan on 4/16/2014.
 */
public class SettingsActivity extends Activity {
    private TextView coinsText;
    private TextView armiesText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        TextView coinsText = (TextView)findViewById(R.id.coinstext);
        TextView armiesText = (TextView)findViewById(R.id.armiestext);

        this.coinsText = coinsText;
        this.armiesText = armiesText;

        Integer currentCoins = PlayerProfile.getGold();
        Integer currentArmies = PlayerProfile.getArmy();

        coinsText.setText("Coins: " + currentCoins.toString());
        armiesText.setText("Armies: " + currentArmies.toString());
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

    public void onProfilePictureClick(View v) {
        Toast.makeText(this, "Picture changing unimplemented!", Toast.LENGTH_LONG).show();
    }
}
