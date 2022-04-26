package com.irvanw.moneybox;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.irvanw.moneybox.model.data_keuangan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DepositActivity extends AppCompatActivity {

    private EditText jumlahTambah;
    private Spinner spJenis;
    private Button Tambah;
    private String getTambah,getTanggal,getJenisTransaksi;
    DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        Tambah = findViewById(R.id.btnTambah);
        spJenis = findViewById(R.id.spOption);
        jumlahTambah = findViewById(R.id.edtJumlahTambah);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        getReference = database.getReference();

        Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getTambah = jumlahTambah.getText().toString();
                getJenisTransaksi = spJenis.getSelectedItem().toString();
                getTanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                Toast.makeText(DepositActivity.this,getTambah,Toast.LENGTH_SHORT).show();
                Toast.makeText(DepositActivity.this,getTanggal,Toast.LENGTH_SHORT).show();

                if(spJenis.getSelectedItem().equals("Setor Uang")){
                    checkDeposit();
                } else if (spJenis.getSelectedItem().equals("Tarik Uang")){
                    checkPenarikan();
                } else {
                    Toast.makeText(DepositActivity.this,"Error pada spinner jenis",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void checkDeposit() {
        if(isEmpty(getTambah) || isEmpty(getTanggal) || isEmpty(getJenisTransaksi)) {
            Toast.makeText(DepositActivity.this,"Silahkan masukkan jumlah saldo yang ingin ditambahkan",Toast.LENGTH_SHORT).show();
        } else {
            getReference.child("Deposit").child("Transaksi").push()
                    .setValue(new data_keuangan(getTambah,getTanggal,getJenisTransaksi));
            Toast.makeText(DepositActivity.this, "Data tersimpan",Toast.LENGTH_SHORT).show();
            goToDashboard();
        }
    }

    private void checkPenarikan() {
        if(isEmpty(getTambah) || isEmpty(getTanggal) || isEmpty(getJenisTransaksi)) {
            Toast.makeText(DepositActivity.this,"Silahkan masukkan jumlah saldo yang ingin ditambahkan",Toast.LENGTH_SHORT).show();
        } else {
            getReference.child("Deposit").child("Transaksi").push()
                    .setValue(new data_keuangan("-"+getTambah,getTanggal,getJenisTransaksi));
            Toast.makeText(DepositActivity.this, "Data tersimpan",Toast.LENGTH_SHORT).show();
            goToDashboard();
        }
    }

    private  void goToDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}