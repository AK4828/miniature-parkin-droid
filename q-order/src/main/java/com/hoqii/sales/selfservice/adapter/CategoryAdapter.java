package com.hoqii.sales.selfservice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.hoqii.sales.selfservice.R;
import com.hoqii.sales.selfservice.entity.Category;
import com.hoqii.sales.selfservice.holder.CategoryHolder;

import org.meruvian.midas.core.defaults.DefaultAdapter;

import java.util.List;

/**
 * Created by ludviantoovandi on 28/01/15.
 */
public class CategoryAdapter extends DefaultAdapter<Category, CategoryHolder> {
    private DisplayMetrics metrics;
    private int selectedPosition = -1;
    private int position;

    public CategoryAdapter(Context context, int layout, List<Category> contents, DisplayMetrics metrics) {
        super(context, layout, contents);

        this.metrics = metrics;
    }

    @Override
    public CategoryHolder ViewHolder(View view) {
        return new CategoryHolder(view);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;

        return super.getView(position, convertView, parent);
    }

    @Override
    public View createdView(View view, CategoryHolder holder, Category category) {
        holder.name.setText(category.getName());
        holder.name.setTag(category);
        holder.name.setBackgroundColor(Color.TRANSPARENT);
        holder.name.setTextColor(Color.BLACK);
        holder.name.setTypeface(null, Typeface.NORMAL);

        if (position == selectedPosition) {
            holder.name.setBackgroundColor(getContext().getResources().getColor(R.color.esales));
            holder.name.setTextColor(Color.WHITE);
            holder.name.setTypeface(null, Typeface.BOLD);

            Animation animation = new TranslateAnimation(metrics.widthPixels / 2, 0, 0, 0);
            animation.setDuration(200);
            view.startAnimation(animation);
            animation = null;
        }

        return view;
    }

    public void setItemSelected(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
