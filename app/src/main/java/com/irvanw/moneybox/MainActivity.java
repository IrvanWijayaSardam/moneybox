package com.irvanw.moneybox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToDashboard();
        //goToLogin();
    }

    public void goToDashboard(){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
    public void goToRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToLogin(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }

    public void goToUpdate(){
        Intent intent = new Intent(this,AccountActivity.class);
        startActivity(intent);
    }

    public void goToListTransaksi(){
        Intent intent = new Intent(this,ListDataTransaksi.class);
        startActivity(intent);
    }
}
