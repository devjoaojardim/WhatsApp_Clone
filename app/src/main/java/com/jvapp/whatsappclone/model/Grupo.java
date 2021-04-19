package com.jvapp.whatsappclone.model;

import com.google.firebase.database.DatabaseReference;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;
import com.jvapp.whatsappclone.helper.Base64Custom;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {
    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {
        DatabaseReference database = ConfiguracaoFirebase.getFirebase();
        DatabaseReference grupoRef =  database.child("grupos");

        String idGrupoFirebase = grupoRef.push().getKey();
        setId(idGrupoFirebase);
    }
    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebase();
        DatabaseReference grupoRef =  database.child("grupos");
        grupoRef.child(getId()).setValue(this);

        //salvar conversa para o membros do grupo
        for(Usuario membro: getMembros()){
            String idRemetente = Base64Custom.codificarBase64(membro.getEmail());
            String idDestinatario = getId();
            Conversa conversa = new Conversa();
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(idDestinatario);
            conversa.setUltimaMensagem("");
            conversa.setIsGruop("true");
            conversa.setGrupo(this);

            conversa.salvar();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
