package com.jvapp.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jvapp.whatsappclone.R;
import com.jvapp.whatsappclone.adapter.ContatosAdapter;
import com.jvapp.whatsappclone.adapter.GrupoSelecionadoAdapter;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;
import com.jvapp.whatsappclone.helper.RecyclerItemClickListener;
import com.jvapp.whatsappclone.helper.UsuarioFirebase;
import com.jvapp.whatsappclone.model.Usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrupoActivity extends AppCompatActivity {
    private RecyclerView recyclerMembrosSelecionados, recyclerMenbros;
    private ContatosAdapter contatosAdapter;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private List<Usuario> listaMembro = new ArrayList<>();
    private List<Usuario> listaMembroSelecionado = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private DatabaseReference usuarioRef;
    private FirebaseUser usuarioAtual;
    private Toolbar toolbar;
    private FloatingActionButton fabAvancarCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configuracoes iniciais
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        recyclerMenbros             = findViewById(R.id.reclyclerMembros);
        usuarioRef                  = ConfiguracaoFirebase.getFirebase().child("usuarios");
        usuarioAtual                = UsuarioFirebase.getUsuarioAtual();
        fabAvancarCadastro          = findViewById(R.id.fabAvancarCadastro);



        //Configura adapter
        contatosAdapter = new ContatosAdapter(listaMembro, getApplicationContext());

        //Configura recyclerView para os contatos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMenbros.setLayoutManager(layoutManager);
        recyclerMenbros.setHasFixedSize(true);
        recyclerMenbros.setAdapter(contatosAdapter);

        //Evento de click do RecyclerView
        recyclerMenbros.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMenbros,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario usuarioSelecionado = listaMembro.get(position);

                                //remove o usuario selecionado da lista
                                listaMembro.remove(usuarioSelecionado);
                                contatosAdapter.notifyDataSetChanged();

                                //Add o usuarioSelecionado na nova lista
                                listaMembroSelecionado.add(usuarioSelecionado);
                                grupoSelecionadoAdapter.notifyDataSetChanged();
                                atualizarMembrosToolbar();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
                );

        //Configura o Recyclerview membros selecionados
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembroSelecionado, getApplicationContext());

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerMembrosSelecionados.setLayoutManager(layoutManagerHorizontal);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter);

        recyclerMembrosSelecionados.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerMembrosSelecionados,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Usuario usuarioSelecionado = listaMembroSelecionado.get(position);

                                //remove da listagem de membros selecionados
                                listaMembroSelecionado.remove(usuarioSelecionado);
                                grupoSelecionadoAdapter.notifyDataSetChanged();

                                //add na lista membro nao selecionados
                                listaMembro.add(usuarioSelecionado);
                                contatosAdapter.notifyDataSetChanged();

                                atualizarMembrosToolbar();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
        //Configurar floating action button
        fabAvancarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GrupoActivity.this, CadastraGrupoActivity.class);
                i.putExtra("membros", (Serializable) listaMembroSelecionado);
                startActivity(i);

            }
        });


    }
    public void recuperarContatos(){
        valueEventListenerMembros = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUsuario = usuarioAtual.getEmail();
                    if(!emailUsuario.equals(usuario.getEmail())){
                        listaMembro.add(usuario);
                    }

                }
                contatosAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void atualizarMembrosToolbar(){
        int totalSelecionado = listaMembroSelecionado.size();
        int total = listaMembro.size() + totalSelecionado;
        toolbar.setSubtitle(totalSelecionado + " de " + total + " selecionados");
    }
    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerMembros);
    }

}
