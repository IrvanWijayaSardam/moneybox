package com.irvanw.moneybox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ConstraintLayout Transaksi,Deposit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Transaksi = findViewById(R.id.constraint_transaksi);
        Deposit = findViewById(R.id.constraint_deposit);

        Deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDeposit();
            }
        });

        Transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTransaksi();
            }
        });
    }
    public void goToDeposit(){
        Intent intent = new Intent(this, DepositActivity.class);
        startActivity(intent);
    }

    public void goToTransaksi(){
        Intent intent = new Intent(this, ListDataTransaksi.class);
        startActivity(intent);
    }
}