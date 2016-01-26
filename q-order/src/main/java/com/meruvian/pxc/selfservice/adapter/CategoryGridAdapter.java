package com.meruvian.pxc.selfservice.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.content.database.adapter.ProductDatabaseAdapter;
import com.meruvian.pxc.selfservice.entity.Category;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;
import com.meruvian.pxc.selfservice.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 8/19/15.
 */
public class CategoryGridAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Category> categories = new ArrayList<Category>();
    private List<Product> products = new ArrayList<Product>();
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private int mutedColor;

    private static LayoutInflater infalter = null;

    public CategoryGridAdapter(Context c) {
        mcontext = c;
        infalter = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(mcontext));
        }

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addItem(Category category) {
        categories.add(category);
        Log.d("categories added?", String.valueOf(categories.size()));
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        View itemView = infalter.inflate(R.layout.adapter_category_grid, null);
        Category category = categories.get(position);

        holder.imageView = (ImageView) itemView.findViewById(R.id.category_image);
        holder.title = (TextView) itemView.findViewById(R.id.category_title);
        holder.cardView = (View) itemView.findViewById(R.id.category_view);
//
//        if (products.size() == 0) {
//            holder.imageView.setImageResource(R.drawable.no_image);
//        }else {
//            imageLoader.displayImage(loadImage, holder.imageView);
//        }
//            if (products.size() != 0) {
//                Bitmap bitmap = BitmapFactory.decodeFile(loadImage);
//
//                try{
//                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                        @Override
//                        public void onGenerated(Palette palette) {
//                            mutedColor = palette.getMutedColor(R.attr.colorPrimary);
//                            holder.cardView.setBackgroundColor(mutedColor);
//                        }
//                    });
//                }catch (IllegalArgumentException e) {
//                    Log.e("Bitmap status", e.getMessage());
//                }
//            } else {
//
//            }
        holder.cardView.setBackgroundColor(mcontext.getResources().getColor(R.color.colorPrimary));
        holder.title.setText(category.getName());
        return itemView;

    }

    public class Holder {
        public ImageView imageView;
        public TextView title;
        public View cardView;
    }
}
