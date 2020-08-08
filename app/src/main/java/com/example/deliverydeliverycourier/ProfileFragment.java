package com.example.deliverydeliverycourier;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    /**********************************************************************************************/

    TextView  logoutButton, updateButton;
    EditText nameEditText,contactEditText;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String uid = "";
    DatabaseReference databaseReferenceTwo;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        this.uid = firebaseUser.getUid();
        nameEditText = getView().findViewById(R.id.nameEditText);
        contactEditText = getView().findViewById(R.id.contactEditText);
        logoutButton = getView().findViewById(R.id.logoutButton);
        updateButton = getView().findViewById(R.id.updateButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateValues();
            }
        });
        setValuesForFields();
    }
    private  void updateValues(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;
        databaseReference = firebaseDatabase.getReference().child("drivers").child(uid);
        final DatabaseReference finalDatabaseReference = databaseReference;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation()){
                    finalDatabaseReference.child("name").setValue(nameEditText.getText().toString());
                    finalDatabaseReference.child("phone").setValue(contactEditText.getText().toString());
                    Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean checkValidation(){
        Boolean flag = true;
        if(nameEditText.getText().toString().trim().isEmpty()){
            flag = false;
            nameEditText.setError("This field is required.");
        }
        if(contactEditText.getText().toString().trim().isEmpty()){
            flag = false;
            contactEditText.setError("This field is required.");
        }
        return flag;
    }
    private void setValuesForFields() {
        databaseReferenceTwo = FirebaseDatabase.getInstance().getReference().child("drivers").child(uid);
        databaseReferenceTwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String flag = dataSnapshot.child("name").getValue().toString();
                nameEditText.setText(flag);
                flag = dataSnapshot.child("phone").getValue().toString();
                contactEditText.setText(flag);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(),LoginActivity.class));
        getActivity().finish();
    }


    /**********************************************************************************************/

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}