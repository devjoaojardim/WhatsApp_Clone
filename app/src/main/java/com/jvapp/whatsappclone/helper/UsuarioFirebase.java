package com.jvapp.whatsappclone.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;
import com.jvapp.whatsappclone.model.Usuario;

public class UsuarioFirebase {


    public static String getIndetificadorUsurario() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String indetificadorUsuario = Base64Custom.codificarBase64(email);

        return indetificadorUsuario;
    }

    public static FirebaseUser getUsuarioAtual() {

        FirebaseAuth usuario = ConfiguracaoFirebase.getAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome) {

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();


            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Sucesso ao atualizar nome de perfil");
                    }

                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean atualizarFotoUsuario(Uri url) {

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();


            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Error ao atualizar foto do perfil");
                    }

                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    public static Usuario getDadosUsuarioLogado(){

        FirebaseUser firebaseUsuario = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUsuario.getEmail());
        usuario.setNome(firebaseUsuario.getDisplayName());

        if(firebaseUsuario.getPhotoUrl() == null){
            usuario.setFoto(" ");
        }else{
            usuario.setFoto(firebaseUsuario.getPhotoUrl().toString());
        }
        return usuario;
    }

}
