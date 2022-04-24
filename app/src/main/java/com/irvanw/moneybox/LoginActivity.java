package com.irvanw.moneybox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView registerHere;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerHere = findViewById(R.id.tv_register_disini);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });


        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDashboard();
            }
        });

    }

    private void goToDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }

    private  void goToRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

}