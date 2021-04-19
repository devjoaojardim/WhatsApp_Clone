package com.jvapp.whatsappclone.model;

import com.google.firebase.database.DatabaseReference;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;

public class Conversa {

    private String idRemetente;
    private String idDestinatario;
    private String ultimaMensagem;
    private Usuario usuarioExibicao;
    private String isGruop;
    private Grupo grupo;

    public Conversa() {
        this.setIsGruop("false");

    }
    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebase();
        DatabaseReference conversaRef = database
                .child("conversas")
                .child(this.getIdRemetente())
                .child(this.getIdDestinatario());
        conversaRef.setValue(this);

    }

    public String getIsGruop() {
        return isGruop;
    }

    public void setIsGruop(String isGruop) {
        this.isGruop = isGruop;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }

    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }
}
