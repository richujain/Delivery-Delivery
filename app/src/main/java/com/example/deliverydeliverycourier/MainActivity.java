package com.example.deliverydeliverycourier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //

    Button btnSignin, btnRegister;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();


        //init

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnSignin = (Button)findViewById(R.id.btnSignIn);





    }
}

