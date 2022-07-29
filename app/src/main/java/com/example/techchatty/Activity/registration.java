package com.example.techchatty.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techchatty.R;
import com.example.techchatty.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {
    TextView txt_signin;
    Button btn_signup;
    EditText reg_name, reg_username, reg_password, reg_confirmpassword;
    CircleImageView profile_image;
    FirebaseAuth auth;
    String emailPattern="[A-Za-z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri imageUri;
    String imageURI;
    String status="Hey I'm using Tech chatty";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        txt_signin = findViewById(R.id.txt_signin);
        reg_name = findViewById(R.id.reg_name);
        reg_username = findViewById(R.id.reg_username);
        reg_password = findViewById(R.id.reg_password);
        profile_image=findViewById(R.id.profile_image);
        reg_confirmpassword = findViewById(R.id.reg_confirmpassword);
        btn_signup=findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name=reg_name.getText().toString();
                String email=reg_username.getText().toString();
                String password=reg_password.getText().toString();
                String confirmpassword=reg_confirmpassword.getText().toString();


                if(TextUtils.isEmpty(name)|| TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmpassword)){
                    progressDialog.dismiss();
                    Toast.makeText(registration.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    progressDialog.dismiss();
                    reg_username.setError("please enter valid email");
                    Toast.makeText(registration.this,"please enter valid email ",Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(confirmpassword)){
                    progressDialog.dismiss();
                    Toast.makeText(registration.this,"password doesnot match",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<6){
                    progressDialog.dismiss();
                    Toast.makeText(registration.this,"enter 6 character password",Toast.LENGTH_SHORT).show();
                }
                else {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                DatabaseReference reference= database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid()) ;
                                if(imageUri!=null)
                                {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                    startActivity(new Intent(registration.this, loginActivity.class));
                                                                }
                                                                else
                                                                {
                                                                    Toast.makeText(registration.this,"Error in creating user",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }else
                                {
                                    String status="Hey I'm using Tech chatty";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/tech-chatty-e1f8c.appspot.com/o/profile_image.png?alt=media&token=9b9291de-b941-46f4-ae68-9d9fe2690117";
                                    Users users=new Users(auth.getUid(),name,email,imageURI,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(registration.this,loginActivity.class));
                                            }
                                            else
                                            {
                                                Toast.makeText(registration.this,"Error in creating user",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(registration.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select picture"),10);
            }
        });

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(registration.this, loginActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
                imageUri=data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }
}