package com.gabrielgatu.allaround.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.utils.ImageUtils;

/**
 * Created by gabrielgatu on 27/05/15.
 */
public class FriendsListAdapter extends BaseAdapter {

    private Context context;
    private User[] friends;

    public FriendsListAdapter(Context context, User[] friends) {
        this.context = context;
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return friends.length;
    }

    @Override
    public Object getItem(int i) {
        return friends[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.partial_list_friends, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else { holder = (ViewHolder) view.getTag(); }

        User user = friends[i];

        String imageString = user.getImage();
        Bitmap userImage = null;
        if (imageString != null) {
            userImage = ImageUtils.fromBase64(imageString);
        } else {
            userImage =  BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.user_no_photo);
        }

        holder.image.setImageBitmap(userImage);
        holder.text.setText(user.getUsername());

        return view;
    }

    class ViewHolder {
        ImageView image;
        TextView text;

        ViewHolder(View root) {
            this.image = (ImageView) root.findViewById(R.id.image);
            this.text = (TextView) root.findViewById(R.id.text);
        }
    }
}
