package com.irvanw.moneybox;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.irvanw.moneybox.model.data_keuangan;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Insight extends AppCompatActivity {

    private PieChart chart;
    private int pemasukan;
    private int pengeluaran;
    private TextView tv_pengeluaran,tv_pemasukan;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private ArrayList<data_keuangan> dataKeuangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insight);

        tv_pengeluaran = findViewById(R.id.tv_pengeluaran_report);
        tv_pemasukan = findViewById(R.id.tv_pemasukan_report);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }


        userId = fAuth.getCurrentUser().getUid();

        chart = findViewById(R.id.pie_chart);

        retriveCollection();

    }


    private void showChart(){
        chart.addPieSlice(new PieModel("Pemasukan ",pemasukan, Color.parseColor("#00E500")));
        chart.addPieSlice(new PieModel("Pengeluaran ",pengeluaran, Color.parseColor("#ea2f22")));

        chart.startAnimation();
    }
    private void retriveCollection(){
        fStore.collection("transaksi").document(userId).collection("detail transaksi").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            dataKeuangan = new ArrayList<>();
                            for (QueryDocumentSnapshot document: task.getResult()){

                                data_keuangan dKeuangan = new data_keuangan();
                                dKeuangan.setJenisTransaksi(document.getString("Jenis Transaksi"));
                                dKeuangan.setJmlTambah(document.getString("Jumlah Transaksi"));
                                dKeuangan.setTanggal(document.getString("Tanggal Transaksi"));
                                dataKeuangan.add(dKeuangan);
                                String sJmlTransaksi = document.getString("Jumlah Transaksi");
                                String jenisTransaksi = document.getString("Jenis Transaksi");

                                if(jenisTransaksi.equalsIgnoreCase("Setor Uang")){
                                    pemasukan = pemasukan + Integer.parseInt(sJmlTransaksi);
                                } else if(jenisTransaksi.equalsIgnoreCase("Tarik Uang")){
                                    pengeluaran = pengeluaran + Integer.parseInt(sJmlTransaksi);
                                }
                            }
                            showChart();

                            pemasukanConvert();
                            pengeluaranConvert();
                            //Toast.makeText(Insight.this, "Pemasukan"+pemasukan+"Pengeluaran"+pengeluaran, Toast.LENGTH_SHORT).show();;
                        } else {
                            Log.d(TAG, "Error getting documents: ",task.getException());
                        }
                    }
                });
    }
    private void pemasukanConvert(){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(pemasukan);
        tv_pemasukan.setText("Rp. "+ yourFormattedString);
    }

    private void pengeluaranConvert(){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(pengeluaran);
        tv_pengeluaran.setText("Rp. "+ yourFormattedString);
    }
}