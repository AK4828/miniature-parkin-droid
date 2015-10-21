package org.meruvian.esales.collector.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;

import org.meruvian.esales.collector.R;
import org.meruvian.esales.collector.fragment.AgentItemFragment;
import org.meruvian.esales.collector.fragment.AgentListFragment;
import org.meruvian.esales.collector.fragment.AgentSettleFragment;
import org.meruvian.esales.collector.fragment.MainFragment;
import org.meruvian.esales.collector.util.AuthenticationUtils;
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

    @Override
    public Fragment mainFragment() {
        return new MainFragment();
    }

    @Override
    public void navigationAdapter(NavigationDrawerAdapter adapter) {
        adapter.addNavigation(new Navigation("Beranda", Navigation.NavigationType.MENU));
        adapter.addNavigation(new Navigation("Daftar Agen", Navigation.NavigationType.MENU));
        adapter.addNavigation(new Navigation("Penerimaan Barang", Navigation.NavigationType.MENU));
        adapter.addNavigation(new Navigation("Settlement", Navigation.NavigationType.MENU));
    }

    @Override
    public void selectedItem(int position) {
        if (position == 0) {
            Fragment fragment = new MainFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (position == 1) {
            Fragment fragment = new AgentListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (position == 2) {
            Fragment fragment = new AgentItemFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (position == 3) {
            Fragment fragment = new AgentSettleFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
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

        clearBackStack();
        MainActivity.this.finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

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

}
