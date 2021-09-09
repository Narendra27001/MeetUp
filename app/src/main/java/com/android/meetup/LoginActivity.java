package com.android.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.lguser);
        password=findViewById(R.id.lgpassword);
        auth=FirebaseAuth.getInstance();
    }
    public void register(View view){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void login(View view){
        String user=username.getText().toString();
        String pass=password.getText().toString();
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass))
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        else{
            auth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                }

            });
        }
    }
}