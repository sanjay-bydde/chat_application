package com.example.techchatty.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techchatty.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    TextView txt_signup;
    EditText login_username,login_password;
    Button login_btn;
    FirebaseAuth auth;
    String emailPattern="[A-Za-z 0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        txt_signup=findViewById(R.id.txt_signup);
        login_btn=findViewById(R.id.login_btn);
        login_username=findViewById(R.id.login_username);
        login_password=findViewById(R.id.login_password);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=login_username.getText().toString();
                String password=login_password.getText().toString();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(loginActivity.this,"enter valid data",Toast.LENGTH_SHORT).show();
                }
                else if(!email.matches(emailPattern))
                {
                    login_username.setError("invalid email");
                    Toast.makeText(loginActivity.this,"invalid email",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6){
                    login_password.setError("invalid password");
                    Toast.makeText(loginActivity.this,"please enter valid password",Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(loginActivity.this, homeActivity.class));
                            } else {
                                Toast.makeText(loginActivity.this, "error in login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        txt_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(loginActivity.this, registration.class));
            }

        });
    }
}