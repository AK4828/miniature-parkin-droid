package com.meruvian.pxc.selfservice.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.fragment.CategoryFragmentGrid;
import com.meruvian.pxc.selfservice.fragment.OrderListFragment;
import com.meruvian.pxc.selfservice.fragment.ProductFragmentGrid;
import com.meruvian.pxc.selfservice.job.RefreshTokenJob;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.path.android.jobqueue.JobManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by miftakhul on 11/13/15.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean isMinLoli = false;
    private static final int ORDER_REQUEST = 300;
    private static final int ORDER_REQUEST_OPTIONS = 301;
    private JobManager jobManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jobManager = SignageAppication.getInstance().getJobManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isMinLoli = true;
        } else {
            isMinLoli = false;
        }
        if (isAccess()) {
            Log.d("Granted", "refresh ok");
        } else {
            Log.d("HMMMMMMMMMM", "refreshing...");
            jobManager.addJobInBackground(new RefreshTokenJob());
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);


        setNav();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        CategoryFragmentGrid categoryFragmentGrid = new CategoryFragmentGrid();

        Bundle bundleHome = new Bundle();
        bundleHome.putInt("tx_id", 2);
        categoryFragmentGrid.setArguments(bundleHome);

        FragmentTransaction categoryGrid = getSupportFragmentManager().beginTransaction();
        categoryGrid.replace(R.id.content_frame, categoryFragmentGrid);
        categoryGrid.addToBackStack(null);
        categoryGrid.commit();


    }

    private void setNav() {
        TextView currentUser = (TextView)findViewById(R.id.username);
        currentUser.setText(AuthenticationUtils.getCurrentAuthentication().getSite().getName());
        NavigationView navi = (NavigationView) findViewById(R.id.nav_view);
        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:

                        CategoryFragmentGrid categoryFragmentGrid = new CategoryFragmentGrid();

                        Bundle bundleHome = new Bundle();
                        bundleHome.putInt("tx_id", 2);
                        categoryFragmentGrid.setArguments(bundleHome);

                        FragmentTransaction categoryGrid = getSupportFragmentManager().beginTransaction();
                        categoryGrid.replace(R.id.content_frame, categoryFragmentGrid);
                        categoryGrid.addToBackStack(null);
                        categoryGrid.commit();
                        break;

                    case R.id.nav_order_purches:
                        CategoryFragmentGrid category_purches = new CategoryFragmentGrid();

                        Bundle bundle = new Bundle();
                        bundle.putInt("tx_id", 2);
                        category_purches.setArguments(bundle);

                        FragmentTransaction transaction_purchse = getSupportFragmentManager().beginTransaction();
                        transaction_purchse.replace(R.id.content_frame, category_purches);
                        transaction_purchse.addToBackStack(null);
                        transaction_purchse.commit();

                        break;

                    case R.id.seller_order_list :
                        Intent orderIntent = new Intent(MainActivity.this, SellerOrderListActivity.class);
                        orderIntent.putExtra("orderListType", "orderList");
                        startActivity(orderIntent);
                        break;

                    case R.id.nav_point :
                        Intent pointIntent = new Intent(MainActivity.this, RewardActivity.class);
                        startActivity(pointIntent);
                        break;

                    case R.id.nav_logout:
                        AuthenticationUtils.logout();
                        SharedPreferences.Editor editorHas = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editorHas.putBoolean("has_sync_point", false);
                        editorHas.commit();

                        clearBackStack();
                        MainActivity.this.finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

    }

    public void order(Intent intent, View startView){
        if (isMinLoli) {
            String transitionName = getString(R.string.transition_string);

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, startView, transitionName);

            startActivityForResult(intent, ORDER_REQUEST, optionsCompat.toBundle());
            Log.d("order", " loli========================================");
        } else {
            startActivityForResult(intent, ORDER_REQUEST);
            Log.d("order", " not loli========================================");
        }
        Log.d("order", "========================================");
    }

    public void orderOption(){
        startActivityForResult(new Intent(this, MainActivityMaterial.class), ORDER_REQUEST_OPTIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("result", "on=========================");

        if (requestCode == ORDER_REQUEST) {

            if (resultCode == RESULT_OK) {
                OrderListFragment orderFragment = new OrderListFragment();
                FragmentTransaction orderList = getSupportFragmentManager().beginTransaction();
                orderList.replace(R.id.content_frame, orderFragment);
                orderList.addToBackStack(null);
                orderList.commitAllowingStateLoss();
                Log.d("result","ok =========================");
            }
        }else if (requestCode == ORDER_REQUEST_OPTIONS){
            if (resultCode == RESULT_OK){
                Log.d("result","ok orderOptions =========================");

                if (data != null){

                    String type = data.getExtras().getString("type",null);
                    Log.d("result type", type);

                    if (type.equalsIgnoreCase("orderList")){

                        OrderListFragment orderFragment = new OrderListFragment();
                        FragmentTransaction orderList = getSupportFragmentManager().beginTransaction();
                        orderList.replace(R.id.content_frame, orderFragment);
                        orderList.addToBackStack(null);
                        orderList.commitAllowingStateLoss();

                    }else if (type.equalsIgnoreCase("category")){


                        Bundle bundle = new Bundle();
                        bundle.putString("parent_category", data.getExtras().getString("parent_category", null));
                        bundle.putInt("tx_id", data.getExtras().getInt("tx_id"));

                        ProductFragmentGrid productFragment = new ProductFragmentGrid();
                        productFragment.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, productFragment).addToBackStack(null).commitAllowingStateLoss();
                    }

                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearBackStack() {
        final FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    public boolean isAccess() {
        boolean access = false;
        long expiresIn = AuthenticationUtils.getCurrentAuthentication().getExpiresIn();
        long loginTime = AuthenticationUtils.getCurrentAuthentication().getLoginTime();
        long curentTime = System.currentTimeMillis();
        long realDuration = curentTime - loginTime;
        long realDurationInSecon = TimeUnit.MILLISECONDS.toSeconds(realDuration);

        if (expiresIn > realDurationInSecon){
            access = true;
            Log.d(getClass().getSimpleName(), "access status : granted");
        }else {
            access = false;
        }
        return access;
    }

}
