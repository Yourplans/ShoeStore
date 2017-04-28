package com.shoestore.chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoestore.R;
import com.shoestore.chat.model.ContactModel;

import java.util.ArrayList;

public class ChatMain extends AppCompatActivity {

    //Instance Firebase Database

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    //Declare Components of Recycler View
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ContactModel> contactModels;
    private LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);





    }
}
