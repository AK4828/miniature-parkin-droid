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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.SignageVariables;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.util.AuthenticationUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by miftakhul on 8/19/15.
 */
public class ProductAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Product> products = new ArrayList<Product>();
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private int mutedColor;


    private static LayoutInflater infalter = null;

    public ProductAdapter(Context c) {
        mcontext = c;
        infalter = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        Product product = products.get(position);
        View itemView = infalter.inflate(R.layout.adapter_product_grid, null);
        String loadImage = SignageVariables.SERVER_URL+"/api/products/" + product.getId() + "/image?access_token=" + AuthenticationUtils.getCurrentAuthentication().getAccessToken();


        holder.title = (TextView) itemView.findViewById(R.id.text_name);
        holder.price = (TextView)itemView.findViewById(R.id.text_price);
        holder.imageView = (ImageView) itemView.findViewById(R.id.image);
        holder.detailLayout = (RelativeLayout) itemView.findViewById(R.id.detail_layout);

        holder.title.setText(product.getName());

        holder.price.setText(decimalFormat.format(product.getSellPrice()) + " Pt");

        Glide.with(mcontext).load(loadImage).into(holder.imageView);

        Bitmap bitmap = BitmapFactory.decodeFile(loadImage);

        try {
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                    holder.detailLayout.setBackgroundColor(mutedColor);
                }
            });
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return itemView;
    }

    public void addProducts(Product product) {
        products.add(product);
        notifyDataSetChanged();
    }

    public class Holder {

        TextView title,price;
        ImageView imageView;
        RelativeLayout detailLayout;
    }


}
