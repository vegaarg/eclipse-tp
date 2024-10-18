package juego;

import java.awt.Color;
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
	private Gnomo gnomo;
	private Gnomo[] gnomosLista;
	private Islas islaBase = new Islas(400, 80, 50, 5);
	private int gnomosSalvados;
	private int gnomosPerdidos;
	private boolean randomBooleano;
	private Random random;
	private boolean direccionDefinida = false;
	private boolean direccionMovimiento;
	private int prueba = 0;

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
		this.gnomosSalvados = 0;
		this.gnomosPerdidos = 0;

        // Inicializar personajes
        pep = new Pep(400, 450, 50, 30, Herramientas.cargarImagen("recursos/pepDer.png"));
        tort1 = new Tortugas(200, 200, 30, 50);
        gnomo = new Gnomo(400, 50, 15, 30);
    }

    @Override
    public void tick() {
        // Procesamiento de tiempo
        entorno.dibujarImagen(Fondo, 400, 300, 0); // Fondo del juego

        // Dibujar cosas en la pantalla
        pep.dibujarse(entorno);
        tort1.dibujarse(entorno);
        
		if(gnomo != null) {
			gnomo.dibujarse(entorno);
			gnomo.contTicks = 0;
		} else {
			gnomo = new Gnomo(400, 50, 15, 30);
		}
		
        dibujarIslas(islaBase); // Dibujar islas
        
		/// HUD (texto en pantalla)
		entorno.cambiarFont("Impact", 20, Color.white);
		entorno.escribirTexto("Puntos: " + gnomosSalvados, 8, 25);
		entorno.escribirTexto("Gnomos Perdidos: " + gnomosPerdidos, 8, 50);

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
        gnomo.estaApoyado = gnomoEstaApoyado(islaBase);
    }
    //mover el gnomo   ////pensaba en hacerlo aleatorio pero al siempre estar confirmando su colicion, el gnomo no para de cambiar de direccion
    private void moverGnomo() {
    	if (gnomo != null) {                                                        // Revisa si el gnomo no es null
			gnomo.estaApoyado = gnomoEstaApoyado(islaBase);

			if (gnomoEstaApoyado(islaBase)) {
				if (!direccionDefinida) {
					direccionMovimiento = random();
					direccionDefinida = true;
				}

				if (direccionMovimiento) {
					gnomo.movimientoDerecha();
				} else {
					gnomo.movimientoIzquierda();
				}
			}
			
			if (!gnomo.estaApoyado) {
				gnomo.saltoYCaida(entorno);
				direccionDefinida = false;                            // Caida del gnomo
			}
			
			if (gnomo.detectarColision(pep.x, pep.y, pep.ancho, pep.alto)) {            // Pep recoge al gnomo y le suma un punto
				gnomo = null;
				gnomosSalvados++;
			}
			
			if (gnomo != null){
				if (gnomo.y > 700) {
					gnomo = null;														/// ESTO TIRA ERROR!!! Es para cuando se caiga, mas tarde lo arreglo
					gnomosPerdidos++;
				}
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
        boolean apoyado = gnomo.detectarColision(isla.x, isla.y, isla.ancho, isla.alto);
        gnomo.estaApoyado = apoyado;
        return apoyado || gnomoEstaApoyado(isla.izq) || gnomoEstaApoyado(isla.der);
    }

    
	public boolean random(){
		random = new Random();
		randomBooleano = random.nextBoolean();

        return randomBooleano;
	}
}
