package juego;

import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    // El objeto Entorno que controla el tiempo y más
    private Entorno entorno;

    // Variables del juego
    private Image Fondo;
    private Pep pep;
    private BolaDeFuego bola;
    private Tortugas tort1;
    private Gnomo gnomo1;
    private Islas islaBase = new Islas(400, 100, 25, 5);

    // Constantes

    public Juego() {
        // Inicializa el objeto entorno
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
        inicializarJuego();
        this.entorno.iniciar();
    }

    private void inicializarJuego() {
        // Inicialización de recursos
        Fondo = Herramientas.cargarImagen("recursos/fondo.png");
        Islas.popular(4, islaBase);  // Popular las islas recursivamente

        // Inicializar personajes
        pep = new Pep(200, 250, 50, 30, Herramientas.cargarImagen("recursos/pepDer.png"));
        tort1 = new Tortugas(200, 200, 30, 50);
        gnomo1 = new Gnomo(400, 70, 15, 30);
    }

    @Override
    public void tick() {
        // Procesamiento de tiempo
        entorno.dibujarImagen(Fondo, 400, 300, 0); // Fondo del juego

        // Dibujar cosas en la pantalla
        pep.dibujarse(entorno);
        tort1.dibujarse(entorno);
        gnomo1.dibujarse(entorno);
        dibujarIslas(islaBase); // Dibujar islas

        // Manejo de entrada para el movimiento de Pep
        manejarMovimientoPep();
        pep.saltoYCaida(entorno); // Manejo de gravedad y saltos

        // Manejo de la bola de fuego
        manejarBolaDeFuego();

        // Manejo de colisiones
        manejarColisiones();

        // Movimiento del gnomo
        moverGnomo();
    }
    /// movimiento de pep
    private void manejarMovimientoPep() {
        if (entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            pep.movimientoIzquierda();
        }
        if (entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            pep.movimientoDerecha();
        }
        if ((entorno.sePresiono('w') || entorno.sePresiono(entorno.TECLA_ARRIBA)) && pep.estaApoyado) {
            pep.saltando = true;
        }
    }
    /// Funcion de bola de fuego
    private void manejarBolaDeFuego() {
        if (!pep.cooldown()) {
            if (entorno.sePresiono('c')) {
                bola = new BolaDeFuego(pep.x, pep.y, 20, 20, pep.lado, 
                    Herramientas.cargarImagen(pep.lado ? "recursos/bolaDeFuego2.gif" : "recursos/bolaDeFuego1.gif"));
                pep.contTicks = 0;
            }
        } else if (bola != null) {
            bola.dibujarse(entorno);
            if (bola.lado) {
                bola.movimientoIzquierda();
            } else {
                bola.movimientoDerecha();
            }
        }
    }
    ///colisiones al estar en plataformas
    private void manejarColisiones() {
        pep.estaApoyado = estaApoyado(islaBase);
        gnomo1.estaApoyado = gnomoEstaApoyado(islaBase);
    }
    //mover el gnomo   ////pensaba en hacerlo aleatorio pero al siempre estar confirmando su colicion, el gnomo no para de cambiar de direccion
    private void moverGnomo() {
        if (!gnomo1.estaApoyado) {
            gnomo1.y += 2;
        } else {
        	int direccionGnomo = direccion();
            if (direccionGnomo == 1 ) {
                gnomo1.movimientoIzquierda();
            } else {
                gnomo1.movimientoDerecha();
            }
        }
    }

    public static void main(String[] args) {
        new Juego();
    }

    public void dibujarIslas(Islas isla) {
        if (isla == null) return;
        isla.dibujarse(entorno);
        dibujarIslas(isla.izq);
        dibujarIslas(isla.der);
    }

    public boolean estaApoyado(Islas isla) {
        if (isla == null) return false;
        return pep.detectarColision(isla.x, isla.y, isla.ancho, isla.alto) ||
               estaApoyado(isla.izq) || estaApoyado(isla.der);
    }

    public boolean gnomoEstaApoyado(Islas isla) {
        if (isla == null) return false;
        boolean apoyado = gnomo1.detectarColision(isla.x, isla.y, isla.ancho, isla.alto);
        gnomo1.estaApoyado = apoyado;
        return apoyado || gnomoEstaApoyado(isla.izq) || gnomoEstaApoyado(isla.der);
    }

    public static int direccion() {
        Random random = new Random(); // Crear una nueva instancia de Random
        return random.nextInt(10);
	}
}
