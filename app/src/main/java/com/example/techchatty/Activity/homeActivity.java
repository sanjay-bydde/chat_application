package com.example.techchatty.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.techchatty.R;
import com.example.techchatty.ModelClass.Users;
import com.example.techchatty.Adapter.userAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class homeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    userAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView imgLogout,imgSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         auth = FirebaseAuth.getInstance();
         database=FirebaseDatabase.getInstance();

         usersArrayList=new ArrayList<>();

         DatabaseReference reference=database.getReference().child("user");

         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users=dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

        imgLogout=findViewById(R.id.img_logout);
        imgSetting=findViewById(R.id.img_setting);

        mainUserRecyclerView=findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new userAdapter(homeActivity.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(homeActivity.this,R.style.Dialoge);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yesBtn,noBtn;

                yesBtn=dialog.findViewById(R.id.yesBtn);
                noBtn=dialog.findViewById(R.id.noBtn);

                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(homeActivity.this, loginActivity.class));
                    }
                });
                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(homeActivity.this, registration.class));
        }
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homeActivity.this,SettingActivity.class));
            }
        });
    }
}