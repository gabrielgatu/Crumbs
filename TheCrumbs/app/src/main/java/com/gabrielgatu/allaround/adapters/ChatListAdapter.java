package com.gabrielgatu.allaround.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.utils.APISystemPreferences;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.gabrielgatu.allaround.utils.Utils;
import com.gabrielgatu.allaround.beans.Message;

import java.util.List;

/**
 * Created by gabrielgatu on 12/05/15.
 */
public class ChatListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages;
    private String userID;

    public ChatListAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;

        userID = StorageManager.getUser(context).getId();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        Message message = messages.get(position);

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (message.getUserId().equals(userID))
            view = inflater.inflate(R.layout.partial_chat_list_message_right, parent, false);
        else
            view = inflater.inflate(R.layout.partial_chat_list_message_left, parent, false);

        holder = new ViewHolder(view);
        view.setTag(holder);

        holder.message.setText(message.getMessage());
        holder.senderUsername.setText(message.getUserName());

        return view;
    }

    class ViewHolder {
        TextView message;
        TextView senderUsername;

        ViewHolder(View root) {
            message = (TextView) root.findViewById(R.id.message);
            senderUsername = (TextView) root.findViewById(R.id.sender_username);
        }
    }
}
