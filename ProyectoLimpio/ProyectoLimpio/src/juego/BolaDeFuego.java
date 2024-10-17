package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import entorno.Entorno;
import entorno.Herramientas;
public class BolaDeFuego {
    int x;
    int y;
    int ancho;
    int alto;
    boolean lado;
    Image img;
    Color myColor = Color.yellow;
    public BolaDeFuego(int x, int y, int alto, int ancho, boolean lado, Image img){
        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;
        this.img = img;
        this.lado = lado;
    }

    public void dibujarse(Entorno entorno){
        entorno.dibujarImagen(this.img, this.x, this.y, 0, 0.3);    //Dibuja la bola de fuego en pantalla
    }

    public void movimientoIzquierda() {
        if (this.x > 0) {
            this.x -= 5;
        }
    }
    public void movimientoDerecha() {
        if (this.x < 800) {
            this.x += 5;
        }
    }
}