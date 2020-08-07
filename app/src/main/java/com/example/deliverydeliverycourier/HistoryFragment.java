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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
/*********************************************************************************************/

    RecyclerView recyclerViewHistory;

    private ArrayList<ModelClass> arrayList;
    private RecyclerAdapterForHistory recyclerAdapterForHistory;
    private Context context;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String uid = "";
    Double totalAmount = 0.0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        this.uid = firebaseUser.getUid();
        recyclerViewHistory = getView().findViewById(R.id.recyclerViewHistory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setHasFixedSize(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        arrayList = new ArrayList<>();
        clearAll();
        getDataFromFirebase();

    }
    private void getDataFromFirebase() {
        Query query = databaseReference.child("orders");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("completedby").getValue().toString().equals(uid)){
                        String key = snapshot.getKey();
                        ModelClass modelClass = new ModelClass();
                        modelClass.setKey(key);
                        modelClass.setImageurl(snapshot.child("imageurl").getValue().toString());
                        modelClass.setDate(snapshot.child("date").getValue().toString());
                        modelClass.setOfferedAmount(snapshot.child("amount").getValue().toString());
                        modelClass.setStatus(snapshot.child("status").getValue().toString());
                        Double amount = Double.parseDouble(snapshot.child("amount").getValue().toString());
                        totalAmount = totalAmount + amount;
                        Log.d("total",""+totalAmount);
                        arrayList.add(modelClass);
                    }
                }
                recyclerAdapterForHistory = new RecyclerAdapterForHistory(getContext(),arrayList);
                recyclerViewHistory.setAdapter(recyclerAdapterForHistory);
                recyclerAdapterForHistory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void clearAll(){
        if(arrayList != null){
            arrayList.clear();
            if(recyclerAdapterForHistory != null){
                recyclerAdapterForHistory.notifyDataSetChanged();
            }
        }
        arrayList = new ArrayList<>();
    }

    /*********************************************************************************************/


    public HistoryFragment() {
        // Required empty public constructor
    }
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}