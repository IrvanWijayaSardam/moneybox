package com.irvanw.moneybox;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView registerHere;
    private TextView tvEmail,tvPassword;
    private Button login;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvEmail = findViewById(R.id.edt_email_login);
        tvPassword = findViewById(R.id.edt_password_login);
        login = findViewById(R.id.btn_login);

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            Toast.makeText(this, fAuth.getCurrentUser().toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                finish();
        } else {
            Toast.makeText(this, "Silahkan Login Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }

        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = tvEmail.getText().toString().trim();
                String password = tvPassword.getText().toString().trim();

                if(isEmpty(email)){
                    tvEmail.setError("Email is required !");
                    return;
                } else if(isEmpty(password)){
                    tvPassword.setError("Password is required !");
                    return;
                } else {
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this,"Error .. !!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        registerHere = findViewById(R.id.tv_register_disini);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });


    }

    private void goToDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }

    private  void goToRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

}