package com.jvapp.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.jvapp.whatsappclone.R;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;
import com.jvapp.whatsappclone.model.Usuario;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText textEmail1, textSenha1;
    private Button buttoEntrar;
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = ConfiguracaoFirebase.getAutenticacao();

        textEmail1 = findViewById(R.id.textEmail1);
        textSenha1 = findViewById(R.id.textSenha1);
        buttoEntrar = findViewById(R.id.buttonEntrar);

        buttoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = textEmail1.getText().toString();
                String textSenha = textSenha1.getText().toString();
                if(!textEmail.isEmpty()){
                    if(!textSenha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail( textEmail );
                        usuario.setSenha( textSenha );
                        validarLogin();


                    }else{
                        Toast.makeText(LoginActivity.this,"Preencha a senha",
                                Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this,"Preencha o email",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //ValidaLogin
    public void validarLogin(){
        auth = ConfiguracaoFirebase.getAutenticacao();
        auth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();

                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não está cadastrado.";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao fazer Login " + e.getMessage();
                        e.printStackTrace();
                    }


                    Toast.makeText(LoginActivity.this,excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //Abrir Main Activity
    public void abrirTelaPrincipal(){
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }


    //metodo para abrir activity Cadastro
    public void abrirTelaCadastro(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);

    }
    //metodo para quando o usuario esquece a senha
    public void enviarEmailSenha(View view){
        final String textEmail = textEmail1.getText().toString();
        if(!textEmail.isEmpty()){
            auth = ConfiguracaoFirebase.getAutenticacao();
            String textoEmail = textEmail1.getText().toString();
            usuario = new Usuario();
            usuario.setEmail( textoEmail );

        }
        String textoEmail = textEmail1.getText().toString();
        auth.sendPasswordResetEmail(textoEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String textoEmail = textEmail1.getText().toString();
                        if (task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Redefinicão de senha Envia para: " +textoEmail,
                                    Toast.LENGTH_LONG).show();
                        }else{
                            String excecao = "";

                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Usuário não está cadastrado.";
                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Digite seu Email";
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this,excecao ,
                                    Toast.LENGTH_LONG).show();
                        }
                        }



                });

    }


}
