package com.jvapp.whatsappclone.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.jvapp.whatsappclone.R;
import com.jvapp.whatsappclone.activity.ChatActivity;
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
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversas;
    private List<Conversa> listaConversa = new ArrayList<>();
    private ConversasAdapter adapter;
    private DatabaseReference reference;
    private DatabaseReference conversaRef;

    private ChildEventListener childEventListenerConversas;


    public ConversasFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        recyclerViewConversas = view.findViewById(R.id.recyclerListaConversas);

        //Configurar adapter
        adapter = new ConversasAdapter(listaConversa, getActivity());

        //Configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);



        //Evento de click no RecyclerView
        recyclerViewConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerViewConversas, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        List<Conversa> listaConversasAtualizada = adapter.getConversas();
                        Conversa conversaSelecionada = listaConversasAtualizada.get(position);

                        if(conversaSelecionada.getIsGruop().equals("true")){
                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatGrupo",conversaSelecionada.getGrupo());
                            startActivity(i);

                        }else{
                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatContato",conversaSelecionada.getUsuarioExibicao());
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



        //Config conversasREF
        reference = ConfiguracaoFirebase.getFirebase();
        String idUsuario = UsuarioFirebase.getIndetificadorUsurario();
        conversaRef = reference.child("conversas")
                .child(idUsuario);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperaConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversaRef.removeEventListener(childEventListenerConversas);
    }
    public void pesquisaConversas(String texto){
        //Log.d("Evento", texto  );
        List<Conversa> listaConversasBusca = new ArrayList<>();

        for(Conversa conversa : listaConversa){

            if(conversa.getUsuarioExibicao() != null){

                String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem();
                if(nome.contains(texto) || ultimaMsg.contains(texto)){
                    listaConversasBusca.add(conversa);

                }
            }else{
                String nome = conversa.getGrupo().getNome().toLowerCase();
                String ultimaMsg = conversa.getUltimaMensagem();
                if(nome.contains(texto) || ultimaMsg.contains(texto)){
                    listaConversasBusca.add(conversa);

                }


            }



        }
        adapter = new ConversasAdapter(listaConversasBusca, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    public void recarregarConversas(){

        adapter = new ConversasAdapter(listaConversa, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    public void recuperaConversas(){

        listaConversa.clear();

        childEventListenerConversas = conversaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //Recupera conversas
                Conversa conversa = snapshot.getValue(Conversa.class);
                listaConversa.add(conversa);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
