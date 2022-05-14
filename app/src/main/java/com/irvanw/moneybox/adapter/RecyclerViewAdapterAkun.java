package com.irvanw.moneybox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.irvanw.moneybox.ListDataAkun;
import com.irvanw.moneybox.R;
import com.irvanw.moneybox.RegisterActivity;
import com.irvanw.moneybox.model.data_akun;
import com.irvanw.moneybox.model.data_keuangan;

import java.util.ArrayList;

public class RecyclerViewAdapterAkun extends RecyclerView.Adapter<RecyclerViewAdapterAkun.ViewHolderAkun> {

    private ArrayList<data_akun> listAkun;
    private Context context;

    public interface dataListener{
        void onDeleteData(data_akun data,int position);
    }

    dataListener listener;

    public RecyclerViewAdapterAkun(ArrayList<data_akun> listAkun, Context context) {
        this.listAkun = listAkun;
        this.context = context;
        listener = (ListDataAkun)context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterAkun.ViewHolderAkun onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design_akun,parent,false);
        return new RecyclerViewAdapterAkun.ViewHolderAkun(V);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterAkun.ViewHolderAkun holder, @SuppressLint("RecyclerView") int position) {
        final String Nama = listAkun.get(position).getNama();
        final String Email = listAkun.get(position).getEmail();
        final String Alamat = listAkun.get(position).getAddress();
        final String JenisKelamin = listAkun.get(position).getJk();
        final String NoTelp = listAkun.get(position).getNope();
        final String Password = listAkun.get(position).getPassword();
        final String gambar = listAkun.get(position).getGambar();


        holder.tvNama.setText("Nama : "+Nama);
        holder.tvEmail.setText("Email : "+Email);
        holder.tvAlamat.setText("Alamat : "+Alamat);
        holder.tvJK.setText("Jenis Kelamin : "+JenisKelamin);
        holder.tvNoTelp.setText("Nomor Telp : "+NoTelp);
        holder.tvPassword.setText("Password : "+Password);
        Bitmap imageBitmap = decodeFromFirebaseBase64(listAkun.get(position).getGambar());
        holder.imgPp.setImageBitmap(imageBitmap);


        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dataNama", listAkun.get(position).getNama());
                bundle.putString("dataEmail", listAkun.get(position).getEmail());
                bundle.putString("dataAlamat", listAkun.get(position).getAddress());
                bundle.putString("dataGambar", listAkun.get(position).getGambar());
                bundle.putString("dataPassword", listAkun.get(position).getPassword());
                bundle.putString("dataNoTelp", listAkun.get(position).getNope());
                bundle.putString("jenisJK", listAkun.get(position).getJk());
                bundle.putString("getPrimaryKey", listAkun.get(position).getKey());
//                                Toast.makeText(view.getContext(), jenisdal, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(), "jenis :"+getjenis, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    listener.onDeleteData(listAkun.get(position),position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listAkun.size();
    }

    public class ViewHolderAkun extends RecyclerView.ViewHolder {
        private TextView tvNama,tvEmail,tvAlamat,tvJK,tvNoTelp,tvPassword;
        private LinearLayout list_item_akun;
        private ImageView imgPp;
        private Button btnUpdate,btnDelete;


        public ViewHolderAkun(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_view_nama);
            tvEmail = itemView.findViewById(R.id.tv_view_email);
            tvAlamat = itemView.findViewById(R.id.tv_view_Alamat);
            tvJK = itemView.findViewById(R.id.tv_view_JK);
            tvNoTelp = itemView.findViewById(R.id.tv_view_nope);
            tvPassword = itemView.findViewById(R.id.tv_view_password);
            list_item_akun =  itemView.findViewById(R.id.list_item_akun);
            imgPp = itemView.findViewById(R.id.imgProfile);
            btnUpdate = itemView.findViewById(R.id.btnUpdateUser);
            btnDelete = itemView.findViewById(R.id.btnDeleteUser);

        }
    }
    private Bitmap decodeFromFirebaseBase64(String image) {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
