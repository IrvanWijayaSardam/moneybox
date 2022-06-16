package com.irvanw.moneybox;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.irvanw.moneybox.model.data_akun;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nama,email,nope,address,password;
    private TextView loginHere,tvAggrement;
    private Button Register;
    private RadioButton rbLaki,rbPerempuan;
    private String getNama,getEmail,getNope,getAddress,getPassword,getJk,getGambar;
    private CheckBox aggrement;
    private ImageButton IBFoto;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;

    private static final int REQUEST_IMAGE_CAPTURE = 111;




    DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        nama = findViewById(R.id.edt_nama);
        email = findViewById(R.id.edt_email);
        nope = findViewById(R.id.edt_phone);
        address = findViewById(R.id.edt_alamat);
        password = findViewById(R.id.edt_password);
        tvAggrement = findViewById(R.id.tv_lisence_agreement);
        IBFoto = findViewById(R.id.IBFoto);

        loginHere = findViewById(R.id.tv_login_disini);

        rbLaki = findViewById(R.id.rbLaki);
        rbPerempuan = findViewById(R.id.rbPerempuan);

        aggrement = findViewById(R.id.checkbox_aggreement);


        Register = findViewById(R.id.btnRegister);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        }


        getReference = database.getReference();
        IBFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
            }
        });

        Intent intent = getIntent();
        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("dataNama")) {
                Register.setText("Update");
                aggrement.setVisibility(GONE);
                tvAggrement.setVisibility(GONE);
                getDatas();
            }
        }

        // ends

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extras != null) {
                    if (extras.containsKey("dataNama")) {
                        getNama = nama.getText().toString();
                        getEmail = email.getText().toString();
                        getNope = nope.getText().toString();
                        getAddress = address.getText().toString();
                        getPassword = password.getText().toString();

                        if(rbLaki.isChecked()){
                            getJk = rbLaki.getText().toString();
                        } else {
                            getJk = rbPerempuan.getText().toString();
                        }

                        data_akun setAkun = new data_akun();
                        setAkun.setNama(getNama);
                        setAkun.setEmail(getEmail);
                        setAkun.setNope(getNope);
                        setAkun.setAddress(getAddress);
                        setAkun.setPassword(getPassword);
                        setAkun.setJk(getJk);
                        setAkun.setGambar(getGambar);
                        //setAkun.setNama(getNama);

                        updateAccount(setAkun);

                    }
                } else {
                    if(aggrement.isChecked()){


                        getNama = nama.getText().toString();
                        getEmail = email.getText().toString();
                        getNope = nope.getText().toString();
                        getAddress = address.getText().toString();
                        getPassword = password.getText().toString();

                        if(rbLaki.isChecked()){
                            getJk = rbLaki.getText().toString();
                        } else {
                            getJk = rbPerempuan.getText().toString();
                        }
                        checkJk();
                        registerAuth();
                    } else {

                        Toast.makeText(RegisterActivity.this, "Anda harus menyetujui lisence & agreement !!",Toast.LENGTH_SHORT).show();
                        tvAggrement.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            }
        });

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });

    }

    private void getDatas(){

        final String dataNama = getIntent().getExtras().getString("dataNama");
        final String dataEmail = getIntent().getExtras().getString("dataEmail");
        final String dataAlamat = getIntent().getExtras().getString("dataAlamat");
        final String dataGambar = getIntent().getExtras().getString("dataGambar");
        final String dataPassword = getIntent().getExtras().getString("dataPassword");
        final String dataNoTelp = getIntent().getExtras().getString("dataNoTelp");
        final String jenisJK = getIntent().getExtras().getString("jenisJK");

        nama.setText(dataNama);
        email.setText(dataEmail);
        nope.setText(dataNoTelp);
        address.setText(dataAlamat);
        password.setText(dataPassword);



        if(jenisJK.indexOf("Laki") != -1){
            rbLaki.setChecked(true);
        }  else {
            rbPerempuan.setChecked(true);
        }


    }

    private void registerAuth(){
        fAuth.createUserWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"User Created..",Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();
                    user.put("Nama Lengkap",getNama);
                    user.put("Email",getEmail);
                    user.put("NoTelp",getNope);
                    user.put("Alamat",getAddress);
                    user.put("Jenis Kelamin",getJk);
                    user.put("Profile Picture",getGambar);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG,"onSuccess: user profile is created for "+userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"onFailure : "+e.toString());
                        }
                    });
                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this,"Error .. !!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateAccount(data_akun akun){
        if(isEmpty(getNama)||isEmpty(getEmail)||isEmpty(getNope)||isEmpty(getAddress)||isEmpty(getJk)||isEmpty(getPassword) ||isEmpty(getPassword)){
            Toast.makeText(RegisterActivity.this, "Data Tidak Boleh Kosong Update Account !!",Toast.LENGTH_SHORT).show();
        } else {
            String getKey = getIntent().getExtras().getString("getPrimaryKey");
            getReference.child("Account")
                    .child("Data")
                    .child(getKey)
                    .setValue(akun)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this, "Transaksi berhasil di update", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        }
    }

    private void checkJk(){
        if(rbLaki.isChecked()){
            getJk = rbLaki.getText().toString();
//            checkAccount();
        } else {
            if(rbPerempuan.isChecked()){
                getJk = rbPerempuan.getText().toString();
//                checkAccount();
            } else {
                Toast.makeText(RegisterActivity.this, "Silahkan Pilih Jenis Kelamin !",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageLabel.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        getGambar = imageEncoded;
    }

}