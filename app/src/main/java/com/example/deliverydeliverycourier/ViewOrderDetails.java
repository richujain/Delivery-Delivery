package com.example.deliverydeliverycourier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.Manifest.permission.CALL_PHONE;

public class ViewOrderDetails extends AppCompatActivity {
    TextView editTextCategory, editTextFrom, editTextTo, editTextDate, editTextWeight, editTextAmount;
    Button acceptButton,rejectButton;
    TextView textViewStatus;
    DatabaseReference databaseReferenceTwo;
    String key;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String uid = "";
    String customerId;
    DatabaseReference databaseReferenceForPhone;
    ImageView contactButton,messageButton;

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
        contactButton = findViewById(R.id.contactButton);
        messageButton = findViewById(R.id.messageButton);
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
                editTextFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+editTextFrom.getText().toString());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });
                editTextTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+editTextTo.getText().toString());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });
                flag = dataSnapshot.child("date").getValue().toString();
                editTextDate.setText(flag);
                flag = dataSnapshot.child("amount").getValue().toString();
                editTextAmount.setText(flag);
                flag = dataSnapshot.child("weight").getValue().toString();
                editTextWeight.setText(flag);
                flag = dataSnapshot.child("status").getValue().toString();
                textViewStatus.setText(flag);

                customerId = dataSnapshot.child("uid").getValue().toString();
                databaseReferenceForPhone = FirebaseDatabase.getInstance().getReference().child("users").child(customerId);
                databaseReferenceForPhone.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String contact = dataSnapshot.child("phone").getValue().toString();
                        contactButton.setVisibility(View.VISIBLE);
                        contactButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact));
                                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivity(intent);
                                } else {
                                    requestPermissions(new String[]{CALL_PHONE}, 1);
                                }

                            }
                        });
                        messageButton.setVisibility(View.VISIBLE);
                        messageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                smsIntent.setType("vnd.android-dir/mms-sms");
                                smsIntent.putExtra("address", contact);
                                smsIntent.putExtra("sms_body","Hey! I would like to do your delivery.");
                                startActivity(smsIntent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}