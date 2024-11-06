package juego;

import java.awt.Image;

import entorno.Herramientas;

public class Nave {
	int x;
    int y;
    int ancho;
    int alto;
    Image encendido;
	Image apagado;
	boolean estaEncendido;
	boolean direccion;
	
	public Nave(int x, int y,int ancho, int alto){
		this.x = x;
		this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.direccion= false;
		apagado = Herramientas.cargarImagen("recursos/Nave2.gif");
		encendido = Herramientas.cargarImagen("recursos/nave.gif");
	}

	public void moverAdelante(double mouseX) {
	    if (mouseX > this.x) { // Si el mouse está a la derecha de la nave, mover a la derecha
	    	this.direccion= false;
	        this.x += 1; // Ajusta la velocidad si es necesario
	    } 
	    else if (mouseX < this.x) {// Si el mouse está a la izquierda de la nave, mover a la izquierda
	    	this.direccion= true;
	        this.x -= 1; // Ajusta la velocidad si es necesario
	    } 
	}
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	public double getAncho() {
		return ancho;
	}
	public double getAlto() {
		return alto;
	}

	public void encender() {
		this.direccion = true;
	}
	
	public void apagar() {
		this.direccion = false;
	}
}