package com.android.meetup.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.meetup.R;
import com.android.meetup.Utility.Parameters;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    AutoCompleteTextView interest;
    EditText username,password,email;
    FirebaseAuth auth;
    DatabaseReference myRef;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        String[] interests=getResources().getStringArray(R.array.interests);
        ArrayAdapter<CharSequence> adapter= new ArrayAdapter(this,R.layout.list_item,interests);
        interest=findViewById(R.id.actvInterest);
        username=findViewById(R.id.user);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        interest.setAdapter(adapter);
        auth=FirebaseAuth.getInstance();
        flag=0;

    }
    public void registration(View view){
        String user=username.getText().toString();
        String pass=password.getText().toString();
        String mail=email.getText().toString();
        String ins=interest.getText().toString();
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(mail) || TextUtils.isEmpty(ins))
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        else register(user,mail,pass,ins);
    }
    private void register(final String username,String email,String password,String interest){
        if(flag==1){
            Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();
        }
        else {
            flag=1;
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userid = firebaseUser.getUid();
                        myRef = FirebaseDatabase.getInstance().getReference(Parameters.MyUsers.toString()).child(userid);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(Parameters.id.toString(), userid);
                        hashMap.put(Parameters.username.toString(), username);
                        hashMap.put(Parameters.interest.toString(), interest);
                        hashMap.put(Parameters.imageURL.toString(), "default");
                        myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else
                        Toast.makeText(RegisterActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}