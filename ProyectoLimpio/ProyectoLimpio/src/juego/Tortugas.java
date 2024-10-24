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
    int velocidad = 1;
    Color myColor = Color.red;
	public boolean estaApoyado;
    boolean direccionDefinida;
	public boolean detectarColision;
	public int contTicks;

    public Tortugas (int x, int y, int ancho, int alto, boolean lado){
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
        this.x -= velocidad;
        this.img = Herramientas.cargarImagen("recursos/tortuga.png");
    }
}

public void movimientoDerecha() {
    if (this.x < 800) {
        this.x += velocidad;
        this.img = Herramientas.cargarImagen("recursos/tortuga2.png");
    }
}

public boolean detectarColision(int x, int y, int w, int h){                                                // Detecta colision.
    return this.x < (x + w) + 15 && this.x + this.ancho > x - 50 && this.y < y + h && this.y + this.alto > y + 3;
    
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
        boolean apoyado = this.detectarColision(isla.x, isla.y, isla.ancho, isla.alto);                /// deberia ser un metodo
        this.estaApoyado = apoyado;
        return apoyado || tortugaEstaApoyado(isla.izq) || tortugaEstaApoyado(isla.der);
    }
    public void moverTortuga(Islas isla) {
            this.estaApoyado = tortugaEstaApoyado(isla);

            if (this.estaApoyado) {
                if (!direccionDefinida) {
                    this.lado = Utilidades.randomBoolean(); //random();
                    direccionDefinida = true;
                }
                if (this.lado) {
                    this.movimientoDerecha();

                } else {
                    this.movimientoIzquierda();
                }
            }

        if (!this.estaApoyado) {
            this.y += 3;                                           // :tiger2:
            direccionDefinida = false;                            // Caida de tortuga
        }
    }
    
    

} 
