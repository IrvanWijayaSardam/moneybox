package com.irvanw.moneybox.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.irvanw.moneybox.ActivityUpdateTransaksi;
import com.irvanw.moneybox.ListDataTransaksi;
import com.irvanw.moneybox.R;
import com.irvanw.moneybox.model.data_akun;
import com.irvanw.moneybox.model.data_keuangan;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<data_keuangan> listTransaksi;
    private Context context;

    public interface dataListener{
        void onDeleteData(data_keuangan data,int position);
    }

    dataListener listener;

    public RecyclerViewAdapter(ArrayList listAkun, Context context) {
        this.listTransaksi = listAkun;
        this.context = context;
        listener = (ListDataTransaksi)context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design_activity,parent,false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final String jumlahSetoran = listTransaksi.get(position).getJmlTambah();
        final String tglSetoran = listTransaksi.get(position).getTanggal();
        final String jenisTransaksi = listTransaksi.get(position).getJenisTransaksi();

        holder.jmlSetor.setText("Rp. "+jumlahSetoran);
        holder.tglSetor.setText("Tanggal : "+tglSetoran);
        holder.jenisTransaksi.setText("Jenis Transaksi : "+jenisTransaksi);

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] action = {"Update","Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("dataJmlTransaksi",listTransaksi.get(position).getJmlTambah());
                                bundle.putString("dataTglTransaksi",listTransaksi.get(position).getTanggal());
                                bundle.putString("dataJenisTransaksi",listTransaksi.get(position).getJenisTransaksi());
                                bundle.putString("getPrimaryKey",listTransaksi.get(position).getKey());
                                Intent intent = new Intent(view.getContext(), ActivityUpdateTransaksi.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;

                            case 1:
                                listener.onDeleteData(listTransaksi.get(position),position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView jmlSetor,tglSetor,jenisTransaksi;
        private LinearLayout ListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jmlSetor = itemView.findViewById(R.id.tv_jumlahDiSetor);
            tglSetor = itemView.findViewById(R.id.tv_tgl_setor);
            jenisTransaksi = itemView.findViewById(R.id.tv_jenis_transaksi);

            ListItem = itemView.findViewById(R.id.list_item);

        }
    }
}