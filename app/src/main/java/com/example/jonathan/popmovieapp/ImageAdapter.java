package com.example.jonathan.popmovieapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Jonathan on 7/27/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String[] mUrls;

    public ImageAdapter(Context c, String[] arrays) {
        mContext = c;
        mUrls = arrays;
    }

    public int getCount() {
        return mUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        //Log.v("IMAGESGALORE: ", mUrls[position]);
        Picasso.with(mContext).load(mUrls[position]).into(imageView);
        return imageView;
    }
}
