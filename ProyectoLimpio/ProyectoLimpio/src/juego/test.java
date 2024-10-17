package juego;



public class test {
    static Islas islaBase;

    public static void main(String[] args) {

        final int cantNiveles = 4;
        int cantNodos = 2;
        islaBase = new Islas(400, 400, 150, 25);
        Islas islaAux = islaBase;
        int j = 0;

        Islas.popular(4, islaBase);



/*        for(int i = 0; i < cantNiveles; i++) {
            if (islaAux.izq != null && islaAux.der == null){
                Islas islaDer = new Islas(islaAux.x + 70, islaAux.y + 100, 150, 25);
                islaAux.der = islaDer;
                islaAux = islaAux.der;
            }
        } */

/*        while(islaAux.der != null && islaAux.izq != null) {
            Islas islaIzq = new Islas(islaAux.x - 100, islaAux.y - 100, 150, 25);
            System.out.println("x = " + islaIzq.x + " y =" + islaIzq.y);
            islaAux = islaIzq;

        }*/

    }
}