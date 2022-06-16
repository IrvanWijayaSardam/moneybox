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

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
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
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class DepositActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;


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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        generateAds();


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

    public void generateAds(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        Toast.makeText(DepositActivity.this,"Ad Loaded", Toast.LENGTH_SHORT).show();
                        interstitialAd.show(DepositActivity.this);
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                super.onAdFailedToShowFullScreenContent(adError);
                                Toast.makeText(DepositActivity.this, "Faild to show Ad", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent();
                                Toast.makeText(DepositActivity.this,"Ad Shown Successfully",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                Toast.makeText(DepositActivity.this,"Ad Dismissed / Closed",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                                Toast.makeText(DepositActivity.this,"Ad Impression Count",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                Toast.makeText(DepositActivity.this,"Ad Clicked",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Toast.makeText(DepositActivity.this,"Failed to Load Ad because="+loadAdError.getMessage(),Toast.LENGTH_SHORT).show();
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


            String penarikan = "-"+getTambah;
            transaksi.put("User ID",userID);
            transaksi.put("Jenis Transaksi",getJenisTransaksi);
            transaksi.put("Jumlah Transaksi",penarikan);
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