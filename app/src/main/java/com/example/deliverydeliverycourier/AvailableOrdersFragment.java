package com.example.deliverydeliverycourier;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AvailableOrdersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    /***********************************************************************************************/
    RecyclerView recyclerView;

    private ArrayList<ModelClass> arrayList;
    private RecyclerAdapter recyclerAdapter;
    private Context context;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceTwo;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String uid = "";
    String vehicleType;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        this.uid = firebaseUser.getUid();
        recyclerView = getView().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceTwo = FirebaseDatabase.getInstance().getReference();
        arrayList = new ArrayList<>();
        clearAll();
        vehicleType = "trial";
        vehicleType = getVehicleType( new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                getDataFromFirebase();

            }
            @Override
            public void onStart() {
                //when starting
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {
                Log.d("onFailure", "Failed");
            }
        });
    }

    private String getVehicleType(final OnGetDataListener listener) {
        listener.onStart();
        Query query = databaseReferenceTwo.child("drivers");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    if(key.equals(uid)){
                        vehicleType = snapshot.child("vehicletype").getValue().toString();
                        listener.onSuccess(dataSnapshot);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        }
        );

        return vehicleType;
    }

    private void getDataFromFirebase() {
        Query query = databaseReference.child("orders");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("status").getValue().toString().equals("Not Accepted") && snapshot.child("vehicletype").getValue().toString().equals(vehicleType)){
                        String key = snapshot.getKey();
                        ModelClass modelClass = new ModelClass();
                        modelClass.setKey(key);
                        modelClass.setImageurl(snapshot.child("imageurl").getValue().toString());
                        modelClass.setDate(snapshot.child("date").getValue().toString());
                        modelClass.setOfferedAmount("$"+snapshot.child("amount").getValue().toString());
                        //category(description) instead of status
                        modelClass.setStatus(snapshot.child("category").getValue().toString());
                        arrayList.add(modelClass);
                    }
                }
                recyclerAdapter = new RecyclerAdapter(getContext(),arrayList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void clearAll(){
        if(arrayList != null){
            arrayList.clear();
            if(recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        arrayList = new ArrayList<>();
    }































    /***********************************************************************************************/
    public AvailableOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvailableOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailableOrdersFragment newInstance(String param1, String param2) {
        AvailableOrdersFragment fragment = new AvailableOrdersFragment();
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
        return inflater.inflate(R.layout.fragment_available_orders, container, false);
    }
}