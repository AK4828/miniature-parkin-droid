package com.meruvian.pxc.selfservice.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.adapter.CategoryGridAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.CategoryDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 11/16/15.
 */
public class CategoryFragmentGrid extends Fragment {

    private CategoryDatabaseAdapter categoryDatabaseAdapter;
    private ProductDatabaseAdapter productDatabaseAdapter;
    private CategoryGridAdapter categoryGridAdapter;
    private int tx = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            tx = getArguments().getInt("tx_id");
            Log.d("tx_id", Integer.toString(tx));
        }

//        if (getArguments().getBoolean("mainActivityMaterial", false)){
//            tx = 2;
//        }
    }

    @Override
    public View onCreateView(LayoutInflater infater, ViewGroup container, Bundle savedInstanceState) {
        View view = infater.inflate(R.layout.fragment_category_grid, container, false);

        categoryDatabaseAdapter = new CategoryDatabaseAdapter(getActivity());
        productDatabaseAdapter = new ProductDatabaseAdapter(getActivity());

        GridView gridView = (GridView) view.findViewById(R.id.gridCategory);

        categoryGridAdapter = new CategoryGridAdapter(getActivity(), dataCategory());
        gridView.setAdapter(categoryGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!getArguments().getBoolean("mainActivityMaterial", false)){

                    Bundle bundle = new Bundle();
                    bundle.putString("parent_category", dataCategory().get(position).getId());
                    bundle.putInt("tx_id", tx);

                    ProductFragmentGrid productFragment = new ProductFragmentGrid();
                    productFragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.content_frame, productFragment).addToBackStack(null).commit();

                }else {
//                    Intent intent = new Intent();
//                    intent.putExtra("type", "category");
//                    intent.putExtra("parent_category", dataCategory().get(position).getId());
//                    intent.putExtra("tx_id", tx);
//                    ((MainActivityMaterial) getActivity()).sendResult(intent);
                }




            }
        });


        return view;
    }

    private List<Category> dataCategory() {
        List<Category> categories = new ArrayList<Category>();

        if (getArguments() != null && getArguments().containsKey("parent_category")) {
            categories.addAll(categoryDatabaseAdapter.getCategoryMenuByIdParent(getArguments().getString("parent_category", null)));
        } else {
            categories.addAll(categoryDatabaseAdapter.getParentCategoryMenu());
        }
        Log.d("jumlah total", Integer.toString(categories.size()));
        return categories;
    }

}
