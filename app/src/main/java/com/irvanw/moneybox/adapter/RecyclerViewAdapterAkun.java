package com.irvanw.moneybox.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.irvanw.moneybox.R;
import com.irvanw.moneybox.model.data_akun;

import java.util.ArrayList;

public class RecyclerViewAdapterAkun extends RecyclerView.Adapter<RecyclerViewAdapterAkun.ViewHolderAkun> {

    private ArrayList<data_akun> listAkun;
    private Context context;

    public RecyclerViewAdapterAkun(ArrayList<data_akun> listAkun, Context context) {
        this.listAkun = listAkun;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterAkun.ViewHolderAkun onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design_akun,parent,false);
        return new RecyclerViewAdapterAkun.ViewHolderAkun(V);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterAkun.ViewHolderAkun holder, int position) {
        final String Nama = listAkun.get(position).getNama();
        final String Email = listAkun.get(position).getEmail();
        final String Alamat = listAkun.get(position).getAddress();
        final String JenisKelamin = listAkun.get(position).getJk();
        final String NoTelp = listAkun.get(position).getNope();
        final String Password = listAkun.get(position).getPassword();


        holder.tvNama.setText("Nama : "+Nama);
        holder.tvEmail.setText("Email : "+Email);
        holder.tvAlamat.setText("Alamat : "+Alamat);
        holder.tvJK.setText("Jenis Kelamin : "+JenisKelamin);
        holder.tvNoTelp.setText("Nomor Telp : "+NoTelp);
        holder.tvPassword.setText("Password : "+Password);

        holder.list_item_akun.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
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



        public ViewHolderAkun(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_view_nama);
            tvEmail = itemView.findViewById(R.id.tv_view_email);
            tvAlamat = itemView.findViewById(R.id.tv_view_Alamat);
            tvJK = itemView.findViewById(R.id.tv_view_JK);
            tvNoTelp = itemView.findViewById(R.id.tv_view_nope);
            tvPassword = itemView.findViewById(R.id.tv_view_password);
            list_item_akun =  itemView.findViewById(R.id.list_item_akun);


        }
    }
}
