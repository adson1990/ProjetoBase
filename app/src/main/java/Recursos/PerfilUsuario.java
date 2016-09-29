package Recursos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adson on 24/09/2016.
 *
 * classe criada com a função de auxiliar a transferência
 * de informações da tela de perfil para a tela de exibirPerfil
 * usando o método de parcelable
 *
 * @see projetobase.projeto.app.com.br.projetobase.Perfil
 */

public class PerfilUsuario implements Parcelable {

    /**
     * uma outra maneira mais rápida porém consome mais processamento
     * seria implementar Serializable, assim poderia ser passado um
     * objeto do tipo PerfilUsuario diretamente na it.putextra("usu", new PerfilUsuario("adson", 25));
     * */

    private String nome;
    private int idade;
    private String usuario;
    private String nomePerfil;
    private String email;
    private int telefone;

    public PerfilUsuario(String nome, int idade){
        this.nome = nome;
        this.idade = idade;
    }

    public PerfilUsuario(String usuario, String nomePerfil,
                         String email, int telefone){
        this.usuario = usuario;
        this.nomePerfil = nomePerfil;
        this.email = email;
        this.telefone = telefone;
    }

    public PerfilUsuario(){};

    private PerfilUsuario(Parcel from){
        usuario = from.readString();
        nomePerfil = from.readString();
        email = from.readString();
        telefone = from.readInt();
    }

    public static final Parcelable.Creator<PerfilUsuario>
                CREATOR = new Parcelable.Creator<PerfilUsuario>(){

        public PerfilUsuario createFromParcel(Parcel in){
            return new PerfilUsuario(in);
        }
        public PerfilUsuario[] newArray(int size){
            return new PerfilUsuario[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(telefone);
        dest.writeString(usuario);
        dest.writeString(nomePerfil);
        dest.writeString(email);
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNomePerfil() {
        return nomePerfil;
    }

    public String getEmail() {
        return email;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setNomePerfil(String nomePerfil) {
        this.nomePerfil = nomePerfil;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }
}
