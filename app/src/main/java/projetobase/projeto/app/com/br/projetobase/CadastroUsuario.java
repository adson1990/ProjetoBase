package projetobase.projeto.app.com.br.projetobase;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Calendar;

import Recursos.ValidaCPF;
import Recursos.ValidarNumero;


/**
 * Created by Adson on 22/10/2015.
 *
 * Tela onde se fará o cadastro do único usuário do sistema
 * nessa tela contem o crud apenas de inserção dos dados
 */

public class CadastroUsuario extends AppCompatActivity {

    SQLiteDatabase banco = null;

    private Button data;
    private int dia, mes, ano;
    private Spinner tpSangue;
    private Spinner fatorRH;
    private EditText nome, nome_login, senha, cpf, telefone, mail;
    private EditText senha_confirma, mail_confirma, plano, carteira;
    private RadioButton sexoF, sexoM;
    private String sexo;
    ValidaCPF vCPF = new ValidaCPF();
    ValidarNumero vNumero= new ValidarNumero();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
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

        //pegando a data para o botão de data de nascimento
        Calendar calendario = Calendar.getInstance();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        ano = calendario.get(Calendar.YEAR);

        //setando a data que foi pega no sistema para o botão
        data = (Button) findViewById(R.id.btn_data);
        data.setText(dia + "/" + (mes + 1) + "/" + ano);

        //adaptador para o spinner do tipo sanguineo
        ArrayAdapter<CharSequence> adapterSangue =
                ArrayAdapter.createFromResource(this, R.array.tp_sangue,
                        android.R.layout.simple_spinner_item);
        //adaptador para o spinner fator RH
        ArrayAdapter<CharSequence> adapterRH =
                ArrayAdapter.createFromResource(this, R.array.fator_rh,
                        android.R.layout.simple_spinner_item);

        //referenciando
        tpSangue = (Spinner) findViewById(R.id.tp_sanguineo);
        tpSangue.setAdapter(adapterSangue);
        fatorRH = (Spinner) findViewById(R.id.fator_rh);
        fatorRH.setAdapter(adapterRH);
        plano = (EditText) findViewById(R.id.plano_saude);
        carteira = (EditText) findViewById(R.id.carteira_plano);
        nome = (EditText) findViewById(R.id.nome_cadastro);
        nome.requestFocus();//foco nesse campo quando abrir a tela
        nome_login = (EditText) findViewById(R.id.nome_login);
        senha = (EditText) findViewById(R.id.senha_cadastro);
        senha_confirma = (EditText) findViewById(R.id.senha_confirma);
        cpf = (EditText) findViewById(R.id.cpf_usuario);
        sexoF = (RadioButton) findViewById(R.id.sexo_fem);
        sexoM = (RadioButton) findViewById(R.id.sexo_masc);
        telefone = (EditText) findViewById(R.id.fone_usuario);
        mail = (EditText) findViewById(R.id.mail_usuario);
        mail_confirma = (EditText) findViewById(R.id.confirma_mail_usuario);

        banco = openOrCreateDatabase("usuario", MODE_WORLD_READABLE, null);
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

    public boolean chkSexo(){
        if (sexoF.isChecked()) {
            sexo = "F";
            return true;
        }else if(sexoM.isChecked()){
            sexo = "M";
            return true;
        }else{
            return false;
        }
    }

    //método que iniciará a view de exibição da data
    public void selecionarData(View view){showDialog(view.getId());}

    //método que uniciará o listener setando a data na view correspondente
    private DatePickerDialog.OnDateSetListener listener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view,
                                      int year, int monthOfYear, int dayOfMonth) {
                    dia = dayOfMonth;
                    mes = monthOfYear;
                    ano = year;
                    data.setText(dia + "/" + (mes+1) + "/" + ano);
                }
            };

    protected Dialog onCreateDialog(int id){
        if(R.id.btn_data == id){
            return new DatePickerDialog(this,
                    listener, dia, mes, ano);
        }
        return null;
    }

    public boolean validarSenha(){
        String n = senha.getText().toString();
        int quantidade = n.length();

        String senha1 = senha.getText().toString();
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
        String rNome = nome.getText().toString();

        boolean re = rNome.matches("\\w+\\D+" + "\\s*");//compara e retorna um bolleano

        if (re == false) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validaMail(){//validor o e-mail
        String email = mail.getText().toString();
        String confirmaEmail = mail_confirma.getText().toString();

        if(!confirmaEmail.equals(email)){//compara se e-mails são iguais
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

    public void gravarRegistro(){
        String verificaCpf = cpf.getText().toString();
        String tel = telefone.getText().toString();

        if(validaNome() == false){//validar o nome do usuário
            alert(this, "Nome inválido.");
            nome.requestFocus();
        }else if(validarSenha() == false){//validar se as senhas são iguais
            senha.requestFocus();
        }else if(verificaCpf.length() != 11){//verifica se o CPF tem 11 dígitos
            alert(this, "O CPF deve conter 11 dígitos.");
            cpf.requestFocus();
        } else if(vCPF.validaCPF(verificaCpf) == false){//validar o CPF
            alert(this, "CPF inválido.");
            cpf.requestFocus();
        }else if(chkSexo() == false){//verifica se o sexo foi escolhido
            alert(this, "Selecione o sexo.");
        }else if(vNumero.verificaTel(tel) == false){//valida o DDD do telefone
            alert(this, "DDD inválido.");
            telefone.requestFocus();
        }else if(validaMail() == false){//verifica se o e-mail tem @ e um provedor
            //alert chamado no método
            mail.requestFocus();
        }
        else {//inseri dados após validações
            banco = openOrCreateDatabase("usuario", MODE_WORLD_READABLE, null);

            String sql = "INSERT INTO usuario (id, nome_login, nome, senha, cpf, data, sexo," +
                    "telefone, mail, tp_sangue, fator_rh, plano, carteira, pin, sn_login) " +
                    "values(1, '" + nome_login.getText().toString() + "', " +
                    "'" + nome.getText().toString() + "', " +
                    "'" + senha.getText().toString() + "', " +
                    "'" + cpf.getText().toString() + "', " +
                    "'" + data.getText().toString() + "', '" + sexo + "', " +
                    Double.parseDouble(telefone.getText().toString()) + ", " +
                    "'" + mail.getText().toString() + "', " +
                    "'" + tpSangue.getAdapter().toString() + "', " +
                    "'" + fatorRH.getAdapter().toString() + "', " +
                    "'" + plano.getText().toString() + "', " +
                    "'" + carteira.getText().toString() + "', " +
                    "'0000', 'S');";
            try {
                banco.execSQL(sql);


                Toast toast = Toast.makeText(this, "Dados gravados com sucesso!", Toast.LENGTH_LONG);
                toast.show();

                fecharBanco();
                onPause();
                onStop();
                onDestroy();
                startActivity(new Intent(this, TelaPrincipal.class));
            } catch (Exception erro) {

                alert(this, "Erro ao inserir dados!");
            }
        }

    }

    public void cadastro(View view){
        gravarRegistro();
    }

    public void fecharBanco(){
        try{
            banco.close();
        }catch (Exception erro){
            alert(this, "Erro ao fechar banco");
        }
    }
}
