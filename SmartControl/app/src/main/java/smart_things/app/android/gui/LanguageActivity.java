package smart_things.app.android.gui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Locale;

import smart_things.app.android.R;

public class LanguageActivity extends AppCompatActivity {

    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
    }

    public void onClickRadio (View view){
        switch (view.getId()){
            case R.id.english:
                setLocal(getString(R.string.en));
                break;
            case R.id.german:
                setLocal(getString(R.string.de));
                break;
            default:
                setLocal(getString(R.string.de));
        }
    }


    // Set info locale and refresh
    public void setLocal(String language) {
        myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
