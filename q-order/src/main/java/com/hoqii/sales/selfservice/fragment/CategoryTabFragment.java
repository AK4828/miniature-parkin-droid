package com.hoqii.sales.selfservice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.content.database.adapter.CategoryDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Category;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by ludviantoovandi on 03/02/15.
 */
public class CategoryTabFragment extends Fragment {
    @InjectView(R.id.tab_category)
    FragmentTabHost tabs;

    private CategoryDatabaseAdapter categoryDbAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_tab, container, false);
        ButterKnife.inject(this, view);

        tabs.setup(getActivity(), getChildFragmentManager(), R.id.container);
        categoryDbAdapter = new CategoryDatabaseAdapter(getActivity());

        for (Category category : categoryDbAdapter.getParentCategoryMenu()) {
            TabHost.TabSpec tabSpec = tabs.newTabSpec(category.getId());
            tabSpec.setIndicator(category.getName());

            Bundle bundle = new Bundle();
            bundle.putString("category", category.getId());

            tabs.addTab(tabSpec, ProductFragmentTab.class, bundle);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tabs = null;
    }
}
