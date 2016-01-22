package com.meruvian.pxc.selfservice.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.adapter.MainFragmentStateAdapter;

/**
 * Created by miftakhul on 11/13/15.
 */
public class MainActivityMaterial extends AppCompatActivity {

    public TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean isMinLoli = false;
    private static final int ORDER_REQUEST = 300;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_material);

        tabLayout = (TabLayout) findViewById(R.id.main_tab);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            isMinLoli = true;
        } else {
            isMinLoli = false;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.main_viewPager);

        MainFragmentStateAdapter viewPagerAdapter = new MainFragmentStateAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void sendResult(Intent intent){
        if (intent != null){
            intent.getExtras().getString("type", null);

            setResult(RESULT_OK, intent);
            finish();
        }
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



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.cart, menu);
//
//        return true;
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home :
                super.onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("result", "on=========================");

        if (requestCode == ORDER_REQUEST) {

            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                intent.putExtra("type", "orderList");

                setResult(RESULT_OK, intent);
                finish();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }


}
