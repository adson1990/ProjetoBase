package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Adson on 19/05/2016.
 * Tela com o objetivo de implementar um perfil para o usuário logado
 * colocando foto escolhendo nome de usuário e alterando a cor do nome.
 * A imagem também pode ser baixada de um endereço URL passado.
 * classe de suporte @see ImageViewActivity
 */

public class Perfil extends AppCompatActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener, DialogInterface.OnClickListener{

    private EditText edtUrl;
    private Button selecionarNome;
    private TextView nomePerfil;
    private EditText recebeNomePerfil;
    private SeekBar seekBarBLUE;
    private SeekBar seekBarGREEN;
    private SeekBar seekBarRED;
    private CheckBox azul;
    private CheckBox verde;
    private CheckBox vermelho;
    private ImageView usuario;

    static final int REQUEST_IMAGE_OPEN = 1;

    SQLiteDatabase banco = null;
    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
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

        edtUrl = (EditText)findViewById(R.id.editText1);
        // Obtendo a ultima URL digitada
        SharedPreferences preferencias = getSharedPreferences(
                "configuracao", MODE_PRIVATE);
        String url = preferencias.getString("url_imagem", "http://");
        edtUrl.setText(url);

        nomePerfil = (TextView) findViewById(R.id.nomePerfil);
        recebeNomePerfil = (EditText) findViewById(R.id.recebeNomePerfil);
        selecionarNome = (Button) findViewById(R.id.selecionarNome);
        seekBarRED = (SeekBar) findViewById(R.id.seekBar_C);
        seekBarGREEN = (SeekBar) findViewById(R.id.seekBar_B);
        seekBarBLUE = (SeekBar) findViewById(R.id.seekBar_A);
        azul = (CheckBox) findViewById(R.id.corAzul);
        verde = (CheckBox) findViewById(R.id.corVerde);
        vermelho = (CheckBox) findViewById(R.id.corVermelho);
        usuario = (ImageView) findViewById(R.id.imageViewUsuario);

        selecionarNome.setOnClickListener(this);
        seekBarRED.setOnSeekBarChangeListener(this);
        seekBarGREEN.setOnSeekBarChangeListener(this);
        seekBarBLUE.setOnSeekBarChangeListener(this);
        azul.setOnCheckedChangeListener(this);
        verde.setOnCheckedChangeListener(this);
        vermelho.setOnCheckedChangeListener(this);

        seekBarBLUE.setEnabled(false);
        seekBarGREEN.setEnabled(false);
        seekBarRED.setEnabled(false);

        abrirBancoPerfil();
        //deletarBanco();
    }

    public void abrirBancoPerfil(){

        try{
            //cria ou abre o banco
            banco = openOrCreateDatabase("perfil", MODE_WORLD_READABLE, null);
            String SQL = "CREATE TABLE IF NOT EXISTS perfil" +
                    "(imagem BLOB, nome_perfil VARCHAR2(12)," +
                    "azul NUMBER, verde NUMBER, vermelho NUMBER);";
            banco.execSQL(SQL);

        }catch (Exception erro){
            alert(this, "Erro ao acessar banco!");
            erro.printStackTrace();
        }
        buscarDados();
    }

    public void buscarDados() {

        String sql       =   "select * from perfil";
        cursor           =   banco.rawQuery(sql, null);

        if(cursor.moveToFirst()) {

            //recupera o array de bytes que representa a foto e o converte para bitmap
            byte[] photo = cursor.getBlob(0);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            usuario.setImageBitmap(theImage);

            nomePerfil.setText(cursor.getString(1));
            if(cursor.getInt(2) != 130){
                azul.setChecked(true);
                seekBarBLUE.setProgress(cursor.getInt(2));
            }
            if(cursor.getInt(3) != 130){
                verde.setChecked(true);
                seekBarGREEN.setProgress(cursor.getInt(3));
            }
            if(cursor.getInt(4) != 130){
                vermelho.setChecked(true);
                seekBarRED.setProgress(cursor.getInt(4));
            }
            updateTextColor();
            //nomePerfil.setTextColor(Color.BLUE);

            if (cursor.getCount() == 0) {
                alert(this, "Dados não encontrados!");
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public void resetarDados(View view){
        usuario.setImageDrawable(getDrawable(R.drawable.usuario));
        nomePerfil.setText("");
        nomePerfil.setTextColor(Color.BLACK);
        seekBarBLUE.setProgress(130);
        seekBarGREEN.setProgress(130);
        seekBarRED.setProgress(130);
        azul.setChecked(false);
        verde.setChecked(false);
        vermelho.setChecked(false);

        try {
            String delSql = "DELETE FROM perfil";
            SQLiteStatement sqLiteStatement = banco.compileStatement(delSql);
            sqLiteStatement.execute();

            Toast toast = Toast.makeText(this, "Dados apagados com sucesso!", Toast.LENGTH_LONG);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public byte[] compactarImagem(){
        //compactar e converter imagem
        Bitmap bitmap = ((BitmapDrawable)usuario.getDrawable()).getBitmap();
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, saida);//tipo que se quer compatar, qualidade, objeto da classe ByteArray
        byte[] img = saida.toByteArray();
        return img;
    }

    public void inserirDados(){

        try {
            String delSql  =  "DELETE FROM perfil";
            SQLiteStatement sqLiteStatement = banco.compileStatement(delSql);
            sqLiteStatement.execute();

            String sqlInsert = "INSERT INTO perfil (imagem, nome_perfil, azul, verde, vermelho) VALUES(?,?,?,?,?)";
            SQLiteStatement insertStmt = banco.compileStatement(sqlInsert);
            insertStmt.clearBindings();
            insertStmt.bindBlob(1, this.compactarImagem());
            insertStmt.bindString(2, recebeNomePerfil.getText().toString());
            insertStmt.bindLong(3, seekBarBLUE.getProgress());
            insertStmt.bindLong(4, seekBarGREEN.getProgress());
            insertStmt.bindLong(5, seekBarRED.getProgress());
            insertStmt.executeInsert();
            banco.close();

            Toast toast = Toast.makeText(this, "Dados gravados com sucesso!", Toast.LENGTH_LONG);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }

        finish();
        onDestroy();
        // startActivity(new Intent(this, TelaPrincipalActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Salva a URL para utiliza-la quando essa tela for re-aberta
        SharedPreferences preferencias = getSharedPreferences(
                "configuracao", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("url_imagem", edtUrl.getText().toString());
        editor.commit();
    }

    public void baixarImagemClick(View v){
        new DownloadImagemAsyncTask().execute(
                edtUrl.getText().toString());
    }

    //classe interna
    class DownloadImagemAsyncTask extends
            AsyncTask<String, Void, Bitmap> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {//iniciando um dialog
            super.onPreExecute();
            dialog = ProgressDialog.show(
                    Perfil.this,
                    "Aguarde", "Carregando a  imagem...");
        }

        @Override
        protected Bitmap doInBackground(String... params) {//método que carregara a imagem da URL passada
            String urlString = params[0];

            try {
                URL url = new URL(urlString);//url passada
                HttpURLConnection conexao = (HttpURLConnection)
                        url.openConnection();//criando conexão *necessida permissão no maninfest
                conexao.setRequestMethod("GET");
                conexao.setDoInput(true);
                conexao.connect();

                //carregara a stream de dados e setará ela em um bitmap
                InputStream is = conexao.getInputStream();
                Bitmap imagem = BitmapFactory.decodeStream(is);
                return imagem;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {//tratamento após a execução
            super.onPostExecute(result);
            dialog.dismiss();//encerra o dialog iniciado nessa classe interna
            if (result != null){
                ImageView img = (ImageView)findViewById(R.id.imageViewUsuario);
                img.setImageBitmap(result);
            } else {//caso não encontre a imagem exibe um Alert para o usuário
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Perfil.this).
                                setTitle("Erro").
                                setMessage("Não foi possivel carregar imagem, tente novamente mais tarde!").
                                setNeutralButton("OK", null);//setPositiveButton("OK", null);
                builder.create().show();
                /**Verificar esse Dialog
                 * Método parece estar com problema.
                 * */
            }
        }
    }

    //método para o usuário escolher de onde capturar a imagem interna no celular, este que e chamado
    public void dialogoDecisao(View view){
        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setTitle("De onde deseja Capturar Imagem?").create();
        dialog.setButton("Câmera", new DialogInterface.OnClickListener() {//método obsoleto
            public void onClick(DialogInterface dialog, int which) {
                capturarImagemCamera();
            }
        });
        dialog.setButton2("Galeria", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                capturarImagemGaleria();
            }
        });
        dialog.show();

        /*existe tb a opção de setar um botão nulo com o comando:
            dialog.setNeutralButton("ok", null);*/

        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("De onde deseja Capturar Imagem?")
                .setPositiveButton("Câmera", this)
                .setNegativeButton("Galeria", this);
       builder.create().show();*/
    }

    public void capturarImagemCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 5678);
    }

    public void capturarImagemGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//new Intent(Intent.ACTION_OPEN_DOCUMENT);
        /*intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "selecione uma Imagem"), 1234);*/
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    public String getPath(Uri uri){
        if(uri == null){
            return null;
        }

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);

        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    //método que tratará o resultado das buscar internas no celular
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_OPEN &&  resultCode == RESULT_OK && data != null){
            //IMAGEM VEIO DA GALERIA
            Uri uriImageGaleria = data.getData();
            String[] caminhoImagem = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uriImageGaleria, caminhoImagem, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndex(caminhoImagem[0]);
            String caminho = cursor.getString(column_index);
            cursor.close();
            Bitmap bitmap = BitmapFactory.decodeFile(caminho);
            usuario.setImageBitmap(bitmap);

        } else if(requestCode == 5678 && resultCode == RESULT_OK){
            //imagem veio da camera
            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");
            usuario.setImageBitmap(imagem);
        }
    }

    //método para alterar a cor do nome do usuário
    private void updateTextColor() {

        int red = seekBarRED.getProgress();
        int green = seekBarGREEN.getProgress();
        int blue = seekBarBLUE.getProgress();

        nomePerfil.setTextColor(0xff000000 + red * 0x10000
                + green * 0x100 + blue);
    }

    @Override
    public void onClick(View v) {
        nomePerfil.setText(recebeNomePerfil.getText().toString());
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which){//método substituido, problemas no positive e negative buttons
        switch (which){
            case DialogInterface.BUTTON_POSITIVE:
                capturarImagemCamera();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                capturarImagemGaleria();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updateTextColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Toast toast = Toast.makeText(this, "Mudando Cor!", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast toast = Toast.makeText(this, "Cor Mudada!", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (azul.isChecked()) {
            seekBarBLUE.setEnabled(true);
        }else if(!azul.isChecked()){
            seekBarBLUE.setEnabled(false);
        }

        if (verde.isChecked()) {
            seekBarGREEN.setEnabled(true);
        } else if (!verde.isChecked()) {
            seekBarGREEN.setEnabled(false);
        }

        if (vermelho.isChecked()) {
            seekBarRED.setEnabled(true);
        } else if (!vermelho.isChecked()) {
            seekBarRED.setEnabled(false);
        }
    }


    public void exibirPerfil(View v){
        Intent it = new Intent(this, ExibirPerfil.class);
        it.putExtra("nome", nomePerfil.getText().toString());
        it.putExtra("idade", 25);
        startActivity(it);
    }

    public void alert(Activity context,String msg) {
        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context).setTitle("Informação!!!").setMessage(msg).create();
        dialog.setButton("OK", new DialogInterface.OnClickListener() {//método obsoleto
            public void onClick(DialogInterface dialog, int which) {
                return;//existe tb a opção de setar um botão nulo com o comando:
            }           //dialog.setNeutralButton("ok", null);
        });
        dialog.show();
    }

    public void deletarBanco(){
        try {
            deleteDatabase("perfil");
            alert(this, "deletado");
        }catch (Exception e){
            alert(this, "erro Deletado.");
        }
    }

    public void salvar(View view){
        inserirDados();
    }

    public void fecharBanco(){
        try{
            banco.close();
        }catch (Exception erro){
            alert(this, "Erro ao fechar banco");
        }
    }

}
