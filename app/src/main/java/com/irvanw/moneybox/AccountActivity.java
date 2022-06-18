package com.irvanw.moneybox;

import static android.content.ContentValues.TAG;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.OnAdMetadataChangedListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.ads.rewarded.RewardedAd;

public class AccountActivity extends AppCompatActivity {

    private EditText newNama,newEmail,newPhone,newAlamat;
    private TextView tvGoToDashboard;
    private RadioButton newRbLaki,newRbPerempuan;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId,ppAccountA;
    private ImageView imgPPA;
    private RewardedAd mRewardedAd;
    private Button btnChangepassword,btnUpdate;
    private ImageButton IBFoto;
    public  final String TAG = "AccountActivity";
    private String userID;
    private String getNama,getEmail,getNope,getAddress,getJk,getGambar;
    private ImageView logoutButton;
    FirebaseUser user;

    private static final int REQUEST_IMAGE_CAPTURE = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Toast.makeText(AccountActivity.this, "initializationStatus completed", Toast.LENGTH_SHORT).show();
            }
        });
        logoutButton = findViewById(R.id.acc_logout);
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad was loaded.");
                        //Toast.makeText(AccountActivity.this, "Add Loaded", Toast.LENGTH_SHORT).show();

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");
                                mRewardedAd = null;
                            }
                        });

                    }
                });


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        IBFoto = findViewById(R.id.IBFotoAcc);

        user = fAuth.getCurrentUser();

        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        } else {
            retriveData();
        }

        btnChangepassword = findViewById(R.id.btnChangePassword);
        btnUpdate = findViewById(R.id.btnUpdate);

        newNama = findViewById(R.id.edt_new_nama);
        newEmail = findViewById(R.id.edt_new_email);
        newPhone = findViewById(R.id.edt_new_phone);
        newAlamat = findViewById(R.id.edt_new_alamat);
        newRbLaki = findViewById(R.id.new_rbLaki);
        newRbPerempuan = findViewById(R.id.new_rbPerempuan);
        imgPPA = findViewById(R.id.img_ppAccountA);





        tvGoToDashboard = findViewById(R.id.tv_dashboard_disini);

        tvGoToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDashboard();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedUpdateAds();
                if(newRbLaki.isChecked()){
                    getJk = "Laki Laki";
                    updateAccount();
                } else if(newRbPerempuan.isChecked()) {
                    getJk = "Perempuan";
                    updateAccount();

                } else {
                    Toast.makeText(AccountActivity.this, "Silahkan Pilih Jenis Kelamin Anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        IBFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLaunchCamera();
            }
        });

        btnChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetPassword = new EditText(view.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Change Password ?");
                passwordResetDialog.setMessage("Enter New Password > 6 Characters");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes",(dialogInterface, i) -> {
                    String newPassword = resetPassword.getText().toString();
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AccountActivity.this, "Password Reset Succesfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AccountActivity.this, "Password Reset Failed", Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"onFailure : "+e.toString());
                            Toast.makeText(AccountActivity.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });

                });

                passwordResetDialog.setNegativeButton("No",(dialogInterface, i) -> {

                });

                passwordResetDialog.create().show();
            }
        });


    }

    private void updateAccount(){
        getNama = newNama.getText().toString();
        getEmail = newEmail.getText().toString();
        getNope = newPhone.getText().toString();
        getAddress = newAlamat.getText().toString();

        if(getNama.isEmpty() || getAddress.isEmpty() || getNope.isEmpty() || getEmail.isEmpty()){
            Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {

            userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            Map<String,Object> user = new HashMap<>();
            user.put("Nama Lengkap",getNama);
            user.put("Email",getEmail);
            user.put("NoTelp",getNope);
            user.put("Alamat",getAddress);
            user.put("Jenis Kelamin",getJk);
            user.put("Profile Picture",getGambar);

            //Toast.makeText(this, getNama+getEmail+getAddress+getJk+getNope+getGambar, Toast.LENGTH_SHORT).show();

            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG,"onSuccess: user profile is created for "+userID);
                    Toast.makeText(AccountActivity.this,"User Updated..",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure : "+e.toString());
                }
            });
        }
    }

    private void clickedUpdateAds(){
        if (mRewardedAd != null) {
            Activity activityContext = AccountActivity.this;
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Toast.makeText(AccountActivity.this, "Congratulation rewards added", Toast.LENGTH_SHORT).show();
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    private void retriveData(){
        if(fAuth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        } else {
            DocumentReference documentReference = fStore.collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    newNama.setText(documentSnapshot.getString("Nama Lengkap"));
                    newEmail.setText(documentSnapshot.getString("Email"));
                    newPhone.setText(documentSnapshot.getString("NoTelp"));
                    newAlamat.setText(documentSnapshot.getString("Alamat"));
                    ppAccountA = documentSnapshot.getString("Profile Picture");
                    getGambar = documentSnapshot.getString("Profile Picture");

                    if(documentSnapshot.getString("Jenis Kelamin").toString().equals("Laki Laki")){
                        getJk = "Laki Laki";
                        newRbLaki.setChecked(true);
                    } else {
                        getJk = "Perempuan";
                        newRbPerempuan.setChecked(true);
                    }

                    Bitmap imageBitmap = decodeFromFirebaseBase64(ppAccountA);
                    imgPPA.setImageBitmap(imageBitmap);

                }
            });
        }
    }

    private Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    private void goToDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
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

    public void logout(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().signOut();

        if(user != null){
            Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Logout Gagal", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

}