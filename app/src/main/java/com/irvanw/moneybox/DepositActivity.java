package com.irvanw.moneybox;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.irvanw.moneybox.model.data_keuangan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class DepositActivity extends AppCompatActivity {

    private EditText jumlahTambah,getDate;
    private Spinner spJenis;
    private Button Tambah;
    private String getTambah,getTanggal,getTanggalDP,getJenisTransaksi;
    DatePickerDialog datePickerDialog;
    DatabaseReference getReference;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Tambah = findViewById(R.id.btnTambah);
        spJenis = findViewById(R.id.spOption);
        jumlahTambah = findViewById(R.id.edtJumlahTambah);

        getDate = findViewById(R.id.edtPilihTanggal);
        getDate.setInputType(InputType.TYPE_NULL);

        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datePickerDialog = new DatePickerDialog(DepositActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                getDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                getTanggalDP = getDate.getText().toString();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        getReference = database.getReference();

        Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getTambah = jumlahTambah.getText().toString();
                getJenisTransaksi = spJenis.getSelectedItem().toString();

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
        if(isEmpty(getTambah) || isEmpty(getTanggalDP) || isEmpty(getJenisTransaksi)) {
            Toast.makeText(DepositActivity.this,"Data Tidak Boleh Kosong ! Silahkan Cek Kembali",Toast.LENGTH_SHORT).show();
        } else {
            userID = fAuth.getCurrentUser().getUid();
            Integer transactionID = generate_random(1,999999999);
            DocumentReference documentReference = fStore.collection("transaksi").document(userID).collection("detail transaksi").document("Deposit-"+transactionID.toString());
            Map<String,Object> transaksi = new HashMap<>();

            transaksi.put("User ID",userID);
            transaksi.put("Jenis Transaksi",getJenisTransaksi);
            transaksi.put("Jumlah Transaksi",getTambah);
            transaksi.put("Tanggal Transaksi",getTanggalDP);
            documentReference.set(transaksi).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG,"onSuccess : transaction created for"+userID);
                    Toast.makeText(DepositActivity.this, "Data tersimpan",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure : "+ e.toString());
                }
            });

        }
    }

    public int generate_random(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    private void checkPenarikan() {
        if(isEmpty(getTambah) || isEmpty(getTanggalDP) || isEmpty(getJenisTransaksi)) {
            Toast.makeText(DepositActivity.this,"Data Tidak Boleh Kosong ! Silahkan Cek Kembali",Toast.LENGTH_SHORT).show();
        } else {
            userID = fAuth.getCurrentUser().getUid();
            Map<String,Object> transaksi = new HashMap<>();
            Integer transactionID = generate_random(1,999999999);
            DocumentReference documentReference = fStore.collection("transaksi").document(userID).collection("detail transaksi").document("Penarikan-"+transactionID.toString());

            transaksi.put("User ID",userID);
            transaksi.put("Jenis Transaksi",getJenisTransaksi);
            transaksi.put("Jumlah Transaksi",getTambah);
            transaksi.put("Tanggal Transaksi",getTanggalDP);


            documentReference.set(transaksi).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG,"onSuccess : transaction created for"+userID);
                    Toast.makeText(DepositActivity.this, "Data tersimpan",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure : "+ e.toString());
                }
            });
        }
    }

    private  void goToDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}