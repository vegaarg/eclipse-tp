package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
	public class Bombas {
	    int x;
	    int y;
	    int ancho;
	    int alto;
	    int velocidad = 1;
	    boolean lado;
	    Image img;
	    Color myColor = Color.yellow;
	    public Bombas(int x, int y, int alto, int ancho, boolean lado, Image img){
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
	    	if (this.x > -50) {
	            this.x -= velocidad;
	        }
	    }
	    
	    public void movimientoDerecha() {
	        if (this.x < 850) {
	            this.x += velocidad;
	        }
	    }

		public boolean detectarBola(int x, int y, int w, int h){                                                // Detecta colision.
        return this.x < (x + w) + 15 && this.x + this.ancho + 5 > x && this.y < (y + h) && this.y + this.alto > y;
		}
	    
	}