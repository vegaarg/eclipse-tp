package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Gnomo {
    int x;
    int y;
    int ancho;
    int alto;
    int contTicks;
    double velocidad = 1;
    boolean estaApoyado;
    boolean direccionDefinida;
    boolean lado;
    Image imgDer;
    Image imgIzq;
    Color myColor = Color.GREEN;

    public Gnomo(int x, int y, int ancho, int alto, boolean lado){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.imgDer = Herramientas.cargarImagen("recursos/gnomoDer.gif");
        this.imgIzq = Herramientas.cargarImagen("recursos/gnomoIzq.gif");
        this.lado = lado;
        this.estaApoyado = false;
        direccionDefinida = false;
    }

    public void dibujarse(Entorno entorno){
        if (lado) {
            entorno.dibujarImagen(this.imgDer, this.x, this.y, 0, 1); //Dibuja al personaje en pantalla (false=izq, true=der)
        } else {
            entorno.dibujarImagen(this.imgIzq, this.x, this.y, 0, 1);
        }
        
    }
    public void movimientoIzquierda() {
        if (this.x > 0) {
            this.x -= velocidad;
        }
    }

    public void movimientoDerecha() {
        if (this.x < 800) {
            this.x += velocidad;
        }
    }

    public boolean detectarColision(int x, int y, int w, int h){ // Detecta colision
        return this.x < (x + w) - 10 && this.x + this.ancho > x - 30 && this.y < y + h && this.y + this.alto > y + 3;    
    }
    
    public boolean detectarPep(int x, int y, int w, int h){ // Detecta colision de Pep
        return this.x < (x + w) && this.x + this.ancho > x && this.y < y + h && this.y + this.alto > y;
        
    }
    public boolean detectarTortuga(int x, int y, int w, int h){ // Detecta colision de Tortuga
    	return this.x < (x + w) && this.x + this.ancho > x && this.y < y + h && this.y + this.alto > y;
    }
    public boolean detectarNave(int x, int y, int w, int h){ // Detecta colision de Nave
    	return this.x < (x + w) && this.x + this.ancho > x && this.y < y + h && this.y + this.alto > y;
    }

    public boolean cooldown() {
        if (contTicks > 1) {
            return false;
        }
        contTicks += 1;
        return true;
    }

    public boolean gnomoEstaApoyado(Islas isla) {
        if (isla == null) return false;
        boolean apoyado = this.detectarColision(isla.x, isla.y, isla.ancho, isla.alto);                /// deberia ser un metodo
        this.estaApoyado = apoyado;
        return apoyado || gnomoEstaApoyado(isla.izq) || gnomoEstaApoyado(isla.der);
    }
    public void moverGnomo(Islas isla) {
            this.estaApoyado = gnomoEstaApoyado(isla);

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
        if (!this.estaApoyado) {
            this.y += 3;                                          
            direccionDefinida = false; // Caida del gnomo
        }
    }
} 