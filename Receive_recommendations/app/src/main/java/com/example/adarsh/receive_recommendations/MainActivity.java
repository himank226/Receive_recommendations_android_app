package com.example.adarsh.receive_recommendations;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;

/**
 * The MainActivity of Receive Recommendations
 */
public class MainActivity extends FragmentActivity implements
        MainActivityFragment.OnLogListener {
    private AppEventsLogger mAppEventsCustomLogger;             //logger events

    public void onNetworkError() {
        mAppEventsCustomLogger.logEvent(Utils.NO_NETWORK_ERROR_EVENT);
    }

    public void onNoFillError() {
        mAppEventsCustomLogger.logEvent(Utils.NO_FILL_ERROR_EVENT);
    }

    public void onAppsScrolled(Bundle parameters) {
        mAppEventsCustomLogger.logEvent(Utils.APPS_SCROLLED_EVENT, parameters);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GetAppsApplication application = (GetAppsApplication) getApplication();
        final AppEventsLogger appEventsLogger = application.getLogger();
        mAppEventsCustomLogger = appEventsLogger;

        setContentView(R.layout.activity_main);


        final TextView adChoices = (TextView) findViewById(R.id.ad_choices);
        final FragmentActivity act = this;
        adChoices.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                appEventsLogger.logEvent(Utils.AD_CHOICES_TAP_EVENT);
                InfoDialog.showWebDialog(act, R.string.ad_choices_url); //here passing the url for playstore of ads
                // used in info_dialog class
                //later passed to customweb View Client
            }
        });
        showNux(act);
    }

    private void showNux(FragmentActivity act) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(Utils.NEW_USER, true)) {
            prefs.edit().putBoolean(Utils.NEW_USER, false).commit();
            InfoDialog.showDialog(act, R.raw.explain, R.string.about_title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_overflow);
        Drawable drawable = getResources().getDrawable(R.drawable.fbui_3_dots_v_l);
        int color = getResources().getColor(R.color.getrecommendations_primary_light);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        menuItem.setIcon(drawable);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) {
            mAppEventsCustomLogger.logEvent(Utils.ABOUT_DIALOG_TAP_EVENT);
            InfoDialog.showDialog(this, R.raw.about, R.string.about_title);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


