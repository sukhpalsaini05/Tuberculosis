package mdimembrane.tuberculosis.main;

import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AccountHelp extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.help_prefs);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.help_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Preference myPref = (Preference) findPreference("FAQ");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                Toast.makeText(getApplicationContext(),"FAQ",Toast.LENGTH_LONG).show();
                return true;
            }
        });


        Preference myPref1 = (Preference) findPreference("contactUs");
        myPref1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                Intent intent = new Intent(AccountHelp.this,AboutUsActivity.class);
                startActivity(intent);
                return true;
            }
        });


        Preference myPref2 = (Preference) findPreference("termsPrivacy");
        myPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                Toast.makeText(getApplicationContext(),"terms Privacy",Toast.LENGTH_LONG).show();
                return true;
            }
        });


        Preference myPref3 = (Preference) findPreference("appInfo");
        myPref3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                Toast.makeText(getApplicationContext(),"appInfo",Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}
