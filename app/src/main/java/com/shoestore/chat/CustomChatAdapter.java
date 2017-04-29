package com.shoestore.chat;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.library.bubbleview.BubbleTextView;
import com.shoestore.R;

import java.util.ArrayList;




/**
 * Created by Daniel on 14/04/2017.
 */
public class CustomChatAdapter extends ArrayAdapter {

    ArrayList<ChatMessages> mArrayList = new ArrayList<>();

    public CustomChatAdapter(Context context, int resource, ArrayList<ChatMessages> objects) {
        super(context, resource, objects);
        mArrayList =objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View mView = convertView;
        LayoutInflater mLayoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(mArrayList.get(position).isType()) {

            mView = mLayoutInflater.inflate(R.layout.item_message_s, null);

        }else {
            mView = mLayoutInflater.inflate(R.layout.item_message_r,null);
        }

        BubbleTextView mBubbleTextView = (BubbleTextView) mView.findViewById(R.id.message_chat);
        mBubbleTextView.setText(mArrayList.get(position).getMessage());

        return mView;
    }
}
