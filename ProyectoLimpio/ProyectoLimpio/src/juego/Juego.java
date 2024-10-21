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
    private Tortugas[] tortugasLista;
    private Gnomo[] gnomosLista;
    private Islas islaBase = new Islas(400, 80, 50, 5);
    private int gnomosSalvados;
    private int gnomosPerdidos;
    private int vidasPerdidas;
    private int tortugasPerdidas;
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
        this.vidasPerdidas = 3;
        this.tortugasPerdidas = 0;

        // Inicializar personajes
        pep = new Pep(400, 450, 50, 15, Herramientas.cargarImagen("recursos/pepDer.png"));
        tortugasLista = new Tortugas[tamanioLista];
        gnomosLista = new Gnomo[tamanioLista];
        bola = new BolaDeFuego(-200, -200, 20, 20, pep.lado,Herramientas.cargarImagen(pep.lado ? "recursos/bolaDeFuego2.gif" : "recursos/bolaDeFuego1.gif"));

        for (int i = 0; i < tamanioLista; i++) {
            tortugasLista[i] = new Tortugas(400, 50, 10, 30, Utilidades.randomBoolean());
        }
    
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
        
        for (int i = 0; i < tamanioLista; i++) {
            if (tortugasLista[i] != null){
            	tortugasLista[i].dibujarse(entorno);
            	tortugasLista[i].contTicks = 3;
            } else {
            	tortugasLista[i] = new Tortugas (400, 50, 15, 30, Utilidades.randomBoolean());
            }
        }
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
        entorno.escribirTexto("Vidas Perdidos: " + vidasPerdidas, 8, 75);
        entorno.escribirTexto("Tortugas Muertas: " + tortugasPerdidas, 8, 100);

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
        for (Tortugas tortuga:tortugasLista) {
            if (tortuga != null) {
                manejarColisionesTortuga(tortuga);
            }
        }

        for (int i = 0; i < tamanioLista; i++) {
            if (tortugasLista[i] != null) {
                tortugasLista[i].moverTortuga(islaBase);
            }
            tortugaColision(i);
            tortugaPerdido(i);
        }
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
                pep = new Pep(50, 300, 50, 30, Herramientas.cargarImagen("recursos/pepDer.png"));
                vidasPerdidas--;
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
    // mover las tortugas 
    private void manejarColisionesTortuga(Tortugas tortuga){
    	tortuga.tortugaEstaApoyado(islaBase);
   	}
        public void tortugaColision(int num) {
    	       if (tortugasLista[num] == null){
    	           return;
    	      }
    	        if (tortugasLista[num].detectarColision(bola.x, bola.y, bola.ancho, bola.alto)) {            // bola mata tortuga y le suma un punto
    	            tortugasLista[num] = null;
    	            tortugasPerdidas++;
    	        }
    	    }     
    	 public void tortugaPerdido(int num) {
    		 if (tortugasLista[num] == null){
    			 return;
    	 }
    		if (tortugasLista[num].y > 700) {
    			tortugasLista[num] = null;
    		}
    		if (tortugasLista[num].detectarColision(pep.x, pep.y, pep.ancho, pep.alto)) {            // bola mata tortuga y le suma un punto
	            pep = null;
	            pep = new Pep(50, 300, 50, 30, Herramientas.cargarImagen("recursos/pepDer.png"));
	            vidasPerdidas--;
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

   
	public boolean random(){
		random = new Random();
		randomBooleano = random.nextBoolean();
		return randomBooleano;
	
    }


}
