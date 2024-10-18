package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import entorno.Entorno;
import entorno.Herramientas;
public class Pep {

    int x;
    int y;
    int ancho;
    int alto;
    Image imgParado;
    Image imgBola;
    Color myColor = Color.green;
    boolean seMueve;
    boolean estaApoyado;
    boolean saltando;
    BolaDeFuego bola;
    int longSalto;
    int contTicks = 0;
    boolean lado;

    public Pep(int x, int y, int alto, int ancho, Image img){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.imgParado = Herramientas.cargarImagen("recursos/pepDer.png");
        this.seMueve = false;
        this.estaApoyado = false;
        this.saltando = false;
        this.longSalto=0;
        lado = false;               // Lado en false equivale a la derecha, en true equivale a la izquierda
    }

    public void dibujarHitbox(Entorno entorno){                                  // Dibuja un rectangulo azul. Esto vamos a usarlo
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, myColor);         // despues para ver la hitbox de la plataforma.
    }
    public void dibujarse(Entorno entorno){
        entorno.dibujarImagen(this.imgParado, this.x, this.y, 0, 0.7);    //Dibuja al personaje en pantalla
    }

    public void movimientoIzquierda() {
        if (this.x > 0) {
            this.x -= 5;
            this.imgParado = Herramientas.cargarImagen("recursos/pepIzq.png");
            lado = true;
        }
    }
    public void movimientoDerecha() {
        if (this.x < 800) {
            this.x += 5;
            this.imgParado = Herramientas.cargarImagen("recursos/pepDer.png");
            lado = false;
        }
    }
    public void saltoYCaida(Entorno e) {
        if(!this.estaApoyado && !saltando) {                                        // Si no esta apoyado y no esta saltando, va cayendo (reduce su y) por tick. El numero
            this.y += 5;                                                          // se puede aumentar o disminuir dependiendo de la velocidad de caida
        }
        if(saltando) {                                                              // Si esta saltando (se activa la tecla) le resta el y. Esta es la velocidad a la que va
            this.y -= 6;                                                            // a saltar y va por ticks. longSalto es el limite a lo que llega el salto, que tambien va
            this.longSalto++;                                                       // por ticks
        }
        if(this.longSalto > 20){                                                    // Chequea si la longitud de salto pasa de un numero. Este numero es el que tan alto puede
            saltando=false;                                                         // puede saltar. Si es mayor, define al booleano saltando como falso y resetea la longitud.
            this.longSalto=0;
        }
    }

    public boolean detectarColision(int x, int y, int w, int h){                                                // Detecta colision.
        return this.x < (x + w) + 20 && this.x + this.ancho > x - 40 && this.y + 5 < y + h && this.y + this.alto > y + 12;
    }

    public boolean cooldown() {
        if (contTicks > 100) {
            return false;
        }
        contTicks += 1;
        return true;
    }


}
