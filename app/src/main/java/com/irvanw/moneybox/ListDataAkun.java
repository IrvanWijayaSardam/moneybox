package com.irvanw.moneybox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irvanw.moneybox.adapter.RecyclerViewAdapter;
import com.irvanw.moneybox.adapter.RecyclerViewAdapterAkun;
import com.irvanw.moneybox.model.data_akun;
import com.irvanw.moneybox.model.data_keuangan;

import java.util.ArrayList;

public class ListDataAkun extends AppCompatActivity {
    private RecyclerView recyclerViewAkun;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<data_akun> dataAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_akun);

        recyclerViewAkun = findViewById(R.id.datalistAkun);
        MyRecyclerViewAkun();
        GetDataAkun();

    }

    private void MyRecyclerViewAkun() {
        layoutManager = new LinearLayoutManager(this);
        recyclerViewAkun.setLayoutManager((layoutManager));
        recyclerViewAkun.setHasFixedSize(true);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.line));
        recyclerViewAkun.addItemDecoration(itemDecoration);
    }
    private void GetDataAkun() {
        Toast.makeText(getApplicationContext(),"Tunggu Sebentar ....",Toast.LENGTH_SHORT).show();
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Account").child("Data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataAkun = new ArrayList<>();

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            data_akun akun = snapshot.getValue(data_akun.class);
                            akun.setKey(snapshot.getKey());
                            dataAkun.add(akun);
                        }
                        adapter = new RecyclerViewAdapterAkun(dataAkun,ListDataAkun.this);
                        recyclerViewAkun.setAdapter(adapter);
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