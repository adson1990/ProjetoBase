package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Adson on 30/10/2015.
 *
 * Tela com as configurações principais do sistema.
 * nesta tela é possível inserir o PIN do usuário (PIN usado para recuperar Senha ou validar chamados),
 * Acessar a tela de edição de alguns dados cadastrais, deletar sua conta do APP e desativar auto login
 *
 * @see EditarDados
 */

public class Configuracao extends AppCompatActivity implements View.OnClickListener {

    private ToggleButton tBPin;
    private EditText recebePin;
    private TextView tvPin;
    private CheckBox auto_login;
    private Button delConta;

    Cursor cursor;

    private SQLiteDatabase banco = null;
    private SQLiteDatabase banco2 = null;
    private SQLiteDatabase banco3 = null;

    Runtime rt = Runtime.getRuntime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
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

        tBPin =(ToggleButton) findViewById(R.id.tBT_ativar_pin);
        tBPin.setOnClickListener(this);

        recebePin = (EditText) findViewById(R.id.pin);

        tvPin = (TextView) findViewById(R.id.tv_pin);

        auto_login = (CheckBox) findViewById(R.id.auto_login);
        delConta = (Button) findViewById(R.id.delConta);

        tvPin.setVisibility(View.INVISIBLE);
        recebePin.setVisibility(View.INVISIBLE);

        checarLogin();
    }

    public void checarLogin(){
        banco = openOrCreateDatabase("usuario", MODE_WORLD_READABLE, null);
        cursor = banco.rawQuery("select sn_login, pin from usuario", null);

        if(cursor != null){
            cursor.moveToFirst();
            if(cursor.getString(0).equals("S")){
                auto_login.setChecked(true);
            }else{
                auto_login.setChecked(false);
            }

            if(!cursor.getString(1).equals("0000")){
                tBPin.setChecked(true);
                tvPin.setVisibility(View.VISIBLE);
                recebePin.setVisibility(View.VISIBLE);
                recebePin.setText(cursor.getString(1));
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v == tBPin){
            if(tBPin.isChecked()){
                tvPin.setVisibility(View.VISIBLE);
                recebePin.setVisibility(View.VISIBLE);
            }else {
                tvPin.setVisibility(View.INVISIBLE);
                recebePin.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void alert(Activity context,String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Informação!!!").setMessage(msg).create();
        dialog.setButton("OK", new DialogInterface.OnClickListener() {//método obsoleto
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        dialog.show();
    }

    public void telaEditar(View view){
        onPause();
        startActivity(new Intent(this, EditarDados.class));
    }

    public void salvarConfig(View view){
        String pegarPin = recebePin.getText().toString();
        int linhasAlteradas = 0;

        try {

            if(tBPin.isChecked()){
                if (!isEmpty(recebePin.getText().toString())) {//verifica se tem número de pin
                    ContentValues atualiza = new ContentValues();
                    atualiza.put("pin", pegarPin);
                    banco.update("usuario", atualiza, null, null);
                    linhasAlteradas++;
                } /*else {
                        alert(this, "Digite um PIN, ou desabilite a opção.");
                    }*/
            }else{
                ContentValues atualiza = new ContentValues();
                atualiza.put("pin", "0000");
                banco.update("usuario", atualiza, null, null);
                linhasAlteradas++;
            }

            if (auto_login.isChecked()) {//verifica se o check box está marcado
                ContentValues atualiza = new ContentValues();
                atualiza.put("sn_login", "S");
                banco.update("usuario", atualiza, null, null);
                linhasAlteradas++;
            }else{//caso não
                ContentValues atualiza = new ContentValues();
                atualiza.put("sn_login", "N");
                banco.update("usuario", atualiza, null, null);
                linhasAlteradas++;
            }

            String retorno = linhasAlteradas + " linhas alteradas.";
            Toast toast = Toast.makeText(this, retorno, Toast.LENGTH_LONG);
            toast.show();

            rt.runFinalization();
            finish();
            onDestroy();
            //startActivity(new Intent(this, TelaPrincipalActivity.class));

        }catch (Exception e){
            alert(this, "Erro ao gravar registo.");
        }
    }

    public void deletarRegistro(View view){
        banco2 =  openOrCreateDatabase("perfil", MODE_WORLD_READABLE, null);
        banco3 =  openOrCreateDatabase("chamados", MODE_WORLD_READABLE, null);

        banco2.delete("perfil", null, null);
        banco.delete("usuario", null, null);
        banco3.delete("chamados", null, null);
        finish();
        onStop();
        onDestroy();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

}
