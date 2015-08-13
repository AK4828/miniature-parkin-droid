package com.hoqii.sales.selfservice.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.adapter.CategoryAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.CategoryDatabaseAdapter;
import com.hoqii.sales.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.hoqii.sales.selfservice.entity.Category;

import org.meruvian.midas.core.defaults.DefaultFragment;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by ludviantoovandi on 01/02/15.
 */
public class CategoryFragment extends DefaultFragment {
    @InjectView(R.id.list_category) ListView categoryList;
    @InjectView(R.id.text_title) TextView title;
    @InjectView(R.id.button_back)
    ImageButton back;

    private CategoryAdapter categoryAdapter;
    private CategoryDatabaseAdapter categoryDbAdapter;
    private ProductDatabaseAdapter productDbAdapter;

    private SharedPreferences preferences;

    private int phase = 1;

    @Override
    protected int layout() {
        return R.layout.fragment_category_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        back.setVisibility(View.GONE);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        categoryDbAdapter = new CategoryDatabaseAdapter(getActivity());
        productDbAdapter = new ProductDatabaseAdapter(getActivity());

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        categoryAdapter = new CategoryAdapter(getActivity(), R.layout.adapter_category, new ArrayList<Category>(), metrics);

        if (getArguments() != null && getArguments().containsKey("parent_category")) {
            categoryAdapter.addAll(categoryDbAdapter.getCategoryMenuByIdParent(getArguments().getString("parent_category", null)));
        } else {
            categoryAdapter.addAll(categoryDbAdapter.getParentCategoryMenu());
        }

        categoryList.setAdapter(categoryAdapter);
    }


    @OnItemClick(R.id.list_category)
    public void onItemClick(int position) {
        categoryAdapter.setItemSelected(position);

        if (getFragmentManager().getBackStackEntryCount() > 5) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        getFragmentManager().popBackStack();

        if (preferences.getBoolean("is_tablet", false)) {
            if (phase == 1) {
                Category parentCategory = categoryAdapter.getItem(position);

                if (!categoryDbAdapter.getCategoryMenuByIdParent(parentCategory.getId()).isEmpty()) {
                    categoryAdapter.clear();
                    categoryAdapter.addAll(categoryDbAdapter.getCategoryMenuByIdParent(parentCategory.getId()));

                    back.setVisibility(View.VISIBLE);
                    title.setText(parentCategory.getName());
                    phase = 2;
                }

                Bundle bundle = new Bundle();
                bundle.putString("parent_category", parentCategory.getId());

                Fragment fragment = new ProductFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();
            } else if (phase == 2) {
                Category category = categoryAdapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putString("category", category.getId());

                Fragment fragment = new ProductFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();
            }
        } else {
            if (getArguments() != null && getArguments().containsKey("parent_category")) {
                Category category = categoryAdapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putString("category", category.getId());

                Fragment fragment = new ProductFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();
            } else {
                Category parentCategory = categoryAdapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putString("parent_category", parentCategory.getId());

                Fragment fragment = null;
                if (!categoryDbAdapter.getCategoryMenuByIdParent(parentCategory.getId()).isEmpty()) {
                    fragment = new CategoryFragment();
                } else {
                    fragment = new ProductFragment();
                }
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, null).addToBackStack(null).commit();
            }
        }
    }

    @OnClick(R.id.button_back)
    public void onClick(ImageButton button) {
        if (button.getId() == R.id.button_back) {
            if (phase == 2) {
                back.setVisibility(View.GONE);

                phase = 1;
                title.setText("Kategori");
                categoryAdapter.clear();

                categoryAdapter.clear();
                categoryAdapter.addAll(categoryDbAdapter.getParentCategoryMenu());
            }
        }
    }
}
