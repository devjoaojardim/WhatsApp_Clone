package com.jvapp.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jvapp.whatsappclone.R;
import com.jvapp.whatsappclone.config.ConfiguracaoFirebase;
import com.jvapp.whatsappclone.fragments.ContatosFragment;
import com.jvapp.whatsappclone.fragments.ConversasFragment;
import com.jvapp.whatsappclone.model.Usuario;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("WhatsApp");
        setSupportActionBar(toolbar);
        auth = ConfiguracaoFirebase.getAutenticacao();


        //Configurar abas
        final FragmentPagerItemAdapter adapter = new  FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Conversas", ConversasFragment.class)
                .add("Contatos", ContatosFragment.class)
                .create()
        );
        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

        //Configuração do search
        searchView = findViewById(R.id.materialSearchPrincipal);
        //Listener para o search view
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                fragment.recarregarConversas();


            }
        });

        //Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Log.d("Evento", "onQueryTextSubmit"  );

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("Evento", "onQueryTextChange" );

                //Verifica se esta pesquisa Conversa ou contatos
                // A artir da tab que esta ativada

                switch (viewPager.getCurrentItem()){
                    case 0 :
                        ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                        if(newText != null && !newText.isEmpty()){
                            fragment.pesquisaConversas(newText.toLowerCase());
                        }else{
                            fragment.recarregarConversas();
                        }

                        break;
                    case 1 :
                        ContatosFragment contatosFragment = (ContatosFragment) adapter.getPage(1);
                        if(newText != null && !newText.isEmpty()){
                            contatosFragment.pesquisaContatos(newText.toLowerCase());
                        }else{
                            contatosFragment.recarregarContatos();
                        }

                        break;
                }



                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //Configura botao pesquisa
        MenuItem item = menu.findItem(R.id.menuPesquisa);
        searchView.setMenuItem(item);


        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair :
                deslogarUsuario();
                finish();
                break;
            case R.id.menuConfiguracao:
                abrirAjuste();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void deslogarUsuario(){
        try {
        auth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void abrirAjuste(){
        Intent intent = new Intent(MainActivity.this, AjusteActivity.class);
        startActivity(intent);

    }
}
