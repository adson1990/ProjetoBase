package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Adson on 31/08/2016.
 */
public class Fragment3 extends Fragment {

    //principal método da criação do fragmente
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_ajuda, null);


        return(view);
    }
}
