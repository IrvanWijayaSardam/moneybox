package com.irvanw.moneybox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irvanw.moneybox.adapter.RecyclerViewAdapter;
import com.irvanw.moneybox.model.data_akun;
import com.irvanw.moneybox.model.data_keuangan;

import java.util.ArrayList;

public class ListDataTransaksi extends AppCompatActivity implements RecyclerViewAdapter.dataListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<data_keuangan> dataKeuangan;
    private Spinner spOptionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_transaksi);

        spOptionList = findViewById(R.id.spOptionList);
        recyclerView = findViewById(R.id.datalist);
        EditText searchView = findViewById(R.id.etSearch);
        MyRecyclerView();

        GetData();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString() != null){
                    SearchData(s.toString());
                } else {
                    GetData();
                }
            }
        });


        spOptionList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchDataByJT(spOptionList.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                GetData();
            }
        });



    }
    public void GetData() {
        //Toast.makeText(getApplicationContext(),"Mohon Tunggu Sebentar ...",Toast.LENGTH_SHORT).show();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Deposit").child("Transaksi")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataKeuangan = new ArrayList<>();

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            data_keuangan mahasiswa = snapshot.getValue(data_keuangan.class);
                            mahasiswa.setKey(snapshot.getKey());
                            dataKeuangan.add(mahasiswa);
                        }
                        adapter = new RecyclerViewAdapter(dataKeuangan,ListDataTransaksi.this);
                        recyclerView.setAdapter(adapter);
                        //Toast.makeText(getApplicationContext(),"Data Berhasil Dimuat",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat",Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity",databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }
    private void MyRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager((layoutManager));
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void SearchData(String data) {
        //Toast.makeText(getApplicationContext(),"Mohon Tunggu Sebentar ...",Toast.LENGTH_SHORT).show();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Deposit").child("Transaksi").orderByChild("tanggal").startAt(data).endAt(data)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataKeuangan = new ArrayList<>();

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            data_keuangan akun = snapshot.getValue(data_keuangan.class);
                            akun.setKey(snapshot.getKey());
                            dataKeuangan.add(akun);
                        }
                        adapter = new RecyclerViewAdapter(dataKeuangan,ListDataTransaksi.this);
                        recyclerView.setAdapter(adapter);
                        //Toast.makeText(getApplicationContext(),"Data Berhasil Dimuat",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat",Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity",databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

    private void searchDataByJT(String JT){
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Deposit").child("Transaksi").orderByChild("jenisTransaksi").startAt(JT).endAt(JT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataKeuangan = new ArrayList<>();

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            data_keuangan akun = snapshot.getValue(data_keuangan.class);
                            akun.setKey(snapshot.getKey());
                            dataKeuangan.add(akun);
                        }
                        adapter = new RecyclerViewAdapter(dataKeuangan,ListDataTransaksi.this);
                        recyclerView.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(),"Data Berhasil Dimuat",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat",Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity",databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

    @Override
    public void onDeleteData(data_keuangan data, int position) {
        if(reference != null){
            reference.child("Deposit")
                    .child("Transaksi")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Data Berhasil Dihapus",Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}