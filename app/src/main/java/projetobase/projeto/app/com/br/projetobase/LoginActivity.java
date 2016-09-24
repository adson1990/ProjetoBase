package projetobase.projeto.app.com.br.projetobase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

/**
 /**
 * Created by Adson on 22/10/2015.
 * Tela de abertura do app, tela que contem os campos para efetuar login,
 * cadastrar usuário ou recuperar senha esquecida.
 * existe a opção de auto login. Para desabilitar o auto login após ativado verificar a tgela de configurações
 *
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SQLiteDatabase banco = null;
    private Cursor cursor = null;

    private EditText usuario;
    private EditText senha;
    private CheckBox chkGravar;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.usuario_login);
        senha = (EditText) findViewById(R.id.senha_login);
        chkGravar = (CheckBox) findViewById(R.id.chkGravar);
        chkGravar.setOnCheckedChangeListener(this);

        abrirBanco();
        //deletarBanco();
    }

    public void abrirBanco(){
        usuario.setText("");
        senha.setText("");
        try{
            //cria ou abre o banco
            banco = openOrCreateDatabase("usuario", MODE_WORLD_READABLE, null);
            String SQL = "CREATE TABLE IF NOT EXISTS usuario" +
                    "(id NUMBER PRIMARY KEY, nome_login VARCHAR2(12) NOT NULL," +
                    "nome VARCHAR2(50) NOT NULL, senha VARCHAR2(8) NOT NULL," +
                    " cpf VARCHAR2(14)NOT NULL, data DATE, sexo CHAR(1) NOT NULL, telefone NUMBER(11) NOT NULL, " +
                    " mail VARCHAR2(50) NOT NULL, tp_sangue CHAR(2), fator_rh CHAR(1)," +
                    " plano VARCHAR2(25), carteira VARCHAR2(20), pin VARCHAR2(5), sn_login CHAR(1));";
            banco.execSQL(SQL);

           /* Toast toast = Toast.makeText(this, "Banco aberto.", Toast.LENGTH_LONG);
            toast.show();*/

        }catch (Exception erro){
            alert(this, "Erro ao acessar banco!");
            erro.printStackTrace();
        }

        tipoLogin();
        //deletarBanco();
    }

    public void tipoLogin() {

        String[] campo = new String[]{ ("sn_login")};

        cursor = banco.query("usuario", campo, "id=1", null, null, null, null, null);

        if (cursor.getCount() > 0) {//cursor != null  -----  cursor.getString(o).equals("S");

            cursor.moveToFirst();

            if (cursor.getString(0).equals("S")) {

                fecharBanco();
                onStop();
                startActivity(new Intent(this, TelaPrincipal.class));

            } else {

                chkGravar.setChecked(false);
                Toast toast = Toast.makeText(this, "Bem vindo!", Toast.LENGTH_SHORT);
                toast.show();//chama um toast para alertar usuário
                usuario.requestFocus();

            }


        } else {

            chkGravar.setChecked(false);
            Toast toast = Toast.makeText(this, "Bem vindo!", Toast.LENGTH_SHORT);
            toast.show();//chama um toast para alertar usuário
            usuario.requestFocus();

        }

    }

    /*comando query (tabela, colunas, selections, selectionsArg, group By, Having, order By)*/
    public void logar(View view) {
        banco = openOrCreateDatabase("usuario", MODE_WORLD_READABLE, null);

      /*  String[] campos = new String[]{
                ("nome_login"), ("senha")};*/
        String userDataBase = null;
        String passDataBase = null;

        if (isEmpty(usuario.getText().toString())) {//chama o método empty
            alert(this, "Informe o login!");//chama o método alert com um AlertDialog
            return;
        } else if (isEmpty(senha.getText().toString())) {
            alert(this, "Informe a senha!");
            return;
        }else{

            try {

                cursor = banco.rawQuery("select nome_login, senha from usuario", null);//banco.query("usuario", campos, null, null, null, null, null);
                if(cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    userDataBase = cursor.getString(0);
                    passDataBase = cursor.getString(1);


                    if (userDataBase.equals(usuario.getText().toString())
                            && passDataBase.equals(senha.getText().toString())) {

                        if (chkGravar.isChecked()) {
                            int linhasAlteradas = 0;
                            ContentValues atualiza = new ContentValues();
                            atualiza.put("sn_login", "S");
                            linhasAlteradas = banco.update("usuario", atualiza, null, null);
                            String retorno = linhasAlteradas + " linhas alteradas.";
                            Toast toast = Toast.makeText(this, retorno, Toast.LENGTH_LONG);
                            toast.show();
                        }

                        fecharBanco();
                        onStop();
                        startActivity(new Intent(this, TelaPrincipal.class));

                    } else {
                        alert(this, "Usuário ou senha não conferem!");
                    }
                } else {
                    cursor.moveToFirst();
                    alert(this, "Usuário não cadastrado, favor efetuar o cadastro!");
                }
            } catch (Exception erro) {
                alert(this, "Erro na pesquisa de dados!");

            }
        }
    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public void alert(Activity context, String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Informação!!!").setMessage(msg).create();
        dialog.setButton("OK", new DialogInterface.OnClickListener() {//método obsoleto
            public void onClick(DialogInterface dialog, int which) {
                return;//existe tb a opção de setar um botão nulo com o comando:
            }           //dialog.setNeutralButton("ok", null);
        });
        dialog.show();
    }

    public void cadastrar(View view){
        fecharBanco();
        startActivity(new Intent(this, CadastroUsuario.class));
    }

    public void esquecerSenha(View view){
        fecharBanco();
        startActivity(new Intent(this, EsquecerSenha.class));
    }

    public void deletarBanco(){
        try {
            deleteDatabase("usuario");
            alert(this, "deletado");
        }catch (Exception e){
            alert(this, "erro Deletado.");
        }
    }

    public void fecharBanco(){
        try{
            banco.close();
        }catch (Exception erro){
            alert(this, "Erro ao fechar banco");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClick(View v) {
    }


}

