package com.irvanw.moneybox.model;

public class data_akun {
    private String nama;
    private String email;
    private String nope;
    private String address;
    private String password;
    private String jk;
    private String key;

    public data_akun() {
    }

    public String getJk() {
        return jk;
    }



    public void setJk(String jk) {
        this.jk = jk;
    }

    public data_akun(String nama, String email, String nope, String address, String password,String jk) {
        this.nama = nama;
        this.email = email;
        this.nope = nope;
        this.address = address;
        this.password = password;
        this.jk = jk;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNope() {
        return nope;
    }

    public void setNope(String nope) {
        this.nope = nope;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
