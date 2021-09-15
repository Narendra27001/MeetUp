package com.android.meetup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

public class MessageActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView dp;
    TextView username;
    Intent intent;
    FirebaseUser firebaseUser;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar=findViewById(R.id.messageToolbar);
        dp=findViewById(R.id.ivUser);
        username=findViewById(R.id.tvUser);

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
        String userid=intent.getStringExtra(Parameters.UserId.toString());
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
    }
}