package Recursos;

import java.util.ArrayList;

/**
 * Created by Adson on 03/06/2016.
 *
 * Classe de auxilio para gravação no banco de dados
 */
public class Chamado {

    private String tipo;
    private int quantidade;
    private ArrayList<String> tipoAcidente = new ArrayList<String>();
    private ArrayList<String> envolvidos = new ArrayList<String>();
    private String dataChamado;

    public Chamado(String tipo, int quantidade, String data){
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataChamado = data;
    }

    public Chamado(){};

    public void setTipoAcidente(String tipoAcid){
        tipoAcidente.add(tipoAcid);
    }

    public ArrayList<String> getTipoAcidente() {
        return tipoAcidente;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getDataChamado() {
        return dataChamado;
    }

    public void setDataChamado(String dataChamado) {
        this.dataChamado = dataChamado;
    }

    public ArrayList<String> getEnvolvidos() {
        return envolvidos;
    }

    public void setEnvolvidos(String envolvidos) {
        this.envolvidos.add(envolvidos);
    }
}
