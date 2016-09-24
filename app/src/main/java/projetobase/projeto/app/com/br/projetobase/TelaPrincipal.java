package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;

/**
 * Created by Adson on 23/10/2015.
 * Tela principal do programa onde daqui partem todas as requisições
 *
 * @see Configuracao
 */

public class TelaPrincipal extends AppCompatActivity {

    //variáveis para serem usadas no menú
    private static final int CONFIGURACAO = 0;
    private static final int CONSULTAR = 1;
    private static final int EDITAR_DADOS = 2;

    private static final int CHAMADOS = 3;
    private static final int EXTRAS = 4;

    Runtime rt = Runtime.getRuntime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
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


        String[] atividades = new String[]{"Atividade 1", "Atividade 2"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, atividades);

        //buscando informações de configurações
        Configuration configuration = getResources().getConfiguration();
        int density = configuration.densityDpi;
        int orientation = configuration.orientation;
        int height = configuration.screenHeightDp;
        int width = configuration.screenWidthDp;
        int mcc = configuration.mcc;
        int mnc = configuration.mnc;

        //exibindo informações no log cat
        Locale locale = configuration.locale;
        Log.d("NGVL", "density: " + density);//tag , texto
        Log.d("NGVL", "orientation: " + orientation);
        Log.d("NGVL", "height: " + height);
        Log.d("NGVL", "width: " + width);
        Log.d("NGVL", "language: " + locale.getLanguage() + "." + locale.getCountry());
        Log.d("NGVL", "mcc: " + mcc);
        Log.d("NGVL", "mnc: " + mnc);

        /*lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnClickListener(chamaMenu());*/

        /*String msg = "Login realizado com sucesso!!!";
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();*/

       /* rt.runFinalization();
        rt.gc();*/
    }

    //exemplo de menú
    public boolean onCreateOptionsMenu(Menu menu){//menu

        try {
            SubMenu config = menu.addSubMenu(CONFIGURACAO, 0, 0, "Configurações");//submenu
            SubMenu consult = menu.addSubMenu(CONSULTAR, 1, 0, "Consultar");//submenu
            MenuItem editarPerfil = menu.add(EDITAR_DADOS, 2, 0, "Editar Dados");//menu

            editarPerfil.setIcon(android.R.drawable.ic_menu_edit);//seleciona icone parao menu
            config.setIcon(android.R.drawable.ic_menu_preferences);
            consult.setIcon(android.R.drawable.ic_menu_more);

            editarPerfil.setShortcut('0', 'E');//adiciona atalho ao menu

            //config.findItem(CONFIGURACAO).setEnabled()false;   desabilita um menu

            consult.add(CONSULTAR, CHAMADOS, 0, "Chamados");//sub item do submenu
            consult.add(CONSULTAR, EXTRAS, 1, "Extra");

        }catch (Exception e){
            e.printStackTrace();
        }

        //MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.layout_menus, menu);

        return super.onCreateOptionsMenu(menu);
    }//menu

    public boolean onOptionsItemSelected(MenuItem menu){//o que acontecerá quando selecionar item do menu
        switch (menu.getItemId()){
            case CONFIGURACAO:
                configuracao();
                break;
            case CONSULTAR:
                break;
            case EDITAR_DADOS:
                onPause();
               // startActivity(new Intent(this, EditarDadosActivity.class));
                break;
            case CHAMADOS:
                consultaChamados();
                break;
            case EXTRAS:
                onPause();
               // startActivity(new Intent(this, FragmentActivity.class));
                break;
        }
        return super.onOptionsItemSelected(menu);
    }

    public void paraVoce(View view){
      //  startActivity(new Intent(this, ParaVoceActivity.class));
    }

    public void paraOutro(View view){
       // startActivity(new Intent(this, ParaOutroActivity.class));
    }

    public void telaEditar(View view){
        onPause();
       // startActivity(new Intent(this, PerfilActivity.class));
    }

    public void configuracao(){
        onPause();
        //startActivity(new Intent(this, ConfiguracoesActivity.class));
    }

    public void consultaChamados(){
        onPause();
        //startActivity(new Intent(this, ConsultarChamadosActivity.class));
    }

    //métodos que controlam a vida de uma activity
    public void onStop(){
        super.onStop();
        Log.i("ALSF", "Telaprincipal::OnStop");
    }

    public void onStart(){
        super.onStart();
        Log.i("ALSF", "Telaprincipal::OnStart");
    }

    public void onResume(){
        super.onResume();
        Log.i("ALSF", "Telaprincipal::OnResume");
    }

    public void onPause(){
        super.onPause();
        Log.i("ALSF", "Telaprincipal::OnPause");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i("ALSF", "Telaprincipal::OnDestroy");
    }

    //logo abaixo um exemplo de menú de contexto


   /* public void onCreateContextMenu(ContextMenu context, View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(context, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.layout_menus, context);

    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.local:
                break;
        }
        return  super.onContextItemSelected(item);
    }*/
}
