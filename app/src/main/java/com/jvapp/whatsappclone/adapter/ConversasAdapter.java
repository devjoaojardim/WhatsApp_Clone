package com.jvapp.whatsappclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jvapp.whatsappclone.R;
import com.jvapp.whatsappclone.model.Conversa;
import com.jvapp.whatsappclone.model.Grupo;
import com.jvapp.whatsappclone.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;


    public ConversasAdapter(List<Conversa> lista, Context c) {
        this.conversas = lista;
        this.context = c;
    }
    public List<Conversa> getConversas(){
        return this.conversas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Conversa conversa = conversas.get(position);
        holder.ultimaMensagem.setText( conversa.getUltimaMensagem());

        if(conversa.getIsGruop().equals("true")){

            Grupo grupo = conversa.getGrupo();
            holder.nome.setText(grupo.getNome());

            if( grupo.getFoto() != null){
                Uri uri = Uri.parse(grupo.getFoto());
                Glide.with(context).load(uri).into(holder.foto);
            }else{
                holder.foto.setImageResource(R.drawable.padrao);
            }

        }else{
            Usuario usuario = conversa.getUsuarioExibicao();
            if(usuario != null){
                holder.nome.setText(usuario.getNome());

                if( usuario.getFoto() != null){
                    Uri uri = Uri.parse(usuario.getFoto());
                    Glide.with(context).load(uri).into(holder.foto);
                }else{
                    holder.foto.setImageResource(R.drawable.padrao);
                }
            }

        }




    }

    @Override
    public int getItemCount() {
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome, ultimaMensagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            ultimaMensagem = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
