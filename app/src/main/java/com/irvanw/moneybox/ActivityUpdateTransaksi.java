package com.irvanw.moneybox;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.irvanw.moneybox.model.data_keuangan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActivityUpdateTransaksi extends AppCompatActivity {

    private EditText jmlBaru,jmlLama,tglLama;
    private Spinner newSpinner;
    private Button btnUpdate;
    private DatabaseReference database;
    private String cekJml,cekTgl,cekJenisTransaksi,getJenisTransaksi;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaksi);

        jmlBaru = findViewById(R.id.edt_jmlTransaksi_baru);
        jmlLama = findViewById(R.id.edt_jmlTransaksi_lama);
        tglLama = findViewById(R.id.edt_tgl_transaksi_baru);
        newSpinner = findViewById(R.id.new_spOption);
        btnUpdate = findViewById(R.id.btnUpdateTransaksi);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        database = FirebaseDatabase.getInstance().getReference();
        userID = fAuth.getCurrentUser().getUid();

        getData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekJml = jmlBaru.getText().toString();
                cekTgl = tglLama.getText().toString();
                cekJenisTransaksi = newSpinner.getSelectedItem().toString();
                if(isEmpty(cekJml) || isEmpty(cekTgl)|| isEmpty(cekJenisTransaksi)){
                    Toast.makeText(getApplicationContext(),"Data Tidak Boleh Kosong !",Toast.LENGTH_SHORT).show();
                } else {
                    data_keuangan setKeuangan = new data_keuangan();
                    setKeuangan.setTanggal(tglLama.getText().toString());
                    if(cekJenisTransaksi.equals("Tarik Uang")){
                        setKeuangan.setJenisTransaksi(cekJenisTransaksi);
                        setKeuangan.setJmlTambah("-"+jmlBaru.getText().toString());
                        updateTransaksi();
                    } else {
                        setKeuangan.setJenisTransaksi(cekJenisTransaksi);
                        setKeuangan.setJmlTambah(jmlBaru.getText().toString());
                        updateTransaksi();
                    }

                }
            }
        });

    }
    private void getData(){
        final String getJml = getIntent().getExtras().getString("dataJmlTransaksi");
        final String getTgl = getIntent().getExtras().getString("dataTglTransaksi");
        final String getJenisTransaksi = getIntent().getExtras().getString("dataJenisTransaksi");
        jmlLama.setText("Rp. "+getJml);
        tglLama.setText(getTgl.toString());
        setSpNewJenis();

    }

    private void updateTransaksi(){
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        Map<String,Object> transaksiUpdate = new HashMap<>();
        DocumentReference documentReference = fStore.collection("transaksi").document(userID).collection("detail transaksi").document(getKey);

        transaksiUpdate.put("User ID",userID);
        transaksiUpdate.put("Jenis Transaksi",cekJenisTransaksi);
        transaksiUpdate.put("Jumlah Transaksi",jmlBaru.getText().toString());
        transaksiUpdate.put("Tanggal Transaksi",tglLama.getText().toString());


        documentReference.update(transaksiUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"onSuccess : transaction created for"+userID);
                Toast.makeText(ActivityUpdateTransaksi.this, "Data Terupdate",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"onFailure : "+ e.toString());
            }
        });
    }


    private void setSpNewJenis(){
        final String getJenisTransaksi = getIntent().getExtras().getString("dataJenisTransaksi");
        if(getJenisTransaksi.toString().equals("Setor Uang")){
            String[] newJT = getResources().getStringArray(R.array.spinnerOptionsItem);
            newSpinner.setSelection(Arrays.asList(newJT).indexOf(getJenisTransaksi.toString()));
        } else if (getJenisTransaksi.toString().equals("Tarik Uang")) {
            String[] newJT = getResources().getStringArray(R.array.spinnerOptionsItem);
            newSpinner.setSelection(Arrays.asList(newJT).indexOf(getJenisTransaksi.toString()));
        } else {
            Toast.makeText(ActivityUpdateTransaksi.this,"Cannot retreive data fakultas",Toast.LENGTH_SHORT).show();
        }
    }
}