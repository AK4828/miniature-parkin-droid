package com.hoqii.sales.selfservice.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.fragment.CatalogFragment;
import com.hoqii.sales.selfservice.fragment.HistoryOrderFragment;
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
//    private boolean startCampaignTimer = true;

    @Override
    public Fragment mainFragment() {
        return new CatalogFragment();
//        return new AgentFragment();
    }

    @Override
    public void navigationAdapter(NavigationDrawerAdapter adapter) {
        adapter.addNavigation(new Navigation("Catalog", Navigation.NavigationType.MENU));
        adapter.addNavigation(new Navigation("Contact", Navigation.NavigationType.MENU));
        adapter.addNavigation(new Navigation("History Order", Navigation.NavigationType.MENU));
        adapter.addNavigation(new Navigation("Settlement", Navigation.NavigationType.MENU));

//        adapter.addNavigation(new Navigation("Agents", Navigation.NavigationType.MENU));
    }

    @Override
    public void selectedItem(int position) {
        if (position == 0) {
            Fragment fragment = new CatalogFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (position == 1) {
            startActivity(new Intent(this, ContactListActivity.class));
        } else if (position == 2) {
            Fragment fragment = new HistoryOrderFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (position == 3) {
            startActivity(new Intent(this, SettleAgentActivity.class));
        }

        /*if (position == 0) {
            Fragment fragment = new AgentFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }*/

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

        clearBackStack();
        MainActivity.this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

//        startActivity(new Intent(this, LoginActivity.class));
//        finish();

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });
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
//                startCampaignTimer = false;
                Log.d(MainActivity.class.getName(), "Start campaign activity");
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else if (getFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            finish();
        }
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    /*@Override
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
    }*/

}
