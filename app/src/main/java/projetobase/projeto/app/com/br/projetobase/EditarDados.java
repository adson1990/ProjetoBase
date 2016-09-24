package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import Recursos.*;

/**
 * Created by Adson on 01/11/2015.
 * Tela para que o usuário possa editar ALGUNS de seus dados cadastrais
 * os dados são nome, senha, e-mail, carteira de saúde e plano de saúde.
 * tela acessada pela tela de configurações.
 *
 * @see Configuracao
 */

public class EditarDados extends AppCompatActivity {

    private static final int MENU = 1;
    private static final int SOBRE = 1;
    private static final int AJUDA = 2;

    private Button menu;
    private EditText nomeCadastro;
    private EditText senhaCadastro;
    private EditText senha_confirma;
    private EditText editaEmail;
    private EditText confirmaEmail;
    private EditText editaCarteira;
    private EditText recebePlano;
    private EditText telefone;

    SQLiteDatabase banco = null;
    Cursor cursor;

    ValidarNumero vNumero = new ValidarNumero();

    Runtime rt = Runtime.getRuntime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dados);
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

        nomeCadastro = (EditText) findViewById(R.id.nome_cadastro);
        senhaCadastro = (EditText) findViewById(R.id.senha_cadastro);
        senha_confirma = (EditText) findViewById(R.id.confirme_a_senha);
        editaEmail = (EditText) findViewById(R.id.edita_email);
        confirmaEmail = (EditText) findViewById(R.id.confirma_email);
        editaCarteira = (EditText) findViewById(R.id.recebe_carteira);
        recebePlano = (EditText) findViewById(R.id.recebe_plano);
        telefone = (EditText) findViewById(R.id.telefone);

        //menu de popup
       /* menu = (Button) findViewById(R.id.menuPopUp);
        menu.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                //criação do menu de popup
                PopupMenu popup;
                popup = new PopupMenu(EditarDadosActivity.this, menu);
                popup.getMenuInflater().inflate(R.menu.menu_editar_dados, popup.getMenu());

                //método set do menu de popup
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().toString().equals("Sobre")) {
                            abrirSobre();
                        }else if(item.getTitle().toString().equals("Ajuda")){
                            abrirAjuda();
                        }
                        //Toast.makeText(EditarDadosActivity.this, "Clicado: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popup.show();
            }
        });*/

        banco = openOrCreateDatabase("usuario", MODE_WORLD_READABLE, null);
        buscarDados();
    }

    public void abrirSobre(){
        startActivity(new Intent(this, Sobre.class));
    }

    public void abrirAjuda(){
        startActivity(new Intent(this, Ajuda.class));
    }

    public void buscarDados(){
        try {
            cursor = banco.rawQuery("select nome, senha, " +
                    "mail, carteira, plano, " +
                    "telefone from usuario", null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                nomeCadastro.setText(cursor.getString(0));
                senhaCadastro.setText(cursor.getString(1));
                editaEmail.setText(cursor.getString(2));
                editaCarteira.setText(cursor.getString(3));
                recebePlano.setText(cursor.getString(4));
                telefone.setText(cursor.getInt(5));
            } else {
                alert(this, "Dados não encontrados!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void atualizarDados(View view) {

        try {
            ContentValues atualiza = new ContentValues();
            String tel = telefone.getText().toString();

            if (recebePlano.getText().length() > 0) {
                atualiza.put("plano", recebePlano.getText().toString());
            } else {
                recebePlano.setText("");
                atualiza.put("plano", recebePlano.getText().toString());
            }

            if (editaCarteira.getText().length() > 0) {
                atualiza.put("carteira", editaCarteira.getText().toString());
            } else {
                editaCarteira.setText("");
                atualiza.put("carteira", editaCarteira.getText().toString());
            }

            if (validaNome() == true && nomeCadastro.getText().length() > 0) {//validar o nome do usuário
                atualiza.put("nome", nomeCadastro.getText().toString());
            } else if (validaNome() == false) {
                alert(this, "Nome inválido.");
                nomeCadastro.requestFocus();
            }

            if (validarSenha() == true && senhaCadastro.getText().length() > 0
                    && senha_confirma.getText().length() > 0) {//validar se as senhas são iguais
                atualiza.put("senha", senhaCadastro.getText().toString());
            } else if (validarSenha() == false) {
                senhaCadastro.requestFocus();
            }

            if(telefone.getText().length() > 0) {
                if (vNumero.verificaTel(tel) == true) {//valida o DDD do telefone
                    atualiza.put("telefone", Double.parseDouble(telefone.getText().toString()));
                } else {
                    alert(this, "DDD ou telefone inválido.");
                    telefone.requestFocus();
                }
            }

            if (validaMail() == true) {//verifica se o e-mail tem @ e um provedor
                atualiza.put("mail", editaEmail.getText().toString());
            } else if (validaMail() == false && editaEmail.getText().length() > 0) {
                editaEmail.requestFocus();
            }
            int linhasAlteradas = banco.update("usuario", atualiza, null, null);

            /*Toast toast = Toast.makeText(this, linhasAlteradas + " Linhas Alteradas.", Toast.LENGTH_LONG);
            toast.show();*/
            String msg = linhasAlteradas + " Linhas Alteradas";

            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Informação!!!").setMessage(msg).create();
            dialog.setButton("OK", new DialogInterface.OnClickListener() {//método obsoleto
                public void onClick(DialogInterface dialog, int which) {

                    rt.runFinalization();
                    finish();
                    onDestroy();
                    return;//existe tb a opção de setar um botão nulo com o comando:
                }           //dialog.setNeutralButton("ok", null);
            });
            dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean validarSenha(){
        int quantidade = senhaCadastro.getText().toString().length();

        String senha1 = senhaCadastro.getText().toString();
        String senha2 = senha_confirma.getText().toString();

        if( quantidade >= 4 ){//verifica se a senha não está nula e é maior que 4 dígitos
            if (!senha1.equals(senha2)) {//verifica se senhas são iguais
                alert(this, "Senhas não conferem.");
                return false;
            } else {
                return true;
            }
        }else{
            alert(this, "A senha deve conter no mínimo 4 dígitos.");
            return false;
        }
    }

    public boolean validaNome(){
        String rNome = nomeCadastro.getText().toString();

        boolean re = rNome.matches("\\w+\\D+" + "\\s*");//compara e retorna um bolleano

        if (re == false) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validaMail(){//validor o e-mail
        String email = editaEmail.getText().toString();
        String mailConfirma = confirmaEmail.getText().toString();

        if(!mailConfirma.equals(email)){//compara se e-mails são iguais
            alert(this, "E-mails não conferem.");
            return false;
        }else {
            boolean re = email.matches(".+@.+\\.\\w{3}|.+@.+\\.\\w{3}\\.\\w{2}");//compara e retorna um bolleano

            if (re == false) {
                alert(this, "E-mail inválido.");
                return false;
            } else {
                return true;
            }
        }
    }

    public void alert(Activity context,String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Informação!!!").setMessage(msg).create();
        dialog.setButton("OK", new DialogInterface.OnClickListener() {//método obsoleto
            public void onClick(DialogInterface dialog, int which) {
                return;//existe tb a opção de setar um botão nulo com o comando:
            }           //dialog.setNeutralButton("ok", null);
        });
        dialog.show();
    }

}
