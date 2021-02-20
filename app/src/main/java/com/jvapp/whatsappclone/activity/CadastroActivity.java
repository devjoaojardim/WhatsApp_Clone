package com.jvapp.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.jvapp.whatsappclone.R;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;
import com.jvapp.whatsappclone.helper.Base64Custom;
import com.jvapp.whatsappclone.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    private TextInputEditText textNome, textEmail, textSenha;
    private Button buttonCadastro;

    //Atributos Autenticação
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        textNome = findViewById(R.id.textNome);
        textEmail = findViewById(R.id.textEmail1);
        textSenha = findViewById(R.id.textSenha1);
        buttonCadastro = findViewById(R.id.buttonCadastro);

        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Recupera o que usuario digitou e transforma em String
                String textoNome = textNome.getText().toString();
                String textoEmail = textEmail.getText().toString();
                String textoSenha = textSenha.getText().toString();

                //Validar se os campos foram preenchidos
                if(!textoNome.isEmpty()){
                    if(!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){

                            usuario = new Usuario();
                            usuario.setNome(textoNome);
                            usuario.setEmail(textoEmail);
                            usuario.setSenha(textoSenha);
                            cadastrarUsuario();


                        }else{
                            Toast.makeText(CadastroActivity.this,"Preencha a senha",
                                    Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(CadastroActivity.this,"Preencha o email",
                                Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha o nome",
                            Toast.LENGTH_LONG).show();

                }
                //Fim da Validação

            }
        });



    }
    public void cadastrarUsuario(){
        auth = ConfiguracaoFirebase.getAutenticacao();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Salva usuario no banco de Dados
                    String email = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setEmail(email);
                    usuario.salvar();
                    finish();

                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um e-mail válido!";

                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esse email ja foi cadastrado";

                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
