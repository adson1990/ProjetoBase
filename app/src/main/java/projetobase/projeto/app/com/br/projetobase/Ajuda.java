package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Adson on 31/05/2016.
 *
 * Classe criada com o objetivo de auxiliar o usuário no uso do APP.
 */

public class Ajuda extends AppCompatActivity implements View.OnClickListener {

    //views
    private TextView abrirChamado;
    private TextView consultarChamado;
    private TextView editarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
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

        //referências das views
        abrirChamado = (TextView) findViewById(R.id.help_chamados);
        consultarChamado = (TextView) findViewById(R.id.help_verificar_chamados);



        abrirChamadoMenu();
        consultarChamadoMenu();
    }

    public void abrirChamadoMenu(){

        //menu de popup
        abrirChamado.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                //criação do menu de popup
                PopupMenu popup;
                popup = new PopupMenu(Ajuda.this, abrirChamado);
                popup.getMenuInflater().inflate(R.menu.menu_abre_chamado, popup.getMenu());

                //método set do menu de popup
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().equals("Chamados Para Si")) {
                            Toast.makeText(Ajuda.this, "Clicado: " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        } else if (item.getTitle().toString().equals("Chamados Para Outro")) {
                            Toast.makeText(Ajuda.this, "Clicado: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    public void consultarChamadoMenu(){

        //menu de popup
        consultarChamado.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                //criação do menu de popup
                PopupMenu popup;
                popup = new PopupMenu(Ajuda.this, consultarChamado);
                popup.getMenuInflater().inflate(R.menu.menu_consulta_chamado, popup.getMenu());

                //método set do menu de popup
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().toString().equals("Chamados Para Si")) {
                            Toast.makeText(Ajuda.this, "Clicado: " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        }else if(item.getTitle().toString().equals("Chamados Para Outro")){
                            Toast.makeText(Ajuda.this, "Clicado: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    //implementar um textview pra aparecer na terceira opção explicando como se edita os dados
    //usando o metodo GONE

    @Override
    public void onClick(View v) {
        finish();
        onDestroy();
    }

}
