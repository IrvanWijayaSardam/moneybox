package com.irvanw.moneybox;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.irvanw.moneybox.adapter.RecyclerViewAdapter;
import com.irvanw.moneybox.model.data_keuangan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListDataTransaksi extends AppCompatActivity implements RecyclerViewAdapter.dataListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<data_keuangan> dataKeuangan;
    private Spinner spOptionList;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    public Integer totalSaldo;



    public Integer getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(Integer totalSaldo) {
        this.totalSaldo = totalSaldo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_transaksi);

        dataKeuangan = new ArrayList<>();



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        spOptionList = findViewById(R.id.spOptionList);
        recyclerView = findViewById(R.id.datalist);
        EditText searchView = findViewById(R.id.etSearch);
        MyRecyclerView();

        retriveCollection();
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
                    //SearchData(s.toString());
                } else {
                    //GetData();
                }
            }
        });


        spOptionList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                searchDataByJT(spOptionList.getSelectedItem().toString());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                retriveCollection();
            }
        });

    }

    private void retriveCollection(){
        fStore.collection("transaksi").document(userID).collection("detail transaksi").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for (QueryDocumentSnapshot document: task.getResult()){
                                data_keuangan dKeuangan = new data_keuangan();
                                dKeuangan.setJenisTransaksi(document.getString("Jenis Transaksi"));
                                dKeuangan.setJmlTambah(document.getString("Jumlah Transaksi"));
                                dKeuangan.setTanggal(document.getString("Tanggal Transaksi"));
                                dKeuangan.setKey(document.getId());
                                dataKeuangan.add(dKeuangan);
                                String sJmlTransaksi = document.getString("Jumlah Transaksi");

                                if(totalSaldo == null){
                                    totalSaldo = Integer.valueOf(sJmlTransaksi);
                                } else {
                                    totalSaldo += Integer.valueOf(sJmlTransaksi);
                                }

                                adapter = new RecyclerViewAdapter(dataKeuangan,ListDataTransaksi.this);
                                recyclerView.setAdapter(adapter);

                                Log.d(TAG,document.getId()+ " => "+document.getString("Jenis Transaksi"));

                            }

                            setTotalSaldo(totalSaldo);
                        } else {
                            Log.d(TAG, "Error getting documents: ",task.getException());
                        }

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
        fStore.collection("transaksi").document(userID).collection("detail transaksi").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                if(document.getString("Jenis Transaksi") == JT){
                                    data_keuangan dKeuangan = new data_keuangan();
                                    dKeuangan.setJenisTransaksi(document.getString("Jenis Transaksi"));
                                    dKeuangan.setJmlTambah(document.getString("Jumlah Transaksi"));
                                    dKeuangan.setTanggal(document.getString("Tanggal Transaksi"));
                                    dKeuangan.setKey(document.getId());
                                    dataKeuangan.add(dKeuangan);
                                    String sJmlTransaksi = document.getString("Jumlah Transaksi");

                                    if(totalSaldo == null){
                                        totalSaldo = Integer.valueOf(sJmlTransaksi);
                                    } else {
                                        totalSaldo += Integer.valueOf(sJmlTransaksi);
                                    }

                                    adapter = new RecyclerViewAdapter(dataKeuangan,ListDataTransaksi.this);
                                    recyclerView.setAdapter(adapter);
                                }
                                else {
                                    //Toast.makeText(ListDataTransaksi.this, document.getString("Jenis Transaksi"), Toast.LENGTH_SHORT).show();
                                }

                                Log.d(TAG,document.getId()+ " => "+document.getString("Jenis Transaksi"));

                            }

                            setTotalSaldo(totalSaldo);
                        } else {
                            Log.d(TAG, "Error getting documents: ",task.getException());
                        }

                    }
                });
    }

    @Override
    public void onDeleteData(data_keuangan data, int position) {

        fStore.collection("transaksi").document(userID).collection("detail transaksi").document(data.getKey())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Data Berhasil Dihapus",Toast.LENGTH_LONG).show();
                        retriveCollection();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Data Gagal Dihapus",Toast.LENGTH_LONG).show();
                    }
                });
    }

}