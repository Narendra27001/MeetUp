package com.android.meetup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.meetup.Adapter.MessageAdapter;
import com.android.meetup.Model.ChatList;
import com.android.meetup.Model.Chats;
import com.android.meetup.Model.Users;
import com.android.meetup.R;
import com.android.meetup.Utility.Parameters;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView dp;
    TextView username;
    RecyclerView recyclerView;
    ImageButton send;
    EditText typedMessage;
    Intent intent;
    FirebaseUser firebaseUser;
    DatabaseReference myRef;
    String userid;
    int flag=0;
    String interest;
    ArrayList<Chats> mChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar=findViewById(R.id.messageToolbar);
        dp=findViewById(R.id.ivUser);
        username=findViewById(R.id.tvUser);
        recyclerView=findViewById(R.id.messageRecycler);
        send=findViewById(R.id.ivSend);
        typedMessage=findViewById(R.id.etMessage);

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(" ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        intent=getIntent();
        userid=intent.getStringExtra(Parameters.UserId.toString());
        interest=intent.getStringExtra(Parameters.interest.toString());
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        myRef= FirebaseDatabase.getInstance().getReference(Parameters.MyUsers.toString()).child(userid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user=snapshot.getValue(Users.class);
                assert user != null;
                username.setText(user.getUsername());
                if(!user.getImageURL().equals("default"))
                    Picasso.get().load(user.getImageURL()).error(R.drawable.ic_default).into(dp);
                readMessage(firebaseUser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=typedMessage.getText().toString();
                if(!message.equals("")){
                    DatabaseReference myRef=FirebaseDatabase.getInstance().getReference();
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put(Parameters.sender.toString(),firebaseUser.getUid());
                    hashMap.put(Parameters.receiver.toString(),userid);
                    hashMap.put(Parameters.message.toString(),message);
                    myRef.child(Parameters.Chats.toString()).push().setValue(hashMap);
                    typedMessage.setText("");
                    readMessage(firebaseUser.getUid(),userid,"default");
                    final DatabaseReference chatRef = FirebaseDatabase.getInstance()
                            .getReference(Parameters.ChatList.toString()).child(firebaseUser.getUid());
                    chatRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snap:snapshot.getChildren()){
                                ChatList chat=snap.getValue(ChatList.class);
                                if(chat.getId().equals(userid)) flag=1;
                            }
                            if(flag==0){
                                HashMap<String,String> myMap=new HashMap<>();
                                myMap.put(Parameters.id.toString(),userid);
                                myMap.put(Parameters.interest.toString(),interest);
                                chatRef.push().setValue(myMap);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    private void readMessage(String myid,String userid,String imageURL){
        mChats=new ArrayList<>();
        myRef=FirebaseDatabase.getInstance().getReference(Parameters.Chats.toString());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChats.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Chats chats=snap.getValue(Chats.class);
                    if(chats.getReceiver().equals(userid) && chats.getSender().equals(myid)
                            || chats.getReceiver().equals(myid) && chats.getSender().equals(userid)){
                        mChats.add(chats);
                    }
                }
                MessageAdapter messageAdapter =new MessageAdapter(MessageActivity.this,mChats,imageURL);
                recyclerView.setAdapter(messageAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
 }