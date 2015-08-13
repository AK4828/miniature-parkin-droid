package com.hoqii.sales.selfservice.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.hoqii.sales.selfservice.R;

import org.meruvian.midas.core.defaults.DefaultFragment;

import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class CatalogFragment extends DefaultFragment {
    @Optional @InjectView(R.id.container_category) FrameLayout containerCategory;

    private SearchView searchView;

    private SharedPreferences preferences;

//    private int close = 1;

    @Override
    protected int layout() {
        return R.layout.fragment_catalog;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(getClass().getSimpleName(), "Click: Search ");
                Bundle bundle = new Bundle();
                bundle.putString("name", s);

                Fragment fragment = new ProductFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        MenuItem refresh = (MenuItem) menu.findItem(R.id.action_refresh);
        if (preferences.getBoolean("sync", false)) {
            refresh.setVisible(false);
        }
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

    //    @Override
//    public void onBackPressed() {
//        Handler handler = new Handler();
//
//        if (getFragmentManager().getBackStackEntryCount() > 0) {
//            getFragmentManager().popBackStack();
//        } else {
//            if (close == 1) {
//                close++;
//
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        close = 1;
//                    }
//                }, 1500);
//            } else if (close == 2) {
//                Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_LONG).show();
//                close++;
//
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        close = 1;
//                    }
//                }, 1500);
//            } else if (close > 1) {
//                finish();
//            }
//        }
//    }

}
