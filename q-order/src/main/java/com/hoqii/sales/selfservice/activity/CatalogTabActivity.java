package com.hoqii.sales.selfservice.activity;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.content.database.adapter.CategoryDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Category;
import com.hoqii.sales.selfservice.fragment.OrderListFragment;
import com.hoqii.sales.selfservice.fragment.ProductFragment;
import com.hoqii.sales.selfservice.fragment.ProductFragmentTab;

import org.meruvian.midas.core.defaults.DefaultActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by ludviantoovandi on 02/02/15.
 */
public class CatalogTabActivity extends DefaultActivity {
    @InjectView(android.R.id.tabhost)
    FragmentTabHost categoryTab;
    @InjectView(R.id.button_back) Button back;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private SearchView searchView;

    private CategoryDatabaseAdapter categoryDbAdapter;

    private int phase = 1;
    private int close = 1;

    @Override
    protected int layout() {
        return R.layout.activity_catalog_tab;
    }

    @Override
    public void onViewCreated(Bundle bundle) {
        setSupportActionBar(toolbar);
        categoryDbAdapter = new CategoryDatabaseAdapter(this);

        categoryTab.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        for (Category category : categoryDbAdapter.getParentCategoryMenu()) {
            TabHost.TabSpec tabSpec = categoryTab.newTabSpec(category.getId());
            tabSpec.setIndicator(category.getName());

            bundle = new Bundle();
            bundle.putString("parent_category", category.getId());

            categoryTab.addTab(tabSpec, ProductFragmentTab.class, bundle);
        }

        categoryTab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if (phase == 1) {
                    categoryTab.clearAllTabs();

                    for (Category category : categoryDbAdapter.getCategoryMenuByIdParent(s)) {
                        TabHost.TabSpec tabSpec = categoryTab.newTabSpec(category.getId());
                        tabSpec.setIndicator(category.getName());

                        Bundle bundle = new Bundle();
                        bundle.putString("category", category.getId());

                        categoryTab.addTab(tabSpec, ProductFragmentTab.class, bundle);
                    }

                    if (categoryTab.getChildCount() > 1) {
                        categoryTab.setCurrentTab(1);
                    }

                    phase = 2;
                    back.setVisibility(View.VISIBLE);
                }
            }
        });
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

                getFragmentManager().beginTransaction().replace(android.R.id.tabcontent, fragment, null).commit();

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

    @OnClick(R.id.button_back)
    public void onClick(Button button) {
        if (button.getId() == R.id.button_back) {
            categoryTab.clearAllTabs();

            for (Category category : categoryDbAdapter.getParentCategoryMenu()) {
                TabHost.TabSpec tabSpec = categoryTab.newTabSpec(category.getId());
                tabSpec.setIndicator(category.getName());

                Bundle bundle = new Bundle();
                bundle.putString("parent_category", category.getId());

                categoryTab.addTab(tabSpec, ProductFragmentTab.class, bundle);
            }

            phase = 1;
            back.setVisibility(View.GONE);
        }
    }
}
