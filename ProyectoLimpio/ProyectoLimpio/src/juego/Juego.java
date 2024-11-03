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
    private Random random;
    private Image Fondo; 
    private Image Casa; 
    private Image Inicio; 
    private Image Pierde; 
    private Image Gana; 
    private Pep pep;
    private BolaDeFuego bola;
    private Tortugas[] tortugasLista;
    private Gnomo[] gnomosLista;
    private Islas islaBase = new Islas(380, 80, 90, 20);
    private int enemigosEliminados=0;
    private int gnomosSalvados;
    private int gnomosPerdidos;
    private int vidasPerdidas;
    private int Puntos;
    private int PuntosAlto;
    private int contTicks;
    private int tiempo;
    private int segundos;
    private int temporizador=1000;
    private boolean pausa = false;
    private boolean final1 = false;
    private boolean inicio = false;
    private boolean randomBooleano;
    private final int tamanioLista = 4;
    private final int tamanioListaTortugas = 4;

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
        this.Puntos = 0;
        this.PuntosAlto =0;

    
        // Inicializar personajes
        Casa = Herramientas.cargarImagen("recursos/casa.png");
        pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/idlepepDer.gif"));
        tortugasLista = new Tortugas[tamanioLista];
        gnomosLista = new Gnomo[tamanioLista];
        if(temporizador == 100 || temporizador == 200 || temporizador == 300|| temporizador == 400 || temporizador == 500 ||temporizador == 150 || temporizador == 250 ) { ///el temporizador se explica abajo
	        for (int i = 0; i < tamanioLista; i++) {
	            if(tortugasLista[i] == null) {
	        	tortugasLista[i] = new Tortugas(random1(), 0, 5, 34, Utilidades.randomBoolean()); //tambien abajo
	        	tortugasLista[i].enCaida = true; 
	        	break;
	            }
	        }
	        for (int i = 0; i < tamanioLista; i++) {
	            gnomosLista[i] = new Gnomo(400, 50, 15, 30, Utilidades.randomBoolean());
	        }
        }
        
    }

    @Override
    public void tick() {
    	///inicio juego
    	for (Tortugas tortuga : tortugasLista) {
    	    if (tortuga != null) {
    	        tortuga.actualizar(islaBase); // Llama a actualizar con la referencia de la isla
    	    }
    	}
                                                                 
    	if (!inicio) {
            if (entorno.estaPresionada(' ') || entorno.estaPresionada(entorno.TECLA_ESCAPE)) {
                inicio = true;
                pausa = false; 															// Comienza el juego con espacio
            } else {
                mostrarPantallaInicio();
                return;
            }
        }
    	contador();                             	//	todo esto abajo, pero es lo que pensas
    	tiempo();
    	colisionGnomosYtortugas();
    	mejorPuntuacion();

    	if (entorno.sePresiono('p') || entorno.sePresiono(entorno.TECLA_ESCAPE)){                    //pausa
            pausa = !pausa ;

    	}
    	if(pausa){                                                                    
    		mostrarPantallaPausa();
    		return;
    	}
    	if(final1) {		                                                                     
    		mostrarPantallaFinal();
    		return;
    	}

        // Procesamiento de tiempo
        entorno.dibujarImagen(Fondo, 400, 300, 0); // Fondo del juego

        // Dibujar cosas en la pantalla
        pep.dibujarse(entorno);
        //CASA
        entorno.dibujarImagen(this.Casa, 400, 30, 0, 0.3);
        // SPAWN TORTUGAS
      
        for (int i = 0; i < tamanioListaTortugas; i++) {
            if (tortugasLista[i] != null){
            	tortugasLista[i].dibujarse(entorno);
            } else if(temporizador == 100 || temporizador == 200 || temporizador == 300|| temporizador == 400 || temporizador == 500) {              //temporizador para las nuevas tortugas
            	
            	tortugasLista[i] = new Tortugas (random1(), 50, 15, 34, Utilidades.randomBoolean());
            	tortugasLista[i].enCaida = true; 
            }	
            
        }
     // SPAWN GNOMOS
        for (int i = 0; i < tamanioLista; i++) {
            if (gnomosLista[i] != null){
                gnomosLista[i].dibujarse(entorno);
                gnomosLista[i].contTicks = 50;
            } else if(temporizador == 50 || temporizador == 150 || temporizador == 250|| temporizador == 350 || temporizador == 450){             //temporizador para los nuevos gnomos
                gnomosLista[i] = new Gnomo(400, 50, 15, 30, Utilidades.randomBoolean());
            }
        }


        dibujarIslas(islaBase); // Dibujar islas

        /// HUD (texto en pantalla)
        entorno.cambiarFont("Impact", 20, Color.white);
        entorno.escribirTexto("Gnomos Salvados: " + gnomosSalvados, 8, 25);
        entorno.escribirTexto("Gnomos Perdidos: " + gnomosPerdidos, 8, 50);
        entorno.escribirTexto("Vidas Perdidas: " + vidasPerdidas, 8, 75);
        entorno.escribirTexto("Puntos: " + Puntos, 8, 100);
        entorno.escribirTexto("Enemigos eliminados: " + enemigosEliminados, 8, 125);
        entorno.escribirTexto("Tiempo Transcurrido: " + segundos, 8, 150);
        //entorno.escribirTexto("temporizador: " + temporizador, 8, 175);
       

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
        if ((entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) && pep.estaApoyado) {
            pep.movimientoIzquierda();
        }else if ((entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)) && pep.estaApoyado) {
            pep.movimientoDerecha();
        } else{
            pep.seMueve = false; // Detiene el movimiento si no se presiona ninguna tecla
        }
       if ((entorno.sePresiono('w') || entorno.sePresiono(entorno.TECLA_ARRIBA)) && pep.estaApoyado && pep.y > 200) {
            pep.saltando = true;
       }
        
        if (pep != null) {
            if (pep.y > 700) {
                pep = null;
                pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/idlepepDer.gif"));
                vidasPerdidas--;
                if(vidasPerdidas <=0) {                                           //si pierde las vida lleva a un final
        	    	final1=true;
        			return;
            	}
            }

        }
    }
    /// Funcion de bola de fuego
    private void manejarBolaDeFuego() {
        if (!pep.cooldown()) {
            if (entorno.sePresiono('c')) {
                bola = new BolaDeFuego(pep.x, pep.y, 40, 0, pep.lado,
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
    }
    
    private void manejarColisionesGnomo(Gnomo gnomo){
        gnomo.gnomoEstaApoyado(islaBase);
    }

    public void gnomoColision(int num) {
        if (gnomosLista[num] == null){
            return;
        }

        if (pep.y >=340) {
	        if (gnomosLista[num].detectarPep(pep.x, pep.y, pep.ancho, pep.alto)) {            // Pep recoge al gnomo y le suma un punto
	            gnomosLista[num] = null;
	            gnomosSalvados++;
	            Puntos += 100;
	            if(gnomosSalvados == 20) {
	            	final1=true;													                                          //si llega a 20 gana
	            }
	        }
        }
    }
	public void gnomoPerdido(int num) {
        if (gnomosLista[num] == null){
            return;
        }
        if (gnomosLista[num].y > 700) {
            gnomosLista[num] = null;
            gnomosPerdidos++;
        if(gnomosPerdidos >= 20) {
            	final1 = true;                                                                           //si pierde 20 pierde
    			return;
            }	
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
    	       if (bola != null) {
    	    	   if (tortugasLista[num].detectarBola(bola.x, bola.y, bola.ancho, bola.alto)) {            // bola mata tortuga y le suma un punto
    	    		   tortugasLista[num] = null;
    	    		   bola = null;
    	    		   contTicks = 80;
    	    		   Puntos += 50;
    	    		   enemigosEliminados++;
    	    	   }
    	      }
        }
    	 public void tortugaPerdido(int num) {
    		 if (tortugasLista[num] == null){
    			 return;
    	 }
    		if (tortugasLista[num].y > 700) {
    			tortugasLista[num] = null;
    		} else if (tortugasLista[num].detectarPep(pep.x, pep.y, pep.ancho, pep.alto)) {            // bola mata tortuga y le suma un punto
	            pep = null;
	            pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/pepDer.png"));
	            vidasPerdidas--;
	            if(vidasPerdidas<=0) {
	            	final1 = true;                                          //si lo mata una tortuga lo lleva a un final
        			return;
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
    private void mostrarPantallaInicio() {                                            //pantalla de inicio,
    	Inicio = Herramientas.cargarImagen("recursos/pepInicio.png");
    	entorno.dibujarImagen(Inicio, 400, 300, 0);
    	return;
    }
    public void mostrarPantallaPausa(){                                                 //pausa
    	entorno.dibujarImagen(Fondo, 400, 300, 0);
		entorno.cambiarFont("Impact", 20, Color.white);
	    entorno.escribirTexto("Puntos: " + gnomosSalvados, 8, 25);
	    entorno.escribirTexto("Gnomos Perdidos: " + gnomosPerdidos, 8, 50);
	    entorno.escribirTexto("Vidas Perdidas: " + vidasPerdidas, 8, 75);
	    entorno.escribirTexto("Puntos: " + Puntos, 8, 100);
	    entorno.escribirTexto("ticks: " + pep.contTicks, 8, 150);
		entorno.cambiarFont("Impact", 50, Color.red);
		entorno.escribirTexto("JUEGO PAUSADO", 250, 300);
    return;
    }
    public void mostrarPantallaFinal(){                                          //pantalla de victoria y derrota
    	Gana = Herramientas.cargarImagen("recursos/pepGana.png");
    	Pierde = Herramientas.cargarImagen("recursos/pepPierde.png");
    	entorno.cambiarFont("Impact", 20, Color.green);
	    if(gnomosSalvados < 20) {	                                          //si tiene menos de 20 gnomos pierde
	    	entorno.dibujarImagen(Pierde, 400, 300, 0);
	    	entorno.cambiarFont("Impact", 20, Color.white);
	    	entorno.escribirTexto(" " + PuntosAlto, 300, 400);
	    	entorno.escribirTexto(" " + Puntos, 200, 460);
		    entorno.escribirTexto(" " + segundos, 200, 515);
		    entorno.escribirTexto(" " + gnomosSalvados, 400, 560);
		    entorno.escribirTexto("Press R to reset" , 350, 590);
	    	if (entorno.estaPresionada('r')) {
	    	    inicializarJuego();
	    	    pausa = false;
	    	    inicio = true;
	    	    final1 = false;
	    	    tiempo = 0;
	    	    
	            
	    	}
	    }else {                                                                    //si tiene mas de 20, ganaste
	    	entorno.dibujarImagen(Gana, 400, 300, 0);	                                         
	    	entorno.cambiarFont("Impact", 20, Color.green);
	    	entorno.escribirTexto(" " + PuntosAlto, 300, 400);
	    	entorno.escribirTexto(" " + Puntos, 200, 460);
		    entorno.escribirTexto(" " + segundos, 200, 515);
		    entorno.escribirTexto(" " + gnomosSalvados, 400, 560);
		    entorno.escribirTexto("Press R to reset" , 350, 590);
	    	if (entorno.estaPresionada('r')) {
	    	    inicializarJuego();
	    	    pausa = false;
	    	    inicio = true;
	    	    final1 = false;
	    	    tiempo = 0;
	    	    
		    
	    }
	    return;
	  }
    }
	
    public boolean estaApoyado(Islas isla) {
        if (isla == null) {
            return false;
        }

        String lado = pep.ladoColision(isla.x, isla.y, isla.ancho, isla.alto);
        if (!lado.isEmpty()){
            switch (lado){
                case "izquierda":
                    pep.x -= pep.velocidad;
                    pep.estaApoyado = false;
                    pep.saltando = false;;
                    break;
                case "derecha":
                    pep.x += pep.velocidad;
                    pep.estaApoyado = false;
                    pep.saltando = false;
                    break;
                case "arriba":
                    break;
                case "abajo":
                    pep.y = isla.y + pep.alto;
                    pep.saltando = false;
                    pep.longSalto = 0;

            }
        }

        return pep.detectarColision(isla.x, isla.y, isla.ancho, isla.alto) ||
               estaApoyado(isla.izq) || estaApoyado(isla.der);
    }

   
	public boolean random(){
		random = new Random();
		randomBooleano = random.nextBoolean();
		return randomBooleano;

	}
	public boolean cooldown() {
        if (contTicks > 80) {
            return false;
        }
        contTicks += 1;
        return true;
    }
	public void contador() {                            // limita el spawn de gnomos y tortugas... 
		if (temporizador <= 500) {
			temporizador ++;
		}else {
			temporizador = 0 ;
		}
		
	}
	public void tiempo() { // tiempo.... no para al terminar
		if (tiempo <= 90 && final1 == false && pausa == false) {
		tiempo++;
			if (tiempo == 90) {
			segundos++;
			tiempo = 0;
			return;
			}
		}
	}
	public int random1() { // Cambia el nombre si es necesario
		Random random = new Random();
		int num;
		do {
			num = random.nextInt(650)+100; // Genera un número entre 0 y 649
			System.out.println("Número generado: " + num); // Imprimir el número generado
			} while ((num >= 300 && num <= 520)); // Excluir el rango de 330 a 490 y menor a 30
			return num;
	}
	public void mejorPuntuacion() {
		if(Puntos > PuntosAlto) {
			PuntosAlto = Puntos;
		}
	}
	public void colisionGnomosYtortugas() {
		for (int i = 0; i < gnomosLista.length; i++) {
			Gnomo gnomo = gnomosLista[i];
			if (gnomo != null) {
				for (int j = 0; j < tortugasLista.length; j++) {
				Tortugas tortuga = tortugasLista[j];
					if (tortuga != null) {
					// Verificar si hay colisión
						if (gnomo.detectarColision(tortuga.x, tortuga.y, tortuga.ancho, tortuga.alto)) {
						gnomosLista[i] = null; // Eliminar el gnomo
						gnomosPerdidos++; // Aumentar contador de gnomos perdidos
						break; // Salir del bucle después de una colisión
						}

					}
				}
			}
		}
	}
    
}
