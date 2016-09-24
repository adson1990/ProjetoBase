package Recursos;


/**
 * Created by Adson on 23/01/2016.
 * Classe criada para validar CPF inserido
 */

public class ValidaCPF {

    double n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11;
    double res1, res2, res3, res4, res5, res6, res7, res8, res9, res10, total1, total2;


    public boolean validaCPF(String cpf){

        n1 = Double.parseDouble(cpf.substring(0, 1));
        n2 = Double.parseDouble(cpf.substring(1, 2));
        n3 = Double.parseDouble(cpf.substring(2, 3));
        n4 = Double.parseDouble(cpf.substring(3, 4));
        n5 = Double.parseDouble(cpf.substring(4, 5));
        n6 = Double.parseDouble(cpf.substring(5, 6));
        n7 = Double.parseDouble(cpf.substring(6, 7));
        n8 = Double.parseDouble(cpf.substring(7, 8));
        n9 = Double.parseDouble(cpf.substring(8, 9));
        n10 = Double.parseDouble(cpf.substring(9, 10));
        n11 = Double.parseDouble(cpf.substring(10, 11));

        res1 = n1*10;
        res2 = n2*9;
        res3 = n3*8;
        res4 = n4*7;
        res5 = n5*6;
        res6 = n6*5;
        res7 = n7*4;
        res8 = n8*3;
        res9 = n9*2;

        total1 = res1+res2+res3+res4+res5+res6+res7+res8+res9;
        total1 = (total1 * 10) % 11;

        if(total1 == 10) {
            total1 = 0;
        }

        if (total1 != n10) {
            return false;
        } else {
            res1 = n1 * 11;
            res2 = n2 * 10;
            res3 = n3 * 9;
            res4 = n4 * 8;
            res5 = n5 * 7;
            res6 = n6 * 6;
            res7 = n7 * 5;
            res8 = n8 * 4;
            res9 = n9 * 3;
            res10 = n10 * 2;

            total2 = res1 + res2 + res3 + res4 + res5 + res6 + res7 + res8 + res9 + res10;
            total2 = (total2 * 10) % 11;

            if(total2 == 10){
                total2 = 0;
            }

            if (total2 != n11) {
                return false;
            }
            return true;
        }
    }
}
