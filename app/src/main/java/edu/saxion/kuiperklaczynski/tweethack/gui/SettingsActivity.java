package edu.saxion.kuiperklaczynski.tweethack.gui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import edu.saxion.kuiperklaczynski.tweethack.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SharedPreferences prefs = this.getSharedPreferences("edu.saxion.kuiperklaczynski.tweethack", this.MODE_PRIVATE);

        //METERED CONNECTION OVERRIDE
        Switch meteredOverride = (Switch) findViewById(R.id.settingMeteredOverrideSwitch);
        meteredOverride.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("meteredOverride", isChecked).apply();
                Toast.makeText(getApplicationContext(), "Metered connection override: "+isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        if(prefs.contains("meteredOverride")) meteredOverride.setChecked(prefs.getBoolean("meteredOverride", true)); //If non-existent, meh, it'll notify the user of change anyway
    }
}
