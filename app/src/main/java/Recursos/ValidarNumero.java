package Recursos;

/**
 * Created by Adson on 31/01/2016.
 * Classe criada para validar um número de celular ou convencional.
 */

public class ValidarNumero {

    boolean resposta = false;
    int[] ddds = {11,12,13,14,15,16,17,18,19,
            21,22,24,27,28,31,32,33,34,35,37,38,41,42,43,44,45,46,47,48,49,
            51,53,54,55,61,62,63,64,65,66,67,68,69,71,73,74,75,77,79,81,82,
            83,84,85,86,87,88,89,91,92,93,94,95,96,97,98,99};

    public boolean verificaTel(String tel){
        String ddd = tel.substring(0,1) + tel.substring(1,2);
        int recebeNumero = Integer.parseInt(ddd);

        int numero = Integer.parseInt(tel.substring(2,tel.length()));
        int conta=0;

        um:
        for (int i=0; i < ddds.length; i++) {
            if(recebeNumero == ddds[i]){
                conta++;
                break um;
            }
        }

        if(tel.codePointCount(2, tel.length()) == 9 || tel.codePointCount(2, tel.length()) == 8){
            conta++;
        }

        if(conta == 2){
            resposta = true;
        }

        return resposta;
    }
}
