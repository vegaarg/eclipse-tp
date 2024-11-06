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
    }
}