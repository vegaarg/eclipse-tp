package juego;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

	public class Juego extends InterfaceJuego {
	
	   private Entorno entorno;
	   private Random random;
	   private Image Fondo; 
	   private Image Casa; 
	   private Image Inicio; 
	   private Image Pierde; 
	   private Image Gana;
	   private Image Escudo;
	   private Pep pep;
	   private Nave Nave;
	   private Bombas bomba;
	   private BolaDeFuego bola;
	   private Tortugas[] tortugasLista;
	   private Gnomo[] gnomosLista;
	   private Islas islaBase = new Islas(380, 80, 90, 20);
	   private int enemigosEliminados=0;
	   private int gnomosSalvados;
	   private int gnomosNecesarios;
	   private int gnomosPerdidos;
	   private int vidasRestantes;
	   private int cantEscudos = 3;
	   private int Puntos = 0;
	   private int PuntosAlto;
	   private int contTicks;
	   private int tiempo;
	   private int segundos;
	   private int temporizador=1000;    
	   private int Modalidad = 0;
	   private boolean pausa = false;
	   private boolean finales = false;
	   private boolean inicio = false;
	   private boolean escudoActivado = false;
	   private boolean randomBooleano;
	   private int tamanioLista = 4;
	
	// Inicializa el objeto entorno
	
	   public Juego() {
	       this.entorno = new Entorno(this, "Al rescate de los gnomos", 800, 600);
	       inicializarJuego();
	       this.entorno.iniciar();
	       this.Escudo = Herramientas.cargarImagen("recursos/escudo.png");
	       if(Puntos > PuntosAlto) {
	    	   PuntosAlto = Puntos;
	       }
	   }
	
	// Inicialización de recursos
	   private void inicializarJuego() {
	       Fondo = Herramientas.cargarImagen("recursos/fondo.png");
	       Islas.popular(4, islaBase);  // Popular las islas recursivamente
	       this.gnomosSalvados = 0;
	       this.gnomosPerdidos = 0;
	       this.vidasRestantes = 3;
	       this.Puntos = 0;
	       this.enemigosEliminados =0;
	  
	
	// Inicializar personajes
	       Nave = new Nave(500, 580,200,200);
	       Casa = Herramientas.cargarImagen("recursos/casa.png");
	       pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/idlepepDer.gif"));
	       tortugasLista = new Tortugas[tamanioLista];
	       gnomosLista = new Gnomo[tamanioLista];
	       if(temporizador == 100 || temporizador == 200 || temporizador == 300|| temporizador == 400 || temporizador == 500 ||temporizador == 150 || temporizador == 250 ) { ///eltemporizador se explica abajo
	        for (int i = 0; i < tamanioLista; i++) {
	           if(tortugasLista[i] == null) {
	               tortugasLista[i] = new Tortugas(randomCaida(), 0, 5, 34, Utilidades.randomBoolean());
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
	//inicio juego
		   
	    if(pausa != true) {
	    for (Tortugas tortuga : tortugasLista) {
	       if (tortuga != null) {
	           tortuga.actualizar(islaBase); // Llama a actualizar conla referencia de la isla
	       		}
	     	}
	    }                                                      
	    if (!inicio) {	// Si se presiona la barra espaciadora,comienza el juego en Modalidad normal
	       if (entorno.estaPresionada(' ')) {
	           inicio = true;
	           pausa = false;
	           gnomosNecesarios = 20;
	       }
	       else if (entorno.estaPresionada('u') ) { // Si se presiona la tecla 'u' o laflecha izquierda, comienza en Modalidad difícil
	           inicio = true;
	           pausa = false;
	           Modalidad = 1; // Modalidad difícil activado
	           gnomosNecesarios = 1000;     
	       }
	       // Si ninguna de las teclas estápresionada, mostrar pantalla de inicio
	       else {
	           mostrarPantallaInicio();
	           return;
	       }
	
	    }
	    contador();
	    tiempo();
	    colisionGnomosYtortugas();
	    colisionGnomosYNave();
	    BombaColision();
	    
	    if (entorno.sePresiono('p') || entorno.sePresiono(entorno.TECLA_ESCAPE)){ ////Funcion de pausa
	           pausa = !pausa ;
	    }
	    if(pausa){                                                                    
	    mostrarPantallaPausa();
	    return;
	    }
	    if(finales) {                                                                    
	    mostrarPantallaFinal();
	    return;
	    }
		if(Puntos > PuntosAlto) {
		PuntosAlto = Puntos;
			}
		
	// Procesamiento de tiempo
	
	       entorno.dibujarImagen(Fondo, 400, 300, 0); // Fondo del juego
	       pep.dibujarse(entorno); // Dibujar cosas en la pantalla
	       entorno.dibujarImagen(this.Casa, 400, 30, 0, 0.3); // Dibuja la casa de arriba
	       if (escudoActivado) {
	           entorno.dibujarImagen(Escudo, pep.getX(), pep.getY(), 0, 0.2);
	       }
	   
	// Spawn tortugas
	
	       for (int i = 0; i < tamanioLista; i++) {
	           if (tortugasLista[i] != null){
	            tortugasLista[i].dibujarse(entorno);
	           } else if(temporizador == 100 || temporizador == 200 || temporizador == 300|| temporizador == 400 || temporizador == 500) { //temporizador para las nuevas tortugas
	            tortugasLista[i] = new Tortugas (randomCaida(), 50, 15, 34, Utilidades.randomBoolean());
	            tortugasLista[i].enCaida = true; 
	           } 
	       }
	      
	//Spawn gnomos
	
	       for (int i = 0; i < tamanioLista; i++) {
	           if (gnomosLista[i] != null){
	               gnomosLista[i].dibujarse(entorno);
	               gnomosLista[i].contTicks = 50;
	           } else if(temporizador == 50 || temporizador == 150 || temporizador == 250|| temporizador == 350 || temporizador == 450){ //temporizador para los nuevos gnomos
	               gnomosLista[i] = new Gnomo(400, 50, 15, 30, Utilidades.randomBoolean());
	           }
	       }
	
	       dibujarIslas(islaBase); // Dibuja islas
	       
	//HUD
	
	       entorno.cambiarFont("Impact", 20, Color.white);
	       entorno.escribirTexto("Gnomos Salvados: " + gnomosSalvados, 8, 25);
	       entorno.escribirTexto("Gnomos Perdidos: " + gnomosPerdidos, 8, 50);
	       entorno.escribirTexto("Vidas Restantes: " + vidasRestantes, 8, 75);
	       entorno.escribirTexto("Escudos: " + cantEscudos, 8, 100);
	       entorno.escribirTexto("Enemigos eliminados: " + enemigosEliminados, 8, 125);
	       entorno.escribirTexto("Tiempo Transcurrido: " + segundos, 8, 150);
	       entorno.escribirTexto("Puntos: " + Puntos, 8, 175);
	       entorno.escribirTexto("Mejor Puntaje: " + PuntosAlto, 8, 200);
	       if(Modalidad == 1) {
	        entorno.cambiarFont("Impact", 20, Color.red);
	        entorno.escribirTexto("Modo Infinito", 8, 590);
	       }
	      
	// Nave
	
	       if( Nave.getX() >= -50 && Nave.getX() <= 830 && Nave != null) {
				if (entorno.estaPresionado(entorno.BOTON_IZQUIERDO)) {
					Nave.encender();
					Nave.moverAdelante(entorno.mouseX());
				}
		   	}else {
		        Nave = null;
		        Nave = new Nave(500, 580,20,100);
		    	
	       }
	
	dibujarNave();
	manejarEscudo();
	}
	
	   public void dibujarNave() {
	       if (Nave.direccion == false) { // Verifica la dirección
	           entorno.dibujarImagen(Nave.apagado, Nave.getX(), Nave.getY(), 0); // Llamar a través de la instancia
	       } else {
	           entorno.dibujarImagen(Nave.encendido, Nave.getX(), Nave.getY(), 0); //Llamar a través de la instancia
	       }
	       
	   colicionNaveYPep();
	   
	// Manejo de entrada para el movimiento de Pep
	
	       manejarMovimientoPep();
	       pep.saltoYCaida(entorno); // Manejo de gravedad y saltos
	
	//bombas
	
	       manejarBomba();
	
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
	
	// Movimiento Tortuga
	
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
	
	// Movimiento de pep
	
	   private void manejarMovimientoPep() {
	       if ((entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) && pep.estaApoyado && !pep.lanzando) {
	           pep.movimientoIzquierda();
	       }else if ((entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)) && pep.estaApoyado && !pep.lanzando) {
	           pep.movimientoDerecha();
	       } else{
	           pep.seMueve = false;                // Detiene el movimiento si no se presiona ninguna tecla
	       }
	      if ((entorno.sePresiono('w') || entorno.sePresiono(entorno.TECLA_ARRIBA)) && pep.estaApoyado && pep.y > 200) {
	           pep.saltando = true;
	      }       
	       if (pep != null) {
	           if (pep.y > 750) {
	               pep = null;
	               escudoActivado = false;
	               pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/idlepepDer.gif"));
	               vidasRestantes--;
	               if(vidasRestantes <=0) {                                           //Si pierde las vidas se pierde lapartida
	            finales=true;
	        return;
	
	            }
	
	           }
	
	       }
	
	   }
	
	// FUNCION ESCUDO
	
	   private void manejarEscudo() {
		   if (entorno.sePresiono('s') && cantEscudos > 0) {
	       escudoActivado = !escudoActivado;
	       		if (escudoActivado) {
	       			entorno.dibujarImagen(Escudo, pep.getX(), pep.getY(), 0, 0.2);// Dibuja el escudo en la posición de Pep
	       		}
		   }
	
	   }      
	
	// Funcion de bola de fuego
	
	   private void manejarBolaDeFuego() {
	       if (!pep.cooldown()) {
	           if (entorno.estaPresionada('c')) {
	            pep.lanzando= true;
	               bola = new BolaDeFuego(pep.x, pep.y, 40, 0, pep.lado,
	                       Herramientas.cargarImagen(pep.lado ? "recursos/bolaDeFuego2.gif" : "recursos/bolaDeFuego1.gif"));
	               pep.contTicks = 0;
	           }else{
	            pep.lanzando= false;
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
	
	
	//funcion bomba
	
	   public void manejarBomba() {
	    for (int i = 0; i < tortugasLista.length; i++) {
	    	Tortugas tortuga = tortugasLista[i] ;
		    if (tortuga != null) {
		       if (!tortuga.cooldownBomba()) {  // Accede a cooldown() a través de la instancia 'tortuga'
		           if (temporizador == 100 || temporizador == 200 || temporizador == 300 || temporizador == 400 || temporizador == 500 ||
		            temporizador == 600 || temporizador == 700 || temporizador == 800 || temporizador == 900) {
		               bomba = new Bombas(tortuga.x, tortuga.y, 60, 49, tortuga.lado,
		            		   Herramientas.cargarImagen(tortuga.lado ? "recursos/BombaDer.gif" : "recursos/BombaIzq.gif"));	
		               Tortugas.contBombasTicks = 0;
		           }
	       }else if (bomba != null) {
	               bomba.dibujarse(entorno);
	               if (bomba.lado) {
	                   bomba.movimientoIzquierda();
	               } else {
	                   bomba.movimientoDerecha();
	               }
	           }
	       }
	    }
	  }
	
	    public void BombaColision() {
	      if (pep == null){
	          return;
	     }
	      if (bomba != null) {
	    	  if (!escudoActivado) {
			    if (pep.detectarBomba(bomba.x, bomba.y, bomba.ancho, bomba.alto)) { 
			      pep = null;
			      pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/idlepepDer.gif"));
			      bomba = null;	
			      vidasRestantes--;
			    }
	      }else {
		      if (pep.detectarBomba(bomba.x, bomba.y, bomba.ancho, bomba.alto)) {
			      escudoActivado=false;
			      bomba = null; 
			      cantEscudos-- ;
		      	}
	      	}
	     }
	    if(bomba != null && bola != null){
		    if (bomba.detectarBola(bola.x, bola.y, bola.ancho, bola.alto)) { // Si la bola mata a una tortuga, suma un punto
		      bomba = null;
		      bola = null;
		      contTicks = 80;
		      Puntos +=50;
		    	}
	     	} 
	   }
	
	//colisiones al estar en plataformas
	
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
		       if (gnomosLista[num].detectarPep(pep.x, pep.y, pep.ancho, pep.alto)) { // Pep recoge al gnomo y le suma un punto
		           gnomosLista[num] = null;
		           gnomosSalvados++;
		           Puntos += 100;
			           if(Modalidad == 1 && gnomosSalvados % 15 == 0)
			            vidasRestantes++;
			           if(Modalidad == 1 && gnomosSalvados % 30 == 0)
			        	   cantEscudos++;
			        if(gnomosSalvados == gnomosNecesarios ) {
			            if(Puntos > PuntosAlto) {
			            	PuntosAlto = Puntos;
			            	}
		
		            finales=true; //si salva a 20 gnomos, se gana
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
	           if(Modalidad == 1){
	            Puntos-=50;
	           }
		       if(gnomosPerdidos >= gnomosNecesarios) {
		            finales = true; // Si pierde 20 gnomos, se pierde
		    return;
	           } 
	        } 
	   }
	
		public void colisionGnomosYNave() {
			for (int i = 0; i < gnomosLista.length; i++) {
				Gnomo gnomo = gnomosLista[i];
					if (gnomo != null) {	
						if (Nave != null) {		
							if (gnomo.detectarColision(Nave.x-50, Nave.y, Nave.ancho-15, Nave.alto)) { //Verifica si hay colisión		
							gnomosLista[i] = null; // Elimina el gnomo	
							gnomosSalvados++; // Aumenta contador de gnomos perdidos	
							if(Modalidad == 1 && gnomosSalvados % 15 == 0)
							      vidasRestantes++;
							if(Modalidad == 1 && gnomosSalvados % 30 == 0)
							cantEscudos++;
							Puntos += 50;
							}else if(gnomosSalvados >= gnomosNecesarios ) {
							      finales = true; // Si pierde 20 gnomos, se pierde
							      return;
	
							         } 
							}
					   }
				}	
		}
	
	// Mover las tortugas 
	
	   private void manejarColisionesTortuga(Tortugas tortuga){
	    tortuga.tortugaEstaApoyado(islaBase);
	   }
	       public void tortugaColision(int num) {
		          if (tortugasLista[num] == null){
		              return;
		              }
			          if (bola != null) {
				          if (tortugasLista[num].detectarBola(bola.x, bola.y, bola.ancho, bola.alto)) { //Si la bola mata a una tortuga, sumaun punto
					          tortugasLista[num] = null;
					          bola = null;
					          contTicks = 80;
					          Puntos += 150;
					          enemigosEliminados++;
				          }
			          }
	       }
	       
	       public void colisionGnomosYtortugas() {
		    for (int i = 0; i < gnomosLista.length; i++) {
			    Gnomo gnomo = gnomosLista[i];
				    if (gnomo != null) {
					    for (int j = 0; j < tortugasLista.length; j++ ) {
					    Tortugas tortuga = tortugasLista[j];
						    if (tortuga != null) {
							    if (gnomo.detectarColision(tortuga.x, tortuga.y, tortuga.ancho, tortuga.alto)) {      //Verifica si hay colisión
							    gnomosLista[i] = null;       // Eliminar el gnomo
							    gnomosPerdidos++;            // Aumenta el contador de gnomos perdidos
								    if(Modalidad == 1){
								             Puntos -=50;
								              }
								    break;                       // Sale del bucle después de una colisión
							    }
						    if(gnomosPerdidos >= gnomosNecesarios) {
						                finales = true; // Si pierde 20 gnomos, se pierde
						        return;
						               } 
						    }
					    }
				    }
			    }
		    }
	       
	    public void tortugaPerdido(int num) {
	    	if (tortugasLista[num] == null){
	    		return;
	    	}
		    if (tortugasLista[num].y > 700) {
			    tortugasLista[num] = null;
			    } else if(!escudoActivado) { 
				    if(tortugasLista[num].detectarPep(pep.x, pep.y, pep.ancho, pep.alto)) {
				           pep = null;
				           pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/pepDer.png"));
				           vidasRestantes--;
				           if(vidasRestantes<=0) {
				            finales = true; // Si a Pep lo mata una tortuga y tenía una sola vida, lo lleva aun final
				        return;}
				    }else {
				    if(tortugasLista[num].detectarPep(pep.x, pep.y, pep.ancho, pep.alto)) {
				          escudoActivado=false;
				          cantEscudos-- ;
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
	
	// Pantalla de inicio
	
	   private void mostrarPantallaInicio() {
	    Inicio = Herramientas.cargarImagen("recursos/pepInicio.png");
	    entorno.dibujarImagen(Inicio, 400, 300, 0);
	    entorno.cambiarFont("Impact", 20, Color.red);
	    entorno.escribirTexto("Activar Modo Infinito con U ", 290, 590);
	    return;
	   }
	
	// Pantalla de victoria y derrota
	
	   public void mostrarPantallaPausa(){ // Pantalla de pausa
	    entorno.dibujarImagen(Fondo, 400, 300, 0);
	    entorno.cambiarFont("Impact", 20, Color.white);
	    entorno.escribirTexto("Gnomos Salvados: " + gnomosSalvados, 8, 25);
	    entorno.escribirTexto("Gnomos Perdidos: " + gnomosPerdidos, 8, 50);
	    entorno.escribirTexto("Vidas Restantes: " + vidasRestantes, 8, 75);
	    entorno.escribirTexto("Escudos: " + cantEscudos, 8, 100);
	    entorno.escribirTexto("Enemigos eliminados: " + enemigosEliminados, 8, 125);
	    entorno.escribirTexto("Tiempo Transcurrido: " + segundos, 8, 150);
	    entorno.escribirTexto("Puntos: " + Puntos, 8, 175);
	    entorno.escribirTexto("Mejor Puntaje: " + PuntosAlto, 8, 200);
		entorno.cambiarFont("Impact", 50, Color.red);
		entorno.escribirTexto("JUEGO PAUSADO", 250, 300);
	   return;
	   }
	
	// Pantalla de victoria y derrota
	
	   public void mostrarPantallaFinal(){
	    Gana = Herramientas.cargarImagen("recursos/pepGana.png");
	    Pierde = Herramientas.cargarImagen("recursos/pepPierde.png");
	    entorno.cambiarFont("Impact", 20, Color.green);
	   if(gnomosSalvados < 20) { // Si tiene menos de 20 gnomos salvados, se pierde
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
	       finales = false;
	       tiempo = 0;
	       segundos = 0;
	       cantEscudos = 3;}
	    } else {                                                                    //Si tiene mas de 20 gnomos salvados, se gana
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
	       finales = false;
	       tiempo = 0;
	       segundos = 0;
	       cantEscudos = 3;}
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
	
		public void contador() { // limita el spawn de gnomos ytortugas
			if (temporizador <= 500) {
			temporizador ++;
			} else {
			temporizador = 0 ;
			}
		}
	
		public void tiempo() { 
			if (tiempo <= 90 && finales == false && pausa == false) {
			tiempo++;
				if (tiempo == 90) {
				segundos++;
				tiempo = 0;
				return;
				}
			}
		}
	
		public int randomCaida() {
			Random random = new Random();
			int num;
			do { num = random.nextInt(650)+100; // Genera un número entre 0 y 649
			} while ((num >= 300 && num <= 520)); // Excluir el rango de 330 a 490 y menor a 30 que es la isla principal y el borde izquierdo
			return num;
		}
		
		private void colicionNaveYPep() {
			if(pep != null) {
				    if( Nave != null)
					    if(pep.detectarColision(Nave.x -50, Nave.y, Nave.ancho - 15, Nave.alto)) {
					    pep = null;
					    pep = new Pep(400, 450, 53, 48, Herramientas.cargarImagen("recursos/idlepepDer.gif"));
					   }
				}
		}
}
