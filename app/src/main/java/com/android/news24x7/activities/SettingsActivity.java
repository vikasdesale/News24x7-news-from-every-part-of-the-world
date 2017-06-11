package com.android.news24x7.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.news24x7.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 6/7/2017.
 */
public class SettingsActivity extends AppCompatActivity
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_SWITCH_PREFERENCE = "enable_notifications";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.pref_content, new SettingsPreferenceFragment()).commit();


    }


    // Registers a shared preference change listener that gets notified when preferences change
    @Override
    protected void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    // Unregisters a shared preference change listener
    @Override
    protected void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        // For other preferences, set the summary to the value's simple string representation.
        preference.setSummary(stringValue);


    }

    // This gets called before the preference is changed
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        setPreferenceSummary(preference, value);
        return true;
    }

    // This gets called after the preference is changed, which is important because we
    // start our synchronization here
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }


    public static class SettingsPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_general);
            SwitchPreference switchPreference;
            PreferenceManager preferenceManager = getPreferenceManager();
            switchPreference = (SwitchPreference) preferenceManager.findPreference(KEY_SWITCH_PREFERENCE);
            if (preferenceManager.getSharedPreferences().getBoolean(KEY_SWITCH_PREFERENCE, true)) {
                switchPreference.setChecked(true);
            } else {
                switchPreference.setChecked(false);
            }
        }
    }
}
