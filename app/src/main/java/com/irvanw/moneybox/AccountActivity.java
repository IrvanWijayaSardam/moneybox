package com.irvanw.moneybox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.irvanw.moneybox.model.data_akun;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private EditText newNama,newEmail,newPhone,newAlamat,newPassword;
    private TextView tvGoToDashboard;
    private RadioButton newRbLaki,newRbPerempuan;
    private Button btnUpdate;
    private DatabaseReference reference;
    private ArrayList<data_akun> dataAkun;
    private Context context;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId,ppAccountA;
    private ImageView imgPPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();

        newNama = findViewById(R.id.edt_new_nama);
        newEmail = findViewById(R.id.edt_new_email);
        newPhone = findViewById(R.id.edt_new_phone);
        newAlamat = findViewById(R.id.edt_new_alamat);
        newPassword = findViewById(R.id.edt_new_password);
        newRbLaki = findViewById(R.id.new_rbLaki);
        newRbPerempuan = findViewById(R.id.new_rbPerempuan);
        imgPPA = findViewById(R.id.img_ppAccountA);
        retriveData();

        tvGoToDashboard = findViewById(R.id.tv_dashboard_disini);

        tvGoToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDashboard();
            }
        });



    }

    private void retriveData(){
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                newNama.setText(documentSnapshot.getString("Nama Lengkap"));
                newEmail.setText(documentSnapshot.getString("Email"));
                newPhone.setText(documentSnapshot.getString("NoTelp"));
                newAlamat.setText(documentSnapshot.getString("Alamat"));
                ppAccountA = documentSnapshot.getString("Profile Picture");

                if(documentSnapshot.getString("Jenis Kelamin").toString().equals("Laki Laki")){
                    newRbLaki.setChecked(true);
                } else {
                    newRbPerempuan.setChecked(false);
                }

                Bitmap imageBitmap = decodeFromFirebaseBase64(ppAccountA);
                imgPPA.setImageBitmap(imageBitmap);

            }
        });

    }

    private Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    private void goToDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }

}