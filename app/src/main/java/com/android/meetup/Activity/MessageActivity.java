package com.android.meetup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;

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
        intent=getIntent();
        userid=intent.getStringExtra(Parameters.UserId.toString());
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
                }
            }
        });
    }
 }