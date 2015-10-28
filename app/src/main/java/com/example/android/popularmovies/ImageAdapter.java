package com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by John on 27. 9. 2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ArrayList<ImageView> mImageViews;

    public ImageAdapter(Context mContext) {
        this.mContext = mContext;
        mImageViews = new ArrayList<>();
    }

    public ImageAdapter(Context mContext, ArrayList<ImageView> imageViews) {
        this.mContext = mContext;
        this.mImageViews = imageViews;
    }

    @Override
    public int getCount() {

        return mImageViews.size();
    }

    @Override
    public Object getItem(int position) {

        return mImageViews.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return mImageViews.get(position);
    }

    public void setmImageViews(ArrayList<ImageView> mImageViews) {
        this.mImageViews = mImageViews;
    }
}
