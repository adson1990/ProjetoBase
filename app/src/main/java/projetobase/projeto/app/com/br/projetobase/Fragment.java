package projetobase.projeto.app.com.br.projetobase;

import android.os.Bundle;
import android.view.View;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Adson on 30/08/2016.
 *
 * classe criada para exemplificar o uso de fragment
 *
 * @see Fragment1
 */


public class Fragment extends android.support.v4.app.FragmentActivity implements View.OnClickListener {
    FragmentManager fm = getSupportFragmentManager();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            //usando fragment via API

            //fragments criadas
            Fragment1 frag1 = new Fragment1();
            Fragment2 frag2 = new Fragment2();
            Fragment3 frag3 = new Fragment3();

            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.layout_inferior, frag1, "frag1");//layout usado para exibir as telas, tela que sera exibida no lugar do fragment, TAG
            ft.commit();//quando se trabalha com Fragment Transection é obrigatório o commit
        }

    }

    public void clickBotao(View view) {

        switch (view.getTag().toString()) {
            case "bt1":
                //
                Fragment1 frag11 = (Fragment1) fm.findFragmentByTag("frag1");

                if (frag11 != null) {
                    frag11.mudaTexto("VISTO");
                }
                break;
            case "bt2":
                Fragment1 frag1 = new Fragment1();

                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.layout_inferior, frag1, "frag1");
                ft.addToBackStack("pilha");//faz com que a ordem de click seja guardada para voltar em caso de clique no botão de anterior
                ft.commit();
                break;
            case "bt3":
                Fragment2 frag2 = new Fragment2();

                FragmentTransaction ft2 = fm.beginTransaction();
                ft2.replace(R.id.layout_inferior, frag2, "frag2");
                ft2.addToBackStack("pilha");
                ft2.commit();
                break;
            case "bt4":
                Fragment3 frag3 = new Fragment3();

                FragmentTransaction ft3 = fm.beginTransaction();
                ft3.replace(R.id.layout_inferior, frag3, "frag3");
                ft3.addToBackStack("pilha");
                ft3.commit();
                break;
        }


        //frangment no xml
        /*Fragment1 frag1 = (Fragment1) fm.findFragmentById(R.id.fragment1);
        frag1.mudaTexto("VISTO");*/
    }

    @Override
    public void onClick(View v) {
    }
}