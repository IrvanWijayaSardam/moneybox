package com.irvanw.moneybox;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.irvanw.moneybox.model.data_keuangan;

public class ActivityUpdateTransaksi extends AppCompatActivity {

    private EditText jmlBaru,jmlLama,tglLama;
    private Button btnUpdate;
    private DatabaseReference database;
    private String cekJml,cekTgl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaksi);

        jmlBaru = findViewById(R.id.edt_jmlTransaksi_baru);
        jmlLama = findViewById(R.id.edt_jmlTransaksi_lama);
        tglLama = findViewById(R.id.edt_tgl_transaksi_baru);
        btnUpdate = findViewById(R.id.btnUpdateTransaksi);

        database = FirebaseDatabase.getInstance().getReference();

        getData();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekJml = jmlBaru.getText().toString();
                cekTgl = tglLama.getText().toString();
                if(isEmpty(cekJml) || isEmpty(cekTgl)){
                    Toast.makeText(getApplicationContext(),"Data Tidak Boleh Kosong !",Toast.LENGTH_SHORT).show();
                } else {
                    data_keuangan setKeuangan = new data_keuangan();
                    setKeuangan.setJmlTambah(jmlBaru.getText().toString());
                    setKeuangan.setTanggal(tglLama.getText().toString());

                    updateTransaksi(setKeuangan);
                }

            }
        });

    }
    private void getData(){
        final String getJml = getIntent().getExtras().getString("dataJmlTransaksi");
        final String getTgl = getIntent().getExtras().getString("dataTglTransaksi");
        jmlLama.setText("Rp. "+getJml);
        tglLama.setText(getTgl.toString());

    }

    private void updateTransaksi(data_keuangan transaksi){
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("Deposit")
                .child("Transaksi")
                .child(getKey)
                .setValue(transaksi)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ActivityUpdateTransaksi.this, "Transaksi berhasil di update", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}