package com.irvanw.moneybox;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.irvanw.moneybox.model.data_keuangan;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private ConstraintLayout Transaksi,Deposit,Settings,ListAkun;
    private TextView tvNama,tvJumlahSaldo;
    private ArrayList<data_keuangan> dataKeuangan;
    private ImageView imgPp;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId,ppDashboard;
    public Integer totalSaldo;
    private AdView mAdView;

    private Button btnLogoutTest;

    public Integer getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(Integer totalSaldo) {
        this.totalSaldo = totalSaldo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvNama = findViewById(R.id.tv_nama_dashboard);
        imgPp = findViewById(R.id.img_ppDashboard);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        userId = fAuth.getCurrentUser().getUid();


        Transaksi = findViewById(R.id.constraint_transaksi);
        Deposit = findViewById(R.id.constraint_deposit);
        Settings = findViewById(R.id.constraint_settings);
        ListAkun = findViewById(R.id.constraint_listAkun);
        tvJumlahSaldo = findViewById(R.id.tv_jumlahSaldo);
        btnLogoutTest = findViewById(R.id.btnLogoutTest);

        retriveData();
        retriveCollection();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        btnLogoutTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

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

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettings();
            }
        });

        ListAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListAkun();
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


    }

    private void retriveData(){
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                tvNama.setText("Hi,  "+documentSnapshot.getString("Nama Lengkap"));
                ppDashboard = documentSnapshot.getString("Profile Picture");
                Bitmap imageBitmap = decodeFromFirebaseBase64(ppDashboard);
                imgPp.setImageBitmap(imageBitmap);

            }
        });

    }

    private Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
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

                                if(totalSaldo == null){
                                    totalSaldo += Integer.valueOf(sJmlTransaksi);
                                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                                    String yourFormattedString = formatter.format(totalSaldo);
                                    tvJumlahSaldo.setText("Rp. "+ yourFormattedString);

                                } else {
                                    totalSaldo += Integer.valueOf(sJmlTransaksi);
                                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                                    String yourFormattedString = formatter.format(totalSaldo);
                                    tvJumlahSaldo.setText("Rp. "+ yourFormattedString);


                                }


                                Log.d(TAG,document.getId()+ " => "+document.getString("Jenis Transaksi"));

                            }

                            setTotalSaldo(totalSaldo);
                        } else {
                            Log.d(TAG, "Error getting documents: ",task.getException());
                        }
                        Toast.makeText(DashboardActivity.this, "Total Saldo : "+totalSaldo, Toast.LENGTH_SHORT).show();
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

    public void goToRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void goToSettings(){
        Intent intent = new Intent(this,AccountActivity.class);
        startActivity(intent);
    }

    public void goToListAkun(){
        Intent intent = new Intent(this,ListDataAkun.class);
        startActivity(intent);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

}