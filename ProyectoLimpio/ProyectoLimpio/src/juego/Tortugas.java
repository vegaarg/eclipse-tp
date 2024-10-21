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
    boolean lado;
    Color myColor = Color.red;
	public boolean estaApoyado;
	public boolean detectarColision;

    public Tortugas(int x, int y, int alto, int ancho){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img =  Herramientas.cargarImagen("recursos/tortuga.png");
        lado = false;
        this.estaApoyado = false;
        this.detectarColision= false;
    }

    public void dibujarHitbox(Entorno entorno){                                  //Dibuja un rectangulo azul. Esto vamos a usarlo
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, myColor);         //despues para ver la hitbox de la plataforma.
    }
    public void dibujarse(Entorno entorno){
        entorno.dibujarImagen(this.img, this.x, this.y, 0, 0.1);   //Dibuja al personaje en pantalla
    }
    public void movimientoIzquierda() {
        if (this.x > 0) {
            this.x -= 1;
            this.img = Herramientas.cargarImagen("recursos/tortuga.png");
            lado = true;
        }
    }

    public void movimientoDerecha() {
        if (this.x < 800) {
            this.x += 1;
            this.img = Herramientas.cargarImagen("recursos/tortuga.png");
            lado = false;

        }
    }
    public void saltoYCaida(Entorno e){
        if(!this.estaApoyado){
            this.y += 3;
        }
    }

    public boolean detectarColision(int x, int y, int w, int h){                                                // Detecta colision.
        return this.x < (x + w) + 15 && this.x + this.ancho > x - 50 && this.y < y + h && this.y + this.alto > y + 3;
        
    }


}