package com.example.bilgisayar.gykproje.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.bilgisayar.gykproje.activities.MainActivity;
import com.example.bilgisayar.gykproje.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.bilgisayar.gykproje.R;


public class ProfileFragment extends Fragment {
    ArrayList<String> myProfilDataList = new ArrayList<String>();
    String myName;
    String myMail;
    String myPhone;
    ListView myProfilListLv;
    ArrayAdapter<String> arrayAdapter;
    private ProgressDialog progressDialog;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myProfilDataList = getMyProfilData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myProfilView = inflater.inflate(R.layout.fragment_profile, container, false);


        myProfilListLv = (ListView) myProfilView.findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, myProfilDataList);
        myProfilListLv.setAdapter(arrayAdapter);
        return myProfilView;
    }


    private ArrayList<String> getMyProfilData() {
        showProgressDialog();
        final ArrayList<String> myProfilData = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("ProfileData");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    myName = ds.child("UserName").getValue().toString();
                    myMail = ds.child("UserMail").getValue().toString();
                    myPhone = ds.child("UserPhone").getValue().toString();
              /*  for(DataSnapshot ds : dataSnapshot.getChildren()){
                    UserModel uInfo = new UserModel();
                    uInfo.setUsername(ds.child(userID).getValue(UserModel.class).getUsername()); //set the name
                    uInfo.setEmail(ds.child(userID).getValue(UserModel.class).getEmail()); //set the email
                    uInfo.setPhonenumber(ds.child(userID).getValue(UserModel.class).getPhonenumber()); //set the phone_num
*/


                    myProfilData.add(myName);
                    myProfilData.add(myMail);
                    myProfilData.add(myPhone);
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        return myProfilData;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    }

