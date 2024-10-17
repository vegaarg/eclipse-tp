package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import entorno.Entorno;
import entorno.Herramientas;


public class Islas {
    int ancho;
    int alto;
    int x;
    int y;
    Image img;
    Color myColor = Color.blue;
    Islas izq = null;
    Islas der = null;

    public Islas(Islas izq, Islas der, int x, int y, int ancho, int alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img = Herramientas.cargarImagen("recursos/plataforma.png");
        this.izq = izq;
        this.der = der;
    }

    public Islas(int x, int y, int ancho, int alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img = Herramientas.cargarImagen("recursos/plataforma.png");
    }

    public void dibujarHitbox(Entorno entorno){                                  // Dibuja un rectangulo azul. Esto vamos a usarlo
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, myColor);         // despues para ver la hitbox de la plataforma.
    }

    public void dibujarse(Entorno entorno){
        entorno.dibujarImagen(this.img, this.x, this.y, 0, 0.1);    //Dibuja al personaje en pantalla
    }

    @Override
    public String toString() {                                                              /// funcion auxiliar para saber el x e y de las islas
        return "x es = " + this.x + " y es = "+ this.y;
    }

    public static void popular(int n, Islas isla){                                                      /// Funcion recursiva. Se encarga de crear nuevas islas
        if (n == 0){                                                                                    /// de derecha e izquierda.
            return;
        }

        Islas islaIzq = new Islas(isla.x - 80, isla.y + 100, isla.ancho, isla.alto);
        Islas islaDer = new Islas(isla.x + 80, isla.y + 100, isla.ancho, isla.alto);

        isla.der = islaDer;
        isla.izq = islaIzq;

        popular(n-1, isla.izq);
        popular(n-1, isla.der);
    }

}
