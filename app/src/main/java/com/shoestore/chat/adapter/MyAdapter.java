package com.shoestore.chat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoestore.R;
import com.shoestore.chat.ChatActivity;
import com.shoestore.chat.ChatMain;
import com.shoestore.chat.UserDetails;
import com.shoestore.chat.model.ContactModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Daniel on 28/04/2017.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.AdapterViewHolder>{


    private ArrayList<ContactModel> contactos;
    private int resource;
    private Activity mActivity;

    public MyAdapter(ArrayList<ContactModel> contactos, int resource, Activity mActivity) {
        this.contactos = contactos;
        this.resource = resource;
        this.mActivity = mActivity;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);


        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, final int position) {

        final ContactModel contactosList = contactos.get(position);
        holder.nameText.setText(contactosList.getName());
        holder.state.setText(contactosList.getDesc());
        Picasso.with(mActivity).load(contactosList.getImage()).resize(100,100).into(holder.imageContact);
        if (contactosList.isNew()==false){
            holder.imageView.setVisibility(View.VISIBLE);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mActivity,ChatActivity.class);
                UserDetails.userChat=holder.nameText.getText().toString();
                UserDetails.tokenUserChat =contactosList.getId();
                UserDetails.imageUserChat=contactosList.getImage();
                UserDetails.index =position;
                mActivity.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return contactos.size();
    }

    public class AdapterViewHolder extends  RecyclerView.ViewHolder{

        private CircleImageView imageContact;
        private ImageView imageView;
        private TextView nameText;
        private TextView state;
        private CardView card;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            imageContact = (CircleImageView) itemView.findViewById(R.id.imageContact);
            imageView = (ImageView)itemView.findViewById(R.id.new_message);
            nameText = (TextView) itemView.findViewById(R.id.textName);
            state = (TextView) itemView.findViewById(R.id.textState);
            card = (CardView) itemView.findViewById(R.id.card);

        }
    }
}
