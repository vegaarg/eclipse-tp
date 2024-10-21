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
    private Gnomo[] gnomosLista;
    private Islas islaBase = new Islas(400, 80, 50, 5);
    private int gnomosSalvados;
    private int gnomosPerdidos;
    private int vidasPerdidos;
    private boolean randomBooleano;
    private Random random;
    private boolean direccionDefinida = false;
    private boolean direccionMovimiento;
    private final int tamanioLista = 4;

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
        this.vidasPerdidos = 3;

        // Inicializar personajes
        pep = new Pep(400, 450, 50, 15, Herramientas.cargarImagen("recursos/pepDer.png"));
        tort1 = new Tortugas(200, 200, 35, 30);
        gnomosLista = new Gnomo[tamanioLista];

        for (int i = 0; i < tamanioLista; i++) {
            gnomosLista[i] = new Gnomo(400, 50, 10, 30, Utilidades.randomBoolean());
        }
    }

    @Override
    public void tick() {
        // Procesamiento de tiempo
        entorno.dibujarImagen(Fondo, 400, 300, 0); // Fondo del juego

        // Dibujar cosas en la pantalla
        pep.dibujarse(entorno);
        tort1.dibujarse(entorno);

        for (int i = 0; i < tamanioLista; i++) {
            if (gnomosLista[i] != null){
                gnomosLista[i].dibujarse(entorno);
                gnomosLista[i].contTicks = 3;
            } else {
                gnomosLista[i] = new Gnomo(400, 50, 15, 30, Utilidades.randomBoolean());
            }
        }


        dibujarIslas(islaBase); // Dibujar islas

        /// HUD (texto en pantalla)
        entorno.cambiarFont("Impact", 20, Color.white);
        entorno.escribirTexto("Puntos: " + gnomosSalvados, 8, 25);
        entorno.escribirTexto("Gnomos Perdidos: " + gnomosPerdidos, 8, 50);
        entorno.escribirTexto("Vidas Perdidos: " + vidasPerdidos, 8, 75);

        // Manejo de entrada para el movimiento de Pep
        manejarMovimientoPep();
        pep.saltoYCaida(entorno); // Manejo de gravedad y saltos

        // Manejo de la bola de fuego
        manejarBolaDeFuego();

        // Manejo de colisiones
        manejarColisiones();

        // Movimiento del gnomo
        for (Gnomo gnomo:gnomosLista) {
            if (gnomo != null) {
                manejarColisionesGnomo(gnomo);
            }
        }

        for (int i = 0; i < tamanioLista; i++) {
            if (gnomosLista[i] != null) {
                gnomosLista[i].moverGnomo(islaBase);
            }
            gnomoColision(i);
            gnomoPerdido(i);
        }
        /// Movimiento Tortuga
        moverTortuga();
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
        if (pep != null) {
            if (pep.y > 700) {
                pep = null;
                pep = new Pep(400, 450, 50, 30, Herramientas.cargarImagen("recursos/pepDer.png"));
                vidasPerdidos--;
            }
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
        String lado = pep.ladoColision(islaBase.x, islaBase.y, islaBase.ancho, islaBase.alto);


        if (!lado.isEmpty()){
            switch (lado){
                case "izquierda":
                    pep.x = islaBase.x - islaBase.ancho/2 - pep.ancho;
                    break;
                case "derecha":
                    pep.x = islaBase.ancho+ islaBase.x;
                    break;
                case "arriba":
                    break;
                case "abajo":
                    pep.y = islaBase.y+pep.alto;
                    pep.saltando = false;
                    System.out.println("colision");
            }
        }

    }
 // mover las tortugas 
    private void moverTortuga() {
    	if (tort1!= null) {                                                        // Revisa si el gnomo no es null
    		tort1.estaApoyado =tort1EstaApoyado(islaBase);

			if (tort1EstaApoyado(islaBase)) {
				if (!direccionDefinida) {
					direccionMovimiento = random2();
					direccionDefinida = true;
				}

				if (direccionMovimiento) {
					tort1.movimientoDerecha();
				} else {
					tort1.movimientoIzquierda();
				}
			}
			
			if (!tort1.estaApoyado) {
				tort1.saltoYCaida(entorno);
				direccionDefinida = false;                            // Caida del gnomo
			}
		}
    }
    private void manejarColisionesGnomo(Gnomo gnomo){
        gnomo.gnomoEstaApoyado(islaBase);
    }

    public void gnomoColision(int num) {
        if (gnomosLista[num] == null){
            return;
        }
        if (gnomosLista[num].detectarColision(pep.x, pep.y, pep.ancho, pep.alto)) {            // Pep recoge al gnomo y le suma un punto
            gnomosLista[num] = null;
            gnomosSalvados++;
        }
    }

    public void gnomoPerdido(int num) {
        if (gnomosLista[num] == null){
            return;
        }
        if (gnomosLista[num].y > 700) {
            gnomosLista[num] = null;
            gnomosPerdidos++;
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
    public boolean tort1EstaApoyado(Islas isla) {
        if (isla == null) return false;
        boolean apoyado = tort1.detectarColision(isla.x, isla.y, isla.ancho, isla.alto);
        tort1.estaApoyado = apoyado;
        return apoyado || tort1EstaApoyado(isla.izq) || tort1EstaApoyado(isla.der);
    }

    
	public boolean random(){
		random = new Random();
		randomBooleano = random.nextBoolean();
		return randomBooleano;
	
    }
		public boolean random2(){
			random = new Random();
			randomBooleano = random.nextBoolean();

        return randomBooleano;
	}
}
