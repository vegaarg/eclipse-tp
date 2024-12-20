package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import static java.lang.Math.min;

public class Pep {

    int x;
    int y;
    int ancho;
    int alto;
    Image lanzaDer;
    Image lanzaIzq;
    Image imgDer;
    Image imgIzq;
    Image imgMovDer;
    Image imgMovIzq;
    Image imgBola;
    Color myColor = Color.green;
    boolean seMueve;
    boolean estaApoyado;
    boolean saltando;
    boolean lanzando;
    BolaDeFuego bola;
    int longSalto;
    int contTicks = 0;
    int velocidad = 3;
    boolean lado;

    public Pep(int x, int y, int alto, int ancho, Image img){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.lanzaDer = Herramientas.cargarImagen("recursos/pepLanza.png");
        this.lanzaIzq = Herramientas.cargarImagen("recursos/peplanza2.png");
        this.imgDer = Herramientas.cargarImagen("recursos/idlepepDer.gif");
        this.imgIzq = Herramientas.cargarImagen("recursos/idlepepIzq.gif");
        this.imgMovDer = Herramientas.cargarImagen("recursos/mover.gif");
        this.imgMovIzq = Herramientas.cargarImagen("recursos/moverizq.gif");
        this.seMueve = false;
        this.estaApoyado = false;
        this.saltando = false;
        this.lanzando = false;
        this.longSalto=0;
        lado = false; // Lado de Pep (false=der, true=izq)
    }
    
    public void dibujarHitbox(Entorno entorno){ // Dibuja un rectangulo azul para ver su colisión
        entorno.dibujarRectangulo(x, y, ancho, alto, 0, myColor); // despues para ver la hitbox de la plataforma.
    }
    public void dibujarse(Entorno entorno) {
        if (lanzando) { 
            if (lado) { 
                entorno.dibujarImagen(this.lanzaIzq, this.x, this.y, 0, 0.7); // Dibuja el PNG de lanzamiento a la izquierda
            } else {
                entorno.dibujarImagen(this.lanzaDer, this.x, this.y, 0, 0.7); // Dibuja el PNG de lanzamiento a la derecha
            } 
        } else if (seMueve) { // Si no está lanzando, verifica si se está moviendo
            if (lado) {
                entorno.dibujarImagen(this.imgMovIzq, this.x, this.y, 0, 0.7); // Dibuja el GIF de movimiento a la izquierda
            } else {
                entorno.dibujarImagen(this.imgMovDer, this.x, this.y, 0, 0.7); // Dibuja el GIF de movimiento a la derecha
            }
        } else { // Si no está lanzando ni moviéndose, está quieto
            if (lado) {
                entorno.dibujarImagen(this.imgIzq, this.x, this.y, 0, 0.7); // Usa la imagen de quieto hacia la izquierda
            } else {
                entorno.dibujarImagen(this.imgDer, this.x, this.y, 0, 0.7); // Usa la imagen de quieto hacia la derecha
            }
        }
        
    }


    public void movimientoIzquierda() {
        if (this.x > 0) {
            this.x -= velocidad;
            lado = true; // Cambia la dirección a izquierda
            seMueve = true; // Cambia a verdadero cuando se mueve
        }
    }

    public void movimientoDerecha() {
        if (this.x < 800) {
            this.x += velocidad;
            lado = false; // Cambia la dirección a derecha
            seMueve = true; // Cambia a verdadero cuando se mueve
        }
    }
    public void detenerMovimiento() {
        this.seMueve = false;  // Cambia el estado a detenido
    }
    public void saltoYCaida(Entorno e) {
        if(!this.estaApoyado && !saltando) { // Si no esta apoyado y no esta saltando, va cayendo (aumenta su y) por tick
            this.y += 4;
        }
        if(saltando) { // Si esta saltando (se activa la tecla) le resta el y. Esta es la velocidad a la que va
            if(!lado){
            	this.y -= 6;
                this.longSalto++; // a saltar y va por ticks. longSalto es el limite a lo que llega el salto
            }else{
            	this.y -= 6; // a saltar y va por ticks. longSalto es el limite a lo que llega el salto
            	this.longSalto++;
        }
        if (this.longSalto > 25){ // Chequea si la longitud de salto pasa del número de que tan alto puede saltar, si es mayor, define al booleano saltando como falso y reeseta la longitud
            saltando=false;
            if(!lado){
                this.x += 40;
            } else {
                this.x -= 40;
            }
            this.longSalto=0;
            }
        }
    }

    public boolean detectarColision(int x, int y, int w, int h){ // Detecta colision
        return this.x < (x + w) && this.x + this.ancho > x && this.y < y + h && this.y + this.alto > y + 13;
    }
    public boolean detectarBomba(int x, int y, int w, int h){                                                // Detecta colision.
        return this.x < (x + w) + 15 && this.x + this.ancho + 5 > x && this.y < (y + h) && this.y + this.alto > y;
    }

    public String ladoColision(int x, int y, int w, int h){
        if (detectarColision(x, y, w, h)){
            float overlapX1 = (this.x + this.ancho) - x;
            float overlapX2 = (x + w) - this.x;
            float overlapY1 = (this.y + this.alto) - y;
            float overlapY2 = (y + h) - this.y;
            float minOverlapX = min(overlapX1, overlapX2);
            float minOverlapY = min(overlapY1, overlapY2);

            if (minOverlapX < minOverlapY) {
                // Collision on X-axis
                if (this.x < x) {
                    return "izquierda";
                } else {
                    return "derecha";
                }
            } else {
                // Collision on Y-axis
                if (this.y < y) {
                    return "arriba";
                } else {
                    return "abajo";
                }
            }
        }
        return "";
    }

    public boolean cooldown() {
        if (contTicks > 80) {
            return false;
        }
        contTicks += 1;
        return true;
    }
    public int limiteSuperior() {
    	return this.y - this.alto / 2;
    	}
    public int limiteInferior() {
    	return this.y + this.alto / 2;
    	}
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    public int getAncho() {
        return this.ancho;
    }

    public int getAlto() {
        return this.alto;
    }
}