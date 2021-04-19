package com.jvapp.whatsappclone.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jvapp.whatsappclone.R;
import com.jvapp.whatsappclone.activity.ChatActivity;
import com.jvapp.whatsappclone.activity.GrupoActivity;
import com.jvapp.whatsappclone.adapter.ContatosAdapter;
import com.jvapp.whatsappclone.adapter.ConversasAdapter;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;
import com.jvapp.whatsappclone.helper.RecyclerItemClickListener;
import com.jvapp.whatsappclone.helper.UsuarioFirebase;
import com.jvapp.whatsappclone.model.Conversa;
import com.jvapp.whatsappclone.model.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private RecyclerView recyvlerViewListaContatos;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaContatos = new ArrayList<>();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListenerContatos;
    private FirebaseUser usuarioAtual;



    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        //Configuração iniciais
        recyvlerViewListaContatos = view.findViewById(R.id.recyvlerViewListaContatos);
        usuarioRef = ConfiguracaoFirebase.getFirebase().child("usuarios");
        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        //Configura adapter
        adapter = new ContatosAdapter(listaContatos, getActivity());


        //Configura RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyvlerViewListaContatos.setLayoutManager(layoutManager);
        recyvlerViewListaContatos.setHasFixedSize(true);
        recyvlerViewListaContatos.setAdapter(adapter);

        //Configurar Evento de Click no recyclerView

        recyvlerViewListaContatos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(), recyvlerViewListaContatos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                List<Usuario> listaUsuariosAtualizada = adapter.getContatos();
                                Usuario usuarioSelecionado = listaUsuariosAtualizada.get(position);
                                boolean cabecalho = usuarioSelecionado.getEmail().isEmpty();
                                if(cabecalho){
                                    Intent g = new Intent(getActivity(), GrupoActivity.class);
                                    startActivity(g);


                                }else{
                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatContato",usuarioSelecionado);
                                    startActivity(i);
                                }



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

        adicionarMenuNovoGrupo();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerContatos);
    }


    public void recuperarContatos(){


       valueEventListenerContatos = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    limpaListaContatos();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUsuario = usuarioAtual.getEmail();
                    if(!emailUsuario.equals(usuario.getEmail())){
                        listaContatos.add(usuario);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void limpaListaContatos(){

        listaContatos.clear();
        adicionarMenuNovoGrupo();

    }
    public void adicionarMenuNovoGrupo(){
        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo Grupo");
        itemGrupo.setEmail("");

        listaContatos.add(itemGrupo);
    }
    public void pesquisaContatos(String texto){
        //Log.d("Evento", texto  );
        List<Usuario> listaContatosBusca = new ArrayList<>();

        for(Usuario usuario : listaContatos){
            String nome = usuario.getNome().toLowerCase();
            if(nome.contains(texto)){
                listaContatosBusca.add(usuario);
            }
        }
        adapter = new ContatosAdapter(listaContatosBusca, getActivity());
        recyvlerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    public void recarregarContatos(){

        adapter = new ContatosAdapter(listaContatos, getActivity());
        recyvlerViewListaContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
