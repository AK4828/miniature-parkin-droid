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

import com.meruvian.pxc.selfservice.R;
import com.meruvian.pxc.selfservice.entity.Product;
import com.meruvian.pxc.selfservice.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by miftakhul on 8/19/15.
 */
public class ProductAdapter extends BaseAdapter {
    private Context mcontext;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<Product> products;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private int mutedColor;


    private static LayoutInflater infalter = null;

    public ProductAdapter(Context c, List<Product> products) {
        mcontext = c;
        infalter = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.products = products;

        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(mcontext));
        }

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
        View itemView = infalter.inflate(R.layout.adapter_product_grid, null);


        holder.title = (TextView) itemView.findViewById(R.id.text_name);
        holder.price = (TextView)itemView.findViewById(R.id.text_price);
        holder.progressImage = (ProgressBar) itemView.findViewById(R.id.progressbar);
        holder.imageView = (ImageView) itemView.findViewById(R.id.image);
        holder.detailLayout = (RelativeLayout) itemView.findViewById(R.id.detail_layout);

        holder.title.setText(products.get(position).getName());

        holder.price.setText(decimalFormat.format(products.get(position).getSellPrice()) + " Pt");

        Log.d("check point", String.valueOf(products.get(position).getSellPrice()));

        imageLoader.displayImage("file://" + ImageUtil.getImagePath(mcontext, products.get(position).getId()), holder.imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressImage.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressImage.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressImage.setVisibility(View.GONE);
                holder.imageView.setImageResource(R.drawable.no_image);
            }
        });

        Log.d("path i   mage",ImageUtil.getImagePath(mcontext, products.get(position).getId()));

        Bitmap bitmap = BitmapFactory.decodeFile(ImageUtil.getImagePath(mcontext, products.get(position).getId()));

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

    public class Holder {

        TextView title,price;
        ProgressBar progressImage;
        ImageView imageView;
        RelativeLayout detailLayout;
    }


}
