package com.irvanw.moneybox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irvanw.moneybox.model.data_akun;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private TextView newNama,newEmail,newPhone,newAlamat,newPassword;
    private RadioButton newRbLaki,newRbPerempuan;
    private Button btnUpdate;
    private DatabaseReference reference;
    private ArrayList<data_akun> dataAkun;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        newNama = findViewById(R.id.edt_new_nama);
        newEmail = findViewById(R.id.edt_new_email);
        newPhone = findViewById(R.id.edt_new_phone);
        newAlamat = findViewById(R.id.edt_new_alamat);
        newPassword = findViewById(R.id.edt_new_password);

    }

    private void GetData(String data) {
        //Toast.makeText(getApplicationContext(),"Mohon Tunggu Sebentar ...",Toast.LENGTH_SHORT).show();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Account").child("Data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataAkun = new ArrayList<>();

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            data_akun account = snapshot.getValue(data_akun.class);
                            account.setKey(snapshot.getKey());
                            dataAkun.add(account);
                        }

                        Toast.makeText(getApplicationContext(),"Data Berhasil Dimuat",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat",Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity",databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

}