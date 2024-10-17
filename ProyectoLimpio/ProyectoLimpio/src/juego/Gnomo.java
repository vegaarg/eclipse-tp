package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import entorno.Entorno;
import entorno.Herramientas;

import java.util.Random;
import java.util.random.*;
public class Gnomo {
    int x;
    int y;
    int ancho;
    int alto;
    boolean estaApoyado;
    boolean lado;
    Image img;
    Color myColor = Color.ORANGE;

    public Gnomo(int x, int y, int ancho, int alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img = Herramientas.cargarImagen("recursos/gnomo1.png");
        this.img = Herramientas.cargarImagen("recursos/gnomo2.png");
        lado = false;
        this.estaApoyado = false;
    }

    public void dibujarHitbox(Entorno entorno){                                  // Dibuja un rectangulo azul. Esto vamos a usarlo
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, myColor);         // despues para ver la hitbox de la plataforma.
    }

    public void dibujarse(Entorno entorno){
        entorno.dibujarImagen(this.img, this.x, this.y, 0, 1);     //Dibuja al personaje en pantalla
        
    }
    public void movimientoIzquierda() {
        if (this.x > 0) {
            this.x -= 1;
            this.img = Herramientas.cargarImagen("recursos/gnomo1.png");
            lado = true;
        }
    }

    public void movimientoDerecha() {
        if (this.x < 800) {
            this.x += 1;
            this.img = Herramientas.cargarImagen("recursos/gnomo2.png");
            lado = false;
        }
    }

//    public void saltoYCaida(Entorno e){
//        if(!this.estaApoyado){
//            this.y += 4;
//        }
//    }

    public boolean detectarColision(int x, int y, int w, int h){                                                // Detecta colision.
        return this.x < (x + w) + 25 && this.x + this.ancho > x - 25 && this.y < y + h && this.y + this.alto > y + 5;
        
    }

	
} 
