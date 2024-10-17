package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import entorno.Entorno;
import entorno.Herramientas;

public class Tortugas {
    int x;
    int y;
    int ancho;
    int alto;
    Image img;
    Color myColor = Color.red;

    public Tortugas(int x, int y, int alto, int ancho){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img =  Herramientas.cargarImagen("recursos/tortuga.png");
    }

    public void dibujarHitbox(Entorno entorno){                                  //Dibuja un rectangulo azul. Esto vamos a usarlo
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, myColor);         //despues para ver la hitbox de la plataforma.
    }
    public void dibujarse(Entorno entorno){
        entorno.dibujarImagen(this.img, this.x, this.y, 0, 0.1);   //Dibuja al personaje en pantalla
    }
}
