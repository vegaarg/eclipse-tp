package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Tortugas {
    int x;
    int y;
    int ancho;
    int alto;
    Image img;
    Image izq;
    Image der;
    boolean lado;
    int velocidad = 1;
    Color myColor = Color.red;
	public boolean estaApoyado;
    boolean direccionDefinida;
	public boolean detectarColision;
	public int contTicks;
	public boolean enCaida;

    public Tortugas (int x, int y, int ancho, int alto, boolean lado){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.img =  Herramientas.cargarImagen("recursos/tortuga.gif");
        this.der =  Herramientas.cargarImagen("recursos/tortugaIzq.gif");
        this.izq =  Herramientas.cargarImagen("recursos/tortugaDer.gif");
        lado = false;
        this.estaApoyado = false;
        this.detectarColision= false;
        this.enCaida = false;
    }

    public void dibujarse(Entorno entorno){
        entorno.dibujarImagen(this.img, this.x, this.y, 0, 0.1);   //Dibuja al personaje en pantalla
    }

public void movimientoIzquierda() {
    if (this.x > 0) {
        this.x -= velocidad;
        this.img = Herramientas.cargarImagen("recursos/tortugaDer.gif");
    }
}

public void movimientoDerecha() {
    if (this.x < 800) {
        this.x += velocidad;
        this.img = Herramientas.cargarImagen("recursos/tortugaIzq.gif");
    }
}

public boolean detectarColision(int x, int y, int w, int h){                                                // Detecta colision.
    return this.x < (x + w) - 20 && this.x + this.ancho > x - 10 && this.y < y + h && this.y + this.alto > y + 3;
    
}

public boolean detectarPep(int x, int y, int w, int h){                                                // Detecta colision.
    return this.x < (x + w) + 8 && this.x + this.ancho + 15 > x && this.y < (y + h) && this.y + this.alto > y + 30;
    
}
public boolean detectarBola(int x, int y, int w, int h){                                                // Detecta colision.
    return this.x < (x + w) + 15 && this.x + this.ancho + 5 > x && this.y < (y + h) && this.y + this.alto > y;
}

public boolean cooldown() {
    if (contTicks > 20) {
        return false;
    }
    contTicks += 1;
    return true;
}
    public boolean tortugaEstaApoyado(Islas isla) {
        if (isla == null) return false;
        boolean apoyado = this.detectarColision(isla.x, isla.y, isla.ancho, isla.alto);
        this.estaApoyado = apoyado;
        return apoyado || tortugaEstaApoyado(isla.izq) || tortugaEstaApoyado(isla.der);
    }
    public void moverTortuga(Islas isla) {
            this.estaApoyado = tortugaEstaApoyado(isla);

            if (this.estaApoyado) {
                if (!direccionDefinida) {
                    this.lado = Utilidades.randomBoolean();
                    direccionDefinida = true;
                }
                if (this.lado) {
                    this.movimientoDerecha();

                } else {
                    this.movimientoIzquierda();
                }
            }        
    }
// Caida de tortuga
    public void gravedadTortuga(Islas isla) {
        this.estaApoyado = tortugaEstaApoyado(isla); // Verifica si la tortuga está apoyada en la isla
        if (!this.estaApoyado && enCaida== true) {
            this.y += 1; // Aplica gravedad para que caiga
        }
    }

    public void direccionTortuga(Islas isla) {
        if (!estaApoyado) { // Cambia la dirección de la tortuga cuando esté apoyada
            lado = !lado; // Alterna la dirección si está apoyada
        }
	        if (lado) { // Movimiento en la dirección actual
	        	this.x -=1;
	            movimientoIzquierda();
	        } else {
	        	this.x +=1;
	            movimientoDerecha();
	        }
	        return;
    }

    public void actualizar(Islas isla) {
        if (enCaida) {
            gravedadTortuga(isla);
            if (estaApoyado) {
                enCaida = false; // Detiene la caída al tocar el suelo
            }
        } else {
            direccionTortuga(isla); // Mueve lateralmente si no está cayendo
        }
    }
}