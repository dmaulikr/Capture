package com.cis350.argame;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cis350.argame.R;

/**
 * Created by Anton on 3/26/14.
 */
public class BuildingDialogActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_dialog);
        Bundle b = getIntent().getExtras();
        String value = b.getString("key");
        buildingDialogSetArmySize(value);
    }

    public void onCaptureButtonClick(View v) {

    }

    public void buildingDialogSetArmySize(String size) {
        TextView tv1 = (TextView)findViewById(R.id.building_army_size_text);
        tv1.setText(size);
    }
}