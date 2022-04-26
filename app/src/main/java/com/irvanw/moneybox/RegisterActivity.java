package com.irvanw.moneybox;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.irvanw.moneybox.model.data_akun;

public class RegisterActivity extends AppCompatActivity {

    private EditText nama,email,nope,address,password;
    private TextView loginHere,tvAggrement;
    private Button Register;
    private RadioButton rbLaki,rbPerempuan;
    private String getNama,getEmail,getNope,getAddress,getPassword,getJk;
    private CheckBox aggrement;


    DatabaseReference getReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = findViewById(R.id.edt_nama);
        email = findViewById(R.id.edt_email);
        nope = findViewById(R.id.edt_phone);
        address = findViewById(R.id.edt_alamat);
        password = findViewById(R.id.edt_password);
        tvAggrement = findViewById(R.id.tv_lisence_agreement);

        loginHere = findViewById(R.id.tv_login_disini);

        rbLaki = findViewById(R.id.rbLaki);
        rbPerempuan = findViewById(R.id.rbPerempuan);

        aggrement = findViewById(R.id.checkbox_aggreement);


        Register = findViewById(R.id.btnRegister);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        getReference = database.getReference();
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aggrement.isChecked()){
                    getNama = nama.getText().toString();
                    getEmail = email.getText().toString();
                    getNope = nope.getText().toString();
                    getAddress = address.getText().toString();
                    getPassword = password.getText().toString();

                    checkJk();
                } else {
                    Toast.makeText(RegisterActivity.this, "Anda harus menyetujui lisence & agreement !!",Toast.LENGTH_SHORT).show();
                    tvAggrement.setTextColor(Color.parseColor("#FF0000"));
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

    private void checkAccount(){
        if(isEmpty(getNama)||isEmpty(getEmail)||isEmpty(getNope)||isEmpty(getAddress)||isEmpty(getJk)||isEmpty(getPassword)){
            Toast.makeText(RegisterActivity.this, "Data Tidak Boleh Kosong !!",Toast.LENGTH_SHORT).show();
        } else {

            getReference.child("Account").child("Data").push()
                    .setValue(new data_akun(getNama,getEmail,getNope,getAddress,getPassword,getJk))
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(RegisterActivity.this, "Registrasi Berhasil !! Silahkan Login",Toast.LENGTH_SHORT).show();
                            goToLogin();
                        }
                    });

        }
    }

    private void checkJk(){
        if(rbLaki.isChecked()){
            getJk = rbLaki.getText().toString();

            checkAccount();
        } else {
            if(rbPerempuan.isChecked()){
                getJk = rbPerempuan.getText().toString();
                checkAccount();
            } else {
                Toast.makeText(RegisterActivity.this, "Silahkan Pilih Jenis Kelamin !",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}