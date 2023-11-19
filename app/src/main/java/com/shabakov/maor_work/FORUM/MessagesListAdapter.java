package com.shabakov.maor_work.FORUM;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shabakov.maor_work.R;

import java.util.Vector;

public class MessagesListAdapter extends BaseAdapter {

    private Context context;
    private static Vector<Message> messagesItems = new Vector<Message>();

    public MessagesListAdapter(Vector<Message> messagesItems) {
        MessagesListAdapter.messagesItems = messagesItems;

    }

    public void add(Message message) {
        messagesItems.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messagesItems.get(position);

        if (!message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_my);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getMessage_content());

        } else if (message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.their_message, null);
            holder.name_their = (TextView) convertView.findViewById(R.id.name_their);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_their);
            holder.name_their.setText(message.getName() + " : " + message.getDate());
            holder.messageBody.setText(message.getMessage_content());
            convertView.setTag(holder);

        }

        return convertView;
    }

    static class MessageViewHolder {
        public TextView name_their;
        public TextView messageBody;
    }
}