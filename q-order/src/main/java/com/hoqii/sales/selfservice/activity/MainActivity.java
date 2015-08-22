package com.hoqii.sales.selfservice.activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hoqii.sales.selfservice.fragment.CatalogFragment;
import com.hoqii.sales.selfservice.util.AuthenticationUtils;

import org.meruvian.midas.core.drawer.Navigation;
import org.meruvian.midas.core.drawer.NavigationDrawer;
import org.meruvian.midas.core.drawer.NavigationDrawerAdapter;

/**
 * Created by ludviantoovandi on 05/02/15.
 */
public class MainActivity extends NavigationDrawer {
    private SharedPreferences preferences;
    private Handler handler;
    private Runnable runnable;
    private boolean startCampaignTimer = true;

    @Override
    public Fragment mainFragment() {
        return new CatalogFragment();
    }

    @Override
    public void navigationAdapter(NavigationDrawerAdapter adapter) {
        /*adapter.addNavigation(new Navigation("Campaign", Navigation.NavigationType.MENU));*/
        adapter.addNavigation(new Navigation("Catalog", Navigation.NavigationType.MENU));
        adapter.addNavigation(new Navigation("Contact", Navigation.NavigationType.MENU));
    }

    @Override
    public void selectedItem(int position) {
        if (position == 0) {
//            startActivity(new Intent(this, PromoActivity.class));
            /*finish();
            startActivity(new Intent(this, MainActivity.class));*/

            getFragmentManager().beginTransaction().replace(org.meruvian.midas.core.R.id.content_frame, mainFragment()).commit();

        } else if (position == 1) {
//            finish();
            startActivity(new Intent(this, ContactListActivity.class));
        }

        closeDrawer();
    }

    @Override
    public void onClickPreference() {
        startActivity(new Intent(this, SettingActivity.class));
        closeDrawer();
    }

    @Override
    public void onClickLogout() {
        AuthenticationUtils.logout();
        SharedPreferences.Editor editorHas = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editorHas.putBoolean("has_sync", false);
        editorHas.commit();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.getString("campaign_name", "NONE");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, PromoActivity.class);
                startActivity(intent);

                handler.removeCallbacks(this);
                startCampaignTimer = false;
                Log.d(MainActivity.class.getName(), "Start campaign activity");
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        if (startCampaignTimer) {
            restartTimer();

            Log.d(MainActivity.class.getName(), "User interaction");
        } else {
            startCampaignTimer = true;
        }
    }

    private void restartTimer() {
        Long idle = Long.parseLong(preferences.getString("slide_idle", "0"));

        if (idle > 0) {
            Log.d(MainActivity.class.getName(), "Slide idle: " + idle);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, idle * 60 * 1000);
        }
    }

}
