package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import Recursos.*;

import java.sql.PreparedStatement;
import java.util.Calendar;

/**
 * Created by Adson on 28/10/2015.
 * Tela que será usada para abrir um chamado para outra pessoa
 *
 * @see Chamado
 */

public class ParaOutro extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener{

    //variáveis check box
    private CheckBox agressao, choque, mal, parto;
    private CheckBox capotagem, colisao, atropelamento, moto, fogo_veiculo;
    private CheckBox afogamento, outros;
    private CheckBox pedestre, ciclista, motociclista, carro, onibus, caminhao;

    private CheckBox quantidade1, quantidade2, quantidade3;

    private TextView paraTransito;

    private RadioButton transito, aquatico, domestico, outro;//varíaveis radio button

    private Chamado chamado = new Chamado();

    private int dia, mes, ano;
    private int codChamado = 0;

    private SQLiteDatabase banco = null;
    private Cursor cursor = null;

    private Runtime rt = Runtime.getRuntime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_outro);
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

        rt.runFinalization();

        //pegando a data
        Calendar calendario = Calendar.getInstance();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        ano = calendario.get(Calendar.YEAR);

        //Text view
        paraTransito = (TextView) findViewById(R.id.paraTransito);

        //quantidade de envolvidos
        quantidade1 = (CheckBox) findViewById(R.id.CB_pes1);
        quantidade2 = (CheckBox) findViewById(R.id.CB_pes2);
        quantidade3 = (CheckBox) findViewById(R.id.CB_pes3);

        //envolvidos acidentes Check box
        pedestre = (CheckBox) findViewById(R.id.CB_pedestre);
        ciclista = (CheckBox) findViewById(R.id.CB_ciclista);
        motociclista = (CheckBox) findViewById(R.id.CB_motociclista);
        carro = (CheckBox) findViewById(R.id.CB_carro);
        onibus = (CheckBox) findViewById(R.id.CB_onibus);
        caminhao = (CheckBox) findViewById(R.id.CB_caminhao);


        //Check boxes de acidentes domésticos
        agressao = (CheckBox) findViewById(R.id.CB_agressao);
        agressao.setOnCheckedChangeListener(this);

        choque = (CheckBox) findViewById(R.id.CB_choque);
        choque.setOnCheckedChangeListener(this);

        mal = (CheckBox) findViewById(R.id.CB_mal);
        mal.setOnCheckedChangeListener(this);

        parto = (CheckBox) findViewById(R.id.CB_parto);
        parto.setOnCheckedChangeListener(this);

        //check boxes de acidentes de transito
        capotagem = (CheckBox) findViewById(R.id.CB_capotagem);
        capotagem.setOnCheckedChangeListener(this);

        colisao = (CheckBox) findViewById(R.id.CB_colisao);
        colisao.setOnCheckedChangeListener(this);

        atropelamento = (CheckBox) findViewById(R.id.CB_atropelamento);
        atropelamento.setOnCheckedChangeListener(this);

        moto = (CheckBox) findViewById(R.id.CB_moto);
        moto.setOnCheckedChangeListener(this);

        fogo_veiculo = (CheckBox) findViewById(R.id.CB_fogo_veículo);
        fogo_veiculo.setOnCheckedChangeListener(this);

        //check box de acidente aquatico
        afogamento = (CheckBox) findViewById(R.id.CB_afogamento);
        afogamento.setOnCheckedChangeListener(this);

        //outros
        outros = (CheckBox) findViewById(R.id.CB_outros);
        outros.setOnCheckedChangeListener(this);

        //referências com os view's do layout
        transito = (RadioButton) findViewById(R.id.RB_transito);
        transito.setOnCheckedChangeListener(this);
        aquatico = (RadioButton) findViewById(R.id.RB_aquatico);
        aquatico.setOnCheckedChangeListener(this);
        domestico = (RadioButton) findViewById(R.id.RB_domestico);
        domestico.setOnCheckedChangeListener(this);
        outro = (RadioButton) findViewById(R.id.RB_outros);
        outro.setOnCheckedChangeListener(this);

        //setando GONE na abertura da activity
        capotagem.setVisibility(View.GONE);
        colisao.setVisibility(View.GONE);
        atropelamento.setVisibility(View.GONE);
        moto.setVisibility(View.GONE);
        fogo_veiculo.setVisibility(View.GONE);
        afogamento.setVisibility(View.GONE);
        outros.setVisibility(View.GONE);
        mal.setVisibility(View.GONE);
        choque.setVisibility(View.GONE);
        agressao.setVisibility(View.GONE);
        parto.setVisibility(View.GONE);
        pedestre.setVisibility(View.GONE);
        ciclista.setVisibility(View.GONE);
        motociclista.setVisibility(View.GONE);
        carro.setVisibility(View.GONE);
        onibus.setVisibility(View.GONE);
        caminhao.setVisibility(View.GONE);
        paraTransito.setVisibility(View.GONE);


        abrirBanco();
    }

    public void abrirBanco(){

        try{
            //cria ou abre o banco
            banco = openOrCreateDatabase("chamados", MODE_WORLD_READABLE, null);
            String SQL = "CREATE TABLE IF NOT EXISTS chamados" +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, tipo_chamado VARCHAR2(12) NOT NULL," +
                    "tipo_acidente VARCHAR2(100) NOT NULL, envolvidos VARCHAR2(100)," +
                    " quantidade NUMBER, data_chamado DATE, para_outro CHAR(1));";
            banco.execSQL(SQL);

        }catch (Exception erro){
            alert(this, "Erro ao acessar banco!");
            erro.printStackTrace();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//método para revelar as opções
        if(buttonView == domestico && isChecked){//acidentes domésticos
            chamado.setTipo(domestico.getText().toString());//objeto que irá para o banco de dados
            capotagem.setVisibility(View.GONE);
            colisao.setVisibility(View.GONE);
            atropelamento.setVisibility(View.GONE);
            moto.setVisibility(View.GONE);
            fogo_veiculo.setVisibility(View.GONE);
            afogamento.setVisibility(View.GONE);
            outros.setVisibility(View.GONE);
            mal.setVisibility(View.VISIBLE);
            choque.setVisibility(View.VISIBLE);
            agressao.setVisibility(View.VISIBLE);
            parto.setVisibility(View.VISIBLE);
            pedestre.setVisibility(View.GONE);
            ciclista.setVisibility(View.GONE);
            motociclista.setVisibility(View.GONE);
            carro.setVisibility(View.GONE);
            onibus.setVisibility(View.GONE);
            caminhao.setVisibility(View.GONE);
            paraTransito.setVisibility(View.GONE);
        }else if (buttonView == transito && isChecked){//acidentes de transito
            chamado.setTipo(transito.getText().toString());//objeto que irá para o banco de dados
            capotagem.setVisibility(View.VISIBLE);
            colisao.setVisibility(View.VISIBLE);
            atropelamento.setVisibility(View.VISIBLE);
            moto.setVisibility(View.VISIBLE);
            fogo_veiculo.setVisibility(View.VISIBLE);
            afogamento.setVisibility(View.GONE);
            outros.setVisibility(View.GONE);
            mal.setVisibility(View.GONE);
            choque.setVisibility(View.GONE);
            agressao.setVisibility(View.GONE);
            parto.setVisibility(View.GONE);
            paraTransito.setVisibility(View.VISIBLE);
            pedestre.setVisibility(View.VISIBLE);
            ciclista.setVisibility(View.VISIBLE);
            motociclista.setVisibility(View.VISIBLE);
            carro.setVisibility(View.VISIBLE);
            onibus.setVisibility(View.VISIBLE);
            caminhao.setVisibility(View.VISIBLE);
        }else if(buttonView == aquatico && isChecked){//acidentes aquaticos
            chamado.setTipo(aquatico.getText().toString());//objeto que irá para o banco de dados
            capotagem.setVisibility(View.GONE);
            colisao.setVisibility(View.GONE);
            atropelamento.setVisibility(View.GONE);
            moto.setVisibility(View.GONE);
            fogo_veiculo.setVisibility(View.GONE);
            afogamento.setVisibility(View.VISIBLE);
            outros.setVisibility(View.GONE);
            mal.setVisibility(View.GONE);
            choque.setVisibility(View.GONE);
            agressao.setVisibility(View.GONE);
            parto.setVisibility(View.GONE);
            pedestre.setVisibility(View.GONE);
            ciclista.setVisibility(View.GONE);
            motociclista.setVisibility(View.GONE);
            carro.setVisibility(View.GONE);
            onibus.setVisibility(View.GONE);
            caminhao.setVisibility(View.GONE);
            paraTransito.setVisibility(View.GONE);
        }else if(buttonView == outro && isChecked){//outros
            chamado.setTipo(outro.getText().toString());//objeto que irá para o banco de dados
            capotagem.setVisibility(View.GONE);
            colisao.setVisibility(View.GONE);
            atropelamento.setVisibility(View.GONE);
            moto.setVisibility(View.GONE);
            fogo_veiculo.setVisibility(View.GONE);
            afogamento.setVisibility(View.GONE);
            outros.setVisibility(View.VISIBLE);
            mal.setVisibility(View.GONE);
            choque.setVisibility(View.GONE);
            agressao.setVisibility(View.GONE);
            parto.setVisibility(View.GONE);
            pedestre.setVisibility(View.GONE);
            ciclista.setVisibility(View.GONE);
            motociclista.setVisibility(View.GONE);
            carro.setVisibility(View.GONE);
            onibus.setVisibility(View.GONE);
            caminhao.setVisibility(View.GONE);
            paraTransito.setVisibility(View.GONE);
        }
    }

    public void prepararDados(){
        if(chamado.getTipo().equals("Trânsito")){

            if(capotagem.isChecked()){
                chamado.setTipoAcidente("Capotagem");
            }
            if(colisao.isChecked()){
                chamado.setTipoAcidente("Colisão");
            }
            if(atropelamento.isChecked()){
                chamado.setTipoAcidente("Atropelamento");
            }
            if(fogo_veiculo.isChecked()){
                chamado.setTipoAcidente("Veículo em Chamas");
            }
            if(moto.isChecked()){
                chamado.setTipoAcidente("Colisão com moto");
            }

            //em cima tipo de acidente, em baixo envolvidos no acidente

            if(pedestre.isChecked()){
                chamado.setEnvolvidos("Pedestre(s)");
            }
            if(ciclista.isChecked()){
                chamado.setEnvolvidos("Ciclista(s)");
            }
            if(motociclista.isChecked()){
                chamado.setEnvolvidos("Motociclista(s)");
            }
            if(carro.isChecked() || onibus.isChecked() || caminhao.isChecked()) {
                chamado.setEnvolvidos("Motorista(s)");
            }

        }else if(chamado.getTipo().equals("aquatico")){
            chamado.setTipoAcidente(afogamento.toString());

        }else if(chamado.getTipo().equals("domestico")){
            if(mal.isChecked()) {
                chamado.setTipoAcidente("Mal súbito");
            }
            if(choque.isChecked()){
                chamado.setTipoAcidente("Choque elétrico");
            }
            if (agressao.isChecked()) {
                chamado.setTipoAcidente("Agressão doméstica");
            }
            if(parto.isChecked()){
                chamado.setTipoAcidente("Trabalho de parto");
            }

        }else if(chamado.getTipo().equals("outro")){
            chamado.setTipoAcidente("Acidentes diversos não especificados.");
        }

        if(quantidade1.isChecked()){
            chamado.setQuantidade(1);
        }else if(quantidade2.isChecked()){
            chamado.setQuantidade(2);
        }else if(quantidade3.isChecked()){
            chamado.setQuantidade(3);
        }

    }

    @Override
    public void onClick(View v) {

    }

    public void gravar(View view){

        prepararDados();

        int resultado;
        String[] campo = new String[]{ ("_id")};
        cursor = banco.query("chamados", campo, null, null, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            resultado = cursor.getCount() + 1;
        }else{
            resultado=1;
        }

        try {
            banco = openOrCreateDatabase("chamados", MODE_WORLD_READABLE, null);

            /*String sqlComand = "INSERT INTO chamados (id_chamado, tipo_chamado, tipo_acidente, " +
                    "envolvidos, quantidade, data_chamado, para_outro)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?);";

            SQLiteStatement statement = banco.compileStatement(sqlComand);

            statement.bindLong(1, resultado);
            statement.bindString(2, chamado.getTipo());
            statement.bindAllArgsAsStrings(3, chamado.getTipoAcidente());
            statement.bindString(4, chamado.getTipo());
            statement.bindLong(5, chamado.getQuantidade());*/


            String sql = "INSERT INTO chamados (_id, tipo_chamado, tipo_acidente, " +
                    "envolvidos, quantidade, data_chamado, para_outro) " +
                    "VALUES (" + resultado +", '" + chamado.getTipo() + "', '" +
                    chamado.getTipoAcidente() + "', '" + chamado.getEnvolvidos() + "', " +
                    chamado.getQuantidade() + ", " +
                    dia + "/" + (mes + 1) + "/" + ano + ", 'S');";

            banco.execSQL(sql);

        }catch (Exception e){
            e.printStackTrace();
        }

        alert(this, "Chamado registrado com sucesso!");
    }

    public void alert(Activity context,String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Informação!!!").setMessage(msg).create();
        dialog.setButton("OK", new DialogInterface.OnClickListener() {//método obsoleto
            public void onClick(DialogInterface dialog, int which) {
                cursor.close();
                banco.close();
                onStop();
                finish();
                return;//existe tb a opção de setar um botão nulo com o comando:
            }           //dialog.setNeutralButton("ok", null);
        });
        dialog.show();

    }

}
