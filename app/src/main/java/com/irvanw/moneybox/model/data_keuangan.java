package com.irvanw.moneybox.model;

import java.util.Date;

public class data_keuangan {
    private String jmlTambah;
    private String tanggal;
    private String key;


    public data_keuangan() {
    }

    public data_keuangan(String jmlTambah, String tanggal) {
        this.jmlTambah = jmlTambah;
        this.tanggal = tanggal;
    }

    public String getJmlTambah() {
        return jmlTambah;
    }

    public void setJmlTambah(String jmlTambah) {
        this.jmlTambah = jmlTambah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
