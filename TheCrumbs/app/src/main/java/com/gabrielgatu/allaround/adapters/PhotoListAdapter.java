package com.gabrielgatu.allaround.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gabrielgatu.allaround.R;

/**
 * Created by gabrielgatu on 26/05/15.
 */
public class PhotoListAdapter extends BaseAdapter {

    private Context context;
    private Bitmap[] images;

    public PhotoListAdapter(Context context, Bitmap[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.partial_grid_media, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else { holder = (ViewHolder) view.getTag(); }

        holder.image.setImageBitmap(images[position]);

        return view;
    }

    class ViewHolder {
        ImageView image;

        ViewHolder(View root) {
            this.image = (ImageView) root.findViewById(R.id.image);
        }
    }
}