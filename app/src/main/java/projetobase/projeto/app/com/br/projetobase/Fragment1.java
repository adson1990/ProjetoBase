package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Adson on 30/08/2016.
 *
 * Classe usada de suport para exemplificar o ciclo de vida de fragment
 * todas as classes que extendem de fragment seu ciclo de vida é gerenciado
 * pela activity que extende de FragmentActivity (pai)
 *
 * @see Fragment
 */
public class Fragment1 extends Fragment {

    TextView tv;

    //principal método da criação do fragmente
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_sobre, null);
        tv = (TextView) view.findViewById(R.id.text1);
        tv.setText("Não Visto ainda");

        return(view);
    }

    public void mudaTexto(String texto){
        TextView tv = (TextView) getView().findViewById(R.id.text1);
        tv.setText(texto);
    }
}