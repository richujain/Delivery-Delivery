package com.example.deliverydeliverycourier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.flarebit.flarebarlib.FlareBar;
import com.flarebit.flarebarlib.Flaretab;
import com.flarebit.flarebarlib.TabEventObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.ArrayList;

public class DriverHomePage extends AppCompatActivity {
    FlareBar bottomBar;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String uid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_page);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        this.uid = firebaseUser.getUid();
        checkIfDriverHasActiveOrder();
        flareBar();
    }

    private void checkIfDriverHasActiveOrder() {
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("orders");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("driver").getValue().toString().equals(uid)){
                        String key = snapshot.getKey();
                        Intent intent = new Intent(DriverHomePage.this,ViewActiveOrderDetails.class);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void flareBar() {
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setBarBackgroundColor(Color.parseColor("#1ab8f4"));
        ArrayList<Flaretab> tabs = new ArrayList<>();
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.neworders),"Available Task","#f0f2f5"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.delivered),"Orders","#80DEEA"));
        tabs.add(new Flaretab(getResources().getDrawable(R.drawable.profile_48px),"Profile","#B39DDB"));
        //tabs.add(new Flaretab(getResources().getDrawable(R.drawable.debt),"Debts","#EF9A9A"));
        //tabs.add(new Flaretab(getResources().getDrawable(R.drawable.settingsb),"Credit","#B2DFDB"));

        bottomBar.setTabList(tabs);
        bottomBar.attachTabs(DriverHomePage.this);
        bottomBar.setTabChangedListener(new TabEventObject.TabChangedListener() {
            @Override
            public void onTabChanged(LinearLayout selectedTab, int selectedIndex, int oldIndex) {
                //tabIndex starts from 0 (zero). Example : 4 tabs = last Index - 3

                Fragment fragment = null;

                switch (selectedIndex) {
                    case 0:
                        fragment = new AvailableOrdersFragment();
                        break;

                    case 1:
                        fragment = new HistoryFragment();
                        break;

                    case 2:
                        fragment = new ProfileFragment();
                        break;

                    case 3:
                        break;
                    case 4:
                        //fragment = new ProfileFragment();
                        break;
                    case 5:
                        //fragment = new SettingsFragment();
                        break;
                }
                loadFragment(fragment);

            }
        });
        loadFragment(new AvailableOrdersFragment());
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerParent, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}