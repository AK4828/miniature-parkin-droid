package com.meruvian.pxc.selfservice.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageAppication;
import com.meruvian.pxc.selfservice.activity.MainActivityMaterial;
import com.meruvian.pxc.selfservice.adapter.CategoryGridAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.CategoryDatabaseAdapter;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Category;
import com.meruvian.pxc.selfservice.entity.MainBody;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.service.CategoryService;
import com.meruvian.pxc.selfservice.service.ProductService;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by miftakhul on 11/16/15.
 */
public class CategoryFragmentGrid extends Fragment {

    private CategoryGridAdapter categoryGridAdapter;
    private int tx = 0;
    private List<Category> categoryList = new ArrayList<Category>();
    private CategoryDatabaseAdapter categoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            tx = getArguments().getInt("tx_id");
            Log.d("tx_id", Integer.toString(tx));
        }

        if (getArguments().getBoolean("mainActivityMaterial", false)){
            tx = 2;
        }
        categoryAdapter = new CategoryDatabaseAdapter(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater infater, ViewGroup container, Bundle savedInstanceState) {
        View view = infater.inflate(R.layout.fragment_category_grid, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridCategory);
        categoryGridAdapter = new CategoryGridAdapter(getActivity());
        gridView.setAdapter(categoryGridAdapter);

        SignageAppication application = SignageAppication.getInstance();
        Map<String, String> param = new HashMap<>();
        param.put("access_token", AuthenticationUtils.getCurrentAuthentication().getAccessToken());
        try {
            CategoryService categoryService = application.getRetrofit().create(CategoryService.class);
            Call<MainBody<Category>> categories = categoryService.getAllCategories(param);
            categories.enqueue(new Callback<MainBody<Category>>() {
                @Override
                public void onResponse(Response<MainBody<Category>> response, Retrofit retrofit) {
                    if (response.isSuccess()) {
                        MainBody<Category> mainBody = response.body();
                        List<Category> pulledCategories = new ArrayList<Category>();
                        for (Category c : mainBody.getContent()) {
                            categoryGridAdapter.addItem(c);
                            categoryList.add(c);

                            Category category = new Category();
                            Category parentCategory = new Category();
                            category.setId(c.getId());
                            if (c.getParentCategory()!=null) {
                                parentCategory.setId(c.getParentCategory().getId());
                            }
                            category.setName(c.getName());
                            category.setParentCategory(parentCategory);
                            pulledCategories.add(category);
                        }
                        categoryAdapter.saveCategory(pulledCategories);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!getArguments().getBoolean("mainActivityMaterial", false)){

                    Bundle bundle = new Bundle();
                    bundle.putString("parent_category", categoryList.get(position).getId());
                    bundle.putInt("tx_id", tx);

                    ProductFragmentGrid productFragment = new ProductFragmentGrid();
                    productFragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.content_frame, productFragment).addToBackStack(null).commit();

                }else {
                    Intent intent = new Intent();
                    intent.putExtra("type", "category");
                    intent.putExtra("parent_category", categoryList.get(position).getId());
                    intent.putExtra("tx_id", tx);
                    ((MainActivityMaterial) getActivity()).sendResult(intent);
                }
            }
        });

        return view;
    }
}
