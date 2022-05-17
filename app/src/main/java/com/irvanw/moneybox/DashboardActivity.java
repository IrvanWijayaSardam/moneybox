package com.irvanw.moneybox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ConstraintLayout Transaksi,Deposit,Settings,ListAkun;
    private TextView tvNama,tvEmail,tvNoTelp,tvAlamat,tvPassword;
    private ImageView imgPp;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId,ppDashboard;

    private Button btnLogoutTest;
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
        btnLogoutTest = findViewById(R.id.btnLogoutTest);

        retriveData();


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