package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by Adson on 27/08/2016.
 *
 * Classe responsável por usar o xml listview
 * para exibir os chamados salvos usando o xml
 * itens_list como adaptador para receber os dados
 */

public class ListarChamados extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SQLiteDatabase banco;
    private Cursor cursor;
    private SimpleCursorAdapter sca;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_chamados);
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

        buscarDados();
    }

    public void buscarDados(){

        String query = "SELECT _id, tipo_chamado, data_chamado FROM chamados";

        try{
            banco = openOrCreateDatabase("chamados", Context.MODE_PRIVATE, null);
            cursor = banco.rawQuery(query, null);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void criarListagem(){

        //pega o ListView que conterá os itens
        listView = (ListView) findViewById(R.id.listview);

        String[] from = {"_id", "tipo_chamado", "data_chamado"};//nomes dos campos das tabelas
        int[] to = {R.id.campoID, R.id.campoChamado, R.id.campoData};//campos do itens_list

        sca = new SimpleCursorAdapter(getApplicationContext(), R.layout.itens_list, cursor, from, to);

        //habilita o click no item da lista
        listView.setOnItemClickListener(this);
        listView.setAdapter(sca);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Forma 1

        /*Cursor c = (SQLiteCursor) sca.getItem(position);
        String nome = c.getString(c.getColumnIndex("_id"));*/

        SQLiteCursor sqLiteCursor = (SQLiteCursor) sca.getItem(position);
        String chamado = sqLiteCursor.getString(sqLiteCursor.getColumnIndex("tipo_chamado"));

        Toast.makeText(getApplicationContext(), "Selecionado o chamado: " + chamado, Toast.LENGTH_SHORT).show();
    }

}
