package com.smartHome.SmartHomeApp.gui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartHome.SmartHomeApp.R;

import org.apache.james.mime4j.field.language.parser.ContentLanguageParser;

/**
 * This Class has tow text views which are clickable. this two text views contain
 * the  connection to the licence Activity and the Impressum Activity.
 * Created by Lia on 12.10.2016.
 */
public class AboutPrefernces extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.aboutprefernces);

        Preference buttonLizenz = (Preference) getPreferenceManager().findPreference("prefLizenz");
        Preference buttonImp = (Preference) getPreferenceManager().findPreference("prefImp");
        Preference buttonLanguage = (Preference) getPreferenceManager().findPreference("prefLanguage");


        buttonLizenz.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getApplicationContext(), LicenceActivity.class);
                startActivity(i);
                return true;
            }
        });
        buttonImp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getApplicationContext(), ImpActivity.class);
                startActivity(i);
                return true;
            }
        });
        buttonLanguage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
               // Intent i = new Intent(getApplicationContext(), LanguageActivity.class);
               // startActivity(i);
                return true;
            }
        });
    }
}
