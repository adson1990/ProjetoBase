package projetobase.projeto.app.com.br.projetobase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import Recursos.PerfilUsuario;


public class ExibirPerfil extends AppCompatActivity {

    private TextView nomeUser;
    private TextView nomePerfilUser;
    private TextView emailUser;
    private TextView telefoneUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeUser = (TextView) findViewById(R.id.nomeUser);
        nomePerfilUser = (TextView) findViewById(R.id.nomePerfilUser);
        emailUser = (TextView) findViewById(R.id.emailUser);
        telefoneUser = (TextView) findViewById(R.id.telefoneUser);

        Intent it = getIntent();//pegando intent que originou o chamado da Activity

        //recebendo objeto da intent
        PerfilUsuario info = it.getParcelableExtra("perfilUser");
        if(info != null){
            //nome completo do usuario
            String usuario = info.getUsuario();
            nomeUser.setText(usuario);
            //login do usuario
            String nomePerfilUsuario = info.getNomePerfil();
            nomePerfilUser.setText(nomePerfilUsuario);
            //e-mail do usuario
            String mailUsuario = info.getEmail();
            emailUser.setText(mailUsuario);
            //telefone usuário
            int fone = info.getTelefone();
            telefoneUser.setText(fone);
        }


        String nome = it.getStringExtra("nome");//caso não seja passado esse parâmetro, virá NULL
        int idade = it.getIntExtra("idade", -1);//caso não seja passado esse parâmetro virá -1
        TextView txtTexto = (TextView) findViewById(R.id.txtTexto);
        //nome de perfil e idade
        txtTexto.setText(String.format("Nome: %s / Idade: %d", nome, idade));
    }

}
