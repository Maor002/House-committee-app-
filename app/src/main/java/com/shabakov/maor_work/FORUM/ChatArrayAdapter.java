package com.shabakov.maor_work.FORUM;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shabakov.maor_work.FORUM.Message;
import com.shabakov.maor_work.FORUM.MessagesListAdapter;
import com.shabakov.maor_work.R;

import java.util.Vector;

class ChatArrayAdapter extends ArrayAdapter<Message> {

    private TextView chatText;
    public Vector<Message> chatMessageList;
    private Context context;

    @Override
    public void add(Message object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId, Vector<Message> chatMessageList) {
        super(context, textViewResourceId);
        this.context = context;
        this.chatMessageList = chatMessageList;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public Message getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MessagesListAdapter.MessageViewHolder holder = new MessagesListAdapter.MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Message message = chatMessageList.get(position);
        if (!message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_my);
            convertView.setTag(holder);
            holder.name_their = (TextView) convertView.findViewById(R.id.name_their);
            holder.name_their.setText(message.getDate());
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
}