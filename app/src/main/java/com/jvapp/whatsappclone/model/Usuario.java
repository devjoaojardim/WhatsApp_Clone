package com.jvapp.whatsappclone.model;

import com.google.firebase.database.DatabaseReference;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;

public class Usuario {
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }
    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebase();
        firebase.child("usuarios")
                .child(this.email)
                .setValue(this);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
