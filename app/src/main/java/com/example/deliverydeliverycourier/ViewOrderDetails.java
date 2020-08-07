package com.example.deliverydeliverycourier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewOrderDetails extends AppCompatActivity {
    TextView editTextCategory, editTextFrom, editTextTo, editTextDate, editTextWeight, editTextAmount;
    Button acceptButton,rejectButton;
    TextView textViewStatus;
    DatabaseReference databaseReferenceTwo;
    String key;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details);
        init();
    }
    private void init() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        this.uid = firebaseUser.getUid();
        key = null;

        if (getIntent().hasExtra("key")) {
            key = getIntent().getStringExtra("key");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "key");
        }
        editTextCategory = findViewById(R.id.editTextCategory);
        textViewStatus = findViewById(R.id.textViewStatus);
        editTextFrom = findViewById(R.id.editTextFrom);
        editTextTo = findViewById(R.id.editTextTo);
        editTextDate = findViewById(R.id.editTextDate);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextAmount = findViewById(R.id.editTextAmount);
        acceptButton = findViewById(R.id.acceptButton);
        rejectButton = findViewById(R.id.rejectButton);
        //updating fields
        databaseReferenceTwo = FirebaseDatabase.getInstance().getReference().child("orders").child(key);
        setValuesForFields();

        //writing
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;
        databaseReference = firebaseDatabase.getReference().child("orders").child(key);

        final DatabaseReference finalDatabaseReference = databaseReference;
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finalDatabaseReference.child("status").setValue("Accepted");
                    finalDatabaseReference.child("driver").setValue(uid);
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDatabaseReference.child("status").setValue("Not Accepted");
                finalDatabaseReference.child("driver").setValue("");
            }
        });
    }

    private void setValuesForFields() {
        databaseReferenceTwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("status").getValue().toString().equals("Not Accepted")){
                    acceptButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.GONE);
                }
                else{
                    rejectButton.setVisibility(View.VISIBLE);
                    acceptButton.setVisibility(View.GONE);
                }
                String flag = dataSnapshot.child("category").getValue().toString();
                editTextCategory.setText(flag);
                flag = dataSnapshot.child("pickuplocation").getValue().toString();
                editTextFrom.setText(flag);
                flag = dataSnapshot.child("dropofflocation").getValue().toString();
                editTextTo.setText(flag);
                flag = dataSnapshot.child("date").getValue().toString();
                editTextDate.setText(flag);
                flag = dataSnapshot.child("amount").getValue().toString();
                editTextAmount.setText(flag);
                flag = dataSnapshot.child("weight").getValue().toString();
                editTextWeight.setText(flag);
                flag = dataSnapshot.child("status").getValue().toString();
                textViewStatus.setText(flag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*Query query = databaseReferenceTwo.child(key).child("category");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String flag = snapshot.child("category").getValue().toString();
                        editTextCategory.setText(flag);
                        flag = snapshot.child("pickuplocation").getValue().toString();
                        editTextFrom.setText(flag);
                        flag = snapshot.child("dropofflocation").getValue().toString();
                        editTextTo.setText(flag);
                        flag = snapshot.child("date").getValue().toString();
                        editTextDate.setText(flag);
                        flag = snapshot.child("amount").getValue().toString();
                        editTextAmount.setText(flag);
                        flag = snapshot.child("weight").getValue().toString();
                        editTextWeight.setText(flag);
                        flag = snapshot.child("status").getValue().toString();
                        textViewStatus.setText(flag);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }


}