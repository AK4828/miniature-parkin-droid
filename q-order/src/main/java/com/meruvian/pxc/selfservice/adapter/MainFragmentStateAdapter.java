package com.meruvian.pxc.selfservice.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.meruvian.pxc.selfservice.fragment.CategoryFragmentGrid;
import com.meruvian.pxc.selfservice.fragment.ProductFragmentGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 11/20/15.
 */
public class MainFragmentStateAdapter extends FragmentStatePagerAdapter{
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    public MainFragmentStateAdapter(FragmentManager fm) {
        super(fm);
        Bundle bundle = new Bundle();
        bundle.putBoolean("mainActivityMaterial", true);
        CategoryFragmentGrid categoryFragmentGrid = new CategoryFragmentGrid();
        ProductFragmentGrid productFragmentGrid = new ProductFragmentGrid();

        categoryFragmentGrid.setArguments(bundle);
        productFragmentGrid.setArguments(bundle);

        fragmentList.add(categoryFragmentGrid);
        fragmentList.add(productFragmentGrid);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragmentList.get(0);

            case 1:
                return fragmentList.get(1);
        }

        return null;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position){
        if (position == 0){
            return "KATEGORI";
        }else if (position == 1){
            return "PRODUK";
        }else {
            return null;
        }
    }
}
