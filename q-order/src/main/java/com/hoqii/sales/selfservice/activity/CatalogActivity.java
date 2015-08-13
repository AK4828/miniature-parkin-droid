package com.hoqii.sales.selfservice.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.fragment.CategoryFragment;
import com.hoqii.sales.selfservice.fragment.MainFragment;
import com.hoqii.sales.selfservice.fragment.OrderListFragment;
import com.hoqii.sales.selfservice.fragment.ProductFragment;

import org.meruvian.midas.core.defaults.DefaultActivity;

import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class CatalogActivity extends DefaultActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @Optional @InjectView(R.id.container_category) FrameLayout containerCategory;

    private SearchView searchView;

    private SharedPreferences preferences;

    private int close = 1;

    @Override
    protected int layout() {
        return R.layout.activity_catalog;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        setSupportActionBar(toolbar);

        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                                }
                            }, 1000);
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (containerCategory != null) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

            transaction.replace(R.id.container_category, new CategoryFragment());
            transaction.replace(R.id.container, new MainFragment());
            transaction.commit();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_tablet", true);
            editor.commit();
        } else {
            transaction.replace(R.id.container, new CategoryFragment());
            transaction.commit();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("is_tablet", false);
            editor.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(getClass().getSimpleName(), "Click: Search ");
                Bundle bundle = new Bundle();
                bundle.putString("name", s);

                Fragment fragment = new ProductFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_order_list) {
            Log.d(getClass().getSimpleName(), "Click: menu_order_list ");
            Fragment fragment = new OrderListFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Handler handler = new Handler();

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (close == 1) {
                close++;

                handler.postDelayed(new Runnable() {
                    public void run() {
                        close = 1;
                    }
                }, 1500);
            } else if (close == 2) {
                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_LONG).show();
                close++;

                handler.postDelayed(new Runnable() {
                    public void run() {
                        close = 1;
                    }
                }, 1500);
            } else if (close > 1) {
                finish();
            }
        }
    }

}
